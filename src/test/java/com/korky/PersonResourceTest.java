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

    // Hello Quarkus Test

    @Test
    @Order(1)
    void testPersonResourceHelloEndpoint() {
        given()
                .when().get("/api/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }

    @Test
    @Order(2)
    void testCreatePerson() {

        String resultId = given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/api/person")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();

        System.out.println("resultId: " + resultId);
    }

    @Test
    @Order(3)
    void testDeletePerson() {

        final String name = "Jannet Hanna";

        PersonEntity personEntity = given().log().all()
                .when().get("/api/person/" + name)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(name))
                .extract()
                .as(PersonEntity.class);

        resultId = personEntity.getId().toHexString();

        ValidatableResponse response = given().log().all()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().delete("/api/person/" + resultId)
                .then()
                .statusCode(200);
    }
}