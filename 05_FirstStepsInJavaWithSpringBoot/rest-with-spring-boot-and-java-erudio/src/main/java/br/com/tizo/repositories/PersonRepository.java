package br.com.tizo.repositories;

import br.com.tizo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository  extends JpaRepository<Person, Long> { }
