package ua.com.zhanna.watching; 

import java.util.*;

import java.nio.file.*;

public class EntryEvent extends EventObject {
    private Path entry;
    
    public EntryEvent(Object source, Path entry) {
	super(source);
	this.entry = entry;
    }

    public Path getEntry() {
	return entry;
    }
}
