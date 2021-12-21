package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.roberto.barrigatest.core.BaseTest;
import br.com.roberto.barrigatest.utils.DateUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {
	
	
	
	private static String CONTA_NAME = "Conta "+System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
	@BeforeClass
	public static void login() {
		Map<String,String> login = new HashMap<String, String>();
		login.put("email","carlosmedeiroslima@gmail.com");
		login.put("senha","123456");
		
		//Capturar Token
		String TOKEN = given()
				.body(login)
			.when()
				.post("/signin") 
			.then()
				.statusCode(200)
				.extract().path("token");
	
		RestAssured.requestSpecification.header("Authorization", "JWT "+ TOKEN);
	}
	
	@Test
	public void t02_deveIncluirContaComSucesso() {
		CONTA_ID =  given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.body("{\"nome\":\""+ CONTA_NAME +"\"}")
		.when()
			.post("/contas") 
		.then()
			.statusCode(201)
			.extract().path("id")
			
		;
		
	}
	
	@Test
	public void t03_deveAlterarContaComSucesso() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.body("{\"nome\":\""+CONTA_NAME+" alterada\"}")
			.pathParam("id",CONTA_ID)
		.when()
			.put("/contas/{id}") 
		.then()
			.statusCode(200)
			.body("nome", is(CONTA_NAME+" alterada"))
		;
		
	}
	
	@Test
	public void t04_naoDeveInserirContaComMesmoNome() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.body("{\"nome\":\""+CONTA_NAME+" alterada\"}")
		.when()
			.post("/contas") 
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {
		Movimentacao movimentacao = getMovimentacaoValida();
		
		MOV_ID = given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes") 
		.then()
			.statusCode(201)
			.extract().path("id")

		;
	}
	
	@Test
	public void t06_deveValidarComposObrigatoriosNaMovimentacao() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
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
	public void t07_naoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao(DateUtils.getDataComDiferencaDias(2));
		
		
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes") 
		.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))

		;
	}
	
	
	@Test
	public void t08_naoDeveRemoverContaComMovimentacao() {
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao("20/05/2050");
		
		
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}") 
		.then()
			.statusCode(500) //Api deveria ter tratado
			.body("constraint", is("transacoes_conta_id_foreign"))

		;
	}
	
	@Test
	public void t09_deveCalcularSaldoContas() {
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao("20/05/2050");
		
		
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
		.when()
			.get("/saldo") 
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("100.00"))

		;
	}
		
	@Test
	public void t10_deveRemoverMovimentacao() {
		given()
			//.header("Authorization", "bearer "+ token) //Vai depender da versão da API
			//.header("Authorization", "JWT "+ TOKEN)
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}") 
		.then()
			.statusCode(204)
		;
	}
	
	@Test
	public void t11_naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization"); //A partir daqui eu retirei o token de autorização
		given()
		.when()
			.get("/contas") 
		.then()
			.statusCode(401)
		
		;
	}
	
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(CONTA_ID);
		//movimentacao.setUsuario_id(APP_PORT);
		movimentacao.setDescricao("Descricao da movimentacao");
		movimentacao.setEnvolvido("Envolvido na movimentacao");
		movimentacao.setTipo("REC");
		movimentacao.setData_transacao(DateUtils.getDataComDiferencaDias(-1));
		movimentacao.setData_pagamento(DateUtils.getDataComDiferencaDias(5));
		movimentacao.setValor(100.00);
		movimentacao.setStatus(true);
		return movimentacao;  
	}

}
