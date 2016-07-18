package com.example.sebastian.reco;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import javax.swing.SwingUtilities;

import com.example.sebastian.reco.pojo.GestureResult;
import com.example.sebastian.reco.pojo.Point;
import com.example.sebastian.reco.pojo.TemplateCollection;
import com.example.sebastian.reco.recognizer.GestureRecognizer;

public class GestureRecognizerMain extends Activity{

    // Holds the current gesture points
    public static List<Point> POINTS = new ArrayList<Point>();
    public static List<Point> PRV_POINTS;

    public static TemplateCollection TEMPLATES = new TemplateCollection();

    public GestureRecognizerMain() {
    }

    public void main(String[] args) {
        runOnUiThread(new Runnable() {
            public void run() {
                GestureRecognizerMain mainView = new GestureRecognizerMain();

            }
        });
    }

    private void copyToPreviousPoints() {
        GestureRecognizerMain.PRV_POINTS = new ArrayList<Point>(
                Arrays.asList(new Point[GestureRecognizerMain.POINTS.size()]));
        Collections.copy(GestureRecognizerMain.PRV_POINTS,
                GestureRecognizerMain.POINTS);
        System.out.println("Prv points: "
                + GestureRecognizerMain.PRV_POINTS.size());
    }

    private void recognizeGesture() {
        GestureRecognizer recognizer = new GestureRecognizer(PRV_POINTS);
        GestureResult[] result = recognizer.recognize(TEMPLATES);
    }
}