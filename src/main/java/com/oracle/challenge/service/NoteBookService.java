package com.oracle.challenge.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.oracle.challenge.common.Enums;
import com.oracle.challenge.common.Interpreter;
import com.oracle.challenge.common.Python;
import com.oracle.challenge.model.NoteBookInput;
import com.oracle.challenge.model.NoteBookOutput;

import antlr.PythonCodeGenerator;

@Service
public class NoteBookService {
	
	@Autowired
    ApplicationContext context;
	
	private static Map<Enums.InterpreterIdentifier, Class<?>> interpretersMap = null;
	
	static {
    	interpretersMap = new HashMap<>();
    	interpretersMap.put(Enums.InterpreterIdentifier.PYTHON, Python.class);
    }
	
	public NoteBookOutput HandelNoteBookInput(NoteBookInput noteBookInput) {
		NoteBookOutput noteBookOutput = new NoteBookOutput();
		try {
			String[] splitedInput = noteBookInput.getCode().split(" ", 2);
			String interpreter = splitedInput[0].substring(1);
			String code = splitedInput[1];
			
			System.out.println(interpreter);
			System.out.println(code);
			/*
			interpretersMap.get(Enums.InterpreterIdentifier.fromValue(interpreter));
			*/
			//script that will hold all inputs, and gets fed with new input when ever the endpoint is called
			BufferedWriter out = new BufferedWriter(new FileWriter("script.py", true));
			out.append("\n"+code);
			out.close();
			
			Process process = null;
			
			//script execution
			if(System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
				process = Runtime.getRuntime().exec(String.format("cmd.exe /c python script.py"));
			}
			else {
				//other os	
			}
			
			//preparing result of script execution to be binded in the output
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = process.getInputStream().read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			
			noteBookOutput.setResult(result.toString().replaceAll("(\\r|\\n)", "|"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return noteBookOutput;
	}

}
