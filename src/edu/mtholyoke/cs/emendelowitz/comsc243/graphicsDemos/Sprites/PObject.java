package edu.mtholyoke.cs.emendelowitz.comsc243.graphicsDemos.Sprites;

import processing.core.PApplet;

public abstract class PObject {
	protected PApplet applet;	
	
	
	public PObject(PApplet applet) {
		this.applet  = applet;

	}
	
	public abstract void draw();
	public abstract void update();

}
