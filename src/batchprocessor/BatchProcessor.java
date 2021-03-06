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
		System.out.println("Executing batch...");
		
		try {	
			for (int c = 0; c < batch.getCommandList().size(); c++)
			{
				Command comm = batch.getCommandList().get(c);
				System.out.println(comm.describe());
				comm.execute(batch);
			}
			
			System.out.println("Finished executing batch.");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			System.err.println("Exiting batch execution due to error.");
		}
	}
	
	public static void main(String[] args) 
	{
		Path batchfile = null;
		if (args.length > 0)
		{
			batchfile = Paths.get(args[0]);
		}
		else
		{
			System.err.println("USAGE: java -jar BatchProcessor.jar [xml filename]");
			System.exit(0);
		}

		batch = BatchParser.buildBatch(batchfile);
		executeBatch();
	}

}
