package ua.com.zhanna.printing;

import java.io.*;

import java.nio.file.*;
import java.nio.charset.*;

import java.awt.print.*;

import javax.swing.*;

class Printing {
    public void print(Path path) {
	JTextArea textArea = new JTextArea();
	try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
			textArea.append(line + "\n");
		    }
		    textArea.print();
	    } catch (Exception e) { /* log some where */ }
    }

    /* TEST IT */
    public static void main(String[] args) {
	//soon
    }
}
