package com.oracle.challenge.ws;

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
	
	@RequestMapping(value = "/execute", produces = { "application/json;charset=utf-8" }, consumes = {
	"application/json;charset=utf-8" }, method = RequestMethod.POST)
	ResponseEntity<NoteBookOutput> executeScript(@Valid @RequestBody NoteBookInput noteBookInput) {
		NoteBookOutput noteBookOutput = new NoteBookOutput();
		noteBookOutput.setResult("123"+noteBookInput.getCode());
		return new ResponseEntity<NoteBookOutput>(noteBookOutput, HttpStatus.CREATED);
	}

}
