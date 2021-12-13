package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.internal.path.xml.NodeImpl;

public class UserXMLTest {

	@Test
	public void devoTrabalharComXML() {

		given().when().get("https://restapi.wcaquino.me/usersXML/3").then().statusCode(200)

				.rootPath("user").body("name", is("Ana Julia")).body("@id", is("3")) // Para XML tudo � String

				.rootPath("user.filhos").body("name.size()", is(2))

				.detachRootPath("filhos").body("filhos.name[0]", is("Zezinho")).body("filhos.name[1]", is("Luizinho"))

				.appendRootPath("filhos").body("name", hasItem("Zezinho"))
				.body("name", hasItems("Luizinho", "Zezinho"));
	}

	@Test
	public void devoFazerPesquisasAvancadasComXML() {
		given().when().get("https://restapi.wcaquino.me/usersXML").then().statusCode(200)
				.body("users.user.size()", is(3)).body("users.user.findAll{it.age.toInteger()<=25}.size()", is(2))
				.body("users.user.@id", hasItems("1", "2", "3"))
				.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
				.body("users.user.findAll{it.name.toString().contains('n')}.name",
						hasItems("Maria Joaquina", "Ana Julia"))
				.body("users.user.salary.find{it!=null}.toDouble()", is(1234.5678d))
				.body("users.user.age.collect{it.toInteger() * 2 }", hasItems(40, 50, 60))
				.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}",
						is("MARIA JOAQUINA"));
	}

	@Test
	public void devoFazerPesquisasAvancadasComXMLEJava() {
		ArrayList<NodeImpl> nomes = given().when().get("https://restapi.wcaquino.me/usersXML").then().statusCode(200)
				.extract().path("users.user.name.findAll{it.toString().contains('n')}");

		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXPath() {
		
		given()
			.when()
				.get("https://restapi.wcaquino.me/usersXML")
			.then()
				.statusCode(200)
				.body(hasXPath("count(/users/user)", is("3")))
			;
	}
//https://www.cod3r.com.br/courses/take/rest-assured/lessons/9520488-xpath
//Continuar a partir dos 4 minutos
}
