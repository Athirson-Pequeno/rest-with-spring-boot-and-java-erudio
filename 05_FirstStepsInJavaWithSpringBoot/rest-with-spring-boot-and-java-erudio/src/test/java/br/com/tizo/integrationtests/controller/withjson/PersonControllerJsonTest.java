package br.com.tizo.integrationtests.controller.withjson;

import br.com.tizo.config.TestConfigs;
import br.com.tizo.data.vo.v1.security.TokenVO;
import br.com.tizo.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.tizo.integrationtests.vo.PersonVO;
import br.com.tizo.integrationtests.vo.security.AccountCredentialsVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

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
        assertNotNull(createdPerson.getFirst_name());
        assertNotNull(createdPerson.getLast_name());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());

        assertEquals("Lambu", createdPerson.getFirst_name());
        assertEquals("Caolho", createdPerson.getLast_name());
        assertEquals("Sousa, Paraiba, Brasil", createdPerson.getAddress());
        assertEquals("Macho", createdPerson.getGender());


    }

    @Test
    @Order(2)
    public void testUpdate() throws IOException {
        person.setLast_name("Caatinga");


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
        assertNotNull(createdPerson.getFirst_name());
        assertNotNull(createdPerson.getLast_name());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());

        assertEquals("Lambu", createdPerson.getFirst_name());
        assertEquals("Caatinga", createdPerson.getLast_name());
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
        assertNotNull(persistedPerson.getFirst_name());
        assertNotNull(persistedPerson.getLast_name());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getAddress());
        assertTrue(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Lambu", persistedPerson.getFirst_name());
        assertEquals("Caatinga", persistedPerson.getLast_name());
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
        assertNotNull(persistedPerson.getFirst_name());
        assertNotNull(persistedPerson.getLast_name());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getAddress());
        assertFalse(persistedPerson.getEnabled());

        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Lambu", persistedPerson.getFirst_name());
        assertEquals("Caatinga", persistedPerson.getLast_name());
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
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<PersonVO> people = objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {
        });

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirst_name());
        assertNotNull(foundPersonOne.getLast_name());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals(1, foundPersonOne.getId());

        assertEquals("Athirson", foundPersonOne.getFirst_name());
        assertEquals("Pequeno", foundPersonOne.getLast_name());
        assertEquals("Queimadas - PB - BR", foundPersonOne.getAddress());
        assertEquals("male", foundPersonOne.getGender());

        PersonVO foundPersonFour = people.get(3);

        assertNotNull(foundPersonFour.getId());
        assertNotNull(foundPersonFour.getFirst_name());
        assertNotNull(foundPersonFour.getLast_name());
        assertNotNull(foundPersonFour.getAddress());
        assertNotNull(foundPersonFour.getGender());


        assertEquals(4, foundPersonFour.getId());

        assertEquals("Lurdinha", foundPersonFour.getFirst_name());
        assertEquals("Nascimento", foundPersonFour.getLast_name());
        assertEquals("Queimadas - PB - BR", foundPersonFour.getAddress());
        assertEquals("female", foundPersonFour.getGender());
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


    private void mockPerson() {

        person.setFirst_name("Lambu");
        person.setLast_name("Caolho");
        person.setAddress("Sousa, Paraiba, Brasil");
        person.setGender("Macho");
        person.setEnabled(true);

    }

}
