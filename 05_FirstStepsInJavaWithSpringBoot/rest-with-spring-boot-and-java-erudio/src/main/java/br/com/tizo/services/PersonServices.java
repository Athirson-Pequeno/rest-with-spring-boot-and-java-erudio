package br.com.tizo.services;

import java.util.List;
import java.util.logging.Logger;

import br.com.tizo.controllers.PersonController;
import br.com.tizo.data.vo.v1.PersonVO;
import br.com.tizo.data.vo.v2.PersonVOV2;
import br.com.tizo.exceptions.RequiredObjectIsNullException;
import br.com.tizo.exceptions.ResourceNotFoundException;
import br.com.tizo.mapper.ModelMapperUtil;
import br.com.tizo.mapper.custom.PersonMapper;
import br.com.tizo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.tizo.model.Person;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	@Autowired
	PersonMapper mapper;
	
	public List<PersonVO> findAll() {
		
		var persons = ModelMapperUtil.parseListObjects(repository.findAll(), PersonVO.class);

		persons.stream().forEach(personVO -> {
            try {
                personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

		return persons;
	}
	
	
	public PersonVO findById(Long id){
		logger.info("FINDING ONE PERSON!");

		var entity = ModelMapperUtil.parseObject(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id.")), PersonVO.class);

		PersonVO vo = ModelMapperUtil.parseObject(entity, PersonVO.class) ;
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

		return vo;
	}

	
	public PersonVO create(PersonVO person) {

		if(person==null) throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");

		var entity = ModelMapperUtil.parseObject(person, Person.class);
		var vo = ModelMapperUtil.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public PersonVOV2 createV2(PersonVOV2 person) {

		if(person==null) throw new RequiredObjectIsNullException();

		logger.info("Creating one person v2!");

		var entity = mapper.convertVoToEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(entity));
		return vo;
	}

	public PersonVO update(PersonVO person) {
		if(person==null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one person!");
		var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id."));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		entity.setAddress(person.getAddress());


		PersonVO vo = ModelMapperUtil.parseObject(repository.save(ModelMapperUtil.parseObject(entity, Person.class)), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id) {

		logger.info("Deleting one person!");
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id."));
		repository.delete(entity);

	}


}
