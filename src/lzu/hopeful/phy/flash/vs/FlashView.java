package lzu.hopeful.phy.flash.vs;
import java.util.*;
import android.graphics.*;

public class FlashView
{
	double t;
	
	double T;
	
	List<Vibration> vibrations;
	
	List<Float> points;
	
	Paint point_paint;
	Paint line_paint;
	Paint info_paint;
	
	public FlashView()
	{
		vibrations = new ArrayList<Vibration>(2);
		
		point_paint = new Paint();
		point_paint.setAntiAlias(true);
		point_paint.setColor(Color.CYAN);
		point_paint.setStrokeWidth(10);
		point_paint.setStrokeCap(Paint.Cap.ROUND);
		line_paint = new Paint();
		line_paint.setAntiAlias(true);
		line_paint.setStrokeWidth(2);
		line_paint.setStyle(Paint.Style.STROKE);
		info_paint = new Paint();
		info_paint.setColor(Color.GREEN);
		info_paint.setTextSize(22);
		info_paint.setAntiAlias(true);
		info_paint.setTextAlign(Paint.Align.LEFT);
		
		//vibrations.add(new Vibration(100,0.6,1,0));
		//vibrations.add(new Vibration(200,2,0,Math.PI/2));
		points = new ArrayList<Float>();
		
		T=getT();
	}
	
	public double getT()
	{
		if(vibrations.size()<=0) return 0;
		double temp=1;//=vibrations.get(1).getT();
		for(Vibration vibration : vibrations)
		{
			temp*=vibration.getT()==0?1:vibration.getT();
			//temp = getLCM(temp,vibration.getT());
		}
		return temp;
	}
	
	public double getLCM(double a,double b)
	{
		if(a==0||b==0) return a+b;
		double c=a*b,r=0;
		if(a<b)
		{
			r=a;a=b;b=r;
		}
		while(true)
		{
			r = a % b;
			if(r == 0)
				return c / b;
		}
	}
	
	public void addVibration(Vibration v)
	{
		vibrations.add(v);
		restart();
	}
	
	public void removeVibration(Vibration v)
	{
		vibrations.remove(v);
		restart();
	}
	
	public void restart()
	{
		t = 0;
		points = new ArrayList<Float>();
		T=getT();
	}
	
	public void run(double delta)
	{
		t += delta / 1000;
	}
	
	public void draw(Canvas c)
	{
		int w=c.getWidth(),h=c.getHeight();
		c.save();
		c.translate(w/2f,h/2f);
		//--axis-
		line_paint.setColor(Color.RED);
		c.drawLine(-w/2f,0,w/2f,0,line_paint);
		c.drawLine(0,-h/2f,0,h/2f,line_paint);
		c.drawPoint(0,0,point_paint);
		//------
		
		float tx=0,ty=0;
		//--per vibration--
		for(Vibration vibration : vibrations)
		{
			float x=(float)vibration.getX(t),y=(float)vibration.getY(t);
			tx += x;ty += y;
			c.drawPoint(x,y,point_paint);
		}
		//----------------
		//--path--
		line_paint.setColor(Color.MAGENTA);
		if(t<T)
		{
			points.add(tx);
			points.add(ty);
		}
		Path path = new Path();
		if(points.size()>0)
			path.moveTo(points.get(0),points.get(1));
		for(int i=2;i<points.size();i+=2)
			path.lineTo(points.get(i),points.get(i+1));
		c.drawPath(path,line_paint);
		//-------
		
		c.drawPoint(tx,ty,point_paint);
		
		//--paint info--
		c.save();
		c.translate(-w/2f,-h/2f);
		for(Vibration vibration : vibrations)
		{
			c.translate(0,22);
			if(vibration.getDirection() % Math.PI == 0)
				c.drawText("振动"+vibrations.indexOf(vibration)+":x="+vibration.getA()+"cos("
						   +vibration.getOmega()+"t+"+vibration.getPhi0()+")",0,0,info_paint);
			else if(vibration.getDirection() % Math.PI == Math.PI / 2)
				c.drawText("振动"+vibrations.indexOf(vibration)+":y="+vibration.getA()+"cos("
						   +vibration.getOmega()+"t+"+vibration.getPhi0()+")",0,0,info_paint);
			else
			{
				c.drawText("振动"+vibrations.indexOf(vibration)+":x="+vibration.getA()+"cos("
						   +vibration.getOmega()+"t+"+vibration.getPhi0()+")*cos("
						   +vibration.getDirection()+")",0,0,info_paint);
				c.translate(0,22);
				c.drawText("振动"+vibrations.indexOf(vibration)+":y="+vibration.getA()+"cos("
						   +vibration.getOmega()+"t+"+vibration.getPhi0()+")*sin("
						   +vibration.getDirection()+")",0,0,info_paint);
				
			}
		}
		c.restore();
		//-------
		c.restore();
	}
}
