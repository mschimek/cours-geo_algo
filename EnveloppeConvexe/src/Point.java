/** La classe Point. */
public class Point implements Comparable<Point>
{
	public static final double EPSILON = 1E-12;
	
	/** distance entre le point et le point de reference*/
	public double dist;
	/** L'angle entre le point et le point de reference*/
	public double angle;
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
	public int compareTo(Point o) {
		
		if (this.angle > o.angle + EPSILON)
			return 1;
		if (this.angle < o.angle - EPSILON)
			return -1;
		
		// this.angle == o.angle
		
		if (this.dist > o.dist + EPSILON)
			return 1;
		if (this.dist < o.dist - EPSILON)
			return -1;
		
		return 0;
	}
}