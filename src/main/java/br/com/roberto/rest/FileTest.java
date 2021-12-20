package br.com.roberto.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {
	
	@Test
	public void deveObrigarEnvioArquivo() {
		
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) //deveria ser 400
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveFazerUploadDeArquivo() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("users.pdf"))
		;
	}
	
	@Test
	public void naoDeveFazerUploadDeArquivoGrande() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf")) //Mudar para um arquivo um pouco maior
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(5000L)) //menor que 5000 Milisegundos
			.statusCode(413)
		;
	}
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		@SuppressWarnings("unused")
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
//			.log().all()
			.statusCode(200)
			.extract().asByteArray();
		;
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		
		//out.write(imagem);
		out.close();
		
		System.out.println(imagem.length());
		Assert.assertThat(imagem.length(), lessThan(100000L));
		
	}
	
	

}
