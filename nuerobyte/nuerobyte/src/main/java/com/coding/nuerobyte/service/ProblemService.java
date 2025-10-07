package com.coding.nuerobyte.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coding.nuerobyte.entity.ProblemEntity;
import com.coding.nuerobyte.repository.ProblemRepository;
import com.codingplatform.openapi.api.ProblemsApi;

@Service
public class ProblemService implements ProblemsApi {
	
	private final ProblemRepository problemRepository;

	public ProblemService(ProblemRepository problemRepository) {
		this.problemRepository = problemRepository;
	}

	public ProblemEntity create(ProblemEntity p) {
		return problemRepository.save(p);
	}

	public List<ProblemEntity> listAll() {
		return problemRepository.findAll();
	}

	public ProblemEntity findById(Long id) {
		return problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
	}
}