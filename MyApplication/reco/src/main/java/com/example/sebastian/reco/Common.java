package com.example.sebastian.reco;

import java.util.ArrayList;
import java.util.List;

import com.example.sebastian.reco.pojo.Point;
import com.example.sebastian.reco.pojo.TemplateCollection;

public class Common {
    public static TemplateCollection TEMPLATES = new TemplateCollection();
    public static List<Point> POINTS = new ArrayList<Point>();
    public static final String CHECK = "check";
    public static final String XXX = "x";
    public static final  String TRIANGLE = "triangle";
    public static final String PIGTAIL = "pigtail";
    public static final String[] TEMPLATES_NAMES = new String[] {CHECK, XXX, TRIANGLE, PIGTAIL};
}