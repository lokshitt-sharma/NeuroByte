package com.coding.nuerobyte.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coding.nuerobyte.Util.Util;
import com.coding.nuerobyte.component.OllamaClient;
import com.coding.nuerobyte.entity.ProblemEntity;
import com.coding.nuerobyte.entity.TestCaseEntity;
import com.coding.nuerobyte.repository.ProblemRepository;
import com.coding.nuerobyte.repository.TestCaseRepository;
import com.codingplatform.openapi.model.Problem.DifficultyEnum;

@Service
@SuppressWarnings({ "rawtypes" , "unchecked"})
public class ProblemGeneratorService {

    @Autowired
    private OllamaClient ollamaClient;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    private static final String MODEL = "gpt-oss:20b";

    /**
     * Kick off async problem generation. Returns problemId of a placeholder that will be populated.
     */
    public ProblemEntity startGeneration(String topic, String difficulty, List<String> languageHints, Long authorId) {
    	
        ProblemEntity p = new ProblemEntity();
        p.setTitle("GENERATING: " + topic);
        p.setDescription("AI generating problem...");
        p.setDifficulty(DifficultyEnum.fromValue(difficulty).toString());
        p.setTimeLimitSeconds(1800);

        ProblemEntity saved = problemRepository.save(p);

        // async worker will populate it
        generateAsync(saved.getId(), topic, difficulty, languageHints, authorId);
        return saved;
    }

	@Async("generationExecutor")
    public void generateAsync(Long problemId, String topic, String difficulty, List<String> languageHints, Long authorId) {
        ProblemEntity problem = problemRepository.findById(problemId).orElseThrow();

        String prompt = buildPrompt(topic, difficulty, languageHints);
        // call model — use a generous timeout
        String raw = ollamaClient.generate(MODEL, prompt, 25000, Duration.ofMinutes(10));

        // raw may be a JSON string or an envelope; try to extract JSON
        // If Ollama returns a JSON envelope, adapt accordingly. Here we assume 'raw' contains JSON.
        Map parsed = Util.fromJson(raw, Map.class);

        // Basic validation
        if (!parsed.containsKey("title") || !parsed.containsKey("testcases")) {
            // mark problem failed
            problem.setDescription("AI generation failed: malformed response");
            problemRepository.save(problem);
            return;
        }

        // update problem fields
        problem.setTitle((String) parsed.get("title"));
        problem.setDescription((String) parsed.get("description"));
        problem.setDifficulty((String) parsed.get("difficulty"));
        problemRepository.save(problem);

        List<Map> testcases = (List<Map>) parsed.get("testcases");

        // validate length
        if (testcases.size() != 20) {
            problem.setDescription(problem.getDescription() + " (expected 20 testcases, got " + testcases.size() + ")");
            problemRepository.save(problem);
        }

        // persist in batches (e.g., 100)
        int batchSize = 5;
        for (int i = 0; i < testcases.size(); i += batchSize) {
            int to = Math.min(i + batchSize, testcases.size());
            List<Map> batch = testcases.subList(i, to);
            persistBatch(problem, batch);
        }

        // Optionally compute and mark problem as READY
        problemRepository.save(problem);
    }

    private void persistBatch(ProblemEntity problem, List<Map> batch) {
        for (Map tc : batch) {
            String input = (String) tc.get("input");
            String expected = (String) tc.get("expectedOutput");
            Boolean hidden = tc.get("isHidden") == null ? true : (Boolean) tc.get("isHidden");

            // basic sanity check: non-empty expected
            if (expected == null) { continue; }

            TestCaseEntity t = new TestCaseEntity();
            t.setProblem(problem);
            t.setInput(input);
            t.setExpectedOutput(expected);
            t.setHidden(hidden);
            testCaseRepository.save(t);
        }
    }

    private String buildPrompt(String topic, String difficulty, List<String> languageHints) {
        String hints = String.join(", ", languageHints);
        String prompt = "SYSTEM: You are an expert contest problem writer. Output ONLY valid JSON...\n";
        prompt += "USER: Create a programming problem in topic: " + topic
                + " Difficulty: " + difficulty
                + " Languages: " + hints + "\n";
        prompt += "Follow this strict JSON schema: {title, description, difficulty, languageHints, testcases:[{input, expectedOutput, isHidden}] }\n";
        prompt += "Produce exactly 20 testcases; 3 must be isHidden=false (visible). Use varied sizes and edge-cases.\n";
        prompt += "Return JSON only.";
        return prompt;
    }
}
