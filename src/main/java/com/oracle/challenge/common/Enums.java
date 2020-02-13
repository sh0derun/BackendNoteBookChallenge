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
}
