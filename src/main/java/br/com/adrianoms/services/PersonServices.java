package br.com.adrianoms.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.adrianoms.exceptions.ResourceNotFoundException;
import br.com.adrianoms.model.Person;
import br.com.adrianoms.repositories.PersonRepository;

@Service
public class PersonServices {

	@Autowired
	private PersonRepository respository;

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	public Person findById(Long id) {
		logger.info("Finding one person!");
		return respository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
	}

	public List<Person> findAll() {
		logger.info("Finding all people!");
		return respository.findAll();
	}

	public Person create(Person person) {
		logger.info("Creating one person!");
		return respository.save(person);
	}

	public Person update(Person person) {
		logger.info("Creating one person!");
		var entity = respository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		return respository.save(entity);
	}

	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = respository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		respository.delete(entity);
	}
}
