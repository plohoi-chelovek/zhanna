package ua.com.zhanna.printing;

import java.nio.file.*;
import java.nio.charset.*;

import java.awt.print.*;

import javax.swing.*;

class Printing {
    public boolean print(Path path) {
	JTextArea textArea = new JTextArea();
	try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
			textArea.append(line + "\n");
		    }
		    textArea.print();
	    }
    }

    /* TEST IT */
    public static void main(String[] args) {
	//soon
    }
}
