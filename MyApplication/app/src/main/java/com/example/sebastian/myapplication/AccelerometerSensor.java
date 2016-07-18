package com.example.sebastian.myapplication;

import java.util.List;

import com.example.sebastian.reco.pojo.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerSensor {
	private SensorManager sensorManager;
	private final List<Point> points;

	public AccelerometerSensor(SensorManager sensorManager, final List<Point> points) {
		this.sensorManager = sensorManager;
		this.points = points;
	}

	public void sensorOn() {
		sensorManager.registerListener(mSensorEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void sensorOff() {
		sensorManager.unregisterListener(mSensorEventListener);
	}

	private SensorEventListener mSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] filtered = FilteredAccValues.get(event.values);
			
			if (points.isEmpty()
					|| !(points.get(points.size() - 1).x == filtered[0] && points
							.get(points.size() - 1).y == filtered[1])){
				points.add(new Point(filtered[0], filtered[1]));
			}
			
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) { /* empty */
		}
	};
}
