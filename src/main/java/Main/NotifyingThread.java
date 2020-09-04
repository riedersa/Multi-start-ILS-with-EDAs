package Main;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This class can be used to create threads, which notify a set of listeners if they finish their computation.
 */
public abstract class NotifyingThread extends Thread {
    //The set of listeners to notify
    private final Set<ThreadCompleteListener> listeners
            = new CopyOnWriteArraySet<ThreadCompleteListener>();


    /**
     * This method adds a listener to the list.
     *
     * @param listener the listener to add
     */
    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }


    /**
     * This method removes a listener from the list. It will no longer be notified.
     *
     * @param listener the listener to remove
     */
    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }


    /**
     * This method notifies each listener.
     */
    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }


    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }


    /**
     * This method contains the stuff, the thread should do.
     */
    public abstract void doRun();
}
