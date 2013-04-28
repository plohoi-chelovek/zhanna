package ua.com.zhanna.watching;

import java.util.*;

public interface WatchingListener extends EventListener {
    public void entryIsCreated(EntryEvent event);
    public void interruptedError(ErrorEvent event);
    public void watchKeyError(ErrorEvent event);
}
