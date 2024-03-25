package com.cs506group12.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {

		testCard test = new testCard();
		test.testAll();
		test.testRank();
		test.testSuit();
	}

}
