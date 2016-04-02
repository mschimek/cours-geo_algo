package delauney;

/** La classe Point. */
public class Point
{
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
	
	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		
		Point p = (Point) o;
		return Math.abs(x - p.x) < Util.EPSILON && Math.abs(x -p.y) < Util.EPSILON;
	}
	public String toString() {
		return "(" + x + "/" + y + ")";
	}
	
}