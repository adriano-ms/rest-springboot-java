package br.com.adrianoms.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.adrianoms.repositories.UserRepository;

@Service
public class UserServices implements UserDetailsService {

	@Autowired
	private UserRepository respository;

	private Logger logger = Logger.getLogger(UserServices.class.getName());

	public UserServices(UserRepository respository) {
		this.respository = respository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by name " + username + "!");
		var user = respository.findByUserName(username);
		if(user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}
}
