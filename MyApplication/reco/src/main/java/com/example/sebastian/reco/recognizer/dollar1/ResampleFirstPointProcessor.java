package com.example.sebastian.reco.recognizer.dollar1;

import java.util.List;

import com.example.sebastian.reco.pojo.Point;

public class ResampleFirstPointProcessor extends PointProcessorFor$1Algorithm {

	public ResampleFirstPointProcessor(List<Point> points) {
		super(points);
	}

	@Override
	public List<Point> preparePointsFor$1() {
    	points = Point.resample(points, N);
    	processPoints();
    	return points;
	}

}
