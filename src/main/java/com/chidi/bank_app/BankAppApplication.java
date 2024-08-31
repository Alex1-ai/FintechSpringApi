package com.chidi.bank_app;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Fintech Bank App",
				description = "Backend Rest APIs for Fintech App ",
				version = "v1.0",
				contact = @Contact(
						name = "Alex1-ai",
						email = "majibade50@gmail.com",
						url = "https://www.linkedin.com/in/emmanuel-onedibe/"
				),
				license = @License(
						name = " Alex1-ai",
						url = "https://www.linkedin.com/in/emmanuel-onedibe/"


				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Then Bank App Documentation",
				url="https://www.linkedin.com/in/emmanuel-onedibe/"
		)

)
public class BankAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAppApplication.class, args);
	}

}
