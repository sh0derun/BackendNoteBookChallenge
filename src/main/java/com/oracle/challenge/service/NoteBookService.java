package com.oracle.challenge.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.oracle.challenge.common.Enums;
import com.oracle.challenge.common.Interpreter;
import com.oracle.challenge.common.Python;
import com.oracle.challenge.model.NoteBookInput;
import com.oracle.challenge.model.NoteBookOutput;

@Service
public class NoteBookService {
	
	@Autowired
    ApplicationContext context;
	
	@Autowired
	private Environment env;
	
	private Map<String, String> sessions = new HashMap<String, String>();
	
	private static Map<Enums.InterpreterIdentifier, Class<?>> interpretersMap = null;
	
	public NoteBookService() {
		
	}
	
	static {
    	interpretersMap = new HashMap<>();
    	interpretersMap.put(Enums.InterpreterIdentifier.PYTHON, Python.class);
    }
	
	public NoteBookOutput HandelNoteBookInput(NoteBookInput noteBookInput) {
		NoteBookOutput noteBookOutput = new NoteBookOutput();
		if(StringUtils.isBlank(noteBookInput.getSessionId())) {
			noteBookOutput.setError(env.getProperty("sessionId_empty"));
			return noteBookOutput;
		}
		if(StringUtils.isBlank(noteBookInput.getCode())) {
			noteBookOutput.setError(env.getProperty("no_code_attribut"));
			return noteBookOutput;
		}
		String[] splitedInput = noteBookInput.getCode().split(" ", 2);
		if(splitedInput[0].charAt(0) != '%') {
			noteBookOutput.setError(env.getProperty("interpreter_modulo_chould"));
			return noteBookOutput;
		}
		String interpreterName = splitedInput[0].substring(1);
		if(Enums.InterpreterIdentifier.fromValue(interpreterName) == null) {
			noteBookOutput.setError(MessageFormat.format(env.getProperty("interpreter_unknown"), interpreterName));
			return noteBookOutput;
		}
		if(splitedInput.length <= 1) {
			noteBookOutput.setError(MessageFormat.format(env.getProperty("empty_code"), interpreterName));
			return noteBookOutput;
		}
		String code = splitedInput[1];
		
		sessions.put(noteBookInput.getSessionId(),sessions.get(noteBookInput.getSessionId()));
		Interpreter interpreter = (Interpreter) context.getBean(interpretersMap.get(Enums.InterpreterIdentifier.fromValue(interpreterName)));
		
		try {
			noteBookOutput = interpreter.interpret(code, sessions, noteBookInput.getSessionId());
		} catch (IOException e) {
			noteBookOutput.setCode("");
			noteBookOutput.setResult("");
			noteBookOutput.setError("Internal Interpreter Error !");
		}
		
		return noteBookOutput;
	}

}
