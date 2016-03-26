/** La classe segment */
public class Segment 
{
	public Point upperSave;
	/** L'extremite a. */
	public Point upper;
	
	/** L'extremite b. */
	public Point lower;
	
	/** Constructeur avec initialisation de a et b. */
	public Segment(Point a, Point b)
	{
		a = new Point(a.x,a.y);
		b = new Point(b.x,b.y);
		upper = upper(a, b);
		if (upper == a)
			lower = b;
		else
			lower = a;
		
		upperSave = new Point(upper.x, upper.y);
	}
	
	/** Constructeur sans initialisation. */
	public Segment(){}
	
	private Point upper(Point a, Point b) {
		if(a.y < b.y)
			return a;
		if (a.y == b.y) {
			if (a.x < b.x)
				return a;
			else 
				return b;
		}
		return b;		
	}
	@Override
	public String toString() {
		return "Segment: upper=" + this.upper+"/save="+ upperSave  + " lower= " + this.lower +"\n";
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Segment))
			return false;
		
		Segment s = (Segment) o;
			
		if (this.upper.compareTo(s.upper) != 0)
			return false;
		if (this.lower.compareTo(s.lower) != 0)
			return false;
		
		return true;
	}
 }
