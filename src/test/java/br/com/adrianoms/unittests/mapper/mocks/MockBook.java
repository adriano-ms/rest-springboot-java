package br.com.adrianoms.unittests.mapper.mocks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.adrianoms.data.vo.v1.BookVO;
import br.com.adrianoms.model.Book;

public class MockBook {
	
	public Book mockEntity() throws ParseException {
		return mockEntity(0);
	}
	
	public BookVO mockVO() throws ParseException {
		return mockVO(0);
	}
	
	public List<Book> mockEntityList() throws ParseException{
		List<Book> books = new ArrayList<>();
		for(int i = 0; i < 14; i++) {
			books.add(mockEntity(i));
		}
		return books;
	}
	
	public List<BookVO> mockVOList() throws ParseException{
		List<BookVO> books = new ArrayList<>();
		for(int i = 0; i < 14; i++) {
			books.add(mockVO(i));
		}
		return books;
	}
	
	public Book mockEntity(Integer number) throws ParseException {
		Book book = new Book();
		book.setId(number.longValue());
		book.setTitle("Title Test" + number);
		book.setAuthor("Author Test" + number);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		book.setLaunchDate(sdf.parse(number +"-05-01"));
		book.setPrice(number.doubleValue());
		return book;
	}
	
	public BookVO mockVO(Integer number) throws ParseException {
		BookVO vo = new BookVO();
		vo.setKey(number.longValue());
		vo.setTitle("Title Test" + number);
		vo.setAuthor("Author Test" + number);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		vo.setLaunchDate(sdf.parse(number +"-05-01"));
		vo.setPrice(number.doubleValue());
		return vo;
	}
}
