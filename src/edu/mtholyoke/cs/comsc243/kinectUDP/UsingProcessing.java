package edu.mtholyoke.cs.comsc243.kinectUDP;

import java.util.*;
import java.util.ArrayList;


import processing.core.PApplet;
import processing.core.PVector;


public class UsingProcessing extends PApplet{

	public static void main(String[] args) {
		PApplet.main(UsingProcessing.class.getName());
	}

	ArrayList<Particle> allParticles = new ArrayList<Particle>();
	Integer maxLevel=5;
	Boolean useFill=false;
	List<Integer> data;
	
	
	class Particle{
		int level;
		int life;
		PVector pos;
		PVector vel;
		Particle(float x, float y, int level){
			this.level=level;
			this.life=0;
			this.pos=new PVector(x,y);
			
			this.vel=PVector.random2D();
			
			this.vel.mult(map(this.level, 0, maxLevel, 5, 2));
			this.move();

			
			
			
		}
		public void move() {
			// TODO Auto-generated method stub
			this.life++;
			this.vel.mult((float)0.9);
			
			this.pos.add(this.vel);
			if(this.life%10==0){
				if(this.level>0){
					this.level-=1;
					Particle newParticle=new Particle(this.pos.x, this.pos.y, this.level-1);
					allParticles.add(newParticle);
				}
			}
		}

	}
	

	
	public void settings(){
		size(640,640);

	}
	public void setup(){
		
		colorMode(HSB, 360);
		background(0);
	}
	
	public void draw(){
		  noStroke();
		  fill(0, 30);
		  rect(0, 0, width, height);
		  
		  for(int i=allParticles.size()-1; i>-1;i--){
			  allParticles.get(i).move();
			  
			  if(allParticles.get(i).vel.mag()<0.01){
				  allParticles.remove(i);
			  }
		  }
		  double[][] pts=new double[allParticles.size()][2];
		  for(int i=0;i<allParticles.size();i++){
			  pts[i][0]=Double.parseDouble(new Float(allParticles.get(i).pos.x).toString());
			  pts[i][1]=Double.parseDouble(new Float(allParticles.get(i).pos.y).toString());
		  }
		  
		  
		  
		  if(allParticles.size()>0){
			  
			  Delaunay d=new Delaunay();
			  
			  try {
				data=d.triangulate(pts);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			  
			  strokeWeight(0.1f);
			  
			  
			  for(int i=0;i<data.size();i+=3){
				  Particle p1=allParticles.get(data.get(i));
				  Particle p2=allParticles.get(data.get(i+1));
				  Particle p3=allParticles.get(data.get(i+2));
				  
				  float distThresh=13;
				  float distance=dist(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y);
				  if (dist(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y) > distThresh) {
					  
				      continue;
				    }
				      
				    if (dist(p2.pos.x, p2.pos.y, p3.pos.x, p3.pos.y) > distThresh) {
				      continue;
				    }
				      
				    if (dist(p1.pos.x, p1.pos.y, p3.pos.x, p3.pos.y) > distThresh) {
				      continue;
				    }
				    if(useFill){
				    	noStroke();
				    	fill((float)(165+p1.life*2), 360,360);
				    }
				    else{
				    	noFill();
				    	stroke((float)(165+p1.life*2), 360,360);
				    }
					triangle(p1.pos.x, p1.pos.y, 
					         p2.pos.x, p2.pos.y, 
					         p3.pos.x, p3.pos.y);
					System.out.println(p1.life);
				    
				      
			  }
		  }
		  
	}
	public void mouseDragged(){
		allParticles.add(new Particle(mouseX, mouseY, maxLevel));
		

	}
	public void keyPressed(){
		useFill=!useFill;
	}
	

	
	
	

}









