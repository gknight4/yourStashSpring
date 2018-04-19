package com.thumbsup.yourstash.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonPasswordsRepository 
	extends JpaRepository<CommonPasswords, Long> {
    Optional<CommonPasswords> findByPassword(String password); 
}

