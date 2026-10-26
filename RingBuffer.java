/**
 * RingBuffer models a fixed-capacity circular queue for audio synthesis.
 * 
 * CONCEPTUAL BRIDGE:
 * - add(x)   == enqueue (places item at rear pointer)
 * - remove() == dequeue (removes item from front pointer)
 * - peek()   == front   (looks at element at front pointer)
 */
public class RingBuffer 
{
    private double[] data;
    private int front;
    private int rear;
    private int size;

    /**
     * Creates an empty ring buffer (circular queue) with the given capacity.
     */
    public RingBuffer(int capacity)
    {
        // TODO: Initialize array of size capacity, front = 0, rear = -1, size = 0
    }

    public int size()
    {
        // TODO: Return current number of items in buffer
        // You can use the size variable instead of calculating from front and rear
        return 0;
    }
    
    public boolean isEmpty()
    {
        // TODO: Return true if buffer is empty
        return false;
    }
    
    public boolean isFull()
    {
        // TODO: Return true if buffer capacity is reached (size == capacity)
        return false;
    }
    
/**
 * Enqueues value to the rear of the ring buffer.
 * Throws IllegalStateException if the buffer is full.
 */
public void add(double value) {
    if (isFull()) {
        throw new IllegalStateException("RingBuffer is full!");
    }
    rear = (rear + 1) % data.length;
    data[rear] = value;
    size++;
}

/**
 * Returns (without deleting) the sample at the front of the queue.
 * Throws QueueUnderflowException if the buffer is empty.
 */
public double peek() {
    if (isEmpty()) {
        throw new QueueUnderflowException("RingBuffer is empty!");
    }
    return data[front];
}

/**
 * Dequeues and returns the sample from the front of the queue.
 * Throws QueueUnderflowException if the buffer is empty.
 */
public double remove() {
    if (isEmpty()) {
        throw new QueueUnderflowException("RingBuffer is empty!");
    }
    double temp = data[front];
    front = (front + 1) % data.length;
    size--;
    return temp;
}
    
    /**
     * Test client to verify RingBuffer circular logic.
     */
    public static void main(String[] args) 
    {
        int capacity = 100;
        RingBuffer buffer = new RingBuffer(capacity);  
        for (int i = 1; i <= capacity; i++) 
            buffer.add(i);
      
        double t = buffer.remove();
        buffer.add(t);
        System.out.println("Size after wrap-around is " + buffer.size());
        
        while (buffer.size() >= 2) 
        {
            double x = buffer.remove();
            double y = buffer.remove();
            buffer.add(x + y);
        }
        System.out.println("Final peek value: " + buffer.peek()); // Expected: 5050.0
    }
}
