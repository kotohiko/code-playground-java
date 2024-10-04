package org.jacob.threads.pcp;

import java.util.concurrent.BlockingQueue;

/**
 * Producer class that implements the {@link Runnable} interface to continuously produce
 * integer elements and place them into a {@link BlockingQueue}. The producer will block
 * if the queue is full, waiting for space to become available.
 *
 * <p>
 * The producer generates an incrementing integer sequence indefinitely until interrupted.
 *
 * @author Kotohiko
 * @since 11:10 Oct 04, 2024
 */
public class Producer implements Runnable {

    private final BlockingQueue<Integer> queue;

    /**
     * Constructs a new {@code Producer} with the specified {@link BlockingQueue}.
     *
     * @param queue the blocking queue where produced elements will be added
     */
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    /**
     * The run method that continuously produces integers and puts them into the queue.
     * It will block if the queue is full, waiting for available space.
     * <p>
     * If the thread is interrupted, the loop will terminate.
     */
    @Override
    public void run() {
        int i = 0;
        while (true) {
            try {
                // Print the current value being produced
                System.out.println("Producing: " + i);

                // Put the element into the queue, blocks if the queue is full
                queue.put(i);

                // Increment the value for the next production
                ++i;

                // Simulate time taken to produce the item
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Restore the interrupt flag and exit the loop
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
