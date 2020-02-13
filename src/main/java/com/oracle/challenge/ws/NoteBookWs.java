package com.oracle.challenge.ws;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oracle.challenge.model.NoteBookInput;
import com.oracle.challenge.model.NoteBookOutput;

@Controller
public class NoteBookWs {

	@RequestMapping(value = "/execute", 
					produces = { "application/json;charset=utf-8" }, 
					consumes = { "application/json;charset=utf-8" }, 
					method = RequestMethod.POST)
	ResponseEntity<NoteBookOutput> executeScript(@Valid @RequestBody NoteBookInput noteBookInput) {
		NoteBookOutput noteBookOutput = new NoteBookOutput();
		try {
			//script that will hold all inputs, and gets fed with new input when ever the endpoint is called
			BufferedWriter out = new BufferedWriter(new FileWriter("script.py"));
			out.write("print "+noteBookInput.getCode());
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
			
			noteBookOutput.setResult(result.toString().replaceAll("(\\r|\\n)", ""));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return new ResponseEntity<NoteBookOutput>(noteBookOutput, HttpStatus.CREATED);
	}

}
