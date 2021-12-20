package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.com.roberto.barrigatest.core.BaseTest;

public class BarrigaTest extends BaseTest {
	
	private static String TOKEN;
	
	@Before
	public void login() {
		Map<String,String> login = new HashMap<String, String>();
		login.put("email","carlosmedeiroslima@gmail.com");
		login.put("senha","123456");
		
		//Capturar Token
		TOKEN = given()
				.body(login)
			.when()
				.post("/signin") 
			.then()
				.statusCode(200)
				.extract().path("token");
	
		;
	}
	
	
	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
			.get("/contas") 
		.then()
			.statusCode(401)
		
		;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			.header("Authorization", "JWT "+ TOKEN)
			.body("{\"nome\":\"Conta de Luz\"}")
		.when()
			.post("/contas") 
		.then()
			.statusCode(201)
		;
		
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			.header("Authorization", "JWT "+ TOKEN)
			.body("{\"nome\":\"conta alterada\"}")
		.when()
			.put("/contas/980556") 
		.then()
			.statusCode(200)
			.body("nome", is("conta alterada"))
		;
		
	}
	
	@Test
	public void naoDeveInserirContaComMesmoNome() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			.header("Authorization", "JWT "+ TOKEN)
			.body("{\"nome\":\"conta alterada\"}")
		.when()
			.post("/contas") 
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao movimentacao = getMovimentacaoValida();
		
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			.header("Authorization", "JWT "+ TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes") 
		.then()
			.statusCode(201)

		;
	}
	
	@Test
	public void deveValidarComposObrigatoriosNaMovimentacao() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			.header("Authorization", "JWT "+ TOKEN)
			.body("{}")
		.when()
			.post("/transacoes") 
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems(
					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					))
		;
	}
	
	@Test
	public void naodeveInserirMovimentacaoComDataFutura() {
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao("20/05/2050");
		
		
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			.header("Authorization", "JWT "+ TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes") 
		.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))

		;
	}
	
	
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(980556);
		//movimentacao.setUsuario_id(APP_PORT);
		movimentacao.setDescricao("Descricao da movimentacao");
		movimentacao.setEnvolvido("Envolvido na movimentacao");
		movimentacao.setTipo("REC");
		movimentacao.setData_transacao("01/01/2000");
		movimentacao.setData_pagamento("10/05/2010");
		movimentacao.setValor(100.00);
		movimentacao.setStatus(true);
		return movimentacao;  
		
	}

}
