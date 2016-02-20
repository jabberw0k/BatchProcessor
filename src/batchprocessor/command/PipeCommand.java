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
 * PipeCommand.java: Command subclass for the pipe command, which is an interconnection
 * between two processes. Requires two sub-commands (PipeCmdCommand) that will be executed
 * concurrently.
 */

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import batchprocessor.Batch;
import batchprocessor.ProcessException;

public class PipeCommand extends Command
{
	private Element L_command, R_command;

	public PipeCommand(Element element) throws ProcessException
	{
		parse(element);
	}
	
	@Override
	public String describe() 
	{
		return "Pipe command...";
	}
	
	@Override
	public void parse(Element element) throws ProcessException
	{
		id = element.getAttribute("id");
		if (id == null || id.isEmpty())
		{
			throw new ProcessException("Missing 'id' attribute in PIPE command");
		}
		
		NodeList nodes = element.getChildNodes();
		
		try
		{
			if (nodes.item(0).getNodeType() == Node.ELEMENT_NODE &&
					nodes.item(1).getNodeType() == Node.ELEMENT_NODE)
			{
				L_command = (Element)nodes.item(0);
				R_command = (Element)nodes.item(1);
				
			}
		}
		catch (Exception ex)
		{
			throw new ProcessException("Error parsing PIPE command");
		}
	}
	
	@Override
	public void execute(Batch batch)
	{
		
	}

}
