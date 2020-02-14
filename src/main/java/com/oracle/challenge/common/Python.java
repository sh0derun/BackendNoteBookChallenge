package com.oracle.challenge.common;

import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.stereotype.Component;

@Component
public class Python implements Interpreter{
	
	private Queue<String> queue;
	public static final String FILENAME = "script.py";
	
	public void initInterpreter() {
		this.queue = new PriorityQueue<String>();
	}
	
	public void interpret(String code) {
		System.out.println("!!!THIS IS PYTHON INTERPRETER!!!");
		System.out.println(code);
	}
	
}
