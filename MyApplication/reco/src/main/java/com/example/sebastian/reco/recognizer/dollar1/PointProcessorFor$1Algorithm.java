package com.example.sebastian.reco.recognizer.dollar1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.sebastian.reco.pojo.Point;

public abstract class PointProcessorFor$1Algorithm {

	//Fitting square size
	protected static double SQUARE_SIZE = 250.0;
	//Amount of points the data should be resampled to
	protected static int N = 256;
    
	protected List<Point> points;

	public PointProcessorFor$1Algorithm(List<Point> points) {
		this.points = cloneArray(points);
	}
	
    List<Point> cloneArray(List<Point> points){
		List<Point> clonedPointList = new ArrayList<Point>(Arrays.asList(new Point[points.size()]));
		Collections.copy(clonedPointList, points);
		return clonedPointList;
	}

	protected void processPoints() {
		//Scale to square
		points = Point.scale(points, SQUARE_SIZE);
	}
	
	public abstract List<Point> preparePointsFor$1();
	
}
