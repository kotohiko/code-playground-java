package org.jacob.threads;

import org.jacob.threads.pcp.Consumer;
import org.jacob.threads.pcp.Producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The ProducerConsumerTest class demonstrates the producer-consumer pattern using
 * a shared blocking queue. It creates and starts threads for both a producer and a
 * consumer, allowing them to communicate through the queue.
 *
 * <p>
 * The producer will generate integers and put them into the queue, while the consumer
 * will take integers from the queue and process them. The blocking queue helps manage
 * the coordination between the producer and consumer, preventing data loss and
 * ensuring thread safety.
 * </p>
 *
 * @author Kotohiko
 * @since 11:07 Oct 04, 2024
 */
public class ProducerConsumerTest {
    public static void main(String[] args) {
        // Create a blocking queue of size 10 to serve as the shared buffer
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // Create producer and consumer threads
        Thread producerThread = new Thread(new Producer(queue));
        Thread consumerThread = new Thread(new Consumer(queue));

        // Start the producer and consumer threads
        producerThread.start();
        consumerThread.start();
    }
}
