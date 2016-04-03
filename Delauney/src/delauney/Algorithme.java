package delauney;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {
	
	/** Algorithme qui prend un ensemble de points et qui retourne un ensemble de segments. */ 
	static Vector<Segment> algorithme1(Vector<Point> points)
	{
		Vector<Segment> segments = new Vector<Segment>();
		
		double m = greatestValue(points);
		Point p_1 = new Point(m,0);
		Point p_2 = new Point(0,m);
		Point p_3 = new Point(-m,-m);
		Triangle initialTriangle = new Triangle(p_1, p_2, p_3);
		Triangle dummy1 = new Triangle(p_1, p_2, new Point(m+10000,0)); 
		Triangle dummy2 = new Triangle(p_1, p_3, new Point(m+10000,0)); 
		Triangle dummy3 = new Triangle(p_2, p_3, new Point(-100000, m));
		initialTriangle.ab = dummy1;
		initialTriangle.ac = dummy2;
		initialTriangle.bc = dummy3;
		Node root = new Node(initialTriangle);
		
		Vector<Point> permutation = permutation(points);
		
		for(int i = 0; i < permutation.size(); i++) {
			Point p = permutation.elementAt(i);
			treatPoint(p, root);
		}
		
		HashSet<Triangle> set = new HashSet<>();
		Tree.getLeaves(root, set);
		
		return getSegments(set);
	}
	static Vector<Segment> getSegments(HashSet<Triangle> set) {
		HashSet<Segment> seg = new HashSet<>();
		
		Iterator<Triangle> it = set.iterator();
		while (it.hasNext()) {
			Triangle t = it.next();
			Segment s1 = new Segment(t.a, t.b);
			Segment s2 = new Segment(t.a, t.c);
			Segment s3 = new Segment(t.b, t.c);
			seg.add(s1);
			seg.add(s2);
			seg.add(s3);
		}
		Iterator<Segment> iter = seg.iterator();
		Vector<Segment> res = new Vector<Segment>(seg.size());
		while (iter.hasNext()) {
			Segment s = iter.next();
			res.add(s);
		}
		return res;
	}
	static void treatPoint(Point p, Node root) {
		Node curNode = root.getNode(p);
		Triangle triangle = curNode.triangle;
		
		if (triangle.isInteriorPoint(p)) {
			Triangle t1 = new Triangle(p, triangle.a, triangle.b);
			Triangle t2 = new Triangle(p, triangle.a, triangle.c);
			Triangle t3 = new Triangle(p, triangle.b, triangle.c);
			curNode.split(t1, t2, t3);
			legalizeEdge(p, t1, new Segment(triangle.a, triangle.b), root);
			legalizeEdge(p, t2, new Segment(triangle.a, triangle.c), root);
			legalizeEdge(p, t3, new Segment(triangle.b, triangle.c), root);
		}
		else {
			Segment s = triangle.pointOnSegment(p);
			Triangle neighbour = triangle.returnNeigbour(s.a, s.b);
			
			//construct the 4 new triangle t1, t2 are constructed from triangle
			// t3,t4 adhere to the neighbour of triangle
			// t1,t2
			Point thirdPoint = triangle.thirdPoint(s.a, s.b);
			assert(thirdPoint != null);
			Triangle t1 = new Triangle(p, s.a, thirdPoint);
			
			assert(thirdPoint != null);
			Triangle t2 = new Triangle(p, s.b, thirdPoint);
			curNode.split(t1, t2);
			
			//t3,t4
			thirdPoint = neighbour.thirdPoint(s.a, s.b);
			assert(thirdPoint != null);
			Triangle t3 = new Triangle(p,s.a, thirdPoint);
			
			assert(thirdPoint != null);
			Triangle t4 = new Triangle(p,s.b, thirdPoint);
			neighbour.container.split(t1, t2);
			
			Tree.splitUpdateNeighbours(triangle, t1, t2, neighbour, t3, t4, s);
			
			// legalisations
			thirdPoint = triangle.thirdPoint(s.a, s.b);
			Segment edge = triangle.rightSegment(s.a, thirdPoint);
			assert(edge != null);
			legalizeEdge(p, t1, edge, root);
			
			edge = triangle.rightSegment(s.b, thirdPoint);
			assert(edge != null);
			legalizeEdge(p, t2, edge, root);
			
			thirdPoint = neighbour.thirdPoint(s.a, s.b);
			edge = neighbour.rightSegment(s.a, thirdPoint);
			assert(edge != null);
			legalizeEdge(p, t3, edge, root);
			
			edge = neighbour.rightSegment(s.b, thirdPoint);
			assert(edge != null);
			legalizeEdge(p, t4, edge, root);
			
		}
		
	}
	private static void legalizeEdge(Point p, Triangle t, Segment segment, Node root) {
		Triangle neighbour = t.returnNeigbour(segment.a, segment.b);
		Point candidate = neighbour.thirdPoint(segment.a, segment.b);
		if (t.circumscribedCircle.contains(candidate)) {
			Point p0 = t.thirdPoint(segment.a, segment.b);
			assert(p0 != null);
			Triangle t1 = new Triangle(p0, segment.a, candidate);
			Triangle t2 = new Triangle(p0, segment.b, candidate);
			Node n1 = new Node(t1);
			Node n2 = new Node(t2);
			t.container.childA = n1;
			t.container.childB = n2;
			neighbour.container.childA = n1;
			neighbour.container.childB = n2;
			
			Tree.flipUpdateNeighbours(t, t1, neighbour, t2, segment);
		}
		
	}
	public static double greatestValue(Vector<Point> points) {
		return 100000;
	}
	
	static Vector<Point> permutation(Vector<Point> points) {
		
		Vector<Point> res = new Vector<Point>(points.size());
		int[] permutation = Util.randomPermutation(points.size());
		
		for(int i = 0; i < permutation.length; i++) {
			res.add(points.elementAt(permutation[i]));
		}
		return points;
	}
	
	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();
		
		if (r < 0) r = -r;
		
		return r % n;
	}
}
