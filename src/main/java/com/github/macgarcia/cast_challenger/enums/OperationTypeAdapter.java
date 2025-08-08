package com.github.macgarcia.cast_challenger.enums;

import java.util.Objects;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OperationTypeAdapter implements AttributeConverter<OperationType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(OperationType type) {
		return Objects.isNull(type) ? null : type.getValue();
	}

	@Override
	public OperationType convertToEntityAttribute(Integer value) {
		return Objects.isNull(value) ? null : OperationType.fromValue(value);
	}

}
