package com.coding.nuerobyte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSubmissionRequest {
	@NotNull
	public Long userId; // for MVP; replace with authenticated principal later
	@NotNull
	public Long problemId;
	@NotBlank
	public String language;
	@NotBlank
	public String sourceCode;
}