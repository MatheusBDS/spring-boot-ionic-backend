package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	BCryptPasswordEncoder pe;
	
	@Autowired
	EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email){
		
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado!");
		}
		
		String newPass = newPassword();
		
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}

	private char randomChar() {

		int opt = rand.nextInt(3);
		if(opt == 0) { //gera um dígito
			return (char) (rand.nextInt(10) + 48);
		}
		else if( opt == 1 ) { //gera letra maiuscula
			return (char) (rand.nextInt(10) + 65);
		}else { //gera letra minúscula
			return (char) (rand.nextInt(10) + 97);
		}
	
	}
	
}
