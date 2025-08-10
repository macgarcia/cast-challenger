package com.github.macgarcia.cast_challenger.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.macgarcia.cast_challenger.exceptions.AccountException;
import com.github.macgarcia.cast_challenger.services.AccountService;
import com.github.macgarcia.cast_challenger.utils.Messages;

@SpringBootTest
public class AccountTest {

	@Autowired
	AccountService service;

	@Test
	void createAcount() {
		assertNotNull(new Account("Tes", ""));
	}

	@Test
	void credit() {
		var acc = new Account("Test", "123");
		acc.credit(new BigDecimal(10));
		assertEquals(new BigDecimal(10), acc.getBalance());
	}

	@Test
	void debit() {
		var acc = new Account("Test", "123");
		acc.credit(new BigDecimal(10));
		acc.debit(new BigDecimal(5));
		assertEquals(new BigDecimal(5), acc.getBalance());
	}

	@Test
	void transfer() {
		var acc = new Account("Test", "123");
		var acc2 = new Account("Test2", "321");
		acc.credit(new BigDecimal(10));
		acc.transfer(acc2, new BigDecimal(8));

		assertEquals(new BigDecimal(2), acc.getBalance());
		assertEquals(new BigDecimal(8), acc2.getBalance());
	}

	@Test
	void errorCreateAccountWithNegativeValue() {
		var ex = assertThrows(AccountException.class, () -> {
			var acc = new Account("Test", "123");
			acc.credit(new BigDecimal(-1));
		});

		assertEquals(Messages.VALUE_LESS_THAN_ZERO, ex.getMessage());
	}

	@Test
	void errorCreateAccountWithNullValue() {
		var ex = assertThrows(AccountException.class, () -> {
			var acc = new Account("Test", "123");
			acc.credit(null);
		});
		assertEquals(Messages.VALUE_NULL, ex.getMessage());
	}

	@Test
	void errorTransferWithInsufficientValue() {
		var ex = assertThrows(AccountException.class, () -> {
			var acc = new Account("Test", "123");
			var acc2 = new Account("Test2", "321");
			acc.credit(new BigDecimal(10));
			acc.transfer(acc2, new BigDecimal(11));
		});
		assertEquals(Messages.INSUFFICIENT_VALUE, ex.getMessage());
	}

	@Test
	void manyOperationsAboutDebits() throws InterruptedException {
		var uuid = service.createAccount("Teste", "123");
		var acc = service.processLogin(uuid, "123");
		service.addCredits(acc.getId(), new BigDecimal(100));

		Runnable run = () -> {
			service.addDebits(uuid, new BigDecimal(10));
		};

		Thread t1 = new Thread(run);
		t1.start();

		Thread t2 = new Thread(run);
		t2.start();

		t1.join();
		t2.join();

		acc = service.processLogin(uuid, "123");
		assertEquals(new BigDecimal(80).setScale(2), acc.getBalance());
	}

	@Test
	void manyOperationsAboutTranfers() throws InterruptedException {
		var uuid = service.createAccount("Teste", "123");
		var acc = service.processLogin(uuid, "123");
		service.addCredits(acc.getId(), new BigDecimal(100));

		var uuid2 = service.createAccount("Teste2", "321");
		// var acc2 = service.processLogin(uuid, "321");

		Runnable run = () -> {
			service.addTransfer(uuid, uuid2.toString(), new BigDecimal(10));
		};

		Thread t1 = new Thread(run);
		t1.start();

		Thread t2 = new Thread(run);
		t2.start();

		Thread t3 = new Thread(run);
		t3.start();

		t1.join();
		t2.join();
		t3.join();

		acc = service.processLogin(uuid, "123");
		var acc2 = service.processLogin(uuid2, "321");

		assertEquals(new BigDecimal(70).setScale(2), acc.getBalance());
		assertEquals(new BigDecimal(30).setScale(2), acc2.getBalance());

	}
}
