package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.roberto.barrigatest.core.BaseTest;
import br.com.roberto.barrigatest.utils.BarrigaUtils;

/*
 * Segunda maneira de realizar os testes
 * Criando através de um usuário específico todos os cenários
 * E assim executando para aquele usuário as funcionalidades de maneira independente
 * */
public class ContasTest extends BaseTest {
	
	
	@Test
	public void deveIncluirContaComSucesso() {
		given()
			.body("{\"nome\":\"Conta Inserida\"}")
		.when()
			.post("/contas") 
		.then()
			.statusCode(201)
			
		;
		
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
		
		given()
			.body("{\"nome\":\"conta alterada\"}")
			.pathParam("id",CONTA_ID)
		.when()
			.put("/contas/{id}") 
		.then()
			.statusCode(200)
			.body("nome", is("conta alterada"))
		;
		
	}
	
	@Test
	public void naoDeveInserirContaComMesmoNome() {
		given()
			.body("{\"nome\":\"Conta mesmo nome\"}")
		.when()
			.post("/contas") 
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	
}
