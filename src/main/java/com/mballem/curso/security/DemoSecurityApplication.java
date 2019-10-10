package com.mballem.curso.security;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@SpringBootApplication
public class DemoSecurityApplication {

	public static void main(String[] args) {
		
		//System.out.println("*****************************");
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
		//System.out.println("*****************************");
		SpringApplication.run(DemoSecurityApplication.class, args);
	}
	
	
	  @Bean 
	  public LocaleResolver localeResolver() { return new
	  FixedLocaleResolver(new Locale("pt", "BR"));
	  
	  }
	 
}
