package br.com.adrianoms.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.adrianoms.data.vo.v1.BookVO;
import br.com.adrianoms.mapper.DozerMapper;
import br.com.adrianoms.model.Book;
import br.com.adrianoms.unittests.mapper.mocks.MockBook;

public class DozerBookConverterTest {
    
    MockBook inputObject;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToVOTest() throws ParseException {
        BookVO output = DozerMapper.parseObject(inputObject.mockEntity(), BookVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Author Test0", output.getAuthor());
        assertEquals(sdf.parse("0-05-01"), output.getLaunchDate());
        assertEquals(Double.valueOf(0), output.getPrice());
    }

    @Test
    public void parseEntityListToVOListTest() throws ParseException {
        List<BookVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), BookVO.class);
        BookVO outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals(sdf.parse("0-05-01"), outputZero.getLaunchDate());
        assertEquals(Double.valueOf(0), outputZero.getPrice());
        
        BookVO outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals(sdf.parse("7-05-01"), outputSeven.getLaunchDate());
        assertEquals(Double.valueOf(7), outputSeven.getPrice());
        
        BookVO outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals(sdf.parse("12-05-01"), outputTwelve.getLaunchDate());
        assertEquals(Double.valueOf(12), outputTwelve.getPrice());
    }

    @Test
    public void parseVOToEntityTest() throws ParseException {
        Book output = DozerMapper.parseObject(inputObject.mockVO(), Book.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Author Test0", output.getAuthor());
        assertEquals(sdf.parse("0-05-01"), output.getLaunchDate());
        assertEquals(Double.valueOf(0), output.getPrice());
    }

    @Test
    public void parserVOListToEntityListTest() throws ParseException {
        List<Book> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Book.class);
        Book outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals(sdf.parse("0-05-01"), outputZero.getLaunchDate());
        assertEquals(Double.valueOf(0), outputZero.getPrice());
        
        Book outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals(sdf.parse("7-05-01"), outputSeven.getLaunchDate());
        assertEquals(Double.valueOf(7), outputSeven.getPrice());
        
        Book outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals(sdf.parse("12-05-01"), outputTwelve.getLaunchDate());
        assertEquals(Double.valueOf(12), outputTwelve.getPrice());
    }
}
