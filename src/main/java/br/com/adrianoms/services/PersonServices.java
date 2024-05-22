package br.com.adrianoms.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.adrianoms.data.vo.v1.PersonVO;
import br.com.adrianoms.exceptions.ResourceNotFoundException;
import br.com.adrianoms.mapper.DozerMapper;
import br.com.adrianoms.model.Person;
import br.com.adrianoms.repositories.PersonRepository;

@Service
public class PersonServices {

	@Autowired
	private PersonRepository respository;

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	public PersonVO findById(Long id) {
		logger.info("Finding one person!");
		var entity = respository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		return DozerMapper.parseObject(entity, PersonVO.class);
	}

	public List<PersonVO> findAll() {
		logger.info("Finding all people!");
		return DozerMapper.parseListObjects(respository.findAll(), PersonVO.class);
	}

	public PersonVO create(PersonVO person) {
		logger.info("Creating one person!");
		var entity = respository.save(DozerMapper.parseObject(person, Person.class));
		return DozerMapper.parseObject(entity, PersonVO.class);
	}

	public PersonVO update(PersonVO person) {
		logger.info("Creating one person!");
		var entity = respository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		return DozerMapper.parseObject(respository.save(entity), PersonVO.class);
	}

	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = respository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		respository.delete(entity);
	}
}
