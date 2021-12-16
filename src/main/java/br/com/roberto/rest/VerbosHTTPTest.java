package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;


public class VerbosHTTPTest {
	
	@Test
	public void deveSalvarUsuarioViaJson() {
		
		given()
			.log().all()
			//.contentType("application/json")
			.contentType(ContentType.JSON)
			.body("{ \"name\":\"Jose\" , \"age\":50 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
		
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		
		given()
			.log().all()
			//.contentType("application/xml")
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		;
		
	}
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario Xml", 40); 
		
		given()
			.log().all()
			//.contentType("application/xml")
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario Xml"))
			.body("user.age", is("40"))
		;
		
	}
	
	@Test
	public void deveDeserializarXMLAoSalvarUsuario() {
		User user = new User("Usuario Xml", 40); 
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);
		;
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("Usuario Xml"));
		Assert.assertThat(usuarioInserido.getAge(), is(40));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
		
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"age\":50 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
	;
	}
	
	
	@Test
	public void deveAlterarUsuarioViaJson() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\":\"Ricardo da Silva\" , \"age\":80 }")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Ricardo da Silva"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
		
	}
	
	@Test
	public void devoCustomizarURL() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\":\"Ricardo da Silva\" , \"age\":80 }")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Ricardo da Silva"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
		
	}
	
	@Test
	public void devoCustomizarURLParte2() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\":\"Ricardo da Silva\" , \"age\":80 }")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Ricardo da Silva"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
		
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	
	@Test
	public void naoDeveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
	
	
	@Test
	public void deveSalvarUsuarioViaJsonUsandoMap() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
			.log().all()
			//.contentType("application/json")
			.contentType(ContentType.JSON)
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25))
		;
		
	}
	
	@Test
	public void deveSalvarUsuarioViaJsonUsandoObjeto() {
		User usuario = new User("Usuario via objeto", 35);

		given()
			.log().all()
			//.contentType("application/json")
			.contentType(ContentType.JSON)
			.body(usuario)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))
		;
		
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario() {
		User usuario = new User("Usuario deserializado", 35);

		User usuarioInserido = 
			given()
				.log().all()
				//.contentType("application/json")
				.contentType(ContentType.JSON)
				.body(usuario)
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class)
				
			;
		Assert.assertThat(usuarioInserido.getId(),notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
		
	}
	

	

	
}



