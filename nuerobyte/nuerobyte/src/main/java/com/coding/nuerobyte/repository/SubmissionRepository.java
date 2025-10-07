package com.coding.nuerobyte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coding.nuerobyte.entity.SubmissionEntity;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionEntity, Long> {
	List<SubmissionEntity> findByUserId(Long userId);
}