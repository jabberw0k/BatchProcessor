package batchprocessor.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import batchprocessor.Batch;
import batchprocessor.ProcessException;

/*
 * CS 5348 - Operating Systems Concepts
 * Project 1: Batch Language Processing
 * 
 * Authors:
 * Shiau Kang Wang (sxw160330)
 * Cheng Kai Huang (cxh152330)
 * Justin Fritcher (jrf130030)
 * 
 * PipeCmdCommand.java: Command subclass for the two cmd commands for a PipeCommand
 */

public class PipeCmdCommand extends Command 
{

	private String path;
	private String args;
	private String in_file;
	private String out_file;
	private ProcessBuilder process;
	
	public PipeCmdCommand(Element element) throws ProcessException
	{
		parse(element);
	}
	
	@Override
	public String describe() 
	{
		return "Command: " + id;
	}

	@Override
	public void parse(Element element) throws ProcessException
	{
		id = element.getAttribute("id");
		if (id == null || id.isEmpty())
		{
			throw new ProcessException("Missing 'id' attribute in CMD command");
		}
		
		path = element.getAttribute("path");
		if (path == null || path.isEmpty())
		{
			throw new ProcessException("Missing 'path' attribute in CMD command");
		}
		
		args = element.getAttribute("args");	
		in_file = element.getAttribute("in");
		out_file = element.getAttribute("out");
	}

	@Override
	public void execute(Batch batch) throws ProcessException
	{
		List<String> command = new ArrayList<String>();
		command.add(path);
		for (String s : args.split(" "))
		{
			if (!s.isEmpty())
			{
				command.add(s);
			}
		}
		process = new ProcessBuilder();
		process.command(command);
		process.directory(new File(batch.getWorkingDir()));
		
		if (!(in_file == null || in_file.isEmpty()))
		{
			if(batch.getCommands().containsKey(in_file))
			{
				try
				{
					File stdin = new File(process.directory(),((FileCommand)batch.getCommands().get(in_file)).getPath());
					process.redirectInput(stdin);
				}
				catch (Exception ex)
				{
					throw new ProcessException("Error loading IN file from FileCommand with id '" + in_file + ".");
				}
			}
			else
			{
				throw new ProcessException("Unable to locate IN FileCommand with id '" + in_file + ".");
			}
		}
		
		if (!(out_file == null || out_file.isEmpty()))
		{
			if(batch.getCommands().containsKey(out_file))
			{
				File stdout = new File(process.directory(),((FileCommand)batch.getCommands().get(out_file)).getPath());
				process.redirectOutput(stdout);
			}
			else
			{
				throw new ProcessException("Unable to locate OUT FileCommand with id '" + out_file + ".");
			}
		}
		System.out.println("Deferring execution for " + path);
	}
	
	public ProcessBuilder getProcessBuilder()
	{
		return process;
	}
	
}
