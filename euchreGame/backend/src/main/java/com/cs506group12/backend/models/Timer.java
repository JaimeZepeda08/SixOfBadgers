package com.cs506group12.backend.models;

public class Timer implements Runnable {

	private int timeLeft = 0;
	private boolean isTurn = true;

	public void setTimer(int seconds) {
		timeLeft = seconds;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTurnOver() {
		isTurn = false;
	}

	public boolean getIfTurn() {
		return isTurn;
	}

	/*
	 * Runs for the duration in seconds or if turn is ended
	 * sets isTurn to false when time limit is up
	 */
	public void run() {
		// long startTime = System.currentTimeMillis(); // more accurate if needed
		// long elapsedTime = System.currentTimeMillis() - startTime;

		while (timeLeft >= 0 && isTurn) {
			timeLeft--;

			try {
				// System.out.println(timeLeft);
				Thread.sleep(1000); // thread waits 1000 milliseconds = 1 second change to currentTime if needed

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		isTurn = false; // time out turn over

	}

	// multithread for main - needed for timer to work
	/*
	 * Timer timer = new Timer();
	 * timer.setTimer(10);
	 * Thread thread = new Thread(timer);
	 * thread.start();
	 */

}
