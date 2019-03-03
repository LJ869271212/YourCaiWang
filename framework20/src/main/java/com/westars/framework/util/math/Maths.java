package com.westars.framework.util.math;

public class Maths {

	public static int random(int mini, int max) {
		return (int) (Math.random() * (max - mini + 1));
	}
}
