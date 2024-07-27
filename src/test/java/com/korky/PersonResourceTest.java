package com.korky;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.bson.json.JsonObject;

@QuarkusTest
class PersonResourceTest {

    PersonEntity personJson = new PersonEntity("{\"name\": \"Jannet Hanna\",\"age\": 46}");
    String resultId;

    // Hello Quarkus Test

    @Test
    void testPersonResourceHelloEndpoint() {
        given()
                .when().get("/api/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testCreatePerson() {

        try {
        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/api/person")
                .then()
                .statusCode(200)
                .body(notNullValue());

        resultId = response.extract().asString();
        System.out.println("resultId: " + resultId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}