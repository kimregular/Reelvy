package com.mysettlement.testUtils;

import org.springframework.restdocs.constraints.ConstraintDescriptions;

import java.util.List;

public abstract class ConstraintExtractor {

	private ConstraintExtractor() {

	}

	public static <T> List<String> extractConstraintOf(Class<T> requestClass, String fieldName) {
		ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(requestClass);
		try {
			return constraintDescriptions.descriptionsForProperty(fieldName);
		} catch (Exception e) {
			throw new IllegalArgumentException("Field '" + fieldName + "' does not exist in " + requestClass.getSimpleName(), e);
		}
	}
}
