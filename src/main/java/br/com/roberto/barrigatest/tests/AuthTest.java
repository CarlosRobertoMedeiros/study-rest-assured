package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import br.com.roberto.barrigatest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

/*
 * Segunda maneira de realizar os testes
 * Criando através de um usuário específico todos os cenários
 * E assim executando para aquele usuário as funcionalidades de maneira independente
 * */
public class AuthTest extends BaseTest {
	
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization"); //A partir daqui eu retirei o token de autorização
		given()
		.when()
			.get("/contas") 
		.then()
			.statusCode(401)
		;
	}
	
}
