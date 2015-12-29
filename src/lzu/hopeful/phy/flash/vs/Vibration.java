package lzu.hopeful.phy.flash.vs;

public class Vibration
{
	private double A,omega,phi0;
	
	private double direction;
	
	public Vibration(double a,double omega,double phi,double dir)
	{
		init(a,omega,phi,dir);
	}
	
	public Vibration(Vibration src)
	{
		init(src.A,src.omega,src.phi0,src.direction);
	}
	
	public Vibration()
	{
		init(1,1,0,0);
	}

	public void setA(double a)
	{
		A = a;
	}

	public double getA()
	{
		return A;
	}

	public void setOmega(double omega)
	{
		this.omega = omega;
	}

	public double getOmega()
	{
		return omega;
	}
	
	public double getT()
	{
		return Math.PI * 2 / omega;
	}

	public void setPhi0(double phi)
	{
		this.phi0 = phi;
	}

	public double getPhi0()
	{
		return phi0;
	}
	
	public double getPhi(double t)
	{
		return omega * t + phi0;
	}

	public void setDirection(double direction)
	{
		this.direction = direction;
	}

	public double getDirection()
	{
		return direction;
	}
	
	public double getX(double t)
	{
		return A * Math.cos(getPhi(t)) * Math.cos(direction);
	}
	
	public double getY(double t)
	{
		return A * Math.cos(getPhi(t)) * Math.sin(direction);
	}
	
	private void init(double a,double omega,double phi,double dir)
	{
		this.A = a;this.omega = omega;this.phi0 = phi;this.direction = dir;
	}
}
