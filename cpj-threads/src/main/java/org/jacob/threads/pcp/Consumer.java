package org.jacob.threads.pcp;

import java.util.concurrent.BlockingQueue;

/**
 * Consumer class that implements the {@link Runnable} interface to continuously consume
 * elements from a {@link BlockingQueue}. The consumer will take elements from the queue,
 * simulate processing, and block if the queue is empty.
 *
 * <p>
 * The consumer is designed to run indefinitely until interrupted.
 *
 * @author Kotohiko
 * @since 11:10 Oct 04, 2024
 */
public class Consumer implements Runnable {

    private final BlockingQueue<Integer> queue;

    /**
     * Constructs a new {@code Consumer} with the specified {@link BlockingQueue}.
     *
     * @param queue the blocking queue from which the consumer will take elements
     */
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    /**
     * The run method that continuously consumes items from the queue. It will block
     * if the queue is empty, waiting for new elements to be added.
     * <p>
     * If the thread is interrupted, the loop will terminate.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Take an element from the queue, blocks if the queue is empty
                Integer item = queue.take();
                System.out.println("Consuming: " + item);

                // Simulate time taken to process the item
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore the interrupt flag and exit the loop
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
