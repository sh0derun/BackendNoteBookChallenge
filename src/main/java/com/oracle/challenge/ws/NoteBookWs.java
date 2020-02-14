package com.oracle.challenge.ws;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oracle.challenge.model.NoteBookInput;
import com.oracle.challenge.model.NoteBookOutput;
import com.oracle.challenge.service.NoteBookService;

@Controller
public class NoteBookWs {

	@Autowired
	private NoteBookService noteBookService;
	
	@RequestMapping(value = "/execute", 
					produces = { "application/json;charset=utf-8" }, 
					consumes = { "application/json;charset=utf-8" }, 
					method = RequestMethod.POST)
	ResponseEntity<NoteBookOutput> executeScript(@Valid @RequestBody NoteBookInput noteBookInput) {
		
		NoteBookOutput noteBookOutput = this.noteBookService.HandelNoteBookInput(noteBookInput);
		return new ResponseEntity<NoteBookOutput>(noteBookOutput, HttpStatus.CREATED);
		
	}

}
