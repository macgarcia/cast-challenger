package com.github.macgarcia.cast_challenger.enums;

import com.github.macgarcia.cast_challenger.exceptions.AccountException;
import com.github.macgarcia.cast_challenger.utils.Messages;

public enum OperationType {
	CREDIT(1), DEBIT(2), TRANSFER(3);

	private Integer value;

	private OperationType(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static OperationType fromValue(Integer value) {
		for (OperationType type : values()) {
			if (value.equals(type.getValue())) {
				return type;
			}
		}
		throw new AccountException(Messages.INVALID_OPERATION_TYPE);
	}

}
