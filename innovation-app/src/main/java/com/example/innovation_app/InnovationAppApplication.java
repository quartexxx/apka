
package com.example.innovation_app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class InnovationAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnovationAppApplication.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode("admin");  // Zakodowanie hasła "admin"
		System.out.println(encodedPassword);  // Wydrukowanie zakodowanego hasła
		}

	}


