package lesson06;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;

public class Parser {
		BufferedReader file;
		int currentLine=0;
		SymbolTable st=new SymbolTable();
	
	
	public Parser(Path pathIn) {
		
		Path absolutePath=pathIn.toAbsolutePath();
		try {
			this.file = Files.newBufferedReader(absolutePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public enum CommandNames {
	    A_COMMAND, L_COMMAND, C_COMMAND, END 
	}
	
	String hasMoreCommands(){
		
		String line;
		
		try {
			if ((line = file.readLine()) != null) {
			    line=line.trim();
			    if (line.equals("")||line.charAt(0)=='/'){
			    	return hasMoreCommands();
			    }
			    else{
			    	if (line.contains("//")){
			    		currentLine++;
			    		return line.substring(0, line.indexOf('/'));
			    		
			    	}
			    	else{
			    		if (!(commandType(line)==CommandNames.L_COMMAND))
			    			currentLine++;
			    		return line;
			    	}
			    }
			}
			else{
				return "END";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "END";
		}
		
	}
	
	CommandNames commandType(String rawLine){
		
		if (rawLine.charAt(0)=='@'){
			return CommandNames.A_COMMAND;
		}
		else if(rawLine.charAt(0)=='('){
			return CommandNames.L_COMMAND;
		}
		else if(rawLine.equals("END")){
			return CommandNames.END;
		}
		else{
			return CommandNames.C_COMMAND;
		}
	}
	
	/**
	 * Takes the string of a command, strips symbols
	 * @param command A or L command
	 * @return just the symbol/decimal
	 */
	String symbol(String command){
		//A Commands
		if (command.charAt(0)=='@'){
			if ((command.charAt(1)>='0')&&(command.charAt(1)<='9'))
				return command.substring(1);
			//symbols
			else{
				if (st.contains(command.substring(1)))
					return st.getAddress(command.substring(1));
				else
					return st.addEntry(command.substring(1));
			}
		}
		else{
			st.addEntry(command.substring(1,command.length()-1), currentLine);
			return "skip";
		}
	}
	
	/**
	 * @param command C command
	 * @return destination mnemonic
	 */
	String dest(String command){
		if (command.contains("=")){
			return command.substring(0, command.indexOf('='));
		}
		else{
			return "null";
		}
	}
	
	/**
	 * @param command C command
	 * @return jump mnemonic
	 */
	String jump(String command){
		if (command.contains(";")){
			return command.substring(command.indexOf(';')+1,command.length());
		}
		else{
			return "null";
		}
	}
	
	/**
	 * @param command C command
	 * @return computation mnemonic
	 */
	String comp(String command){
		if (command.contains("=")){
			if (command.contains(";")){
				return command.substring(command.indexOf('=')+1, command.indexOf(';'));
			}
			else{
				return command.substring(command.indexOf('=')+1, command.length());
			}
		}
		else{
			if (command.contains(";")){
				return command.substring(0, command.indexOf(';'));
			}
			else{
				return command;
			}
		}
	}
	

}
