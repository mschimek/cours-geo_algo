package delauney;

public class Triangle {
	public Point a;
	public Point b;
	public Point c;
	public Triangle ab;
	public Triangle bc;
	public Triangle ac;
	public Circle circumscribedCircle;
	public Node container = null;
	public Triangle(Point a, Point b, Point c) {
		insertSortedPoints(a,b,c);
		this.circumscribedCircle = circumscribedCircle();
	}
	public Triangle(Point a, Point b, Point c, Node container) {
		this(a, b, c);
		this.container = container;	
	}
	public Triangle(Point a, Point b, Point c, Node container, Triangle t1, Triangle t2, Triangle t3) {
		this(a,b,c,container);
		updateNeighbour(t1);
		updateNeighbour(t2);
		updateNeighbour(t3);
	}
	private void insertSortedPoints(Point a, Point b, Point c) {
		Point[] arr = {a,b,c};
		java.util.Arrays.sort(arr);
		this.a = arr[0];
		this.b = arr[1];
		this.c = arr[2];
		
			
	}
	public void updateNeighbour(Triangle t) {
		if (t.contains(a) && t.contains(b))
				ab = t;
		else if (t.contains(a) && t.contains(c))
				ac = t;
		else if (t.contains(b) && t.contains(c)) 
				bc = t;
		else {
			throw new IllegalArgumentException("Triangle t + " + t  + " is not a neighbour of the triangle: " + this);
		}
		
	}
	public Circle circumscribedCircle(){
		double var = Util.adaptedCrossProduct(Util.vector(b, a), Util.vector(c,b));
		var = var * var;
		double divisor = 2 * var;
		double alpha = Util.squaredEucledianDistance(b, c) * Util.scalarProduct(Util.vector(b,a),Util.vector(c, a)) / divisor;
	    double beta = Util.squaredEucledianDistance(a, c) * Util.scalarProduct(Util.vector(a,b),Util.vector(c, b)) / divisor;
	    double gamma = Util.squaredEucledianDistance(a, b) * Util.scalarProduct(Util.vector(a,c),Util.vector(b, c)) / divisor;
	    
	    Point a_ = Util.factor(alpha, a);
	    Point b_ = Util.factor(beta, b);
	    Point c_ = Util.factor(gamma, c);
	    
	    Point center = Util.plus(Util.plus(a_,b_), c_);
	    double radius = Util.eucledianDistance(center, a);
	    return new Circle(center,radius);
	}
	public Segment rightSegment(Point p1, Point p2) {
		if (p1 == null || p2 == null)
			return null;
		else if ((p1.equals(a) && p2.equals(b)) || (p1.equals(b)&& p2.equals(a))) 
			return new Segment(a, b);
		else if ((p1.equals(a) && p2.equals(c)) || (p1.equals(c)) && p2.equals(a))
			return new Segment(a, c);
		else if ((p1.equals(b) && p2.equals(c)) || (p1.equals(c)) && p2.equals(b))
			return new Segment(b,c);
		else 
			throw new IllegalArgumentException("Points p1 and p2 are not points of the triangle");
	}
	public Segment pointOnSegment(Point p) {
		Segment ab = new Segment(a,b);
		Segment ac = new Segment(a,c);
		Segment bc = new Segment(b,c);
		if (ab.contains(p))
			return ab;
		if (ac.contains(p))
			return ac;
		if (bc.contains(p))
			return bc;
		return null;
	}
	public Triangle returnNeigbour(Point p1, Point p2) {
		if (ab.contains(p1) && ab.contains(p2)) 
			return ab;
		else if (ac.contains(p1) && ac.contains(p2)) 
			return ac;
		else if (bc.contains(p1) && bc.contains(p2))
			return bc;
		else 
			throw new IllegalArgumentException("Points are not coins of the triagle");
	}
	public boolean isInteriorPoint(Point p) {
		int p_res = 0;
		int a_res = Util.rightLeft(b, c, a);
		p_res = Util.rightLeft(b, c, p);
		if (a_res != p_res)
			return false;
		
		int b_res = Util.rightLeft(c, a, b);
		p_res = Util.rightLeft(c, a, p);
		if (b_res != p_res)
			return false;
		
		int c_res = Util.rightLeft(a, b, c);
		p_res = Util.rightLeft(a, b, p);
		
		if (c_res != p_res)
			return false;
		
		return true;
	}
	public boolean contains(Point p) {
		if (pointOnSegment(p) != null)
			return true;
		return isInteriorPoint(p);
	}
	public Point thirdPoint(Point p1, Point p2) {
		if ((p1.equals(a) && p2.equals(b)) || (p1.equals(b) && p2.equals(a)))
			return c;
		if ((p1.equals(a) && p2.equals(c)) || (p1.equals(c) && p2.equals(a)))
			return b;
		if ((p1.equals(b) && p2.equals(c)) || (p1.equals(c) && p2.equals(b)))
			return a;
		throw new IllegalArgumentException("Points are not coins of the triagle");
	}
	public String toString() {
		return "a: " + a + " b: " + b + " c: " + c;
	}
	public static void main(String[] args) {
		double m = Algorithmes.greatestValue(null);
		Point p_1 = new Point(-8,85);
		Point p_2 = new Point(m,0);
		Point p_3 = new Point(-m,-m);
		Triangle t = new Triangle(p_1,p_2,p_3);
		System.out.println(t.contains(new Point(0,0)));
	}   
	
}
