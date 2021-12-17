package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class AuthTest {
	
	@Test(expected = AssertionError.class)
	public void deveAcessarApiPublicaSWAPI() {
		//Deveria ter retornado corretamente, mudaram a api
		given()
			.log().all()
			.contentType(ContentType.JSON)
		.when()
			.get("https://swapi.co/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
			
		;
		
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
			
			;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() {
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			
			;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() {
		given()
			.log().all()
			.auth().basic("admin","senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			
			;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica3() {
		//Usando Challenge ou Primitiva
		given()
			.log().all()
			.auth().preemptive().basic("admin","senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			
			;
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT() {
		
		Map<String,String> login = new HashMap<String,String>();
		login.put("email", "carlosmedeiroslima@gmail.com");
		login.put("senha", "123456");
		
		//Login na Api
		//Reveber o Token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");
		;
		
		//Obter as Contas
		given()
			.log().all()
			.header("Authorization", "JWT "+ token)
			.contentType(ContentType.JSON)
		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta de teste")) //Usei hasItem por ter uma coleção de contas e não uma apenas
		;
	}
	

}
