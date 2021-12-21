package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import br.com.roberto.barrigatest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

/*
 * Segunda maneira de realizar os testes
 * Criando atrav�s de um usu�rio espec�fico todos os cen�rios
 * E assim executando para aquele usu�rio as funcionalidades de maneira independente
 * */
public class AuthTest extends BaseTest {
	
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization"); //A partir daqui eu retirei o token de autoriza��o
		given()
		.when()
			.get("/contas") 
		.then()
			.statusCode(401)
		;
	}
	
}
