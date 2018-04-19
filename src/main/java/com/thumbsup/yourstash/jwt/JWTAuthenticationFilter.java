package com.thumbsup.yourstash.jwt;

//import com.auth0.samples.authapi.user.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thumbsup.yourstash.model.YsUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.thumbsup.yourstash.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.thumbsup.yourstash.jwt.SecurityConstants.HEADER_STRING;
import static com.thumbsup.yourstash.jwt.SecurityConstants.SECRET;
import static com.thumbsup.yourstash.jwt.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
        	System.out.println("auth try");
            YsUser creds = new ObjectMapper()
                    .readValue(req.getInputStream(), YsUser.class);

    		String userName = creds.getUsername();
    		String parentName = creds.getParentname();
    		String password = creds.getPassword();
    		if ((userName == null) || (userName == "")) {
    			userName = parentName ;
    		} else {
    			userName = parentName + "/" + userName ;
    		}
    		System.out.println(userName + ", " + parentName);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userName, // creds.getUsername(),
                            password, // creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
        	System.out.println("exception");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	System.out.println("successful authorization");
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.getWriter().print("im here");
    }
}