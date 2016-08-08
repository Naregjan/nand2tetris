package lesson06;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	HashMap<String, Integer> symbol = new HashMap<String, Integer>(30);
	int nextFree=16;
	
	public SymbolTable(){		
		symbol.put("SP",0);
		symbol.put("LCL",1);
		symbol.put("ARG",2);
		symbol.put("THIS",3);
		symbol.put("THAT",4);
		symbol.put("R0",0);
		symbol.put("R1",1);
		symbol.put("R2",2);
		symbol.put("R3",3);
		symbol.put("R4",4);
		symbol.put("R5",5);
		symbol.put("R6",6);
		symbol.put("R7",7);
		symbol.put("R8",8);
		symbol.put("R9",9);
		symbol.put("R10",10);
		symbol.put("R11",11);
		symbol.put("R12",12);
		symbol.put("R13",13);
		symbol.put("R14",14);
		symbol.put("R15",15);
		symbol.put("SCREEN",16384);
		symbol.put("KBD",24576);
	}
	
	
	void addEntry(String key,int value){
		symbol.put(key, value);
	}
	
	/**
	 * puts label in next available memory slot, starting at 16
	 * somewhat expensive, thanks to need to check value
	 * @param key symbol to be placed in table
	 */
	String addEntry(String key){
		if (symbol.containsValue(nextFree)){
			nextFree++;
			return addEntry(key);
		}
		symbol.put(key, nextFree);
		nextFree++;
		return Integer.toString(nextFree-1);
	}
	
	boolean contains(String key){
		return symbol.containsKey(key);
	}
	
	String getAddress(String key){
		return Integer.toString(symbol.get(key));
	}

}
