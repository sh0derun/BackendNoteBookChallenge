package com.oracle.challenge.common;

import java.io.IOException;
import java.util.Map;

import com.oracle.challenge.model.NoteBookOutput;

public abstract class Interpreter {
	
	protected String stateMemory = new String("");
	
	protected abstract void initInterpreter();
	public abstract NoteBookOutput interpret(String code, Map<String, String> sessions, String session) throws IOException;
	protected abstract String saveState(String code);

}
