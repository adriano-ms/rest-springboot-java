package br.com.adrianoms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.adrianoms.data.vo.v1.security.AccountCredentialsVO;
import br.com.adrianoms.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint")
public class AuthController {

	@Autowired
	private AuthServices service;
	
	@PostMapping(value = "/signin")
	@Operation(summary = "Authenticates a user and returns a token!")
	public ResponseEntity<?> signin(@RequestBody AccountCredentialsVO data){
		if (checkIfParamsIsNotNull(data)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		}
		var token = service.signin(data);
		if(token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		}
		return token;
	}

	private Boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
		return data == null || data.getUserName() == null || data.getUserName().isBlank()
				|| data.getPassword() == null || data.getPassword().isBlank();
	}
}
