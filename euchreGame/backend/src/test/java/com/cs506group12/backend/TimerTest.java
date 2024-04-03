package com.cs506group12.backend;

import com.cs506group12.backend.models.Timer;

public class TimerTest {

    public static boolean testSetTimer(){
        Timer timer = new Timer();
        timer.setTimer(100);
        return (timer.getTimeLeft() == 100);
    }

    public static  boolean testEnd(){
		Timer timer = new Timer();
		timer.setTimer(2);
		Thread thread = new Thread(timer);
		thread.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return (timer.getTimeLeft() == 0);
    }    
    
    public static boolean testWaiting(){
		Timer timer = new Timer();
		timer.setTimer(5);
		Thread thread = new Thread(timer);
		thread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return (timer.getTimeLeft() == 4 && timer.getIfTurn() == true);
    }

    public static boolean testEndTurn(){
        Timer timer = new Timer();
		timer.setTimer(10);
		Thread thread = new Thread(timer);
		thread.start();
		try {
			Thread.sleep(1000);
            timer.setTurnOver();
            Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return (timer.getTimeLeft() != 8000);
    }

    public static boolean allTimerTests(){
        System.out.println("Test SetTimer: " + testSetTimer());
        System.out.println("Test Midway: " + testWaiting());
        System.out.println("Test Timer Ending: " + testEnd());
        System.out.println("Test Ending Turn Early: " + testEndTurn());
		return true;

    }
    public static void main(String args[]){
        System.out.println("Test : " + allTimerTests());
  
    }
}
