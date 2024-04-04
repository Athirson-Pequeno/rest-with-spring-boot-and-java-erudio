package br.com.tizo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import br.com.tizo.data.vo.v1.PersonVO;
import br.com.tizo.exceptions.ResourceNotFoundException;
import br.com.tizo.mapper.Mapper;
import br.com.tizo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tizo.model.Person;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;
	
	public List<PersonVO> findAll() {
		
		return Mapper.parseListObjects(repository.findAll(), PersonVO.class);
	}
	
	
	public PersonVO findById(Long id) {
		logger.info("FINDING ONE PERSON!");
		return Mapper.parseObject(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id.")), PersonVO.class);
	}

	
	public PersonVO create(PersonVO person) {
		
		logger.info("Creating one person!");

		return Mapper.parseObject(repository.save(Mapper.parseObject(person, Person.class)), PersonVO.class);
	}

	public PersonVO update(PersonVO person) {
		
		logger.info("Updating one person!");
		var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id."));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		entity.setAddress(person.getAddress());

		return Mapper.parseObject(repository.save(Mapper.parseObject(entity, Person.class)), PersonVO.class);
	}

	public void delete(Long id) {

		logger.info("Deleting one person!");
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id."));
		repository.delete(entity);

	}
}
