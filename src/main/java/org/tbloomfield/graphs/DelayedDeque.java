package org.tbloomfield.graphs;

import java.time.Duration;
import java.util.ArrayDeque;

/**
 * A collection with an artificial delay on poll()
 * @param <T>
 */
public class DelayedDeque<T> extends ArrayDeque<T> {
    private final Duration delay;
    
    public DelayedDeque(Duration delay) { 
        this.delay = delay;
    }
    
    
    public T pop() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //TODO
        } finally { 
            return super.pop();
        }
    }
}
