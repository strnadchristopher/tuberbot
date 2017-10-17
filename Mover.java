import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Mover
{
	String downloadsFolderPath = "D:/Chris/Downloads";
	String picturesFolderPath = "D:/Chris/Pictures";
	String fileTypes[] =
	{
		".jpg", ".jpeg", ".png", ".bmp", ".tiff", ".webm", ".gif"
	};
	Random rand = new Random();
	File[] listOfFiles;
	public static void main(String[] args)
	{
		Mover mover = new Mover();
		if(args.length > 0)
		{
			mover.loop(Integer.parseInt(args[0]));
		}
		else
		{
			mover.loop(0);
		}
	}
	
	public void loop(int wait)
	{
		System.out.println("It's showtime!");
		while(true)
		{
			System.out.println("Finding Downloads Folder and listing files");
			//Check all the files in the downloads folder and if they're pictures put them in the pictures folder
			File downloadsFolder = new File(downloadsFolderPath);
			if(listOfFiles != downloadsFolder.listFiles())
			{
				//Set list of files to the updated list
				listOfFiles = downloadsFolder.listFiles();
			
				//Check if every file in the folder is an image
				for(int i = 0; i < listOfFiles.length; i++)
				{
					if(listOfFiles[i].isFile())
					{
						//Check if its an image
						if(isFilePicture(listOfFiles[i].getPath()))
						{
							//If it is, attempt to move it
							System.out.println("///////////////////////////////////////////////////");
							System.out.println(listOfFiles[i].getPath() + " is a picture, attempting to move.");
							try
							{
								Files.copy(new File(listOfFiles[i].getPath()).toPath(), new File(picturesFolderPath + "/" + listOfFiles[i].getName()).toPath());
								Files.delete(new File(listOfFiles[i].getPath()).toPath());
								//Success
								System.out.println("File moved successfully");
								System.out.println("///////////////////////////////////////////////////");
							} 
							catch (IOException e)
							{
								//Failure
								e.printStackTrace();
								System.out.println("Failed to move file, possibly because there is already an existing file name,\n I'll now try to "
										+ "just add some random characters to the name.");
								//Try again
								try
								{
									Files.copy(new File(listOfFiles[i].getPath()).toPath(), new File(picturesFolderPath + "/" 
											+ rand.nextInt(999999)
											+ listOfFiles[i].getName()).toPath());
									Files.delete(new File(listOfFiles[i].getPath()).toPath());
									System.out.println("File moved successfully!");
									System.out.println("///////////////////////////////////////////////////");
								}
								catch (IOException e1)
								{
									//Double failure, give up
									e1.printStackTrace();
									System.out.println("Oh gosh that didn't work either. Uh........AHHHHHHHHHHH!");
									System.out.println("///////////////////////////////////////////////////");
								}
							}
						}
						else
						{
							//It's not a picture
							System.out.println(listOfFiles[i].getPath() + " is not a picture.");
							//Move it to a folder named after it's file type
							try
							{
								//See if the path exists, if not, create that path
								if(!Files.exists(Paths.get(downloadsFolder + "/" + FilenameUtils.getExtension(listOfFiles[i].getPath()).toUpperCase())))
								{
									System.out.println("No directory for this file type exists, creating one now!");
									boolean success = new File(downloadsFolder + "/" + FilenameUtils.getExtension(listOfFiles[i].getPath()).toUpperCase()).mkdirs();
									if(success)
									{
										System.out.println("Successfully created the directory, placing file there now.");
									}
									else
									{
										System.out.println("Failed to create the directory, oops.");
									}
								}
								//Files.copy(new File(listOfFiles[i].getPath()).toPath(), new File(downloadsFolder + "/" + FilenameUtils.getExtension(listOfFiles[i].getPath()).toUpperCase() + "/" + listOfFiles[i].getName()).toPath());
								//Files.delete(new File(listOfFiles[i].getPath()).toPath());
								FileUtils.moveFile(new File(listOfFiles[i].getPath()), new File(downloadsFolder + "/" + FilenameUtils.getExtension(listOfFiles[i].getPath()).toUpperCase() + "/" + listOfFiles[i].getName()));
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("Couldn't move the file for some reason.");
							}
						}
					}
				}
			}
			//If there is a waiting period between update cycle here's the logic for that
			if(wait != 0)
			{
				int time = wait;
				while(time >= 0)
				{
					System.out.print("Finished reading folder. Waiting " + time + " seconds until trying again.");
					//Wait however long the user set
					try
					{
						Thread.sleep(1000);
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					System.out.print("\r");
					time --;
				}
			}
		System.out.println("\n");
		}
	}
	
	public boolean isFilePicture(String ref)
	{
		//if its a picture returns true
		for(int i = 0; i < fileTypes.length; i++)
		{
			if(org.apache.commons.lang3.StringUtils.containsIgnoreCase(ref, fileTypes[i]))
			{
				return true;
			}
		}
		return false;
	}
}
