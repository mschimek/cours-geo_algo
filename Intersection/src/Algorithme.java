import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

/** La classe algorithme. */
public class Algorithme {
	
	/** Algorithme qui prend un ensemble de segements et qui retourne un ensemble de points d'intersection. */ 
	static Vector<Segment> algorithme1(Vector<Segment> segments)
	{
		TreeSet<Event> queue = initializeQueue(segments);
		MyComparator comp = new MyComparator();
		TreeSet<Segment> sweepLine = new TreeSet<Segment>(comp);
		
		while (!queue.isEmpty()) {
			Event e = queue.pollFirst();
			handleEvent(e,queue,sweepLine, comp);
		} 
		
		
		System.out.println("-------------------------------------------------------\n\n\n");
		return segments;
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
	static TreeSet<Event> initializeQueue(Vector<Segment> seg) {
		
		Comparator<Event> comp = new Comparator<Event>(){

			@Override
			public int compare(Event arg0, Event arg1) {
				if (arg0.y < arg1.y)
					return -1;
				else if (arg0.y == arg1.y) {
					if (arg0.x < arg1.x)
						return -1;
					else if (arg0.x == arg1.x)
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
	private static void printIntersections(Event e) {
		System.out.println("Following Segments intersects at point x:" + e.x + " y:" + e.y);
		
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
	/**
	 * removes all segments contained in the list segments from the state of the sweepline
	 * @param segments
	 * @param sweepLine
	 */
	private static void deleteSegments(List<Segment> segments, TreeSet<Segment> sweepLine) {
		Iterator<Segment> it = segments.iterator();
		while(it.hasNext())
			sweepLine.remove(it.next());
	}
	
	private static void  insertInSweepLine(Event e, TreeSet<Segment> sweepLine, MyComparator comp) {
		comp.setY(e.y);
		Iterator<Segment> it = e.cut.iterator();
		while(it.hasNext())
			sweepLine.add(it.next());
		
		it = e.upper.iterator();
		while(it.hasNext())
			sweepLine.add(it.next());
	}
	public static Segment leftmost(Event e) {
		Segment seg = null;
		double x = Integer.MAX_VALUE;
		Iterator<Segment> it = e.upper.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res = Util.intersection(cur, e.y);
			if (x_res < x) {
				x = x_res;
				seg = cur;
			}
		}
		
		it = e.cut.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res = Util.intersection(cur, e.y);
			if (x_res < x) {
				x = x_res;
				seg = cur;
			}
		}
		return seg;
	}
	public static Segment rightmost(Event e) {
		Segment seg = null;
		double x = Integer.MIN_VALUE;
		Iterator<Segment> it = e.upper.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res = Util.intersection(cur, e.y);
			if (x_res > x) {
				x = x_res;
				seg = cur;
			}
		}
		
		it = e.cut.iterator();
		while(it.hasNext()) {
			Segment cur = it.next();
			double x_res = Util.intersection(cur, e.y);
			if (x_res > x) {
				x = x_res;
				seg = cur;
			}
		}
		return seg;
	}
	private static void newEvent(Segment s1, Segment s2, Event e, TreeSet<Event> queue) {
		Point res = Util.intersection(s1, s2, false);
		if (res == null)
			return;
		if (res.y <= e.y)
			return;
		
		Event newEvent = new Event(res.x, res.y);
		newEvent.cut.add(s1);
		newEvent.cut.add(s2);
		addToQueue(newEvent, queue);
	}
	public static void handleEvent(Event e, TreeSet<Event> queue, TreeSet<Segment> sweepLine, MyComparator comp) {
		List<Segment> upper = e.upper;
		List<Segment> lower = e.lower;
		List<Segment> cut = e.cut;
		
		//U(e) + L(e) + C(e) contain more than one segment
		if (upper.size() + lower.size() + cut.size() > 1)
			printIntersections(e);
		
		deleteSegments(lower, sweepLine);
		deleteSegments(cut, sweepLine);
		
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
			newEvent(left,left_left,e,queue);
			newEvent(right,right_right,e,queue);
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
