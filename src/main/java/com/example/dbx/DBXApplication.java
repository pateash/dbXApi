package com.example.dbx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class DBXApplication {
	final PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(DBXApplication.class, args);
	}

}
