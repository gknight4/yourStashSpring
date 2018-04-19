package com.thumbsup.yourstash.controller;

import static com.thumbsup.yourstash.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.thumbsup.yourstash.jwt.SecurityConstants.SECRET;
import static com.thumbsup.yourstash.jwt.SecurityConstants.HEADER_STRING;
import static com.thumbsup.yourstash.jwt.SecurityConstants.TOKEN_PREFIX;
import static com.thumbsup.yourstash.shared.SharedConstants.USER_FLAG_CONFIRMED;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.thumbsup.yourstash.shared.LoginInfo;
import com.thumbsup.yourstash.jackson.LoginInfo;
import com.thumbsup.yourstash.model.YsUser;
//import com.thumbsup.yourstash.model.YsUser;
import com.thumbsup.yourstash.shared.SuccessResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.thumbsup.yourstash.model.CommonPasswordsRepository;
import com.thumbsup.yourstash.model.YsUserRepository;

@RestController
@CrossOrigin
@RequestMapping(value = "/open")
public class YsOpenRestController {
	private final YsUserRepository userRepository ;
	private final CommonPasswordsRepository commonPasswordsRepository ;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
//	public YsOpenRestController() {}


	public YsOpenRestController (YsUserRepository userRepository,
			CommonPasswordsRepository commonPasswordsRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder
			) {
		this.userRepository = userRepository;
		this.commonPasswordsRepository = commonPasswordsRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	String makeToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	SuccessResponse putLogin(
			@RequestBody LoginInfo loginInfo,
			HttpServletResponse response
			) {
		Optional<YsUser> user = userRepository.findByUsername(loginInfo.getUsername()) ;
		if(user.isPresent() && 
			(bCryptPasswordEncoder.matches(loginInfo.getPassword(), 
					user.get().getPassword()))
		) {
			YsUser ysUser = user.get();
			String token = makeToken(loginInfo.getUsername());
			response.addHeader("Access-Control-Expose-Headers", "Authorization");
			response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
			return new SuccessResponse(true) ;
		} else {
			return new SuccessResponse(false) ;
		}
		
	}
	
}
