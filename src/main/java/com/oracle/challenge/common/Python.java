package com.oracle.challenge.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.oracle.challenge.common.python.AstUtility;
import com.oracle.challenge.common.python.ErrorHandler;
import com.oracle.challenge.common.python.Python3Lexer;
import com.oracle.challenge.common.python.Python3Parser;
import com.oracle.challenge.common.python.Python3Parser.File_inputContext;
import com.oracle.challenge.model.NoteBookOutput;

@Component
public class Python extends Interpreter {

	protected void initInterpreter() {
	}

	public NoteBookOutput interpret(String code, Map<String, String> sessions, String session) throws IOException {
		NoteBookOutput noteBookOutput = new NoteBookOutput();

		String codeToExecute;
		String result = saveState(code);
		
		if (StringUtils.isBlank(sessions.get(session))) {
			if(StringUtils.isNotBlank(result)) {
				sessions.replace(session, code);
			}
			codeToExecute = code;
		} else {
			codeToExecute = StringUtils.joinWith(";", sessions.get(session), code);
		}

		Process process = executeCode(codeToExecute);

		ByteArrayOutputStream goodResult = new ByteArrayOutputStream(), errorResult = new ByteArrayOutputStream();

		// preparing result of script execution to be binded in the output
		byte[] buffer = new byte[1024];
		int length;
		while ((length = process.getErrorStream().read(buffer)) != -1) {
			errorResult.write(buffer, 0, length);
		}
		while ((length = process.getInputStream().read(buffer)) != -1) {
			goodResult.write(buffer, 0, length);
		}

		String sep = StringUtils.isNotBlank(result)?";":"";
		
		if (StringUtils.isBlank(goodResult.toString()) && StringUtils.isBlank(errorResult.toString())) {
			noteBookOutput.setCode(code);
			String newCode = StringUtils.joinWith(sep, sessions.get(session), result);
			sessions.replace(session, newCode);
		} else if (StringUtils.isNotBlank(goodResult.toString())) {
			noteBookOutput.setResult(goodResult.toString());
			String newCode = StringUtils.joinWith(sep, sessions.get(session), result);
			sessions.replace(session, newCode);
		} else if (StringUtils.isNotBlank(errorResult.toString())) {
			noteBookOutput.setError(errorResult.toString());
		}

		return noteBookOutput;

	}

	private Process executeCode(String code) throws IOException {
		Process process = null;

		if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
			String command = "cmd.exe /c python -c \"" + code + "\"";
			process = Runtime.getRuntime().exec(command);
		} else {
			// other os
		}

		return process;
	}

	protected String saveState(String code) {
		Python3Lexer python3Lexer = new Python3Lexer(CharStreams.fromString(code));
		CommonTokenStream tokens = new CommonTokenStream(python3Lexer);
		Python3Parser parser = new Python3Parser(tokens);

		parser.setErrorHandler(new ErrorHandler());
		File_inputContext context = parser.file_input();

		AstUtility astUtility = new AstUtility();
		return astUtility.getStatements(context);

	}

}
