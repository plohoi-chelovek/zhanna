package ua.com.zhanna;

import java.io.*;

import org.pmw.tinylog.Logger;

import ua.com.zhanna.watching.*;
import ua.com.zhanna.operation.*;
import ua.com.zhanna.printing.*;

public class Application {
    private Watching watching;
    private Operation operation;
    private Printing printing;

    public Application(String path) throws IOException {
	watching = new Watching(path);
	operation = new EncodingOperation();
	printing = new Printing();
    }

    public void start() {
	watching.addWatchingListener(new WatchingListener() {
		public void entryIsCreated(EntryEvent e) {
		    printing.print(operation.operate(e.getEntry()));
		}
		public void interruptedError(ErrorEvent event) {
		    Logger.error("interrupted error");
		}
		public void watchKeyError(ErrorEvent event) {
		    Logger.error("watchkey error");
		}
	    });
	watching.start();
    }

    /* ENTER POINT */
    public static void main(String[] args) throws IOException {
	new Application(args[0]).start();
    }
}
