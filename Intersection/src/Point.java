/** La classe Point. */
public class Point implements Comparable<Point>
{
	public static final double EPSILON = 1E-12;
	/** Le segment associe à ce point */
	public Segment seg = null;
	
	/** indige si point est le point superieure d'un segment */
	public boolean upperPoint = false;
	/** La valeur de x. */
	public double x;
	
	/** La valeur de y. */
	public double y;
	
	/** Constructeur avec initialisation de x et y. */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/** Constructeur sans initialisation. */
	public Point(){}

	@Override
	public int compareTo(Point arg) {
		double compY = this.y - arg.y;
		if (compY < - EPSILON )
			return -1;
		if (compY > EPSILON)
			return 1;
		double compX = this.x - arg.x;
		if (compX < -EPSILON) 
			return -1;
		if (compX > EPSILON)
			return 1;
		
		return 0;
	}
}