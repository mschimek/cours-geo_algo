package delauney;

/** La classe segment */
public class Segment 
{
	/** L'extremite a. */
	public Point a;
	
	/** L'extremite b. */
	public Point b;
	
	public double distance;
	/** Constructeur avec initialisation de a et b. */
	public Segment(Point a, Point b)
	{
		this.a = a;
		this.b = b;
		distance = Util.eucledianDistance(a, b);
	}
	
	/** Constructeur sans initialisation. */
	public Segment(){}
	
	public boolean equals(Object o) {
		if (o instanceof Segment) {
			Segment s = (Segment) o;
			if ((s.a.equals(a) || s.a.equals(b)) && (s.b.equals(a) || s.b.equals(b)))
					return true;
		}
		return false;
	}
	public String toString() {
		return "a: " + a + " b: " + b + "\n";
	}
	public boolean contains(Point p) {
		if (a.equals(p))
			return true;
		if (b.equals(p))
			return true;
		
		double min_x = a.x < b.x ? a.x : b.x;
		double min_y = a.y < b.y ? a.y : b.y;
		double max_x = a.x > b.x ? a.x : b.x;
		double max_y = a.y > b.y ? a.y : b.y;
		
		if (p.x < min_x - Util.EPSILON || p.x >  max_x + Util.EPSILON)
			return false;
		if (p.y < min_y - Util.EPSILON || p.y > max_y + Util.EPSILON)
			return false;
		
		if (Math.abs(b.x - a.x) < Util.EPSILON)
			return Math.abs(p.x - a.x) < Util.EPSILON;
		
		double m =(b.y - a.y)/(b.x - a.x);
		double c = a.y - m*a.x;
		
		return   Math.abs((m*p.x + c) -p.y) < Util.EPSILON;
		/*
		//vector form of line equation:
		double quotientX = 0.0;
		double quotientY = 0.0;
		double tmpX = p.x - a.x;
		double tmpY = p.y - a.y;
		double directionX = b.x - a.x;
		double directionY = b.y - a.y;
		
		if (Math.abs(directionX) < Util.EPSILON && Math.abs(directionY) < Util.EPSILON)
			return Math.abs(tmpX) < Util.EPSILON && Math.abs(tmpY) < Util.EPSILON;
		
		if (Math.abs(directionX) < Util.EPSILON) {
			return Math.abs(tmpX) < Util.EPSILON;
		}
		if (Math.abs(directionY) < Util.EPSILON) {
			return Math.abs(tmpY) < Util.EPSILON;
		}
		
		quotientX = tmpX / directionX;
		quotientY = tmpY / directionY;
		
		return Math.abs(quotientX - quotientY) < Util.EPSILON;
		*/
	}
	public static void main(String[] args) {
		Segment s1 = new Segment(new Point(1E9,0), new Point(0,1E9));
		Segment s2 = new Segment(new Point(1E9,0), new Point(0,1E9));
		System.out.println(s1.equals(s2));
	}
}
