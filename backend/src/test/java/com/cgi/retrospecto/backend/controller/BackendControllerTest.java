package com.cgi.retrospecto.backend.controller;

import com.cgi.retrospecto.backend.app.BackendApplication;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(
		classes = BackendApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BackendControllerTest {

	@LocalServerPort
	private int port;

	@BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

	@Test
	public void saysHello() {
		when()
			.get("/api/hello")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat()
				.body(is(equalTo(BackendController.HELLO_TEXT)));
	}

}
