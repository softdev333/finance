package com.mb.finance;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceApplication {

	public static void main(String[] args) throws IOException, IllegalBlockSizeException, BadPaddingException {

		SpringApplication.run(FinanceApplication.class, args);
	}

}
