package lesson06;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

/**
 * Assumes that the file chosen will be a Hack .asm file, properly formatted.
 * There is little to no error-handling.
 * ...it was also fairly hastily coded, so apologies for the piss-poor modularity. 
 * @author iskan
 *
 */
public class Run {

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();

		chooser.showOpenDialog(null);
		
		//temporary file created for first passthrough
		String readfilePath=chooser.getSelectedFile().getPath();
		String writefilePath=readfilePath.substring(0, readfilePath.length()-4)+".hack";
		String tempPath=readfilePath.substring(0, readfilePath.length()-4)+".tmp";
		Path readfile = Paths.get(readfilePath);
		Path writefile = Paths.get(writefilePath);
		Path temp = Paths.get(tempPath);

		String current;

		Parser firstPass = new Parser(readfile);
		
		//adds loops to symbol table
		try (BufferedWriter firstWrite = Files.newBufferedWriter(temp);){
			do{
				current=firstPass.hasMoreCommands();
				if (firstPass.commandType(current)==Parser.CommandNames.L_COMMAND)
					current=firstPass.symbol(current);
				if (!current.equals("skip")&&!current.equals("END")){
					firstWrite.write(current);
					firstWrite.newLine();
					firstWrite.flush();
				}	
			}while(!current.equals("END"));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//make sure new Parser symbolTable has old symbols
		Parser parser = new Parser(temp);
		parser.st=firstPass.st;

		String comp,dest,jump;

		try (BufferedWriter writer = Files.newBufferedWriter(writefile);){

			Code code = new Code();

			do{
				current=parser.hasMoreCommands();
				//parse, code, write
				switch(parser.commandType(current)){
				case A_COMMAND:
					current=parser.symbol(current);
					current=code.symbol(current);
					writer.write(current);
					writer.newLine();
					writer.flush();
					break;
				case C_COMMAND:
					comp=parser.comp(current);
					dest=parser.dest(current);
					jump=parser.jump(current);
					current=code.cCommand(comp, dest, jump);
					writer.write(current);
					writer.newLine();
					writer.flush();
					break;
				case L_COMMAND:
					//should be none left
					break;
				case END:
				default:
					break;
				}

			}while(!current.equals("END"));
			
			//delete temp
			Files.deleteIfExists(temp);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}


