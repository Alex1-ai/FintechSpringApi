package com.chidi.bank_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=test")
class BankAppApplicationTests {

	@Test
	void contextLoads() {
		// Spring context now loads using H2 in-memory DB
	}
}
