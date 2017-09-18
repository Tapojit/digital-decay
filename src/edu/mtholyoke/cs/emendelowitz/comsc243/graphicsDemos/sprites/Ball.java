package edu.mtholyoke.cs.emendelowitz.comsc243.graphicsDemos.sprites;

import processing.core.PApplet;

public class Ball extends PObject {
	
	float x; 
	float y;
	float d;
	
	float dx;
	float dy;

	public Ball(PApplet applet, float height, float minD, float maxD) {
		super(applet);
		x = (float) (Math.random() * applet.width);
		y = (float) (Math.random() * height);
		d = (float) applet.random(minD, maxD);
	}

	public void update() {
		if(y+d >= applet.height) {
			if(dy >= 0) {
				dy *= -.9f;
			}
		}
		dy += .01; //acceleration due to gravity
		y += dy;
		
	}
	
	public void draw() {
		applet.ellipse(x,y,d,d);
	}

}
