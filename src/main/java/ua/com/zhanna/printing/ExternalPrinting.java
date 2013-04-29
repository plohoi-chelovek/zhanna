package ua.com.zhanna.printing;

import java.io.*;

import java.nio.file.*;
import java.nio.charset.*;

import org.pmw.tinylog.Logger;

public class ExternalPrinting implements Printing {
    private String command;

    public ExternalPrinting(String command) {
	this.command = command;
    }

    public void print(final Path path) {
	new Thread(new Runnable() {
		public void run() {
		    try {
			Process p = Runtime.getRuntime().exec(command + " " + path.toString());  
			BufferedReader in = new 
			    BufferedReader(new 
					   InputStreamReader(p.getInputStream()));  
			String line = null;  
			while ((line = in.readLine()) != null) {  }  
		    } catch (Exception e) {
			Logger.error(e.getMessage());
		    }
		}
	    }).start();
    }

    /* TEST IT */
    public static void main(String[] args) {
	ExternalPrinting printing = new ExternalPrinting("emacs");
	printing.print(Paths.get(args[0]));
	System.out.println("exit from main");
    }
}




