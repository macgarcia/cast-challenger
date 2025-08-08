package com.github.macgarcia.cast_challenger.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.github.macgarcia.cast_challenger.enums.OperationType;
import com.github.macgarcia.cast_challenger.enums.OperationTypeAdapter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OPERATION")
@Getter
@Setter
@NoArgsConstructor
public class Operation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_MASTER")
	private Account master;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_SLAVE")
	private Account slave;

	@Convert(converter = OperationTypeAdapter.class)
	@Column(name = "OPERATION_TYPE")
	private OperationType operationType;

	@Column(name = "OPERATION_DATE")
	private LocalDate date;

	@Column(name = "OPERATION_VALUE")
	private BigDecimal value;

	public Operation(Account master, OperationType operation, BigDecimal value) {
		this.master = master;
		this.slave = null;
		this.operationType = operation;
		this.date = LocalDate.now();
		this.value = value;
	}

	public Operation(Account master, Account slave, BigDecimal value) {
		this.master = master;
		this.slave = slave;
		this.operationType = OperationType.TRANSFER;
		this.date = LocalDate.now();
		this.value = value;
	}

}
