package com.mawen.learn.basic.sockets.chatper4;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/28
 */
public class ThreadExample implements Runnable {

	private String greeting;

	public ThreadExample(String greeting) {
		this.greeting = greeting;
	}

	@Override
	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName() + ": " + greeting);

			try {
				// Sleep 0 to 100 milliseconds
				TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
			}
			catch (InterruptedException e) {
				// Should not happen
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new ThreadExample("Hello")).start();
		new Thread(new ThreadExample("Aloha")).start();
		new Thread(new ThreadExample("Ciao")).start();
	}
}
