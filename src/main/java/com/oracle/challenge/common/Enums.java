package com.oracle.challenge.common;

public class Enums {
	public enum InterpreterIdentifier{
		PYTHON("python");
		private String value;
		
		InterpreterIdentifier(String value) {
			this.value = value;
		}

		public String toString() {
			return String.valueOf(value);
		}
		
		public static InterpreterIdentifier fromValue(String text) {
			for (InterpreterIdentifier b : InterpreterIdentifier.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}
	
	public enum Statement{
		EXPRESSION_STMT("expr_stmt"),
		FUNCTION_DEF("funcdef"),
		CLASS_DEF("classdef");
		
		private String value;
		
		private Statement(String value) {
			this.value = value;
		}

		public String toString() {
			return String.valueOf(value);
		}
		
		public static Statement fromValue(String text) {
			for (Statement b : Statement.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}
}
