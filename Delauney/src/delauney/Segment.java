package delauney;

/** La classe segment */
public class Segment 
{
	/** L'extremite a. */
	public Point a;
	
	/** L'extremite b. */
	public Point b;
	
	/** Constructeur avec initialisation de a et b. */
	public Segment(Point a, Point b)
	{
		this.a = a;
		this.b = b;
	}
	
	/** Constructeur sans initialisation. */
	public Segment(){}
	
	public boolean contains(Point p) {
		double m =(b.y - a.y)/(b.x - a.x);
		double c = a.y - m*a.x;
		return Math.abs((m*p.x + c) -p.y) < Util.EPSILON;
	}
}
