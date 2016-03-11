/** La classe segment */
public class Segment 
{
	/** est diagonale */
	public boolean isDiagonale = false;
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
}
