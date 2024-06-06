package br.com.tizo.integrationtests.controller.withjson;

import br.com.tizo.config.TestConfigs;
import br.com.tizo.data.vo.v1.security.TokenVO;
import br.com.tizo.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.tizo.integrationtests.vo.PersonVO;
import br.com.tizo.integrationtests.vo.security.AccountCredentialsVO;
import br.com.tizo.integrationtests.vo.wrappers.WrapperPersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();

    }

    @Test
    @Order(0)
    public void authorization() throws IOException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken =
                given()
                        .basePath("/auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class)
                        .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws IOException {
        mockPerson();


        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(person)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;

        assertTrue(createdPerson.getId() > 0);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());

        assertEquals("Lambu", createdPerson.getFirstName());
        assertEquals("Caolho", createdPerson.getLastName());
        assertEquals("Sousa, Paraiba, Brasil", createdPerson.getAddress());
        assertEquals("Macho", createdPerson.getGender());


    }

    @Test
    @Order(2)
    public void testUpdate() throws IOException {
        person.setLastName("Caatinga");


        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(person)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;

        assertEquals(createdPerson.getId(), person.getId());

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());

        assertEquals("Lambu", createdPerson.getFirstName());
        assertEquals("Caatinga", createdPerson.getLastName());
        assertEquals("Sousa, Paraiba, Brasil", createdPerson.getAddress());
        assertEquals("Macho", createdPerson.getGender());


    }

    @Test
    @Order(3)
    public void testFindById() throws IOException {
        mockPerson();

        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("id", person.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertTrue(persistedPerson.getId() > 0);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getAddress());
        assertTrue(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Lambu", persistedPerson.getFirstName());
        assertEquals("Caatinga", persistedPerson.getLastName());
        assertEquals("Sousa, Paraiba, Brasil", persistedPerson.getAddress());
        assertEquals("Macho", persistedPerson.getGender());


    }

    @Test
    @Order(4)
    public void disablePersonById() throws IOException {

        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("id", person.getId())
                        .when()
                        .patch("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertTrue(persistedPerson.getId() > 0);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getAddress());
        assertFalse(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Lambu", persistedPerson.getFirstName());
        assertEquals("Caatinga", persistedPerson.getLastName());
        assertEquals("Sousa, Paraiba, Brasil", persistedPerson.getAddress());
        assertEquals("Macho", persistedPerson.getGender());


    }

    @Test
    @Order(5)
    public void testDelete() throws IOException {

        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("id", person.getId())
                        .when()
                        .delete("{id}")
                        .then()
                        .statusCode(204);


    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonVO wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersons();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals(266, foundPersonOne.getId());

        assertEquals("Alisha", foundPersonOne.getFirstName());
        assertEquals("MacLoughlin", foundPersonOne.getLastName());
        assertEquals("17388 Logan Avenue", foundPersonOne.getAddress());
        assertEquals("Bigender", foundPersonOne.getGender());
        assertFalse(foundPersonOne.getEnabled());

        PersonVO foundPersonFour = people.get(3);

        assertNotNull(foundPersonFour.getId());
        assertNotNull(foundPersonFour.getFirstName());
        assertNotNull(foundPersonFour.getLastName());
        assertNotNull(foundPersonFour.getAddress());
        assertNotNull(foundPersonFour.getGender());


        assertEquals(978, foundPersonFour.getId());

        assertEquals("Almeta", foundPersonFour.getFirstName());
        assertEquals("Alam", foundPersonFour.getLastName());
        assertEquals("161 Merchant Junction", foundPersonFour.getAddress());
        assertEquals("Female", foundPersonFour.getGender());
        assertTrue(foundPersonFour.getEnabled());
    }

    @Test
    @Order(7)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }


    @Test
    @Order(8)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("firstName", "ath")
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("findPersonsByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonVO wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersons();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals(294, foundPersonOne.getId());

        assertEquals("Agathe", foundPersonOne.getFirstName());
        assertEquals("Risbie", foundPersonOne.getLastName());
        assertEquals("153 Kinsman Street", foundPersonOne.getAddress());
        assertEquals("Female", foundPersonOne.getGender());
        assertTrue(foundPersonOne.getEnabled());

    }

    @Test
    @Order(9)
    public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/266\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/904\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/859\"}}}"));

        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));

        assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1004,\"totalPages\":101,\"number\":3}}"));
    }

    private void mockPerson() {

        person.setFirstName("Lambu");
        person.setLastName("Caolho");
        person.setAddress("Sousa, Paraiba, Brasil");
        person.setGender("Macho");
        person.setEnabled(true);

    }

}
