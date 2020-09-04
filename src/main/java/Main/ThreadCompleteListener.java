package Main;

/**
 * This interface describes a listener, which can be notified by a {@link NotifyingThread}
 */
public interface ThreadCompleteListener {
    /**
     * The method to be executed, when a thread finishes.
     *
     * @param thread the thread that finished its work
     */
    void notifyOfThreadComplete(final Thread thread);
}
