package com.Merlin.Inventory.Management.System;

import org.springframework.boot.SpringApplication;

public class TestInventoryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(InventoryManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
