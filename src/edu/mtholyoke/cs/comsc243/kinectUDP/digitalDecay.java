package edu.mtholyoke.cs.comsc243.kinectUDP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.mtholyoke.cs.comsc243.kinectUDP.UsingProcessing.Particle;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author eitan
 *
 */
public class digitalDecay extends PApplet {

	KinectBodyDataProvider kinectReader;
	public static float PROJECTOR_RATIO = 1080f/1920.0f;

	public void createWindow(boolean useP2D, boolean isFullscreen, float windowsScale) {
		if (useP2D) {
			if(isFullscreen) {
				fullScreen(P2D);  			
			} else {
				size((int)(1920 * windowsScale), (int)(1080 * windowsScale), P2D);
			}
		} else {
			if(isFullscreen) {
				fullScreen();  			
			} else {
				size((int)(1920 * windowsScale), (int)(1080 * windowsScale));
			}
		}		
	}
	
	// use lower numbers to zoom out (show more of the world)
	// zoom of 1 means that the window is 2 meters wide and appox 1 meter tall.
	public void setScale(float zoom) {
		scale(zoom* width/2.0f, zoom * -width/2.0f);
		translate(1f/zoom , -PROJECTOR_RATIO/zoom );		
	}

	public void settings() {
		createWindow(true, true, .5f);
	}

	public void setup(){
		colorMode(HSB, 360);
		background(0);
		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder 
		 */
		
		try {
			kinectReader = new KinectBodyDataProvider("floorTest.kinect", 10);
		} catch (IOException e) {
			System.out.println("Unable to creat e kinect producer");
		}
		
		
//		kinectReader = new KinectBodyDataProvider(8008);
		kinectReader.start();

	}
	
	ArrayList<Particle> allParticles = new ArrayList<Particle>();
	Integer maxLevel=5;
	Boolean useFill=false;
	List<Integer> data;
	PVector diff=new PVector(0,0);
	PVector headP;
	int factor=4000;
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
	
	public void draw(){
		setScale(.002f);
		
//		
//		noStroke();



		
		noStroke();
		fill(0,30);
		rectMode(CENTER);
		rect(0, 0, width, height);

		// leave trails instead of clearing background \ 
		//noStroke();
		//fill(0,0,0, 10);
		//rect(-1,-1, 2, 2); //draw transparent rect of the window

//		KinectBodyData bodyData = kinectReader.getMostRecentData();
		KinectBodyData bodyData = kinectReader.getData();
		Body person = bodyData.getPerson(0);
		if(person != null){
			PVector head = person.getJoint(Body.HEAD);
			if(head!=null){
				PVector coord=addHead(head,1);
				diffCalc(coord);
				System.out.println("diffx:"+diff.x*1000+"diffy:"+diff.y*1000);
				System.out.println("headx:"+coord.x+"heady:"+coord.y);
				
				
				
				
				
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
//					  System.out.println("x1:"+pts[i][0]);
//					  System.out.println("y1:"+pts[i][1]);
//					  System.out.println("headx:"+coord.x+"heady:"+coord.y);
					  
				  }
				  
				  if(allParticles.size()>0){
					  
					  Delaunay d=new Delaunay();
					  
					  try {
						data=d.triangulate(pts);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					  
					  strokeWeight(0.4f);
					  
					  
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
						    	fill((float)(165+p1.life*1.5), 360,360);
						    }
						    else{
						    	noFill();
						    	stroke((float)(165+p1.life*1.5), 360,360);
						    }
							triangle(p1.pos.x+diff.x*factor, p1.pos.y+diff.y*factor, 
							         p2.pos.x+diff.x*factor, p2.pos.y+diff.y*factor, 
							         p3.pos.x+diff.x*factor, p3.pos.y+diff.y*factor);
							
				
					  		}

//					  		fill(255);
//							noStroke();
//							drawIfValid(head.add(diff).mult(500));
				
				
				

				  			}
				
			
		}}
	}
	public PVector addHead(PVector head,int factor){
		PVector neo=new PVector(head.x*factor,head.y*factor);
		allParticles.add(new Particle(neo.x,neo.y, maxLevel));
		return neo;
	}

	/**
	 * Draws an ellipse in the x,y position of the vector (it ignores z).
	 * Will do nothing is vec is null.  This is handy because get joint 
	 * will return null if the joint isn't tracked. 
	 * @param vec
	 */
	public void drawIfValid(PVector vec) {
		if(vec != null) {
			ellipse(vec.x, vec.y, 10f,10f);
		}

	}
	public void diffCalc(PVector head){
		if(headP==null){
			headP=head;
			diff=head;
			System.out.println("print");
		}
		else{
			diff=new PVector(head.x-headP.x,head.y-headP.y);
			headP=head;
		}
	}


	public static void main(String[] args) {
		PApplet.main(digitalDecay.class.getName());
	}

}
