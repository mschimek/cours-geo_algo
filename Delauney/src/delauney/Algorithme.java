package delauney;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {
	
	
	/** Algorithme qui prend un ensemble de points et qui retourne un ensemble de segments. */ 
	static Pair algorithme1(Vector<Point> points)
	{
		System.out.println("start algo");
		HashSet<Triangle> leaves = new HashSet<>(points.size() * 3);
		double m = greatestValue(points);
		Point p_1 = new Point(-m,-m);
		Point p_2 = new Point(1,m);
		Point p_3 = new Point(m,1);
		Triangle initialTriangle = new Triangle(p_1, p_2, p_3);
		Triangle dummy1 = new Triangle(p_1, p_2, new Point(-5*m,1)); 
		Triangle dummy2 = new Triangle(p_1, p_3, new Point(5*m,1)); 
		Triangle dummy3 = new Triangle(p_2, p_3, new Point(5*m,1));
		initialTriangle.ab = dummy1;
		initialTriangle.ac = dummy2;
		initialTriangle.bc = dummy3;
		Node root = new Node(initialTriangle);
		
		Vector<Point> permutation = permutation(points);
		
		for(int i = 0; i < permutation.size(); i++) {
			Point p = permutation.elementAt(i);
			treatPoint(p, root);
			//root.visit(0);
			//System.out.println("------------------------------");
		}
		System.out.println("finished computation");
		HashSet<Triangle> set = new HashSet<>(permutation.size());
		Vector<Circle> circles = new Vector<>(permutation.size());
		Tree.getLeaves(root, set);
		set = filterTriangles(set, p_1, p_2, p_3);
		for (Triangle t : set) {
			circles.add(t.circumscribedCircle);
		}
		Vector<Segment> segs = getSegments(set);
		System.out.println("call test");
		boolean isDelauneyTriang = test(set,points);
		System.out.println("return from test");
		Pair pair = new Pair(segs, circles, isDelauneyTriang);
		
		System.out.println("Is delauney triangulation: " + isDelauneyTriang);
		return pair;
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
	private static HashSet<Triangle> filterTriangles(HashSet<Triangle> triangles, Point ... negativeFilter) {
		HashSet<Point> negFilter = new HashSet<Point>();
		HashSet<Triangle> res = new HashSet<Triangle>(triangles.size());
		for (Point p : negativeFilter)
			negFilter.add(p);
		
		for (Triangle t : triangles) {
			
			
			if (!negFilter.contains(t.a) && !negFilter.contains(t.b) && !negFilter.contains(t.c))
				res.add(t);
		}
		return res;
	}
	private static Vector<Segment> filterSegments(Vector<Segment> segments, Point ... negativeFilter) {
		HashSet<Point> negFilter = new HashSet<Point>();
		Vector<Segment> res = new Vector<Segment>(segments.size());
		for (Point p : negativeFilter)
			negFilter.add(p);
		
		for (int i = 0; i < segments.size(); i++) {
			Segment s = segments.elementAt(i);
			Point a = s.a;
			Point b = s.b;
			if (!negFilter.contains(a) && !negFilter.contains(b))
				res.add(s);
		}
		return res;
	}
	static void treatPoint(Point p, Node root) {
		Node curNode = root.getNode(p);
		Triangle triangle = curNode.triangle;
		int sum = 0;
		if (triangle.isInteriorPoint(p)) {
			Triangle t1 = new Triangle(p, triangle.a, triangle.b);
			Triangle t2 = new Triangle(p, triangle.a, triangle.c);
			Triangle t3 = new Triangle(p, triangle.b, triangle.c);
			curNode.split(t1, t2, t3);
			sum = legalizeEdge(p, t1, new Segment(triangle.a, triangle.b), root,0);
			sum += legalizeEdge(p, t2, new Segment(triangle.a, triangle.c), root,0);
			sum += legalizeEdge(p, t3, new Segment(triangle.b, triangle.c), root,0);
		}
		else {
			//System.out.println("else");
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
			
			//t3,t4
			thirdPoint = neighbour.thirdPoint(s.a, s.b);
			assert(thirdPoint != null);
			Triangle t3 = new Triangle(p,s.a, thirdPoint);
			
			assert(thirdPoint != null);
			Triangle t4 = new Triangle(p,s.b, thirdPoint);
			
			Tree.splitUpdateNeighbours(triangle, t1, t2, neighbour, t3, t4, s);
			
			// legalisations
			thirdPoint = triangle.thirdPoint(s.a, s.b);
			Segment edge = triangle.rightSegment(s.a, thirdPoint);
			assert(edge != null);
			sum = legalizeEdge(p, t1, edge, root, 0);
			
			edge = triangle.rightSegment(s.b, thirdPoint);
			assert(edge != null);
			sum += legalizeEdge(p, t2, edge, root,0);
			
			thirdPoint = neighbour.thirdPoint(s.a, s.b);
			edge = neighbour.rightSegment(s.a, thirdPoint);
			assert(edge != null);
			sum += legalizeEdge(p, t3, edge, root,0);
			
			edge = neighbour.rightSegment(s.b, thirdPoint);
			assert(edge != null);
			sum += legalizeEdge(p, t4, edge, root, 0);
			
		}
		//System.out.println("legalizeChaines " + sum);
		
	}
	private static int legalizeEdge(Point p, Triangle t, Segment segment, Node root, int count) {
		Triangle neighbour = t.returnNeigbour(segment.a, segment.b);
		Point candidate = neighbour.thirdPoint(segment.a, segment.b);
		if (t.circumscribedCircle.contains(candidate)) {
			Point p0 = t.thirdPoint(segment.a, segment.b);
			assert(p0 != null);
			assert(p.equals(p0)); 	
			Triangle t1 = new Triangle(p0, segment.a, candidate);
			Triangle t2 = new Triangle(p0, segment.b, candidate);
			Node n1 = new Node(t1);
			Node n2 = new Node(t2);
			t.container.childA = n1;
			t.container.childB = n2;
			neighbour.container.childA = n1;
			neighbour.container.childB = n2;
			
			Tree.flipUpdateNeighbours(t, t1, neighbour, t2, segment);
			count += legalizeEdge(p, t1, new Segment(t1.thirdPoint(p, candidate), candidate), root, count+1);
			count += legalizeEdge(p, t2, new Segment(t2.thirdPoint(p, candidate), candidate), root, count+1);
			
		}
		return count;
		
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
		
		return res;
	}
	static boolean test(HashSet<Triangle> triangles, Vector<Point> points) {
		for(Triangle t : triangles) {
			Point a = t.a;
			Point b = t.b;
			Point c = t.c;
			int counter = 0;
			for (Point p : points) {
				//if (counter > 1000)
				//	break;
				if (p.equals(a) || p.equals(b) || p.equals(c))
					continue;
				if (t.circumscribedCircle.contains(p))
					return false;
				//counter++;
			}
		}
		return true;
	}
	
	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();
		
		if (r < 0) r = -r;
		
		return r % n;
	}
}
