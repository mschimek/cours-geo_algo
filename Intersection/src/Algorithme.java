import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

/** La classe algorithme. */
public class Algorithme {
	static int counter = 0;
	/** Algorithme qui prend un ensemble de segements et qui retourne un ensemble de points d'intersection. */ 
	static Result algorithme1(Vector<Segment> segments)
	{
		counter = 0;
		Vector<Point> intersections = new Vector<>();
		TreeSet<Event> queue = initializeQueue(segments);
		MyComparator comp = new MyComparator();
		TreeSet<Segment> sweepLine = new TreeSet<Segment>(comp);
		
		while (!queue.isEmpty()) {
			Event e = queue.pollFirst();
			handleEvent(e,queue,sweepLine, comp, intersections);
		} 
		
		
		System.out.println("-------------------------------------------------------");
		
		for (int i = 0; i < segments.size(); i++)
			resetHorizontal(segments.elementAt(i));
		System.out.println("intersections " + counter);
		return new Result(segments,intersections);
	}
	
	/**
	 * calculates all intersection points using the brute force method
	 * @param segments
	 * @return
	 */
	static Result bruteForce(Vector<Segment> segments) {
		Vector<Point> intersections = new Vector<>();
		HashSet<Point> intersectionsSet = new HashSet<Point>();
		
		for(int i = 0; i < segments.size(); i++) {
			Segment outer = segments.elementAt(i);
			for (int j = i + 1; j < segments.size(); j++) {
				Segment inner = segments.elementAt(j);
				Point p = Util.intersection(outer, inner, false);
				if (p == null) {
					if (outer.upper.equals(inner.upper))
						p = outer.upper;
					if (outer.upper.equals(inner.lower))
						p = outer.upper;
					if (outer.lower.equals(inner.upper))
						p = outer.lower;
					if (outer.lower.equals(inner.lower))
						p = outer.lower;

				}
				if (p != null) {
										
					p.intersection = true;
					if (!intersectionsSet.contains(p)) {
						intersections.add(p);
						intersectionsSet.add(p);
					}
				}
			}
		}
		return new Result(segments, intersections);
	}
	
	private static void addToQueue(Event e, TreeSet<Event> queue) {
		if (queue.contains(e)) {
			Event inQueue = queue.ceiling(e);
			inQueue.upper.addAll(e.upper);
			inQueue.lower.addAll(e.lower);
			inQueue.cut.addAll(e.cut);
		}
		else {
			queue.add(e);
		}
	}
	static void resetHorizontal(Segment s) {
		if (Util.isHorizontal(s)) {
			s.upper.x = s.upperSave.x;
			s.upper.y = s.upperSave.y;
		}
			
	}
	static TreeSet<Event> initializeQueue(Vector<Segment> seg) {
		
		Comparator<Event> comp = new Comparator<Event>(){

			@Override
			public int compare(Event arg0, Event arg1) {
				if (arg0.y + MyComparator.EPSILON < arg1.y)
					return -1;
				else if (Math.abs(arg0.y - arg1.y) < MyComparator.EPSILON) {
					if (arg0.x + MyComparator.EPSILON  < arg1.x)
						return -1;
					else if (Math.abs(arg0.x - arg1.x) < MyComparator.EPSILON)
				    	return 0;
					else 
						return 1;
				}
				else
					return 1;		    
			}
		};
		TreeSet<Event> events = new TreeSet<>(comp);
		for (int i = 0; i < seg.size(); i++) {
			Segment curSeg = seg.elementAt(i);
			Point upper = curSeg.upper;
			Point lower = curSeg.lower;
			Event upperEv = new Event(upper.x, upper.y);
			Event lowerEv = new Event(lower.x, lower.y);
			if (events.contains(upperEv)) {
				Event e = events.floor(upperEv);
				e.upper.add(curSeg);
			}
			else {
				upperEv.upper.add(curSeg);
				events.add(upperEv);
			}
			if (events.contains(lowerEv)) {
				Event e = events.floor(lowerEv);
				e.lower.add(curSeg);
			}
			else {
				lowerEv.lower.add(curSeg);
				events.add(lowerEv);
			}
		}
		return events;
	}
	/**
	 * prints all intersection points of the event e
	 * @param e 
	 */
	private static void printIntersections(Event e, List<Point> intersections) {
		counter++;
		System.out.println("Following Segments intersects at point x:" + e.x + " y:" + e.y);
		Point inter = new Point(e.x, e.y);
		inter.intersection = true;
		intersections.add(inter);
		
		Iterator<Segment> it = e.lower.iterator();
		while(it.hasNext())
			System.out.println("\t" + it.next());
		
		it = e.upper.iterator();
		while(it.hasNext())
			System.out.println("\t" + it.next());
		
		it = e.cut.iterator();
		while(it.hasNext())
			System.out.println("\t" + it.next());
		
	}
	private static void dummyPrintIntersections(Event e, List<Point> intersections) {
		counter++;
		Point inter = new Point(e.x, e.y);
		inter.intersection = true;
		intersections.add(inter);
	}
	/**
	 * removes all segments contained in the list segments from the state of the sweepline
	 * @param lower
	 * @param sweepLine
	 */
	private static void deleteSegments(Event e, HashSet<Segment> lower, TreeSet<Segment> sweepLine, MyComparator comp) {
		Iterator<Segment> it = lower.iterator();
		
		while(it.hasNext()) {
			Segment seg = it.next();
			
			//System.out.println(sweepLine.contains(seg));
			if (!sweepLine.contains(seg)) {
				comp.setY(e.y - MyComparator.EPSILON - MyComparator.FORWARD);
				sweepLine.remove(seg);
				comp.setY(e.y);
			}
			else {
				sweepLine.remove(seg);
			}
			
		}
	}
	
	private static void  insertInSweepLine(Event e, TreeSet<Segment> sweepLine, MyComparator comp) {
		comp.setY(e.y);
		Iterator<Segment> it = e.cut.iterator();
		List<Segment> horizontalSwitched = new ArrayList<>();
		
		double x_max = -1;
		int count = 0;
		
		while(it.hasNext()) {
			Segment s = it.next();
			if (Util.isHorizontal(s)) {
				horizontalSwitched.add(s);
			} 
			else {
				double x_res = Util.intersection(s, e.y + MyComparator.FORWARD);
				x_max = x_max < x_res ? x_res : x_max;
				sweepLine.add(s);
				count++;
			}
		}
		it = e.upper.iterator();
		while(it.hasNext()) {
			Segment s = it.next();
			if (Util.isHorizontal(s)) {
				horizontalSwitched.add(s);
			} 
			else {
				double x_res = Util.intersection(s, e.y + MyComparator.FORWARD / 2);
				x_max = x_max < x_res ? x_res : x_max;
				sweepLine.add(s);
				count++;
			}
		}
		
		it = horizontalSwitched.iterator();
		while(it.hasNext()) {
			Segment s = it.next();
			if (count > 0)
				s.upper.x = x_max + MyComparator.FORWARD;
			else 
				s.upper.x += MyComparator.FORWARD;
			boolean b = sweepLine.contains(s);
			sweepLine.add(s);
			 b = sweepLine.contains(s);
		} 
			
		
	}
	/**
	 * finds the segment in the current event which is at the leftmost position
	 * @param e
	 * @return
	 */
	public static Segment leftmost(Event e) {
		Segment seg = null;
		double x = Integer.MAX_VALUE;
		Iterator<Segment> it = e.upper.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res;
			if (Util.isHorizontal(cur)) {
				x_res = cur.upper.x;
			}	
			else {
				x_res = Util.intersection(cur, e.y + MyComparator.FORWARD);
			}
			if (x_res < x) {
				x = x_res;
				seg = cur;
			}
		}
		
		it = e.cut.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res;
			if (Util.isHorizontal(cur)) {
				x_res = cur.upper.x;
			}	
			else {
				x_res = Util.intersection(cur, e.y + MyComparator.FORWARD);
			}
			if (x_res < x) {
				x = x_res;
				seg = cur;
			}
		}
		return seg;
	}
	
	/**
	 * finds the element in the current event which is at the rightmost position
	 * @param e
	 * @return
	 */
	public static Segment rightmost(Event e) {
		Segment seg = null;
		double x = Integer.MIN_VALUE;
		Iterator<Segment> it = e.upper.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res;
			if (Util.isHorizontal(cur)) {
				x_res = cur.upper.x;
			}	
			else {
				x_res = Util.intersection(cur, e.y + MyComparator.FORWARD);
			}
			if (x_res > x) {
				x = x_res;
				seg = cur;
			}
		}
		
		it = e.cut.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res;
			if (Util.isHorizontal(cur)) {
				x_res = cur.upper.x;
			}	
			else {
				x_res = Util.intersection(cur, e.y + MyComparator.FORWARD);
			}
			if (x_res > x) {
				x = x_res;
				seg = cur;
			}
		}
		return seg;
	}
	/**
	 * if s1 and s2 have an intersection the method inserts this new event in the priority queue
	 * @param s1
	 * @param s2
	 * @param e
	 * @param queue
	 */
	private static void newEvent(Segment s1, Segment s2, Event e, TreeSet<Event> queue) {
		Point res = Util.intersection(s1, s2, false);
		if (res == null)
			return;
		if (res.y < (e.y - MyComparator.EPSILON))
			return;
		
		Event newEvent = new Event(res.x, res.y);
		
		if(!newEvent.cut.contains(s1))
			newEvent.cut.add(s1);
		if (!newEvent.cut.contains(s2))
			newEvent.cut.add(s2);
		addToQueue(newEvent, queue);
	}
	/**
	 *  treats an event
	 * @param e
	 * @param queue
	 * @param sweepLine
	 * @param comp
	 * @param intersections
	 */
	public static void handleEvent(Event e, TreeSet<Event> queue, 
			TreeSet<Segment> sweepLine, MyComparator comp , List<Point> intersections) {
		
		HashSet<Segment> upper = e.upper;
		HashSet<Segment> lower = e.lower;
		HashSet<Segment> cut = e.cut;
		
		deleteSegments(e, lower, sweepLine, comp);
		deleteSegments(e, cut, sweepLine, comp);
		deleteLowerFromCut(e);

		if (upper.size() + lower.size() + cut.size() > 1)
			printIntersections(e, intersections);
		
		
		
		insertInSweepLine(e, sweepLine, comp);
		
		if (upper.size() + cut.size() == 0) {
			Segment test = new Segment(new Point(e.x,e.y + MyComparator.FORWARD), new Point(e.x, e.y-5 + MyComparator.FORWARD));
			
			Segment left = sweepLine.lower(test);
			Segment right = sweepLine.higher(test);
			newEvent(left,right,e,queue);
		}
		else {
			Segment left = leftmost(e);
			Segment right = rightmost(e);
			Segment left_left = sweepLine.lower(left);
			Segment right_right = sweepLine.higher(right);
			
			if(!Util.isHorizontal(left))
				newEvent(left,left_left,e,queue);
			
			newEvent(right,right_right,e,queue);
		}
	}
	
	private static void deleteLowerFromCut(Event e) {
		Iterator<Segment> it = e.lower.iterator();
		while(it.hasNext()) {
			e.cut.remove(it.next());
			
		}
		
	}
	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();
		
		if (r < 0) r = -r;
		
		return r % n;
	}
	
} 

/*


		*/
