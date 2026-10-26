# Unit 3 - Assignment 2: StringSynth (Karplus-Strong Audio Synthesis)

## Overview
In this project, you will build an interactive 37-note musical synthesizer called **StringSynth**. 

At the heart of real-time sound synthesis is a **Queue Abstract Data Type (ADT)**. You will build a fixed-capacity circular queue called `RingBuffer` to model energy traveling along a vibrating guitar string using the **Karplus-Strong algorithm**.

---

## Acoustics & Math: Calculating String Frequencies

In Western music, equal temperament tuning divides each octave (a $2\times$ doubling of sound frequency) into 12 mathematically equal intervals called **semitones** (half-steps). Because pitch perception is logarithmic, moving up by a single semitone requires multiplying the fundamental frequency by $2^{1/12} \approx 1.05946$.

In `StringSynth`, we map 37 physical keys on the QWERTY keyboard to 37 chromatic notes spanning three full octaves. By convention, **Concert A** ($440\text{ Hz}$) is assigned to index $i = 24$ (corresponding to key `'i'`). The fundamental frequency $f_i$ for any string index $i$ ($0 \le i < 37$) is calculated as:

$$f_i = 440.0 \times 2^{\frac{i - 24}{12}}$$

As frequency increases, wavelength shrinks, requiring a smaller ring buffer capacity $N$:

$$N = \left\lceil \frac{\text{Sample Rate}}{\text{frequency}} \right\rceil = \left\lceil \frac{44100}{f_i} \right\rceil$$

A high-pitched note cycles through its buffer rapidly, producing a shorter acoustic period, while a low-pitched note requires a larger buffer to hold its longer wave.

---

## How Queues Model Physics: The Karplus-Strong Algorithm

When a guitar string is plucked, energy travels back and forth in acoustic waves. We model this physical vibration using a circular queue of capacity $N$.

### 1. Plucking the String (`pluck()`)
Plucking excites the string with white noise. We model this by filling all $N$ slots of our queue with random floating-point values in the range $[-0.5, +0.5]$.

### 2. Simulating Wave Decay (`tic()`)
As the sound wave reflects off the fixed ends of the string, energy decays and higher harmonics smooth out. Every time step (`tic()`), the simulation performs three queue operations:

1. **Dequeue** the sample at the front of the queue ($x$).
2. **Peek** at the next sample now at the front of the queue ($y$).
3. **Enqueue** a new decay sample back to the rear:

$$\text{newSample} = 0.994 \times \frac{x + y}{2}$$

```text
Trace Example:
  time t:    [ .2 ] [ .4 ] [ .5 ] [ -.2 ]  <-- Dequeue .2, Peek .4
               \      /
             0.994 * 0.5 * (.2 + .4) = .2982
               ↓
  time t+1:  [ .4 ] [ .5 ] [ -.2 ] [ .2982 ] <-- Enqueue .2982 at rear
```

---

## Key Architectural Concepts

### 1. The Principle of Superposition (Polyphony)
When multiple guitar strings vibrate simultaneously, their sound waves interfere constructively and destructively in the air. To simulate realistic polyphonic chords, your audio loop must calculate the **superposition sum** of all active strings during every iteration:

$$\text{totalSample} = \sum_{i=0}^{36} \text{string}_i.\text{sample}()$$

This single combined amplitude value is then passed to `StdAudio.play(totalSample)`.

### 2. Keyboard Lookup Table (`KEYBOARD.indexOf`)
The string mapping is defined as:
```java
String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
```
To map a typed character `key` to its corresponding string index in your array, use:
```java
int index = KEYBOARD.indexOf(key);
```
If `index >= 0`, `index` directly matches the index in your 37-element `GuitarString[]` array.

---

## Your Tasks & Recommended Verification Pipeline

Follow this build pipeline step-by-step to test and verify each component before moving to the next.

### Task 1: `RingBuffer.java`
Implement a circular array queue of doubles using bounded array storage.
- `RingBuffer(int capacity)` – Constructs an empty queue of fixed capacity with `front = 0`, `rear = -1`, `size = 0`.
- `void add(double x)` – Enqueues $x$ at `rear` modulo capacity. Throws `RuntimeException` if full.
- `double remove()` – Dequeues sample from `front` modulo capacity. Throws `QueueUnderflowException` if empty.
- `double peek()` – Returns sample at `front` without removing.
- `int size()` & `boolean isEmpty()` & `boolean isFull()` – Helper state queries.

**Verification Step 1:** Compile and run `RingBuffer.main()`. Your output must match:
```text
Size after wrap-around is 100
Final peek value: 5050.0
```

### Task 2: `GuitarString.java`
Models a single vibrating guitar string using `RingBuffer`.
- `GuitarString(double frequency)` – Calculates $N = \lceil 44100 / f \rceil$ using `Math.ceil()` and initializes `RingBuffer` filled with $N$ zeroes.
- `GuitarString(double[] init)` – Construct a string directly from an array (useful for deterministic unit testing).
- `pluck()` – Replaces all elements in `RingBuffer` with random values in $[-0.5, +0.5]$.
- `tic()` – Dequeues $x$, peeks $y$, enqueues $0.994 \times (x + y) / 2$, and increments `ticCount`.
- `sample()` – Returns the value currently at `peek()`.
- `time()` – Returns `ticCount`.

**Verification Step 2:** Compile and run `GuitarString.main()`. It will print values from buffer indices 0 to 24, with index 24 showing `0.2223`.

**Verification Step 3:** Compile and run `StringSynthLite.java`. Click the graphics window and press `'a'` and `'c'` to play a 2-note duet.

### Task 3: `StringSynth.java`
Build the full 37-note interactive keyboard synthesizer.
- Initialize an array of 37 `GuitarString` objects using the formula $f_i = 440.0 \times 2^{(i - 24) / 12}$.
- In the main simulation loop:
  1. Detect key presses via `StdDraw.hasNextKeyTyped()` and `pluck()` the matching string.
  2. Compute superposition (sum of `sample()` across all 37 strings).
  3. Send the combined sample sum to `StdAudio.play(totalSample)`.
  4. Advance time by calling `tic()` on all 37 strings.

**Verification Step 4:** Run `StringSynth.java` and play notes using your QWERTY keyboard! Refer to `keyboard.png` for key-to-note mapping.

---

## Troubleshooting Audio Issues

If you hear silence, stuttering, or get an audio driver error in the console:

1. **AirPods / Bluetooth Headsets:** Disconnect Bluetooth headphones. Bluetooth hands-free profiles lower audio sample rates and can lock Java's `javax.sound` system line. Switch to built-in laptop speakers.
2. **Exclusive Audio Locks:** Close external apps that lock sound devices (Zoom, Teams, Discord, Spotify).
3. **WSL / Remote Containers:** WSL2 does not route GUI/audio out to Windows natively without pulse-audio configuration. Run the project in native Windows/macOS VS Code.

---

## Optional Creative Challenge: Alternative Instruments

The Karplus-Strong algorithm can be modified to synthesize completely different acoustic instruments! Try creating a second instrument class (e.g., `DrumString.java` or `PianoString.java`):

* **Drum / Percussion:** Flip the sign of the new sample with probability $0.5$ during `tic()`:

$$\text{newSample} = \begin{cases} +0.994 \times \frac{x + y}{2} & \text{with prob } 0.5 \\[6pt] -0.994 \times \frac{x + y}{2} & \text{with prob } 0.5 \end{cases}$$

* **Harp / Piano:** Adjust the decay factor (e.g., $0.998$ for long piano sustain) or change the excitation during `pluck()` from white noise to a square wave or sine wave.
