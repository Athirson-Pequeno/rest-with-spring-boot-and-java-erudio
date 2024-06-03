package br.com.tizo.services;

import br.com.tizo.controllers.BookController;
import br.com.tizo.data.vo.v1.BookVO;
import br.com.tizo.exceptions.RequiredObjectIsNullException;
import br.com.tizo.exceptions.ResourceNotFoundException;
import br.com.tizo.mapper.ModelMapperUtil;
import br.com.tizo.model.Book;
import br.com.tizo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<BookVO> assembler;

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

        var bookPage = bookRepository.findAll(pageable);
        var bookVOsPage = bookPage.map(book -> ModelMapperUtil.parseObject(book, BookVO.class));
        bookVOsPage.map(bookVO -> bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel()));


        var books = ModelMapperUtil.parseListObjects(bookRepository.findAll(), BookVO.class);


        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(),"asc")).withSelfRel();
        return assembler.toModel(bookVOsPage, link);
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
