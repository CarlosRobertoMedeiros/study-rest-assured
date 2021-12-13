package br.com.roberto.rest;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("O StatusCode deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		RestAssured.given() //pré-condições
			.when() //ação
				.get("http://restapi.wcaquino.me/ola")
			.then() //assertivas
				.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		Assert.assertThat("Maria", is("Maria"));
		Assert.assertThat(128, is(128));
		Assert.assertThat(128, isA(Integer.class));
		Assert.assertThat(128d, isA(Double.class));
		Assert.assertThat(129d, greaterThan(128d));
		Assert.assertThat(128d, lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, containsInAnyOrder(1,3,5,7,9));
		assertThat(impares, hasItem(1));//Pesquiso para ver se tem um item na lista
		assertThat(impares, hasItems(1,5));//Pesquiso para ver se tem esses items na lista
		
		assertThat("Maria", is(not("João"))); // not é opcional
		assertThat("Maria", not("João"));
		
		assertThat("Maria", anyOf(is("Maria"), is("Joaquina"))); //ou
		assertThat("Joaquina", Matchers.allOf(Matchers.startsWith("Joa"), Matchers.endsWith("ina"), Matchers.containsString("qui"))); //and
	}
	
	@Test
	public void devoValidarBody() {
		RestAssured.given() //pré-condições
		.when() //ação
			.get("http://restapi.wcaquino.me/ola")
		.then() //assertivas
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!")) //Ir do mais restritivo ao menos restritivo
			.body(Matchers.containsString("Mundo"))
			.body(is(not(Matchers.nullValue())));
	}
	
	
	

}
