package com.coding.nuerobyte.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateProblemRequest {
	@NotBlank
	public String title;
	@NotBlank
	public String description;
	public String difficulty;
	public String starterCode;
	public Integer timeLimitSeconds = 60;
}