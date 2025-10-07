package com.coding.nuerobyte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coding.nuerobyte.entity.TestCaseEntity;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Long> {
}