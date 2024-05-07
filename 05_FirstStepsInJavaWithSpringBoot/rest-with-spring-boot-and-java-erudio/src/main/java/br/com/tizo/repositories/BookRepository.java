package br.com.tizo.repositories;

import br.com.tizo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> { }
