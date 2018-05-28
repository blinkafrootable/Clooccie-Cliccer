package clicc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class SaveLoad {

    public void saveGame(Generator[] generators, long clooccies) {
        StringBuilder baseCosts = new StringBuilder().append("bc/"),
                productions = new StringBuilder().append("p/"),
                counts = new StringBuilder().append("c/"),
                indexes = new StringBuilder().append("i/"),
                timerDelays = new StringBuilder().append("td/"),
                labels = new StringBuilder().append("l/");
        for (Generator g : generators) {
            baseCosts.append(g.getBaseCost()).append("/");
            productions.append(g.getProduction()).append("/");
            counts.append(g.getCount()).append("/");
            indexes.append(g.getIndex()).append("/");
            timerDelays.append((g.getTimer().getDelay())).append("/");
            labels.append(g.getLabel()).append("/");
        }
        StringBuilder[] data = {baseCosts, productions, counts, indexes, timerDelays, labels};
        StringBuilder output = new StringBuilder();
        output.append("cl/").append(clooccies).append("\n");
        File path;
        OutputStream outputStream;
        try {
            path = new File("SaveData.txt");
            outputStream = new FileOutputStream(path);
            for (StringBuilder b : data) {
                b.append("\n");
                output.append(b);
            }
            outputStream.write(output.toString().getBytes());
            outputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public boolean saveDataExists() {
    	boolean returnValue = false;
    	try {
    		BufferedReader bufferedReader = new BufferedReader(new FileReader("SaveData.txt"));
    		returnValue = bufferedReader.readLine() != null;
    		bufferedReader.close();
    		return returnValue;
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    		returnValue =  false;
    	}
    	return returnValue;
    }
    
    public Object fetchData(String label) {
    	Object returnValue = null;
    	try {
    		File saveDataFile = new File("SaveData.txt");
    		Scanner scanner = new Scanner(saveDataFile);
    		
    		while (scanner.hasNextLine()) {
    			String content = scanner.nextLine();
    			String[] contentArray = content.split("/");
    			if (contentArray[0].equals(label)) {
    				switch (label) {
    				case "cl":
    					returnValue = Long.parseLong(contentArray[1]);
    					break;
    				case "bc": case "p":
						long[] longArray = new long[contentArray.length-1];
    					for (int i = 1; i < contentArray.length; i++) {
    						longArray[i-1] = Long.parseLong(contentArray[i]);
    					}
    					returnValue = longArray.clone();
    					break;
    				case "c": case "i": case "td":
    					int[] intArray = new int[contentArray.length-1];
    					for (int i = 1; i < contentArray.length; i++) {
    						intArray[i-1] = Integer.parseInt(contentArray[i]);
    					}
    					returnValue = intArray.clone();
    					break;
    				case "l": 
    					String[] stringArray = new String[contentArray.length-1];
    					for (int i = 1; i < contentArray.length; i++) {
    						stringArray[i-1] = contentArray[i];
    					}
    					returnValue = stringArray.clone();
    					break;
    				}
    			}
    		}
    		scanner.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return returnValue;
    }
    
    public void wipeSave() {
    	File path;
    	OutputStream outputStream;
    	try {
            path = new File("SaveData.txt");
            outputStream = new FileOutputStream(path);
            outputStream.write("".getBytes());
            outputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
