package com.coding.nuerobyte.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.coding.nuerobyte.Util.Util;
import com.coding.nuerobyte.constants.Constants;
import com.coding.nuerobyte.service.SubmissionService;
import com.codingplatform.openapi.api.SubmissionsApiDelegate;
import com.codingplatform.openapi.model.BasicResponseDTO;
import com.codingplatform.openapi.model.Submission;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubmissionImpl implements SubmissionsApiDelegate {

	@Autowired
	SubmissionService service;

	@Override
	public ResponseEntity<BasicResponseDTO> createSubmission(Long userId, Long problemId, String body) {

		BasicResponseDTO dto = new BasicResponseDTO();

		try {
			Submission inputBody = Util.fromJson(body, Submission.class);
			service.createSubmission(userId, problemId, inputBody);
		} catch (Exception e) {
			log.error("Error in saving create submission :" , e);
			dto.setStatus(false);
			dto.setResponseCode(500);
			dto.setMessage(Constants.STANDARD_ERROR_MESSAGE);
			return ResponseEntity.internalServerError().build() ; 
		}
		
		dto.setStatus(true);
		dto.setResponseCode(200);
		dto.setMessage("Created Submission");
		
		return ResponseEntity.ok().body(dto);
	}
	
	@Override
	public ResponseEntity<BasicResponseDTO> getSubmissionById(Long id) {
		
		BasicResponseDTO dto = new BasicResponseDTO();
		
		Submission submissionById = null ; 
		try {
			submissionById = service.getSubmissionById(id);
		} catch (Exception e) {
			log.error("Error in fetching submission for id : {} :" , id  , e);
			dto.setStatus(false);
			dto.setResponseCode(500);
			dto.setMessage(Constants.STANDARD_ERROR_MESSAGE);
			return ResponseEntity.internalServerError().build() ; 
		}
		
		dto.setStatus(true);
		dto.setResponseCode(200);
		dto.setMessage("Fetched successfully!!");
		dto.setResult(submissionById);
		
		return ResponseEntity.ok().body(dto);
	}
}
