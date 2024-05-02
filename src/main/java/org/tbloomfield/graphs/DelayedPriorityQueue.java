package org.tbloomfield.graphs;

import java.time.Duration;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A collection with an artificial delay on poll()
 * @param <T>
 */
public class DelayedPriorityQueue<T> extends PriorityQueue<T> {
    private final Duration delay;
    
    public DelayedPriorityQueue(Duration delay, Comparator<? super T> comparator) {
      super(comparator);
      this.delay = delay;
    }
    
    public T poll() {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {}
      return super.poll();
    }
}
