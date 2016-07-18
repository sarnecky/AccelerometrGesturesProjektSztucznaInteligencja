package com.example.sebastian.myapplication;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.sebastian.reco.pojo.GestureTemplate;
import com.example.sebastian.reco.pojo.Point;
import com.example.sebastian.reco.pojo.TemplateCollection;
import android.content.Context;

public class GestureTemplateCreator {
	
	public static TemplateCollection addTemplate(List<Point> points, String templateName, Context ctx) {
		// Create the templates in both files based on the raw input points
		GestureTemplateCreator.addTemplateToFile(new GestureTemplate(templateName, points, true),
				"ResampledFirstTemplates.txt", ctx);
		GestureTemplateCreator.addTemplateToFile(new GestureTemplate(templateName, points, false),
				"ResampledLastTemplates.txt", ctx);

		return readTemplates(ctx);
	}

	public static void addTemplateToFile(GestureTemplate template, String filename, Context ctx) {
		System.out.println("Adding template with "+template.points.size()+" points.");
		try {
			FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_APPEND);
		    PrintWriter out = new PrintWriter(fos);
		    
		    out.println();
		    out.println("#"+template.name);
		    
		    for(Point p:template.points){
		    	out.println(p.x+" "+p.y);
		    }
		    
		    out.close();
		} catch (IOException e) {
			System.out.println("Could not write to file.");
		}
	}
	
	public static TemplateCollection readTemplates(Context ctx){
		TemplateCollection templates = new TemplateCollection();
		GestureTemplateCreator.readTemplates("ResampledFirstTemplates.txt", templates.resampledFirstTemplates, ctx);
		GestureTemplateCreator.readTemplates("ResampledLastTemplates.txt", templates.resampledLastTemplates, ctx);		
		return templates;
	}

	static void readTemplates(String fileName, List<GestureTemplate> templates, Context ctx){
		try {			
			Scanner sc = new Scanner(ctx.openFileInput(fileName));
	
			String curTemplate = "";
			ArrayList<Point> curPoints = null;
	
			while(sc.hasNext()){
				String cur = sc.next();
	
				if(cur.charAt(0) == '#'){
					if(!curTemplate.equals("") && curPoints != null){
						templates.add(new GestureTemplate(curTemplate, curPoints));
					}
	
					curTemplate = cur.substring(1);
					curPoints = new ArrayList<Point>();
				} else {
					if(curPoints != null){
						curPoints.add(new Point(Double.parseDouble(cur), Double.parseDouble(sc.next())));
					}
				}
			}
			
			sc.close();
			
			if(!curTemplate.equals("") && curPoints != null){
				//Add the last template
				templates.add(new GestureTemplate(curTemplate, curPoints));
			}
		} catch (FileNotFoundException e) {
			//GestureRecognizerMain.LOG.warn("Can't find file.");
		}
	
		//GestureRecognizerMain.LOG.debug(templates.size()+" resampled templates loaded.");
	}
}
