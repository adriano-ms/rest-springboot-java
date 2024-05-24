package br.com.adrianoms.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.adrianoms.controllers.BookController;
import br.com.adrianoms.data.vo.v1.BookVO;
import br.com.adrianoms.exceptions.RequiredObjectIsNullException;
import br.com.adrianoms.exceptions.ResourceNotFoundException;
import br.com.adrianoms.mapper.DozerMapper;
import br.com.adrianoms.model.Book;
import br.com.adrianoms.repositories.BookRepository;

@Service
public class BookServices {

	@Autowired
	private BookRepository repository;
	
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	public BookVO findById(Long id) {
		logger.info("Finding one book!");
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		BookVO vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public List<BookVO> findAll(){
		logger.info("Finding all books!");
		var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
		books.forEach(b -> {
			b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel());
		});
		return books;
	}
	
	public BookVO create(BookVO book) {
		if(book == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating one book!");
		var entity = repository.save(DozerMapper.parseObject(book, Book.class));
		BookVO vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public BookVO update(BookVO book) {
		if(book == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one book!");
		var entity = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		entity.setTitle(book.getTitle());
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		BookVO vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one book!");
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found to this ID!"));
		repository.delete(entity);
	}
}
