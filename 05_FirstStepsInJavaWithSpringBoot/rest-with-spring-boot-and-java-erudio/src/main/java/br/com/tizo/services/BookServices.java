package br.com.tizo.services;

import br.com.tizo.controllers.BookController;
import br.com.tizo.data.vo.v1.BookVO;
import br.com.tizo.exceptions.RequiredObjectIsNullException;
import br.com.tizo.exceptions.ResourceNotFoundException;
import br.com.tizo.mapper.ModelMapperUtil;
import br.com.tizo.model.Book;
import br.com.tizo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Service
public class BookServices {

    @Autowired
    BookRepository bookRepository;


    public List<BookVO> findAll() {

        var books = ModelMapperUtil.parseListObjects(bookRepository.findAll(), BookVO.class);

        books.stream().forEach(bookVO -> {
            bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        });

        return books;
    }

    public BookVO findById(Integer id) {

        var book = ModelMapperUtil.parseObject(bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id.")), BookVO.class);
        book.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return book;
    }

    public BookVO create(BookVO book) {

        if(book==null) throw new RequiredObjectIsNullException();

        var entity = ModelMapperUtil.parseObject(book, Book.class);
        var bookVO = ModelMapperUtil.parseObject(bookRepository.save(entity), BookVO.class);

        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());

        return bookVO;
    }

    public BookVO update(BookVO book) {

        if(book==null) throw new RequiredObjectIsNullException();

        var entity = bookRepository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id."));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        BookVO bookVO = ModelMapperUtil.parseObject(bookRepository.save(entity), BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());

        return bookVO;
    }

    public void delete(Integer id) {

        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id."));
        bookRepository.delete(entity);

    }
}
