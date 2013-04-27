package ua.com.zhanna.watching;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

class Watching {
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;

    public Watching(String dir) throws IOException {
	this.watcher = FileSystems.getDefault().newWatchService();
	this.keys = new HashMap<WatchKey,Path>();
	register(Paths.get(dir));
    }

    public void start() {
	try {
	    while (true) {
		/* get key */
		WatchKey key = watcher.take();
		/* and test it */
		if (!keys.containsKey(key))
		    continue; //watch key doesnt recognizable;
		/* if we are here then we get events associated with register dir
		 * its time to process it 
		 */
		if (!processKeyEvents(key))
		    break; //and maybe fire some thing
	    }
	} catch (InterruptedException x) {
	    return; //fire some thing?;
	}
    }

    private boolean processKeyEvents(WatchKey key) {
	for (WatchEvent<?> event: key.pollEvents()) {
	    WatchEvent.Kind kind = event.kind();
	    if (kind == OVERFLOW) {
		continue;
	    } else if (kind == ENTRY_CREATE) {
		WatchEvent<Path> ev = cast(event);
		Path name = ev.context();
		// Path child = dir.resolve(name);
		System.out.println(name);
		break;
	    }
	}
	return key.reset();
    }

    @SuppressWarnings("unchecked")
    <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    private void register(Path dir) throws IOException {
	WatchKey key = dir.register(watcher, ENTRY_CREATE);
	keys.put(key, dir);
    }

    /* TEST THIS SUBSYSTEM */
    public static void main(String args[]) throws IOException {
	new Watching(args[0]).start();
    }
}    


