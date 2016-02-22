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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
		
		ProcessBuilder L_procbuilder = ((PipeCmdCommand)batch.getCommands().get(L_id)).getProcessBuilder();
		ProcessBuilder R_procbuilder = ((PipeCmdCommand)batch.getCommands().get(R_id)).getProcessBuilder();
		
		L_procbuilder.redirectErrorStream(true);
		R_procbuilder.redirectErrorStream(true);

		try
		{
			final Process L_process = L_procbuilder.start();
			InputStream pipe_in = L_process.getInputStream();
			final Process R_process = R_procbuilder.start();
			OutputStream pipe_out = R_process.getOutputStream();	
			pipeStream(pipe_in, pipe_out);
			
		}
		catch (Exception ex)
		{
			throw new ProcessException("Error creating and running process: " + ex.getMessage());
		}
		System.out.println("Pipe completed: " + L_id + " | " + R_id);
	}

	public Element getLCommand()
	{
		return L_command;
	}
	
	public Element getRCommand()
	{
		return R_command;
	}
	
	static void pipeStream(final InputStream pipe_in, final OutputStream pipe_out)
	{
		Runnable pipeThread = (new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					int achar;
					while ((achar = pipe_in.read()) != -1)
					{
						pipe_out.write(achar);
					}
					pipe_out.close();
				} catch (IOException ex)
				{
					throw new RuntimeException("IOException when running pipe Thread: " + ex.getMessage());
				}
			}
		});
	
		new Thread(pipeThread).start();
	}
	
}
