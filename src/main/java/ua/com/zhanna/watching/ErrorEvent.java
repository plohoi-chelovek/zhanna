package ua.com.zhanna.watching; 

import java.util.*;

import java.nio.file.*;

public class ErrorEvent extends EventObject {
    private WatchKey watchKey;
    private String message;
    
    public ErrorEvent(Object source, WatchKey watchKey) {
	super(source);
	this.watchKey = watchKey;
    }

    public ErrorEvent(Object source, String message) {
	super(source);
	this.message = message;
    }

    public WatchKey getWatchKey() {
	return watchKey;
    }

    public String getMessage() {
	return message;
    }
}
