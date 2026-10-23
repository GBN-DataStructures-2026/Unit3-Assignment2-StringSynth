/**
 * StringSynth simulates a 37-string guitar played interactively
 * via a piano-mapped QWERTY keyboard.
 */
public class StringSynth 
{
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final int NUM_NOTES = 37;

    public static void main(String[] args) 
    {
        // TODO:
        // 1. Create an array of 37 GuitarString objects.
        // 2. Frequency formula for note i: 440.0 * Math.pow(2, (i - 24) / 12.0)
        
        GuitarString[] strings = new GuitarString[NUM_NOTES];
        for (int i = 0; i < NUM_NOTES; i++) 
        {
            double freq = 440.0 * Math.pow(2, (i - 24) / 12.0);
            strings[i] = new GuitarString(freq);
        }

        System.out.println("StringSynth is ready! Click on the graphics window and play keys.");

        while (true) 
        {
            // Check for user key press
            if (StdDraw.hasNextKeyTyped()) 
            {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);

                // Pluck matching string if key exists in KEYBOARD string
                if (index != -1) 
                {
                    strings[index].pluck();
                }
            }

            // Superposition: Sum samples across all 37 guitar strings
            double sample = 0.0;
            for (int i = 0; i < NUM_NOTES; i++) 
            {
                sample += strings[i].sample();
            }

            // Send superposition sample to audio card
            StdAudio.play(sample);

            // Advance simulation 1 tic for all strings
            for (int i = 0; i < NUM_NOTES; i++) 
            {
                strings[i].tic();
            }
        }
    }
}