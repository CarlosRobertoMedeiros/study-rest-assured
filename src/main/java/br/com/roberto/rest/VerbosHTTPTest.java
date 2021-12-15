package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class VerbosHTTPTest {
	
	@Test
	public void deveSalvarUsuario() {
		
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\":\"Jose\" , \"age\":50 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
		
	}
	
	//Continuar a partir do validacao-ao-salvar

}
