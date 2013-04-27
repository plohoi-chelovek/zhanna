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
	// for (WatchEvent<?> event: key.pollEvents()) {
	//     WatchEvent.Kind kind = event.kind();
	//     // TBD - provide example of how OVERFLOW event is handled
	//     if (kind == OVERFLOW) {
	// 	continue;
	//     }
	//     // Context for directory entry event is the file name of entry
	//     WatchEvent<Path> ev = cast(event);
	//     Path name = ev.context();
	//     Path child = dir.resolve(name);

	//     if (!key.reset()) {
	// 	keys.remove(key);
	// 	if (keys.isEmpty())
	// 	    break;
	//     }
	return true;
    }

	

    private void register(Path dir) throws IOException {
	WatchKey key = dir.register(watcher, ENTRY_CREATE);
	keys.put(key, dir);
    }
}    


