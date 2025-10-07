package com.coding.nuerobyte.entity;


import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "submission")
public class SubmissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private AppUserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id")
	private ProblemEntity problem;

	private String language;

	@Column(columnDefinition = "text")
	private String sourceCode;

	private String verdict; // e.g., PENDING, ACCEPTED, WRONG_ANSWER

	private Integer runtimeMs;
	private Integer memoryKb;
	private Integer score;

	private OffsetDateTime createdAt = OffsetDateTime.now();

}