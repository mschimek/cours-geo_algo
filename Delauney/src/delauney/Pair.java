package delauney;

import java.util.Vector;

public class Pair {
	public Vector<Segment> segments;
	public Vector<Circle> circles;
	public boolean isDelauneyTriang;
	public Pair(Vector<Segment> segments, Vector<Circle> circles, boolean isDelauneyTriang) {
		this.segments = segments;
		this.circles = circles;
		this.isDelauneyTriang = isDelauneyTriang;
	}
	
}
