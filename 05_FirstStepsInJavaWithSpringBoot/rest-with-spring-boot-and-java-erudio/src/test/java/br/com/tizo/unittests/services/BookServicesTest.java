package br.com.tizo.unittests.services;


import br.com.tizo.data.vo.v1.BookVO;
import br.com.tizo.exceptions.RequiredObjectIsNullException;
import br.com.tizo.model.Book;
import br.com.tizo.repositories.BookRepository;
import br.com.tizo.services.BookServices;
import br.com.tizo.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	BookRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);
		entity.setId(1);
		
		when(repository.findById(1)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Author test1", result.getAuthor());
		assertEquals("Title Test1", result.getTitle());
		assertEquals(BigDecimal.valueOf(result.getKey()), result.getPrice());
		assertNotNull(result.getLaunchDate());
	}
	
	@Test
	void testCreate() {
		Book entity = input.mockEntity(1);
		entity.setId(1);
		
		Book persisted = entity;
		persisted.setId(1);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Author test1", result.getAuthor());
		assertEquals("Title Test1", result.getTitle());
		assertEquals(BigDecimal.valueOf(result.getKey()), result.getPrice());
		assertNotNull(result.getLaunchDate());
	}
	
	@Test
	void testCreateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void testUpdate() {
		Book entity = input.mockEntity(1);
		entity.setId(1);
		
		Book persisted = entity;
		persisted.setId(1);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1);
		

		when(repository.findById(1)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Author test1", result.getAuthor());
		assertEquals("Title Test1", result.getTitle());
		assertEquals(BigDecimal.valueOf(result.getKey()), result.getPrice());
		assertNotNull(result.getLaunchDate());

	}
	

	
	@Test
	void testUpdateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {
		Book entity = input.mockEntity(1); 
		entity.setId(1);
		
		when(repository.findById(1)).thenReturn(Optional.of(entity));
		
		service.delete(1);
	}


}
