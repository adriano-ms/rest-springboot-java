package br.com.adrianoms.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import br.com.adrianoms.controllers.PersonController;
import br.com.adrianoms.data.vo.v1.PersonVO;
import br.com.adrianoms.exceptions.RequiredObjectIsNullException;
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
		PersonVO vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}

	public List<PersonVO> findAll() {
		logger.info("Finding all people!");
		var persons = DozerMapper.parseListObjects(respository.findAll(), PersonVO.class);
		persons
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}

	public PersonVO create(PersonVO person) {
		if(person == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating one person!");
		var entity = respository.save(DozerMapper.parseObject(person, Person.class));
		PersonVO vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public PersonVO update(PersonVO person) {
		if(person == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating one person!");
		var entity = respository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		PersonVO vo = DozerMapper.parseObject(respository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = respository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		respository.delete(entity);
	}
}
