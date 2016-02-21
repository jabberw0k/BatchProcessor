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

import java.lang.ProcessBuilder.Redirect;

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
		return "Pipe command";
	}
	
	@Override
	public void parse(Element element) throws ProcessException
	{
		id = element.getAttribute("id");
		if (id == null || id.isEmpty())
		{
			throw new ProcessException("Missing 'id' attribute in PIPE command");
		}
		
		Node node = element.getFirstChild();
		try
		{
			do
			{
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					L_command = (Element)node;
					node = node.getNextSibling();
					break;
				}
				node = node.getNextSibling();
			} while (node != null);
			
			if (node != null)
			{
				do
				{
					if (node.getNodeType() == Node.ELEMENT_NODE)
					{
						R_command = (Element)node;
						break;
					}
					node = node.getNextSibling();
				} while (node != null);
			}

			if (node == null || L_command == null || R_command == null)
			{
				throw new ProcessException("Unable to locate child CMD elements for PIPE command.");
			}
		}
		catch (Exception ex)
		{
			throw new ProcessException(ex.getMessage());
		}
	}
	
	@Override
	public void execute(Batch batch) throws ProcessException
	{
		String R_id = R_command.getAttribute("id");
		String L_id = L_command.getAttribute("id");
		
		ProcessBuilder R_procbuilder = ((PipeCmdCommand)batch.getCommands().get(R_id)).getProcessBuilder();
		ProcessBuilder L_procbuilder = ((PipeCmdCommand)batch.getCommands().get(L_id)).getProcessBuilder();
		/*
		R_procbuilder.redirectOutput(Redirect.PIPE);
		L_procbuilder.redirectInput(Redirect.PIPE);
		
		
		
		try
		{
			Process R_process = R_procbuilder.start();
			R_process.waitFor();
			Process L_process = L_procbuilder.start();
			L_process.waitFor();
			
		}
		catch (Exception ex)
		{
			throw new ProcessException("Error creating and running process: " + ex.getMessage());
		}
		
		*/
	}

	public Element getLCommand()
	{
		return L_command;
	}
	
	public Element getRCommand()
	{
		return R_command;
	}
	
}
