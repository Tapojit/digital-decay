package edu.mtholyoke.cs.emendelowitz.comsc243.graphicsDemos.movement;

import processing.core.PApplet;
import processing.opengl.PGraphics2D;
public class PhysicsDemo1 extends PApplet {
	float x=100;
	float y=0;
	
	//velocity
	float dx=.05f;
	float dy=0f;
	
	//force
	float fx = 0;
	float fy = 0.01f;
	
	
	

	public void settings(){
		size(300,300, P2D);
		size(300,300, FX2D);
	}

	public void setup(){
	}

	public void draw(){
		background(200);
		ellipse(x,y, 10.0f,10.0f);
		x+=dx;
		y+=dy;
		
		dx+=fx;
		dy+=fy;
		
		if(y + 10 > height) {
			if(dy > 0) {
				dy = -dy * 0.9f;
			}
		}

		
	}

	public static void main(String[] args) {
		PApplet.main(PhysicsDemo1.class.getName());
	}

}
