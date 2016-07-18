package com.example.sebastian.reco.recognizer;

import java.util.List;

import com.example.sebastian.reco.Common;
import com.example.sebastian.reco.pojo.GestureResult;
import com.example.sebastian.reco.pojo.GestureTemplate;
import com.example.sebastian.reco.pojo.GestureUtil;
import com.example.sebastian.reco.pojo.Point;
import com.example.sebastian.reco.pojo.TemplateCollection;
import com.example.sebastian.reco.recognizer.dollar1.ResampleFirstPointProcessor;
import com.example.sebastian.reco.recognizer.dollar1.ResampleLastPointProcessor;

public class GestureRecognizer {

	private static final double GESTURE_MATCHING_LIMIT = 0.60;
	private static final double SPECIAL_ARC_LIMIT = 0.65;
	//Amount of points the data should be resampled to
	public static int N = 64;
	//Fitting square size
	public static double SQUARE_SIZE = 250.0;
    //Angel range
    private static double THETA = 45.0;
    //Angel precision
    private static double D_THETA = 2.0;
    
	private List<Point> points;
    
	public GestureRecognizer(List<Point> points) {
		this.points = points;
	}
	
    public GestureResult[] recognize(TemplateCollection templates){
    	List<Point> ptsResampledFirst = new ResampleFirstPointProcessor(points).preparePointsFor$1();
    	List<Point> ptsResampledLast = new ResampleLastPointProcessor(points).preparePointsFor$1();
    	GestureResult resampledFirst = recognize(ptsResampledFirst, templates.resampledFirstTemplates);
    	GestureResult resampledLast = recognize(ptsResampledLast, templates.resampledLastTemplates);
    	return new GestureResult[]{resampledFirst, resampledLast};
    }
    
    private GestureResult recognize(List<Point> points, List<GestureTemplate> templateList){
    	GestureResult resultingGesture = new GestureResult("NO TEMPLATES - EMPTY FILE", 0.0f);
    	double bestMatch = Double.MAX_VALUE;
    	int indexOfBestMatch = -1;
    	    	
    	//Find a match
    	for(int i=0; i < templateList.size(); i++){
    		double currentMatchedValue = 0.0;
    		currentMatchedValue = GestureUtil.distanceAtBestAngle(points, templateList.get(i), -THETA, THETA, D_THETA);
    		
    		if(currentMatchedValue < bestMatch){
    			bestMatch = currentMatchedValue;
    			indexOfBestMatch = i;
    		}
    	}
    	
        float score = calculateMatchingScore(bestMatch);
        double limit = GESTURE_MATCHING_LIMIT;

        if (indexOfBestMatch > -1) {
            String bestMatchName = templateList.get(indexOfBestMatch).name;
            if (Common.XXX.equals(bestMatchName) || Common.CHECK.equals(bestMatchName)) {
                limit = SPECIAL_ARC_LIMIT;
            }
            if (score > limit) {
                resultingGesture = new GestureResult(bestMatchName, score);
            } else {
                resultingGesture = new GestureResult("NO MATCH", 0.0f);
            }
        }
    	
    	return resultingGesture;
    }

	private float calculateMatchingScore(double bestMatch) {
		double twoTimesSquareSizeMultSqareSize = SQUARE_SIZE * SQUARE_SIZE + SQUARE_SIZE * SQUARE_SIZE;
		double lengthOfSquareSizeLine = 0.5 * Math.sqrt( twoTimesSquareSizeMultSqareSize );
		float diffenceValue = (float) (bestMatch/lengthOfSquareSizeLine);
		return 1.0f - diffenceValue;
	}
	
}
