package batchprocessor.command;

/*
 * CS 5348 - Operating Systems Concepts
 * Project 1: Batch Language Processing
 * 
 * Authors:
 * Shiau Kang Wang (sxw160330)
 * Cheng Kai Huang (cxh152330)
 * Justin Fritcher (jrf130030)
 * 
 * Command.java: Abstract class for the command types that the batch processor can execute. 
 */

import org.w3c.dom.Element;

public abstract class Command 
{
	//prints a command to the console when the Command is executed.
	public abstract String describe();
	
	//parses a given XML element and extracts the information contained therein.
	public abstract void parse(Element element);
	
	//executes the command
	public abstract void execute(String workingDir);
}