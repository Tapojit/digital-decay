package edu.mtholyoke.cs.emendelowitz.comsc243.graphicsDemos.sprites;

import java.util.ArrayList;

import processing.core.PApplet;

public class PObjectDemo extends PApplet {
	ArrayList<PObject> pobjects = new ArrayList<PObject>();

	public void settings(){
		size(300,300, P2D);
	}

	public void setup(){
		for(int i = 0; i< 50; i++) {
			pobjects.add(new Ball(this, 50, 5, 15));
		}
	}


	public void update() {
	}
	public void draw(){
		
		
		background(200);

		for(PObject obj : pobjects) {
			obj.update();
			obj.draw();
		}


	}

	public static void main(String[] args) {
		PApplet.main(PObjectDemo.class.getName());
	}

}
