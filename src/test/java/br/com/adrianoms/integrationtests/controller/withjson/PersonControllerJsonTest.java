package br.com.adrianoms.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.adrianoms.configs.TestConfigs;
import br.com.adrianoms.exceptions.ExceptionResponse;
import br.com.adrianoms.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.adrianoms.integrationtests.vo.AccountCredentialsVO;
import br.com.adrianoms.integrationtests.vo.PersonVO;
import br.com.adrianoms.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	private static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO credentials = new AccountCredentialsVO("adriano", "123456");

		var accessToken = given()
			.basePath("/auth/signin")
			.port(TestConfigs.SERVER_PORT)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(credentials)
			.when()
				.post()
			.then()
				.statusCode(200)
				.extract().body().as(TokenVO.class).getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ADRIANOMS)
			.body(person)
			.when()
				.post()
			.then()
				.statusCode(200)
				.extract().body().asString();

		assertNotNull(content);
		
		PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
		person = createdPerson;

		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Lidia", createdPerson.getFirstName());
		assertEquals("Santana", createdPerson.getLastName());
		assertEquals("Pernambuco", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GOOGLE)
			.body(person)
			.when()
				.post()
			.then()
				.statusCode(403)
				.extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ADRIANOMS)
			.pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(200)
				.extract().body().asString();

		assertNotNull(content);
		
		PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
		person = createdPerson;

		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Lidia", createdPerson.getFirstName());
		assertEquals("Santana", createdPerson.getLastName());
		assertEquals("Pernambuco", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GOOGLE)
			.pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(403)
				.extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException, IOException {

		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ADRIANOMS)
			.when()
				.get()
			.then()
				.statusCode(200)
				.extract().body().asString();

		assertNotNull(content);
		
		List<PersonVO> createdPersons = 
				objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {});

		assertTrue(createdPersons != null);
		assertTrue(!createdPersons.isEmpty());
		createdPersons.forEach(createdPerson -> {
			assertNotNull(createdPerson);
			assertNotNull(createdPerson.getId());
			assertNotNull(createdPerson.getFirstName());
			assertNotNull(createdPerson.getLastName());
			assertNotNull(createdPerson.getAddress());
			assertNotNull(createdPerson.getGender());

			assertTrue(createdPerson.getId() > 0);
		});
	}

	@Test
	@Order(6)
	public void testFindAllWithWrongOrigin() throws JsonMappingException, JsonProcessingException, IOException {

		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GOOGLE)
			.when()
				.get()
			.then()
				.statusCode(403)
				.extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
		
	}
	
	@Test
	@Order(7)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		person.setFirstName("Lydia");
		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ADRIANOMS)
			.body(person)
			.when()
				.put()
			.then()
				.statusCode(200)
				.extract().body().asString();
		
		assertNotNull(content);
		
		PersonVO updatedPerson = objectMapper.readValue(content, PersonVO.class);
		
		assertNotNull(updatedPerson);
		assertNotNull(updatedPerson.getId());
		assertNotNull(updatedPerson.getFirstName());
		assertNotNull(updatedPerson.getLastName());
		assertNotNull(updatedPerson.getAddress());
		assertNotNull(updatedPerson.getGender());
		
		assertTrue(updatedPerson.getId() > 0);

		assertEquals(updatedPerson.getId(), person.getId());
		assertEquals("Lydia", updatedPerson.getFirstName());
		assertEquals("Santana", updatedPerson.getLastName());
		assertEquals("Pernambuco", updatedPerson.getAddress());
		assertEquals("Female", updatedPerson.getGender());
	}
	
	@Test
	@Order(8)
	public void testUpdateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		person.setFirstName("Lydia");
		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GOOGLE)
			.body(person)
			.when()
				.put()
			.then()
				.statusCode(403)
				.extract().body().asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
	
	@Test
	@Order(9)
	public void testDelete() {
		given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ADRIANOMS)
			.pathParam("id", person.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(204);
		
		var content = 
			given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ADRIANOMS)
				.pathParam("id", person.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(404)
					.extract().body().as(ExceptionResponse.class).getMessage();
		
		assertNotNull(content);
		assertEquals(content, "No records found to this ID!");
	}
	
	@Test
	@Order(10)
	public void testDeleteWithWrongOrigin() {
		var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.headers(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_GOOGLE)
			.pathParam("id", person.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(403)
				.extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
	
	private void mockPerson() {
		person.setFirstName("Lidia");
		person.setLastName("Santana");
		person.setAddress("Pernambuco");
		person.setGender("Female");
	}
}
