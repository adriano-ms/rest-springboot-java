package br.com.adrianoms.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.adrianoms.data.vo.v1.BookVO;
import br.com.adrianoms.exceptions.RequiredObjectIsNullException;
import br.com.adrianoms.model.Book;
import br.com.adrianoms.repositories.BookRepository;
import br.com.adrianoms.services.BookServices;
import br.com.adrianoms.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {
	
	MockBook input;
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	BookRepository repository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() throws ParseException {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Title Test1", result.getTitle());
		assertEquals("Author Test1", result.getAuthor());
		assertEquals(sdf.parse("1-05-01"), result.getLaunchDate());
		assertEquals(Double.valueOf(1L), result.getPrice());
	}

	@Test
	void testFindAll() throws ParseException {
		List<Book> entities = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(entities);
		
		var people = service.findAll();
		assertNotNull(people);
		assertEquals(14, people.size());
		
		people.stream().forEach(b -> {
			assertNotNull(b);
			assertNotNull(b.getKey());
			assertNotNull(b.getLinks());
			assertTrue(b.toString().contains("links: [</api/book/v1/"+ b.getKey() +">;rel=\"self\"]"));
			int index = + people.indexOf(b);
			assertEquals("Title Test" + index, b.getTitle());
			assertEquals("Author Test" + index, b.getAuthor());
			try {
				assertEquals(sdf.parse(index + "-05-01"), b.getLaunchDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			assertEquals(Double.valueOf(index), b.getPrice());
		});
	}

	@Test
	void testCreate() throws ParseException {
		Book entity = input.mockEntity(1);
		Book persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Title Test1", result.getTitle());
		assertEquals("Author Test1", result.getAuthor());
		assertEquals(sdf.parse("1-05-01"), result.getLaunchDate());
		assertEquals(Double.valueOf(1L), result.getPrice());
	}
	
	@Test
	void testCreateWithNullBook() {
		Exception e = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
		String expectedMsg = "It is not allowed to persist a null object!";
		String actualMsg = e.getMessage();
		assertTrue(actualMsg.contains(expectedMsg));
	}

	@Test
	void testUpdate() throws ParseException {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		Book persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Title Test1", result.getTitle());
		assertEquals("Author Test1", result.getAuthor());
		assertEquals(sdf.parse("1-05-01"), result.getLaunchDate());
		assertEquals(Double.valueOf(1L), result.getPrice());
	}
	
	@Test
	void testUpdateWithNullBook() {
		Exception e = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
		String expectedMsg = "It is not allowed to persist a null object!";
		String actualMsg = e.getMessage();
		assertTrue(actualMsg.contains(expectedMsg));
	}

	@Test
	void testDelete() throws ParseException {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
	}

}
