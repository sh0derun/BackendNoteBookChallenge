package com.oracle.challenge.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.oracle.challenge.common.Enums;
import com.oracle.challenge.common.Interpreter;
import com.oracle.challenge.common.Python;
import com.oracle.challenge.model.NoteBookInput;
import com.oracle.challenge.model.NoteBookOutput;
import com.oracle.challenge.service.Python3Parser.File_inputContext;

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
			String interpreterName = splitedInput[0].substring(1);
			String code = splitedInput[1];
			
			Interpreter interpreter = (Interpreter) context.getBean(interpretersMap.get(Enums.InterpreterIdentifier.fromValue(interpreterName)));
			interpreter.interpret(code);
			
			//script that will hold all inputs, and gets fed with new input when ever the endpoint is called
			BufferedWriter out = new BufferedWriter(new FileWriter("script.py", true));
			out.append("\n"+code);
			out.close();
			
			CharStream charStream = CharStreams.fromString(code);
			Python3Lexer lexer = new Python3Lexer(charStream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			Python3Parser parser = new Python3Parser(tokens);
			File_inputContext context = parser.file_input();
			String text = context.getText();
			
			
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
			while ((length = process.getErrorStream().read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			
			noteBookOutput.setResult(result.toString().replaceAll("(\\r|\\n)", "|"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return noteBookOutput;
	}

}
