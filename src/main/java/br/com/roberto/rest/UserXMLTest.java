package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserXMLTest {

	public static RequestSpecification reqSpec;
	public static ResponseSpecification respSpec;
	
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 443;
//		RestAssured.basePath = "/v2"; //Normalmente altera as vers?es
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
		responseSpecBuilder.expectStatusCode(200);
		respSpec = responseSpecBuilder.build();
		
		//Colocando aqui eu herdo automaticamente
		RestAssured.requestSpecification= reqSpec;
		RestAssured.responseSpecification= respSpec;
				
	}
	
	@Test
	public void devoTrabalharComBaseUri() {

		given()
			//.spec(reqSpec)
		.when()
			.get("/usersXml/3")
		.then()
			//.statusCode(200)
			//.spec(respSpec)

			.rootPath("user")
			.body("name", is("Ana Julia"))
			.body("@id", is("3")) // Para XML tudo ? String

			.rootPath("user.filhos")
			.body("name.size()", is(2))

			.detachRootPath("filhos")
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			.appendRootPath("filhos")
			.body("name", hasItem("Zezinho"))
			.body("name", hasItems("Luizinho", "Zezinho"));
			;
	}
	
	@Test
	public void devoTrabalharComXML() {
		

		given().when().get("/usersXML/3").then().statusCode(200)

				.rootPath("user").body("name", is("Ana Julia")).body("@id", is("3")) // Para XML tudo ? String

				.rootPath("user.filhos").body("name.size()", is(2))

				.detachRootPath("filhos").body("filhos.name[0]", is("Zezinho")).body("filhos.name[1]", is("Luizinho"))

				.appendRootPath("filhos").body("name", hasItem("Zezinho"))
				.body("name", hasItems("Luizinho", "Zezinho"));
	}

	@Test
	public void devoFazerPesquisasAvancadasComXML() {
		given().when().get("/usersXML").then().statusCode(200)
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
		ArrayList<NodeImpl> nomes = given().when().get("/usersXML").then().statusCode(200)
				.extract().path("users.user.name.findAll{it.toString().contains('n')}");

		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXPath() {
		//Roseta Stone XPath vai mostrar um documento com v?rias caracter?sticas de busca com XPath
		given()
			.when()
				.get("/usersXML")
			.then()
				.statusCode(200)
				.body(hasXPath("count(/users/user)", is("3")))
				.body(hasXPath("/users/user[@id = '1']"))
				.body(hasXPath("//user[@id = '1']"))
				.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
				.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"),containsString("Luizinho"))))
				.body(hasXPath("/users/user/name", is("Jo?o da Silva")))
				.body(hasXPath("//name", is("Jo?o da Silva")))
				.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
				.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
				.body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))
				.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
				.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
				.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
				.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
			;
	}
//https://www.cod3r.com.br/courses/take/rest-assured/lessons/9520488-xpath
//Continuar a partir dos 4 minutos
}

