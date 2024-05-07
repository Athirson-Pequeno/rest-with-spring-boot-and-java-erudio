package br.com.tizo.unittests.mapper;

import br.com.tizo.data.vo.v1.BookVO;
import br.com.tizo.mapper.ModelMapperUtil;
import br.com.tizo.model.Book;
import br.com.tizo.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperBookConverterTest {
    
    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToVOTest() {
        BookVO output = ModelMapperUtil.parseObject(inputObject.mockEntity(), BookVO.class);
        assertEquals(Integer.valueOf(0), output.getKey());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Author test0", output.getAuthor());

        assertEquals(BigDecimal.valueOf(output.getKey()), output.getPrice());
        assertEquals(new Date(output.getKey()), output.getLaunchDate());

    }

    @Test
    public void parseEntityListToVOListTest() {
        List<BookVO> outputList = ModelMapperUtil.parseListObjects(inputObject.mockEntityList(), BookVO.class);

        BookVO outputZero = outputList.get(0);

        assertEquals(Integer.valueOf(0), outputZero.getKey());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Author test0", outputZero.getAuthor());
        assertEquals(BigDecimal.valueOf(outputZero.getKey()), outputZero.getPrice());
        assertEquals(new Date(outputZero.getKey()), outputZero.getLaunchDate());

        BookVO outputSete = outputList.get(7);

        assertEquals(Integer.valueOf(7), outputSete.getKey());
        assertEquals("Title Test7", outputSete.getTitle());
        assertEquals("Author test7", outputSete.getAuthor());
        assertEquals(BigDecimal.valueOf(outputSete.getKey()), outputSete.getPrice());
        assertEquals(new Date(outputSete.getKey()), outputSete.getLaunchDate());

        BookVO outputDoze = outputList.get(12);

        assertEquals(Integer.valueOf(12), outputDoze.getKey());
        assertEquals("Title Test12", outputDoze.getTitle());
        assertEquals("Author test12", outputDoze.getAuthor());
        assertEquals(BigDecimal.valueOf(outputDoze.getKey()), outputDoze.getPrice());
        assertEquals(new Date(outputDoze.getKey()), outputDoze.getLaunchDate());
    }

    @Test
    public void parseVOToEntityTest() {
        Book output = ModelMapperUtil.parseObject(inputObject.mockEntity(), Book.class);
        assertEquals(Integer.valueOf(0), output.getId());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Author test0", output.getAuthor());

        assertEquals(BigDecimal.valueOf(output.getId()), output.getPrice());
        assertEquals(new Date(output.getId()), output.getLaunchDate());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Book> outputList = ModelMapperUtil.parseListObjects(inputObject.mockEntityList(), Book.class);

        Book outputZero = outputList.get(0);

        assertEquals(Integer.valueOf(0), outputZero.getId());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Author test0", outputZero.getAuthor());
        assertEquals(BigDecimal.valueOf(outputZero.getId()), outputZero.getPrice());
        assertEquals(new Date(outputZero.getId()), outputZero.getLaunchDate());

        Book outputSete = outputList.get(7);

        assertEquals(Integer.valueOf(7), outputSete.getId());
        assertEquals("Title Test7", outputSete.getTitle());
        assertEquals("Author test7", outputSete.getAuthor());
        assertEquals(BigDecimal.valueOf(outputSete.getId()), outputSete.getPrice());
        assertEquals(new Date(outputSete.getId()), outputSete.getLaunchDate());

        Book outputDoze = outputList.get(12);

        assertEquals(Integer.valueOf(12), outputDoze.getId());
        assertEquals("Title Test12", outputDoze.getTitle());
        assertEquals("Author test12", outputDoze.getAuthor());
        assertEquals(BigDecimal.valueOf(outputDoze.getId()), outputDoze.getPrice());
        assertEquals(new Date(outputDoze.getId()), outputDoze.getLaunchDate());
    }
}
