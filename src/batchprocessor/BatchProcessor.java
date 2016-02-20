package batchprocessor;





/*
 * CS 5348 - Operating Systems Concepts
 * Project 1: Batch Language Processing
 * 
 * Authors:
 * Shiau Kang Wang (sxw160330)
 * Cheng Kai Huang (cxh152330)
 * Justin Fritcher (jrf130030)
 * 
 * BatchProcessor.java: The main class which drives both the parsing of the batch file into commands
 * 	and the execution of those commands.
 */

import java.nio.file.Path;
import java.nio.file.Paths;

import batchprocessor.command.Command;


public class BatchProcessor 
{
	private static Batch batch;
	
	public static void executeBatch() 
	{
		System.err.println("Executing batch...");
		
		try {	
			for (int c = 0; c < batch.getCommandList().size(); c++)
			{
				Command comm = batch.getCommandList().get(c);
				System.err.println(comm.describe());
				comm.execute(batch);
			}
			
			System.err.println("Finished executing batch.");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			System.err.println("Exiting batch execution due to error.");
		}
	}
	
	public static void main(String[] args) 
	{
		Path batchfile;
		if (args.length > 0)
		{
			batchfile = Paths.get(args[0]);
		}
		else
		{
			batchfile = Paths.get(System.getProperty("user.dir"), "work", "batch1.dos.xml");
		}

		batch = BatchParser.buildBatch(batchfile);
		executeBatch();
	}

}
