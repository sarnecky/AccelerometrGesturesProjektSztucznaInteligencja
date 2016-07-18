package com.example.sebastian.myapplication;


public class FilteredAccValues {
	private static final float ALPHA = 0.3f;
	private static final float SMOOTHING = 600;
	private static final float THRESHOLD = 0.1f;
	private static final int DIMENSION_NUMBER = 3;
	
	private static float[] gravity, filtered;

	static {
		gravity = new float[3];
		filtered = new float[3];
	}

	public static float[] get(float[] values){
		for (int i = 0; i < DIMENSION_NUMBER; i++) {
			accFilterGravity(values,i);
			accFilterNoise(values,i);
			accFilterThreshold(i);
		}
		
		return filtered.clone();
	}
	
	private static void accFilterGravity(float[] values,int index) {
		if (gravity[index] == 0.0f) {
			gravity[index] = values[index];
		}

		gravity[index] = gravity[index] * ALPHA + values[index] * (1.0f - ALPHA);
		filtered[index] = values[index] - gravity[index];
	}
	
	
	private static void accFilterNoise(float[] values,int index) {
		filtered[index] += (values[index] - filtered[index])/SMOOTHING;
	}
	
	private static void accFilterThreshold(int index) {
		filtered[index] = Math.abs(filtered[index]) > THRESHOLD ? filtered[index] : 0;
	}
}
