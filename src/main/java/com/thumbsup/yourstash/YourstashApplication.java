package com.thumbsup.yourstash;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class YourstashApplication {

	@Bean(name="passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
    	BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
    	return bcpe;
    }
    
	  @Bean(name="dataSource")
	  public DataSource dataSource() {
	  	DriverManagerDataSource dmds = new DriverManagerDataSource();
	  	dmds.setDriverClassName("com.mysql.jdbc.Driver");
	  	dmds.setUsername("jdbc");
	  	dmds.setPassword("jdbc");
	  	dmds.setUrl("jdbc:mysql://localhost/TEST");
	  	return dmds ;
	  }
  
	public static void main(String[] args) {
		SpringApplication.run(YourstashApplication.class, args);
	}
}
