package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.roberto.barrigatest.core.BaseTest;
import br.com.roberto.barrigatest.utils.BarrigaUtils;

/*
 * Segunda maneira de realizar os testes
 * Criando atrav�s de um usu�rio espec�fico todos os cen�rios
 * E assim executando para aquele usu�rio as funcionalidades de maneira independente
 * */
public class SaldoTest extends BaseTest {
	
	
	@Test
	public void deveCalcularSaldoContas() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para saldo");
				
		given()
		.when()
			.get("/saldo") 
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))

		;
	}
	
	
	
}
