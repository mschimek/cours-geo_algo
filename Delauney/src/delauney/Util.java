package delauney;



public class Util {
	public static final double EPSILON = 1E-12;
	public static Point intersect(Segment s1, Segment s2) {
		if (s1 == null || s2 == null)
			return null;
		
		double x1 = s1.a.x;
		double x2 = s1.b.x;
		double x3 = s2.a.x;
		double x4 = s2.b.x;
		double y1 = s1.a.y;
		double y2 = s1.b.y;
		double y3 = s2.a.y;
		double y4 = s2.b.y;
		
		double determinant = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (Math.abs(determinant) < EPSILON) 
			return null;
		   
		double inter_x = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/determinant;
		double inter_y = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/determinant;
		    
		Point intersection = new Point(inter_x, inter_y);
		return intersection;
	}
	public static double squaredEucledianDistance(Point a, Point b) {
		if (a == null || b == null)
			return Double.NaN;
		double x = a.x - b.x;
		double y = a.y - b.y;
		double d = x*x + y*y;
		return d;
	}
	public static  double eucledianDistance(Point a, Point b) {
		double d = squaredEucledianDistance(a,b);
		if (d == Double.NaN)
			return -1;
		
		return Math.sqrt(d);
	}
	public static Point vector(Point src, Point dest) {
		if (src == null || dest == null)
			return null;
		return new Point(dest.x - src.x, dest.y - src.y);
	}
	
	
	public static double adaptedCrossProduct(Point a, Point b) {
		return a.x*b.y - a.y*b.x;
	}
	public static double scalarProduct(Point a, Point b) {
		return a.x * b.x + a.y*b.y;
	}
	public static Point plus(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
	public static Point factor(double alpha, Point p) {
		return new Point(alpha * p.x, alpha * p.y);
	}
	public static void main(String[] args) {
		Point a = new Point(10,0);
		Point b = new Point(0,10);
		Point c = new Point(-10, 0);
		Triangle t = new Triangle(a,b,c);
		Circle circle = t.circumscribedCircle();
		boolean answer = circle.onBorder(new Point(0,10.00006));
		System.out.println(circle);
		System.out.println(answer);
	}
	static int vectorProduct(Point p1, Point p2, Point p3) {
		double x1 = p1.x - p2.x;
		double y1 = p1.y - p2.y;

		double x2 = p3.x - p2.x;
		double y2 = p3.y - p2.y;

		double product = x1 * y2 - y1 * x2;
		if (Math.abs(product) < EPSILON)
			return 0;
		
		return product > 0 ? 1 : -1;
	}
}

