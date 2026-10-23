//import java.io.*;
import javax.sound.sampled.*;

/**
 * Standard Audio library adapted with diagnostic logging for StringSynth.
 */
public final class StdAudio {

    public static final int SAMPLE_RATE = 44100;
    private static final int BYTES_PER_SAMPLE = 2;
    private static final int BITS_PER_SAMPLE = 16;
    private static final double MAX_16_BIT = Short.MAX_VALUE;
    private static final int SAMPLE_BUFFER_SIZE = 4096;

    private static SourceDataLine line;
    private static byte[] buffer;
    private static int bufferSize = 0;

    private StdAudio() { }

    static { init(); }

    private static void init() {
        try {
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
            line.start();
        } catch (Exception e) {
            System.err.println("\n=======================================================");
            System.err.println("*** AUDIO ENGINE INITIALIZATION ERROR ***");
            System.err.println("Could not open system audio line. Diagnostic Checklist:");
            System.err.println("  1. Disconnect Bluetooth headsets/AirPods (switch to built-in speakers).");
            System.err.println("  2. Close apps locking audio output (Zoom, Teams, Discord, Spotify).");
            System.err.println("  3. Ensure system volume is unmuted.");
            System.err.println("  4. If using WSL/Remote VM, run locally on Windows/macOS.");
            System.err.println("=======================================================\n");
            e.printStackTrace();
        }
    }

    public static void close() {
        if (line != null) {
            line.drain();
            line.stop();
        }
    }

    public static void play(double in) {
        if (line == null) return; // Fail gracefully if audio initialized with errors

        if (in < -1.0) in = -1.0;
        if (in > +1.0) in = +1.0;

        short s = (short) (MAX_16_BIT * in);
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);

        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    public static void play(double[] input) {
        for (double sample : input) {
            play(sample);
        }
    }
}
