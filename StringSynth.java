/**
 * StringSynth simulates a 37-string guitar played interactively
 * via a piano-mapped QWERTY keyboard.
 */
public class StringSynth 
{
    // Piano keyboard mapping corresponding to 37 chromatic notes from Concert C to High C
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final int NUM_NOTES = 37;

    public static void main(String[] args) 
    {
        // TODO: Create an array of 37 GuitarString objects.
        // Initialize each string i with frequency: 440.0 * Math.pow(2, (i - 24) / 12.0)
        


        System.out.println("StringSynth is ready! Click on the graphics window and start playing.");

        // Main simulation and audio loop
        while (true) 
        {
            // TODO: Step 1 - Handle user input
            // Check if user typed a key (StdDraw.hasNextKeyTyped()).
            // If true, read character (StdDraw.nextKeyTyped()) and find its index in KEYBOARD.
            // If index is valid (>= 0), pluck the corresponding string in your array.


            // TODO: Step 2 - Compute superposition
            // Calculate the sum of sample() across all 37 guitar strings.


            // TODO: Step 3 - Play audio sample
            // Send the total sample sum to StdAudio.play()


            // TODO: Step 4 - Advance physics simulation
            // Call tic() on all 37 guitar strings.

        }
    }
}
