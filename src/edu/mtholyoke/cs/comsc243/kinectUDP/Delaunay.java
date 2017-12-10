package edu.mtholyoke.cs.comsc243.kinectUDP;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
public class Delaunay {

	float EPSILON=(float)(0.1/1048576.0);
	

	
	
	private ArrayList<PVector> supertriangle(ArrayList<PVector> vertices){
		
		double xmin=Double.POSITIVE_INFINITY;
		double ymin=Double.POSITIVE_INFINITY;
		double xmax=Double.NEGATIVE_INFINITY;
		double ymax=Double.NEGATIVE_INFINITY;
		int i;
		double dx;
		double dy;
		double dmax;
		double xmid;
		double ymid;
		
		for(i=vertices.size()-1;i>=0;i--){
			if(vertices.get(i).x<xmin){
				xmin=vertices.get(i).x;
			}
			if(vertices.get(i).x>xmax){
				xmax=vertices.get(i).x;
			}
			if(vertices.get(i).y<ymin){
				ymin=vertices.get(i).y;
			}
			if(vertices.get(i).y>ymax){
				ymax=vertices.get(i).y;
			}
		}
			
			dx=xmax-xmin;
			dy = ymax - ymin;
		    dmax = Math.max(dx, dy);
		    xmid = xmin + dx * 0.5;
		    ymid = ymin + dy * 0.5;
		    PVector a=new PVector((float)(xmid - 20 * dmax), (float)(ymid - dmax));
		    PVector b=new PVector((float)(xmid), (float)(ymid + 20 * dmax));
		    PVector c=new PVector((float)(xmid + 20 * dmax), (float)(ymid -      dmax));
		    
		    ArrayList<PVector> res=new ArrayList<PVector>();
		    res.add(a);
		    res.add(b);
		    res.add(c);
		    
		    return res;
			
		}
		class circumcircle{
			Integer i;
			Integer j;
			Integer k;
			float x;
			float y;
			float r;
			public circumcircle(ArrayList<PVector> vertices, int i, int j, int k){
				
				float x1=vertices.get(i).x;
				float y1=vertices.get(i).y;
				float x2=vertices.get(j).x;
				float y2=vertices.get(j).y;
				float x3=vertices.get(k).x;
				float y3=vertices.get(k).y;
				
				
				float fabsy1y2 = Math.abs(y1 - y2);
		        float fabsy2y3 = Math.abs(y2 - y3);
		        float xc, yc, m1, m2, mx1, mx2, my1, my2, dx, dy;
		        
		        if(fabsy1y2 < EPSILON) {
		            m2  = -((x3 - x2) / (y3 - y2));
		            mx2 = (x2 + x3) / (float)2.0;
		            my2 = (y2 + y3) / (float)2.0;
		            xc  = (x2 + x1) / (float)2.0;
		            yc  = m2 * (xc - mx2) + my2;
		          }

				else if(fabsy2y3 < EPSILON) {
				  m1  = -((x2 - x1) / (y2 - y1));
				  mx1 = (x1 + x2) / (float)2.0;
				  my1 = (y1 + y2) / (float)2.0;
				  xc  = (x3 + x2) / (float)2.0;
				  yc  = m1 * (xc - mx1) + my1;
				}
		        
				else {
					  m1  = -((x2 - x1) / (y2 - y1));
					  m2  = -((x3 - x2) / (y3 - y2));
					  mx1 = (x1 + x2) / (float)2.0;
					  mx2 = (x2 + x3) / (float)2.0;
					  my1 = (y1 + y2) / (float)2.0;
					  my2 = (y2 + y3) / (float)2.0;
					  xc  = (m1 * mx1 - m2 * mx2 + my2 - my1) / (m1 - m2);
					  yc  = (fabsy1y2 > fabsy2y3) ?
					    m1 * (xc - mx1) + my1 :
					    m2 * (xc - mx2) + my2;
					}
			        dx = x2 - xc;
			        dy = y2 - yc;
			        
			        
			        this.i=i;
			        this.j=j;
			        this.k=k;
			        this.x=xc;
			        this.y=yc;
			        this.r=dx * dx + dy * dy;
			}
		}
	


		
	private ArrayList<Integer> dedup(ArrayList<Integer> edges){
		int i,a,b,m,n;
		int j=edges.size();
		while(j>0){
			b=edges.get(--j);
			a=edges.get(--j);
			i=j;
			while(i>0){
				n=edges.get(--i);
				m=edges.get(--i);
				
				if((a==m && b==n)||(a==n && b==m)){
					int[] remove1={edges.get(j), edges.get(j+1)};
					edges.removeAll(Arrays.asList(remove1));
					int[] remove2={edges.get(i), edges.get(i+1)};
					edges.removeAll(Arrays.asList(remove2));
					break;
				}
			}
		}
		return edges;
	}
	public ArrayList<Integer> triangulate(ArrayList<PVector> vertices){
		int n=vertices.size();
		
		if(n<3){
			return new ArrayList<Integer>();
		}
		
		
//		ArrayList<Integer> indices=new ArrayList<Integer>(n);
		Integer[] indices2=new Integer[n];
		for(int i=n-1;i>-1;i--){
			indices2[i]=i;
		}
		ArrayList<Integer> indices=new ArrayList<Integer>(Arrays.asList(indices2));
		
		Collections.sort(indices, new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2){
				return Float.compare(vertices.get(o1).x, vertices.get(o2).x);
			}


		});
		
		ArrayList<PVector> st=supertriangle(vertices);
		
		vertices.add(st.get(0));
		vertices.add(st.get(1));
		vertices.add(st.get(2));
		
		circumcircle circCircle=new circumcircle(vertices, n+0, n+1, n+2);
		
		if(circCircle.i==null && circCircle.j==null && circCircle.k==null && circCircle.x==0.0f && circCircle.y==0.0f && circCircle.r==0.0f){
			return new ArrayList<Integer>();
		}
		ArrayList<circumcircle> open=new ArrayList<circumcircle>();
		ArrayList<circumcircle> closed=new ArrayList<circumcircle>();
		ArrayList<Integer> edges=new ArrayList<Integer>();
		
		open.add(new circumcircle(vertices, n+0, n+1, n+2));
		
		for(int i=indices.size()-1;i>-1;i--){
			edges.clear();
			int c=indices.get(i);
			for(int j=open.size()-1;j>-1;j--){
				float dx=vertices.get(c).x-(float)open.get(j).x;
				if(dx>0.0 && dx>(float)open.get(j).r){
					closed.add(open.get(j));
					open.remove(j);
					continue;
				}
				
				float dy=vertices.get(c).x-(float)open.get(j).x;
				if(dx * dx + dy * dy - (float)open.get(j).r > EPSILON){
					continue;
				}
				edges.add(open.get(j).i);
				edges.add(open.get(j).j);
				edges.add(open.get(j).j);
				edges.add(open.get(j).k);
				edges.add(open.get(j).k);
				edges.add(open.get(j).i);
				
				open.remove(j);
				
				
			}
			edges=dedup(edges);
			int j=edges.size();
			while(j>0){
				int b=edges.get(--j);
				int a=edges.get(--j);
				open.add(new circumcircle(vertices, a, b, c));
			}
		}
		
		
		for(int i=open.size()-1;i>-1;i--){
			closed.add(open.get(i));
		}
		open.clear();
		ArrayList<Integer> res=new ArrayList<Integer>();
		int count=0;
		for(int i=closed.size()-1;i>-1;i--){
			count+=1;
			if(count>700){
				break;
			}
			if(closed.get(i).i < n && closed.get(i).j < n && (Integer)closed.get(i).k < n){
				res.add(closed.get(i).i);
				res.add(closed.get(i).j);
				res.add(closed.get(i).k);
			}
		          
			
		}


		
		
		return res;
		
	}
	public Delaunay(){
		
	}
	
}
