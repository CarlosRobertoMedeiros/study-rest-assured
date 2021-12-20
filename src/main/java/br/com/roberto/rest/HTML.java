package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

//Não é muito comum testar HTML com Rest Assured
public class HTML {
	
	@Test
	public void deveFazerBuscasComHTML() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
			;
	}
	
	
	@Test
	public void deveFazerBuscasComXpathEmHTML() {
		//Pode dar erro devido a varias libs implementadas no xml
		//usei um path sem libs para realizar esses testes
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=clean")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body(hasXPath("count(//table/tr)", is("4")))
			.body(hasXPath("//td[text() = '2']/../td[2]", is("Maria Joaquina")))
			;
	}

	/*
	 * Testes para serem realizados em aplicação Web não Api diretamente*
	 */
	@Test
	public void deveAcessarAplicacaoWeb() {
		//login
		String cookie =  given()
			.log().all()
			.formParam("email", "carlosmedeiroslima@gmail.com")
			.formParam("senha", "123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))//Para sistemas com login
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")

		;
		
		cookie = cookie.split("=")[1].split(";")[0]; //Olhar com cuidado
				
		//obterConta
		String body = given()
			.log().all()
			.cookie("connect.sid",cookie)
		.when()
		  	.get("http://seubarriga.wcaquino.me/contas")
	   .then()
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
			.extract().body().asString()

		;
		
		System.out.println("--------------------------------------");
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
			
	}
	
	 
	 
	 

}
