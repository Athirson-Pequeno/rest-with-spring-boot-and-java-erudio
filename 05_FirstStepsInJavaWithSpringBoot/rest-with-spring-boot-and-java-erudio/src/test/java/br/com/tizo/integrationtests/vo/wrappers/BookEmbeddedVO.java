package br.com.tizo.integrationtests.vo.wrappers;

import br.com.tizo.integrationtests.vo.BookVO;
import br.com.tizo.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@XmlRootElement
public class BookEmbeddedVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("bookVOList")
    private List<BookVO> books;

    public BookEmbeddedVO() {
    }

    public List<BookVO> getBooks() {
        return books;
    }

    public void setPersons(List<BookVO> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEmbeddedVO that)) return false;
        return Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(books);
    }
}
