package br.com.roberto.barrigatest.tests.suit;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.roberto.barrigatest.core.BaseTest;
import br.com.roberto.barrigatest.tests.AuthTest;
import br.com.roberto.barrigatest.tests.ContasTest;
import br.com.roberto.barrigatest.tests.MovimentacaoTest;
import br.com.roberto.barrigatest.tests.SaldoTest;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
	
})
public class Suite extends BaseTest {

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
