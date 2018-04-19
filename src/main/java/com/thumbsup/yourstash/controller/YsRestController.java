package com.thumbsup.yourstash.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thumbsup.yourstash.model.TransactionRepository;
import com.thumbsup.yourstash.model.YsUserRepository;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
//@RepositoryRestController
public class YsRestController {
	private final YsUserRepository userRepository ;
	private final TransactionRepository transactionRepository ;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public YsRestController(YsUserRepository userRepository,
			TransactionRepository transactionRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder
			) {
		this.userRepository = userRepository ;
		this.transactionRepository = transactionRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	

}
