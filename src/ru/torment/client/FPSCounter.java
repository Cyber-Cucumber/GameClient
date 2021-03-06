/*
 * Copyright (c) 2008 Golden T Studios.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.torment.client;

/**
 * A utility class to calculate timer frame per seconds (FPS) in convenient way.
 * <p>
 * 
 * How to use :
 * 
 * <pre>
 * FPSCounter counter;
 * // game loop
 * while (true) {
 * 	counter.getCurrentFPS(); // returns current fps
 * 	counter.calculateFPS(); // calculating fps
 * }
 * </pre>
 */
public class FPSCounter
{
	private long lastCount; // last time the fps is counted
	private int currentFPS, // the real fps achieved
	        frameCount;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>FPSCounter</code>.
	 */
	public FPSCounter() {
	}
	
	/**
	 * Refresh the FPS counter, reset the fps to 0 and the timer counter to
	 * start counting from current time.
	 */
	public void refresh() {
		System.out.println("+ Mario::FPSCounter::refresh()");
		this.frameCount = 0;
		this.lastCount = System.currentTimeMillis();
	}
	
	/**
	 * The main method that calculating the frame per second.
	 */
	public void calculateFPS()
	{
		System.out.println("+ Mario::FPSCounter::calculateFPS()");
		this.frameCount++;
		if (System.currentTimeMillis() - this.lastCount > 1000) {
			this.lastCount = System.currentTimeMillis();
			this.currentFPS = this.frameCount;
			this.frameCount = 0;
		}
	}
	
	/**
	 * Returns current FPS.
	 * @return The current FPS.
	 * @see #calculateFPS()
	 */
	public int getCurrentFPS() {
		System.out.println("+ Mario::FPSCounter::getCurrentFPS()");
		return this.currentFPS;
	}
	
}
