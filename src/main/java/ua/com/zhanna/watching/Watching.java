package ua.com.zhanna.watching;

import java.nio.file.*;
import java.nio.file.attribute.*;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;

import java.io.*;

import java.util.*;

import javax.swing.event.*;


public class Watching extends Thread {
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;

    private EventListenerList listeners = new EventListenerList();

    public Watching(String dir) throws IOException {
	this(new File(dir));
    }

    public Watching(File dir) throws IOException {
	this.watcher = FileSystems.getDefault().newWatchService();
	this.keys = new HashMap<WatchKey,Path>();
	register(Paths.get(dir.toURI()));
    }

    public boolean cancel() {
	keys.keySet().iterator().next().cancel();
	try {
	    watcher.close();
	} catch (IOException e) { return false; }
	return true;
    }

    public void run() {
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
		if (!processKeyEvents(key)) {
		    fireWatchKeyError(key); //some thing bad is hapenning
		    break;
		}
	    }
	} catch (InterruptedException x) {
	    fireInterruptedError(x.getMessage());
	} catch (ClosedWatchServiceException x) {
	    fireInterruptedError(x.getMessage());
	}
    }

    private boolean processKeyEvents(WatchKey key) {
	for (WatchEvent<?> event: key.pollEvents()) {
	    WatchEvent.Kind kind = event.kind();
	    if (kind == OVERFLOW) {
		continue;
	    } else if (kind == ENTRY_CREATE) {
		WatchEvent<Path> ev = cast(event);
		fireEntryIsCreated(keys.get(key).resolve(ev.context()));
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

    
    public void addWatchingListener(WatchingListener l) {
	listeners.add(WatchingListener.class, l);
    }

    public void removeWatchingListener(WatchingListener l) {
	listeners.remove(WatchingListener.class, l);
    }
    
    private void fireEntryIsCreated(Path entry) {
	EntryEvent event = new EntryEvent(this, entry);
	Object[] l = listeners.getListenerList();
	for (int i = l.length-2; i>=0; i-=2) {
	    if (l[i]==WatchingListener.class) {
		((WatchingListener)l[i+1]).entryIsCreated(event);
	    }
	}
    }

    private void fireInterruptedError(String message) {
	ErrorEvent event = new ErrorEvent(this, message);
	Object[] l = listeners.getListenerList();
	for (int i = l.length-2; i>=0; i-=2) {
	    if (l[i]==WatchingListener.class) {
		((WatchingListener)l[i+1]).interruptedError(event);
	    }
	}
    }

    private void fireWatchKeyError(WatchKey key) {
	ErrorEvent event = new ErrorEvent(this, key);
	Object[] l = listeners.getListenerList();
	for (int i = l.length-2; i>=0; i-=2) {
	    if (l[i]==WatchingListener.class) {
		((WatchingListener)l[i+1]).watchKeyError(event);
	    }
	}
    }

    /* TEST THIS SUBSYSTEM */
    public static void main(String args[]) throws IOException, InterruptedException {
	Watching simpleWatching = new Watching(args[0]);
	simpleWatching.addWatchingListener(new WatchingListener() {
		public void entryIsCreated(EntryEvent e) {
		    System.out.println(e.getEntry());
		}
		public void interruptedError(ErrorEvent event) {
		    System.out.println("interruptedError");
		}
		public void watchKeyError(ErrorEvent event) {
		    System.out.println("watchKeyError");
		}
	    });
	simpleWatching.start();
    }
}    


