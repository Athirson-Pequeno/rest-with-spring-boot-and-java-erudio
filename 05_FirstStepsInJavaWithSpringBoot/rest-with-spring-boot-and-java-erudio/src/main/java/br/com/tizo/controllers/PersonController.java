package br.com.tizo.controllers;

import br.com.tizo.data.vo.v1.PersonVO;
import br.com.tizo.data.vo.v2.PersonVOV2;
import br.com.tizo.services.PersonServices;
import br.com.tizo.util.MediaTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices service;
    //private PersonServices service =  new PersonServices();

    @GetMapping(produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    public List<PersonVO> findAll() throws Exception {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
            produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML},
            consumes = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    public PersonVO create(@RequestBody PersonVO person) throws Exception {
        return service.create(person);
    }

    @PostMapping(
            value = "/v2",
            produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML},
            consumes = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    public PersonVOV2 createV2(@RequestBody PersonVOV2 person) throws Exception {
        return service.createV2(person);
    }

    @PutMapping(
            produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML},
            consumes = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    public PersonVO update(@RequestBody PersonVO person) throws Exception {
        return service.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
