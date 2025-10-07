package com.coding.nuerobyte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.coding.nuerobyte.Util.Util;
import com.coding.nuerobyte.constants.Constants;
import com.coding.nuerobyte.entity.ProblemEntity;
import com.coding.nuerobyte.repository.ProblemRepository;
import com.codingplatform.openapi.api.ProblemsApiDelegate;
import com.codingplatform.openapi.model.BasicResponseDTO;
import com.codingplatform.openapi.model.Problem;
import com.codingplatform.openapi.model.Submission;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProblemService implements ProblemsApiDelegate {
	
	private final ProblemRepository problemRepository;
	
	@Autowired
	ProblemGeneratorService generator ; 

	public ProblemService(ProblemRepository problemRepository) {
		this.problemRepository = problemRepository;
	}

	@Override
	public ResponseEntity<BasicResponseDTO> createProblem(Problem input) {
		BasicResponseDTO dto = new BasicResponseDTO();
		
		ProblemEntity problem = null ; 
		try {
			problem = generator.startGeneration(input.getType(), input.getDifficulty().getValue(), input.getLanguage());
			log.info("problem id generated : {}" , problem.getId());
			
		} catch (Exception e) {
			log.error("Error in generating problem :" , e);
			dto.setStatus(false);
			dto.setResponseCode(500);
			dto.setMessage(Constants.STANDARD_ERROR_MESSAGE);
			return ResponseEntity.internalServerError().build() ; 
		}
		
		dto.setStatus(true);
		dto.setResponseCode(200);
		dto.setMessage(String.format("Created problem : %s", problem.getId()));
		
		return ResponseEntity.ok().body(dto);
	}

	public List<ProblemEntity> listAll() {
		return problemRepository.findAll();
	}

	public ProblemEntity findById(Long id) {
		return problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
	}
}