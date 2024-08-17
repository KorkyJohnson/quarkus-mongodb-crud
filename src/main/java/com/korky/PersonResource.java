package com.korky;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    PersonRepository personRepository;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    @Path("/person")
    public String createPerson(PersonEntity person) {
        return personRepository.add(person);
    }

    @GET
    @Path("/persons")
    public List<PersonEntity> getPersons() {
        return personRepository.getPersons();
    }

    @GET
    @Path("/person/{name}")
    public PersonEntity getPerson(@PathParam("name") String name) {
        return personRepository.getPerson(name);
    }

    @PUT
    @Path("/person/{id}")
    public long anniversaryPerson(@PathParam("id") String id) {
        return personRepository.anniversaryPerson(id);
    }

    @DELETE
    @Path("person/{id}")
    public long deletePerson(@PathParam("id") String id) {
        return personRepository.deletePerson(id);
    }
}
