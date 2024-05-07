package br.com.tizo.unittests.mapper.mocks;

import br.com.tizo.data.vo.v1.BookVO;
import br.com.tizo.model.Book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number);
        book.setTitle("Title Test" + number);
        book.setAuthor("Author test" + number);
        book.setPrice(BigDecimal.valueOf(number));
        book.setLaunchDate(new Date(number));


        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setTitle("Title Test" + number);
        book.setPrice(BigDecimal.valueOf(number));
        book.setLaunchDate(new Date(number));
        book.setKey(number);
        book.setAuthor("Author test" + number);
        return book;
    }

}
