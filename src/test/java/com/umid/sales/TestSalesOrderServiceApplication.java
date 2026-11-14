package com.umid.sales;

import org.springframework.boot.SpringApplication;

public class TestSalesOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SalesOrderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
