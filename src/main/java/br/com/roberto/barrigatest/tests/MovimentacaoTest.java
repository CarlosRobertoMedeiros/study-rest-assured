package br.com.roberto.barrigatest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.roberto.barrigatest.core.BaseTest;
import br.com.roberto.barrigatest.utils.BarrigaUtils;
import br.com.roberto.barrigatest.utils.DateUtils;

public class MovimentacaoTest extends BaseTest {
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao movimentacao = getMovimentacaoValida();
		
		given()
			.body(movimentacao)
		.when()
			.post("/transacoes") 
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveValidarComposObrigatorios() {
		given()
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
	public void naoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao(DateUtils.getDataComDiferencaDias(2));
		
		given()
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
	public void naoDeveRemoverContaComMovimentacao() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta com movimentacao");

		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}") 
		.then()
			.statusCode(500) //Api deveria ter tratado
			.body("constraint", is("transacoes_conta_id_foreign"))

		;
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		Integer MOV_ID = BarrigaUtils.getIdMovimentacaoPelaDescricao("Movimentacao para exclusao");
		
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}") 
		.then()
			.statusCode(204)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
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
