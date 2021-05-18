package com.example.RegisterDemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RegisterDemoApplicationTests {

	@Test
	void contextLoads() {
		Assertions.fail("Ooops test failed");
	}

	Calculator underTest = new Calculator();

	@Test
	void itShouldAddTwoNumbers() {
		// given
		int numberOne = 20;
		int numberTwo = 30;

		// when
		int result = underTest.add(numberOne, numberTwo);

		// then
		int expected = 50;
		assertThat(result).isEqualTo(expected);

	}

	static class Calculator {
		int add(int a, int b) {
			return a+b;
		}
	}

}
