package com.github.macgarcia.cast_challenger.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.macgarcia.cast_challenger.enums.OperationType;
import com.github.macgarcia.cast_challenger.models.Account;
import com.github.macgarcia.cast_challenger.models.Operation;
import com.github.macgarcia.cast_challenger.repositories.AccountRepository;
import com.github.macgarcia.cast_challenger.repositories.OperationRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final OperationRepository operationRepository;

	public AccountService(AccountRepository accountRepository, OperationRepository operationRepository) {
		this.accountRepository = accountRepository;
		this.operationRepository = operationRepository;
	}

	public Account findById(Long id) {
		return accountRepository.findById(id).get();
	}

	public Account processLogin(UUID account, String document) {
		return accountRepository.findByNumberAccountAndDocument(account, document);
	}

	public UUID createAccount(String name, String document) {
		Account acc = new Account(name, document);
		acc = accountRepository.saveAndFlush(acc);
		return acc.getNumberAccount();
	}

	public Account addCredits(Long id, BigDecimal value) {
		Account acc = accountRepository.findById(id).get();
		acc.credit(value);
		accountRepository.saveAndFlush(acc);
		Operation operation = new Operation(acc, OperationType.CREDIT, value);
		operationRepository.saveAndFlush(operation);
		return acc;
	}

	public Account addDebits(Long id, BigDecimal value) {
		Account acc = accountRepository.findById(id).get();
		acc.debit(value);
		accountRepository.saveAndFlush(acc);
		Operation operation = new Operation(acc, OperationType.DEBIT, value);
		operationRepository.saveAndFlush(operation);
		return acc;
	}

	@Transactional
	public void addTransfer(UUID masterNumberAccount, String slaveNumberAccount, BigDecimal value) {
		Account master = accountRepository.findByNumberAccount(masterNumberAccount);
		Account slave = accountRepository.findByNumberAccount(UUID.fromString(slaveNumberAccount));
		master.transfer(slave, value);
		Operation operation = new Operation(master, slave, value);
		operationRepository.saveAndFlush(operation);
	}

}
