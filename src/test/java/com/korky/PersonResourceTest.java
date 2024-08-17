package com.korky;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.net.http.HttpResponse;

import org.apache.http.HttpStatus;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import org.hamcrest.Matchers;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
class PersonResourceTest {

    PersonEntity personJson = new PersonEntity("{\"name\": \"Jannet Hanna\",\"age\": 46}");
    String resultId;
    final String JANNET_HANNA = "Jannet Hanna";
    final String API_PERSON = "/api/person";

    // Hello Quarkus Test

    @Test
    @Order(1)
    void testPersonResourceHelloEndpoint() {
        given()
                .when().get("/api/hello")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello from Quarkus REST"));
    }

    @Test
    @Order(2)
    void testCreatePerson() {

        String resultId = given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post(API_PERSON)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();

        System.out.println("resultId: " + resultId);
    }

    @Test
    @Order(3)
    void testAnniversaryPerson() {

        // Query the database by name to get the document
        PersonEntity personEntity = given().log().all()
                .when().get(API_PERSON + "/" + JANNET_HANNA)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(JANNET_HANNA))
                .extract()
                .as(PersonEntity.class);

        resultId = personEntity.getId().toHexString();
        int expectedAge = personEntity.getAge() + 1;

        given().log().all()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put(API_PERSON + "/" + resultId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        // Query the database again and check to see if the age was in fact increased
        given().log().all()
                .when().get(API_PERSON + "/" + JANNET_HANNA)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(JANNET_HANNA))
                .body("age", equalTo(expectedAge));
    }

    @Test
    @Order(4)
    void testDeletePerson() {

        PersonEntity personEntity = given().log().all()
                .when().get(API_PERSON + "/" + JANNET_HANNA)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(JANNET_HANNA))
                .extract()
                .as(PersonEntity.class);

        resultId = personEntity.getId().toHexString();

        given().log().all()
                .when().delete(API_PERSON + "/" + resultId)
                .then()
                .assertThat()
                .statusCode(200);
    }
}