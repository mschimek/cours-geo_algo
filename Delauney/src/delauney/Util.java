package delauney;
import java.util.Random;


public class Util {
	public static final double EPSILON = 1E-9;
	
	/**
	 * if s1 and s2 intersect the intersection point is returned
	 * else null
	 * @param s1
	 * @param s2
	 * @return
	 */
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
	
	/** crossProduct for 2D Vectors
	 */
	public static double adaptedCrossProduct(Point a, Point b) {
		return a.x*b.y - a.y*b.x;
	}
	public static double scalarProduct(Point a, Point b) {
		return a.x * b.x + a.y*b.y;
	}
	/** plus for each component 
	 * e.g. p1 = (x1,y1), p2=(x2,y2) => plus(p1,p2) = (x1+x2,y1+y2)
	 */
	public static Point plus(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
	/** multiply for each component 
	 * e.g. p1 = (x1,y1), alpha => plus(p1,p2) = (alpha * x1, alpha * y1)
	 */
	public static Point factor(double alpha, Point p) {
		return new Point(alpha * p.x, alpha * p.y);
	}
	
	/** 
	 * if c is on the lefthand side of the segment [a;b] return 1 
	 * if c is on the righthand side of the segment [a;b] returns -1
	 * if c is on the segment [a;b] returns 0
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static int rightLeft(Point a, Point b, Point c) {
		double res = (b.x - a.x)*(c.y - a.y)-(c.x-a.x)*(b.y-a.y);
		if (Math.abs(res) < EPSILON)
			return 0;
		if (res > 0)
			return 1;
		
		return -1;
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
	static int[] randomPermutation(int n) {
		int[] permutation = new int[n];
		for (int i = 0; i < n; i++) {
			permutation[i] = i;
		}
		Random rand = new Random();
		for (int j = 0; j < 2; j++) {
			for (int i = n - 1; i >= 0; i--) {
				int r = rand.nextInt(i + 1);
				int tmp = permutation[r];
				permutation[r] = permutation[i];
				permutation[i] = tmp;
			}
		}
		return permutation;
	}
	
	/*public static void main(String[] args) {
		Point a = new Point(0,0);
		Point b = new Point(10,0);
		Point c = new Point(0, 10);
		System.out.println(rightLeft(a,b,c));
	} */
} 

