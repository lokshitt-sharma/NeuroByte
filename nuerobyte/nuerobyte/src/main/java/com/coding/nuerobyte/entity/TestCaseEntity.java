package com.coding.nuerobyte.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@SuppressWarnings("unused")
@Table(name = "test_case")
public class TestCaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id")
	private ProblemEntity problem;

	@Column(columnDefinition = "text")
	private String input;

	@Column(columnDefinition = "text")
	private String expectedOutput;

	private boolean isHidden = false;

}