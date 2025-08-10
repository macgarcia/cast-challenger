package com.github.macgarcia.cast_challenger.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.github.macgarcia.cast_challenger.enums.OperationType;
import com.github.macgarcia.cast_challenger.exceptions.AccountException;
import com.github.macgarcia.cast_challenger.utils.Messages;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NUMBER_ACCOUNT")
	private UUID numberAccount;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DOCUMENT")
	private String document;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "master")
	private List<Operation> operations;

	public Account(String name, String document) {
		this.numberAccount = UUID.randomUUID();
		this.name = name;
		this.document = document;
		this.balance = BigDecimal.ZERO;
	}

	private void validValue(BigDecimal value) {
		if (Objects.isNull(value)) {
			throw new AccountException(Messages.VALUE_NULL);
		}
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw new AccountException(Messages.VALUE_LESS_THAN_ZERO);
		}
	}

	public void credit(BigDecimal value) {
		this.validValue(value);
		this.balance = this.balance.add(value);
	}

	public void debit(BigDecimal value) {
		this.validValue(balance);
		if (this.balance.compareTo(value) >= 0) {
			this.balance = this.balance.subtract(value);
		} else {
			throw new AccountException(Messages.INSUFFICIENT_VALUE);
		}
	}

	public void transfer(Account acc, BigDecimal value) {
		if (Objects.isNull(acc)) {
			throw new AccountException(Messages.ACCOUNT_NOT_FOUND);
		}
		if (Objects.equals(acc.getNumberAccount(), this.numberAccount)) {
			throw new AccountException(Messages.NO_PERMITED_THIS_OPERATION);
		}
		this.validValue(value);
		if (this.balance.compareTo(value) >= 0) {
			this.debit(value);
			acc.credit(value);
		} else {
			throw new AccountException(Messages.INSUFFICIENT_VALUE);
		}
	}

	public List<Operation> getDebits() {
		if (Objects.isNull(this.operations))
			return null;
		return this.operations.stream() //
				.filter(e -> e.getOperationType().equals(OperationType.DEBIT)) //
				.sorted(Comparator.comparing(Operation::getDate)) //
				.toList();
	}

	public List<Operation> getCredits() {
		if (Objects.isNull(this.operations))
			return null;
		return this.operations.stream() //
				.filter(e -> e.getOperationType().equals(OperationType.CREDIT)) //
				.sorted(Comparator.comparing(Operation::getDate)) //
				.toList();
	}

	public List<Operation> getTransfers() {
		if (Objects.isNull(this.operations))
			return null;
		return this.operations.stream() //
				.filter(e -> e.getOperationType().equals(OperationType.TRANSFER)) //
				.sorted(Comparator.comparing(Operation::getDate)) //
				.toList();
	}

}
