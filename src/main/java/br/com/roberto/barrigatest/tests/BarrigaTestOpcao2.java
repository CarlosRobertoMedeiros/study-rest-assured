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

/*
 * Segunda maneira de realizar os testes
 * Criando através de um usuário específico todos os cenários
 * E assim executando para aquele usuário as funcionalidades de maneira independente
 * */
public class BarrigaTestOpcao2 extends BaseTest {
	
	
	
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
		
		RestAssured.get("/reset").then().statusCode(200);
		
		
	}
	
	
}
