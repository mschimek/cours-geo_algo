package delauney;

/** La classe Point. */
public class Point implements Comparable<Point>
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
	
	@Override
	public int compareTo(Point arg) {
		if (Math.abs(y - arg.y) <= Util.EPSILON && Math.abs(x - arg.x) <= Util.EPSILON)
			return 0;
		double compX = this.x - arg.x;
		if (compX < -Util.EPSILON) 
			return -1;
		if (compX > Util.EPSILON)
			return 1;
		double compY = this.y - arg.y;
		if (compY < - Util.EPSILON )
			return -1;
		if (compY > Util.EPSILON)
			return 1;
		
		return 0;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		
		Point p = (Point) o;
		double res1 = Math.abs(x - p.x);
		double res2 = Math.abs(y - p.y);
		return   res1 < Util.EPSILON && res2 < Util.EPSILON;
	}
	public String toString() {
		return "(" + x + "/" + y + ")";
	}
	
}