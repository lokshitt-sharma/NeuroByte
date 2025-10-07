package com.coding.nuerobyte.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.coding.nuerobyte.service.ProblemService;
import com.codingplatform.openapi.api.ProblemsApiDelegate;
import com.codingplatform.openapi.model.BasicResponseDTO;
import com.codingplatform.openapi.model.Problem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProblemServiceImpl implements ProblemsApiDelegate{
	
	@Autowired
	ProblemService service;

	//@Override
	public ResponseEntity<BasicResponseDTO> createProblem(Problem body) {
		return null ; 
	}
}
