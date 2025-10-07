package com.coding.nuerobyte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coding.nuerobyte.entity.AppUserEntity;

@Repository
public interface UserRepository extends JpaRepository<AppUserEntity, Long> {
}