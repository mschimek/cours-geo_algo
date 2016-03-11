import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {
	
	/** Algorithme qui prend un ensemble de segements et qui retourne un ensemble de points d'intersection. */ 
	static Vector<Point> algorithme1(Vector<Segment> segments)
	{
		Vector<Point> ext = trierExt(segments);
		Deque<Point> file = initialiserFile(ext);
		
		
		
		return null;
	}
	/** trier les extremités des segement recu par parmetre */
	static Vector<Point> trierExt(Vector<Segment> segments) {
		if (segments == null)
			return new Vector<Point>();
		
		Vector<Point> points = new Vector<>(2*segments.size());
		for (int i = 0; i < segments.size(); i++) {
			Point p1 = segments.elementAt(i).a;
			Point p2 = segments.elementAt(i).b;
			points.add(p1);
			points.add(p2);
		}
		
		Collections.sort(points);
		return points;
		
	}
	/** initialiser la file d'attente */
	static Deque<Point> initialiserFile(Vector<Point> sortedPoints) {
		if (sortedPoints == null)
			return new ArrayDeque<Point>();
		
		ArrayDeque<Point> file = new ArrayDeque<>();
		for (int i = 0; i < sortedPoints.size(); i++) {
			Point p = sortedPoints.elementAt(i);
			file.addLast(p);
		}
		return file;
	}
	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();
		
		if (r < 0) r = -r;
		
		return r % n;
	}
}
