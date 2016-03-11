
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {
	
	/** Algorithme qui prend un ensemble de points et qui retourne un ensemble de segments. */ 
	static Vector<Segment> algorithme1(Vector<Point> points)
	{
		System.out.println("Length Points " + points.size());
		// Creation de la liste des segments
		Vector<Segment> segments = new Vector<Segment>();
		Vector<Point> adapted = deleteDuplicates(points);
		for (int i = 0; i < points.size(); i++) {
			System.out.println("POINT:" + points.elementAt(i).x + ":" + points.elementAt(i).y);
					}
		/*PrintWriter pWriter = null;
        try {
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("test")));
            for (int i = 0; i < points.size(); i++) {
    			pWriter.println("POINT:" + points.elementAt(i).x + ":" + points.elementAt(i).y);
    		}
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null){
                pWriter.flush();
                pWriter.close();
            }
        } */
		
		
	/*	for (int i = 0; i < points.size(); i++) {
			Point p1 = points.elementAt(i);
			System.out.println("Point: x = " + p1.x + "  y = " + p1.y + "  angle = " + p1.angle);
		}
		Point lowest = findLowestPoint(points);
		System.out.println("LowestPoint : x = " + lowest.x + " y = " + lowest.y);*/
		
		System.out.println("length adapted points " + adapted.size());
		
	/*	for (int i = 0; i < points.size(); i++) {
			Point p1 = points.elementAt(i);
			System.out.println("Point: x = " + p1.x + "  y = " + p1.y + "  angle = " + p1.angle);
		}
		System.out.println();*/
		return calculerEnveloppeConv(adapted);
		
	}
	static Vector<Point> deleteDuplicates(Vector<Point> points) {
		calculateAnglesAndDistance(points);
		Vector<Point> sorted = sortByAngle(points);
		boolean[] dup = new boolean[points.size()];
		int counter = 0;
		for (int i = 1; i < sorted.size(); i++) {
			Point p1 = sorted.elementAt(i - 1);
			Point p2 = sorted.elementAt(i);
			if (p2.compareTo(p1) == 0)
				dup[i] = true;
		}
		for (int i = 0; i < dup.length; i++) {
			if (!dup[i])
				counter++;
		}
		Vector<Point> res = new Vector<>(counter);
		
		int curPos = 0;
		for (int i = 0; i < sorted.size(); i++) {
			if (!dup[i])
			res.add(curPos++, sorted.elementAt(i));
		}
		return res;
	}
	static int caractizeTriangle(Point p1, Point p2, Point p3) {
		double arg = (p2.x - p1.x)*(p3.y - p1.y) - (p3.x * - p1.x)*(p2.y - p1.y);
		return (arg > 0) ? 1 : -1;
	}
	static double calculateAngle(Point p1, Point p2, Point p3) {
		Vector2D v1 = new Vector2D(p2, p1);
		Vector2D v2 = new Vector2D(p2, p3);
		return Vector2D.angle(v1, v2);
	}
	/** trier par angle et comme deuxième parametre par distance croissante depuis le point de reference */
	static Vector<Point> sortByAngle(Vector<Point> points) {
		if (points == null)
			return null;
		Vector<Point> res = new Vector<>(points.size());
		for (int i = 0; i < points.size(); i++) {
			Point p = points.elementAt(i);
			res.add(i, p);
		}
		Collections.sort(res);
		return res;
	}
	static Vector<Point> calculateAnglesAndDistance(Vector<Point> points) {
		if (points == null)
			return null;
		if (points.size() == 1) {
			points.elementAt(0).angle = 0.0;
			return points;
		}
		
		Point refPoint = findLowestPoint(points);
		Point auxPoint = new Point(refPoint.x + 10, refPoint.y);
		for (int i = 0; i < points.size(); i++) {
			Point curPoint = points.elementAt(i);
			curPoint.angle = calculateAngle(auxPoint,refPoint, curPoint);
			curPoint.dist = Vector2D.distance(curPoint, refPoint);
		}
		return points;
	}
	static Point findLowestPoint(Vector<Point> points) {
		if (points == null)
			return null;
		Point res = new Point(Double.MAX_VALUE, Double.MIN_VALUE);
		for (int i = 0; i < points.size(); i++) {
			Point curPoint = points.elementAt(i);
			
			if (curPoint.y > res.y) {
				res.x = curPoint.x;
				res.y = curPoint.y;
			}
			else if (curPoint.y == res.y && curPoint.x > res.x) {
				res.y = curPoint.y;
			}
		}
		return res;
	}
	static boolean checkNextTriangle(Stack<Point> stack, Point curPoint) {
		if (stack.size() < 2)
			return false;
		
		Point head = stack.pop();
		Point second = stack.peek();
		stack.push(head);
		if (produitVectoriel(second, head, curPoint) <= 0) {
			stack.pop();
			return true;
		}
		
		return false;
			
		
	}
	static int produitVectoriel(Point p1, Point p2, Point p3) {
		double x1 = p1.x - p2.x;
		double y1 = p1.y - p2.y;

		double x2 = p3.x - p2.x;
		double y2 = p3.y - p2.y;

		double produit = x1 * y2 - y1 * x2;
		return produit > 0 ? 1 : -1;
	}
	static Vector<Segment> calculerEnveloppeConv(Vector<Point> points) {
		Vector<Segment> envConv = new Vector<>();
		
		if (points == null)
			return null;
		if (points.size() <= 1)
			return envConv;
		if (points.size() == 2) {
			Segment intervalle = new Segment(points.elementAt(0), points.elementAt(1));
			envConv.add(intervalle);
			return envConv;
		}
		//GrahamScan
		Stack<Point> stack = new Stack<>();
		Vector<Point> sortedPoints = calculateAnglesAndDistance(points);
		
		stack.push(sortedPoints.elementAt(0));
		stack.push(sortedPoints.elementAt(1));
		
		for (int i = 2; i < sortedPoints.size(); i++) {
			Point curPoint = sortedPoints.elementAt(i);
			while (checkNextTriangle(stack, curPoint));
			stack.push(curPoint);	
		}
		
		
		
		Point head = stack.pop();
		Point p1 = head;
		Point p2 = null;
		while(!stack.isEmpty()) {
			p2 = stack.pop();
			Segment seg = new Segment(p1, p2);
			envConv.add(seg);
			p1 = p2;
		}
		Segment closingSegment = new Segment(head, p2);
		envConv.add(closingSegment);
		return envConv;
	}
	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();
		
		if (r < 0) r = -r;
		
		return r % n;
	}
	public static class Vector2D {
		public double x;
		public double y;
		Vector2D(Point p1, Point p2) {
			x = p2.x - p1.x;
			y = p2.y - p1.y;
		}
		public double length() {
			return Math.sqrt(x*x + y*y);
		}
		public static double scalarProduct(Vector2D v1, Vector2D v2) {
			return v1.x * v2.x + v1.y * v2.y;
		}
		
		public static int produitVectoriel(Point p1, Point p2, Point p3) {
			Vector2D v1 = new Vector2D(p2, p1);
			Vector2D v2 = new Vector2D(p2, p3);
			return produitVectoriel(v1, v2);

			
		}
		public static int produitVectoriel(Vector2D v1, Vector2D v2) {
			double produit = v1.x * v2.y - v1.y * v2.x;
			return produit > 0 ? 1 : -1;
		}
		public static double angle(Vector2D v1, Vector2D v2) {
			if (v1.length() == 0 || v2.length() == 0)
				return 0.0;
			double arg = scalarProduct(v1, v2) / (v1.length() * v2.length());
			if (Double.isNaN(arg))
					arg = 0.0;
			arg = Math.acos(arg);
			if (produitVectoriel(v1, v2) > 0) {
				// angle > 180 
				arg = 2*Math.PI - arg;
			}
			return arg;
		}
		public static double distance(Point p1, Point p2) {
			Vector2D v = new Vector2D(p1,p2);
			return v.length();
		}
	}
}
