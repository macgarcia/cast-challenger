package com.github.macgarcia.cast_challenger.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.github.macgarcia.cast_challenger.exceptions.AccountException;
import com.github.macgarcia.cast_challenger.utils.Messages;

public class AccountTest {

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
}
