package com.coding.nuerobyte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.nuerobyte.entity.AppUserEntity;
import com.coding.nuerobyte.entity.ProblemEntity;
import com.coding.nuerobyte.entity.SubmissionEntity;
import com.coding.nuerobyte.enums.VerdictEnum;
import com.coding.nuerobyte.repository.ProblemRepository;
import com.coding.nuerobyte.repository.SubmissionRepository;
import com.coding.nuerobyte.repository.UserRepository;
import com.codingplatform.openapi.model.Submission;

@Service
public class SubmissionService{

	@Autowired
	private SubmissionRepository submissionRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Save a new submission for a user and problem.
	 */
	public void createSubmission(Long userId, Long problemId, Submission inputBody) {
		Optional<AppUserEntity> userOpt = userRepository.findById(userId);
		Optional<ProblemEntity> problemOpt = problemRepository.findById(problemId);

		if (userOpt.isEmpty() || problemOpt.isEmpty()) {
			throw new RuntimeException("Invalid user or problem ID");
		}

		SubmissionEntity submission = new SubmissionEntity();
		submission.setUser(userOpt.get());
		submission.setProblem(problemOpt.get());
		submission.setLanguage(inputBody.getLanguage());
		submission.setSourceCode(inputBody.getCode());
		submission.setVerdict(VerdictEnum.PENDING.name());
		submission.setScore(0);

		submissionRepository.save(submission);
		
	}

	/**
	 * Get all submissions for a particular user.
	 */
	public List<SubmissionEntity> getSubmissionsByUser(Long userId) {
		return submissionRepository.findAll().stream().filter(s -> s.getUser().getId().equals(userId)).toList();
	}

	/**
	 * Get all submissions for a specific problem.
	 */
	public List<SubmissionEntity> getSubmissionsByProblem(Long problemId) {
		return submissionRepository.findAll().stream().filter(s -> s.getProblem().getId().equals(problemId)).toList();
	}

	/**
	 * Update the evaluation result after AI/Judge completes evaluation.
	 */
	public SubmissionEntity updateSubmissionResult(Long submissionId, String status, Integer score, String feedback) {
		Optional<SubmissionEntity> submissionOpt = submissionRepository.findById(submissionId);
		if (submissionOpt.isEmpty()) {
			throw new RuntimeException("Submission not found");
		}

		SubmissionEntity submission = submissionOpt.get();
		submission.setVerdict(status);
		submission.setScore(score);
		return submissionRepository.save(submission);
	}

	/**
	 * Get a specific submission by ID.
	 */
	public Submission getSubmissionById(Long id) {
		SubmissionEntity entity = submissionRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Submission not found"));
		
		Submission submission = new Submission() ;
		submission.setCode(entity.getSourceCode());
		submission.setVerdict(entity.getVerdict());
		submission.setLanguage(entity.getLanguage());
		submission.setProblemId(entity.getProblem().getId());
		submission.setScore(entity.getScore());
		
		return submission ; 
	}

}
