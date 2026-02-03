package com.chidi.bank_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BankAppApplicationTests {

	@Test
	void contextLoads() {
		// Spring context now loads using H2 in-memory DB
	}
}