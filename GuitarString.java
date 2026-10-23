/**
 * Models a vibrating guitar string using a RingBuffer (Queue)
 * and the Karplus-Strong sound synthesis algorithm.
 */
public class GuitarString 
{
    private static final int SAMPLE_RATE = 44100;
    private static final double DECAY_FACTOR = 0.994;

    private RingBuffer buffer; // The circular queue storing displacement samples
    private int ticCount;      // Total number of tics elapsed

    /**
     * Creates a guitar string of the given frequency.
     * Capacity N = ceil(SAMPLE_RATE / frequency).
     * Fills the buffer queue with N zeroes (string at rest).
     */
    public GuitarString(double frequency) 
    {
        // TODO: Calculate capacity N, instantiate RingBuffer, enqueue N zeroes
    }

    /**
     * Creates a guitar string with initial buffer contents given by array.
     * Used primarily for testing and debugging.
     */
    public GuitarString(double[] array) 
    {
        // TODO: Instantiate RingBuffer with length equal to array, enqueue all values
    }

    /**
     * Plucks the string by replacing all samples in the buffer queue
     * with random white noise in the range [-0.5, +0.5].
     */
    public void pluck() 
    {
        // TODO: Empty current buffer and refill with random samples between -0.5 and 0.5
    }

    /**
     * Advances the Karplus-Strong simulation by one time step (1 tic):
     * 1. Dequeue front sample (x)
     * 2. Peek at new front sample (y)
     * 3. Enqueue new sample: DECAY_FACTOR * 0.5 * (x + y)
     * 4. Increment ticCount
     */
    public void tic() 
    {
        // TODO: Perform Karplus-Strong queue update
    }

    /**
     * Returns the current audio sample at the front of the queue.
     */
    public double sample() 
    {
        // TODO: Return buffer.peek()
        return 0.0;
    }

    /**
     * Returns total number of times tic() was called.
     */
    public int time() 
    {
        return ticCount;
    }

    public static void main(String[] args) 
    {
        double[] samples = { .2, .4, .5, .3, -.2, .4, .3, .0, -.1, -.3 };  
        GuitarString testString = new GuitarString(samples);
        for (int i = 0; i < 25; i++) 
        {
            int t = testString.time();
            double sample = testString.sample();
            System.out.printf("%6d %8.4f\n", t, sample);
            testString.tic();
        }
    }
}