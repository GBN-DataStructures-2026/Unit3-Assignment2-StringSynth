# Unit 3 - Assignment 2: StringSynth (Karplus-Strong Audio Synthesis)

## Overview
In this project, you will build an interactive 37-note musical synthesizer called **StringSynth**. 

At the heart of sound synthesis is a **Queue Abstract Data Type (ADT)**. You will build a fixed-capacity circular queue called `RingBuffer` to model the energy traveling back and forth along a vibrating guitar string using the **Karplus-Strong algorithm**.

---

## How Queues Model Physics: The Karplus-Strong Algorithm

When a guitar string is plucked, energy travels along the string in acoustic waves. We model a string vibrating at frequency $f$ using a circular queue of capacity $N$:

$$N = \left\lceil \frac{\text{Sample Rate}}{\text{frequency}} \right\rceil = \left\lceil \frac{44100}{f} \right\rceil$$

### 1. Plucking the String (`pluck()`)
Plucking excites the string with white noise. We model this by filling all $N$ slots of our queue with random floating-point values between $-0.5$ and $+0.5$.

### 2. Simulating Wave Decay (`tic()`)
As the sound wave bounces back and forth, energy decays and higher harmonics smooth out. Every time step (`tic()`), the simulation performs three queue operations:

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

## Your Tasks

### Task 1: `RingBuffer.java`
Implement a circular array queue of doubles.
* `RingBuffer(int capacity)` – Constructs an empty queue of fixed capacity.
* `void add(double x)` – Enqueues $x$ at `rear`. Throws an exception if full.
* `double remove()` – Dequeues sample from `front`. Throws `QueueUnderflowException` if empty.
* `double peek()` – Returns sample at `front` without removing.
* Maintain `front` and `rear` pointers using modulo capacity math: `(index + 1) % capacity`.
* Now compile and run the main method in `RingBuffer` to make sure it works.  Your output should say "Size after wrap-around is 100.  Final peek value: 5050.0"

### Task 2: `GuitarString.java`
Models a single guitar string using `RingBuffer`.
* `GuitarString(double frequency)` – Calculates $N = \lceil 44100 / f \rceil$ and initializes `RingBuffer` with $N$ zeroes.  Note that this is using the ceiling function, which you can find in Java's Math class.
* `pluck()` – Replaces all elements in `RingBuffer` with random values in $[-0.5, +0.5]$.
* `tic()` – Executes one step of Karplus-Strong averaging and updates `ticCount`.
* `sample()` - Return the value of peek.
* Now compile and run the main method in `GuitarString`.  It should print out cells 0-24 of the buffer with cell 24 showing 0.2223
* Next, compile and run `StringSynthLite`.  You should be able to play the notes A and C by clicking in the popup window and typing 'a' and 'c'.

### Task 3: `StringSynth.java`
Build the full 37-note keyboard synthesizer.
* Create an array of 37 `GuitarString` objects corresponding to keys in:
  `q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' `
* In the main simulation loop:
  1. Detect key presses and `pluck()` matching strings.
  2. Compute superposition (sum of `sample()` across all 37 strings).
  3. Send total sample to `StdAudio.play(sample)`.
  4. Call `tic()` on all 37 strings.
* Now you can compile and run and play a song!  Look at `keyboard.png` to see what keys are what notes.

---

## Troubleshooting Audio Issues

If you hear silence, stuttering, or get an audio error console message:

1. **AirPods / Bluetooth Headsets:** Disconnect Bluetooth headphones. Bluetooth headset profiles lower sample rates and can lock Java's audio driver. Switch to built-in laptop speakers.
2. **Exclusive App Locks:** Close apps using active audio/mic lines (Zoom, Teams, Discord, Spotify).
3. **WSL / Remote Containers:** WSL2 does not route GUI/audio out to Windows by default. Run the assignment in native Windows/macOS VS Code.

---

## Optional Creative Challenge: Alternative Instruments

The Karplus-Strong algorithm can be modified to synthesize completely different acoustic instruments! Try creating a second instrument class (e.g., `DrumString.java` or `PianoString.java`):

* **Drum / Percussion:** Flip the sign of the new sample with probability $0.5$ during `tic()`:
  $$\text{newSample} = \begin{cases} +0.994 \times \frac{x + y}{2} & \text{prob } 0.5 \\ -0.994 \times \frac{x + y}{2} & \text{prob } 0.5 \end{cases}$$
* **Harp / Piano:** Adjust the decay factor (e.g., $0.998$ for long piano sustain) or change the excitation during `pluck()` from white noise to a square wave or sine wave.
