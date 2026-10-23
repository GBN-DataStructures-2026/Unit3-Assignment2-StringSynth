/**
 * StringSynthLite plays two guitar strings (Concert A and Concert C) when the user
 * types the lowercase letters 'a' and 'c' in the graphics window.
 * 
 * Use this starter program to test that your RingBuffer and GuitarString 
 * implementations work before building the full 37-note StringSynth.
 */
public class StringSynthLite 
{
    public static void main(String[] args) 
    {
        // Create two guitar strings: Concert A (440 Hz) and Concert C (~523.25 Hz)
        double CONCERT_A = 440.0;
        double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
        
        GuitarString stringA = new GuitarString(CONCERT_A);
        GuitarString stringC = new GuitarString(CONCERT_C);

        System.out.println("StringSynthLite is running!");
        System.out.println("Click the graphics window and press 'a' or 'c' to play notes.");

        // Main simulation & sound loop
        while (true) 
        {
            // Check if user has typed a key in the StdDraw window
            if (StdDraw.hasNextKeyTyped()) 
            {
                char key = StdDraw.nextKeyTyped();

                // Pluck the corresponding string
                if (key == 'a') 
                {
                    stringA.pluck();
                }
                if (key == 'c') 
                {
                    stringC.pluck();
                }
            }

            // Superposition: Compute the sum of the acoustic wave samples[cite: 51, 58]
            double sample = stringA.sample() + stringC.sample();[cite: 51]

            // Send superposition sample to sound card[cite: 51]
            StdAudio.play(sample);

            // Advance the simulation of each guitar string by one time step (tic)[cite: 51]
            stringA.tic();
            stringC.tic();
        }
    }
}