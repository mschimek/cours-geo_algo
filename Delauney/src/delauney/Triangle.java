package delauney;

public class Triangle {
	public Point a;
	public Point b;
	public Point c;
	public Triangle ab;
	public Triangle bc;
	public Triangle ac;
	public Circle circumscribedCircle;
	public Tree container = null;
	public Triangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.circumscribedCircle = circumscribedCircle();
	}
	public Triangle(Point a, Point b, Point c, Triangle ab, Triangle bc, Triangle ac) {
		this(a,b,c);
		this.ab = ab;
		this.bc = bc;
		this.ac = ac;
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
	public void updateRightNeighbour(Triangle t) {
		if (t.contains(a) && t.contains(b)) 
			ab = t;
		else if (t.contains(a) && t.contains(c)) 
			ac = t;
		else if (t.contains(b) && t.contains(c))
			bc = t;
		else 
			System.out.println("updateRightNeighbour failed! (this:" + this + "t: " + t);
	}
	public boolean contains(Point p) {
		Segment ab = new Segment(a,b);
		Segment ac = new Segment(a,c);
		Segment bc = new Segment(b,c);
		int p_res = 0;
		if (ab.contains(p) || ac.contains(p) || bc.contains(p))
				return true;
		
		int a_res = Util.vectorProduct(b, c, a);
		p_res = Util.vectorProduct(b, c, p);
		if (a_res != p_res)
			return false;
		
		int b_res = Util.vectorProduct(a, c, b);
		p_res = Util.vectorProduct(a, c, p);
		if (b_res != p_res)
			return false;
		
		int c_res = Util.vectorProduct(a, b, c);
		p_res = Util.vectorProduct(a, b, p);
		
		if (c_res != p_res)
			return false;
		
		return true;
	}
	
	
	
}
