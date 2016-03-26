import java.util.Vector;

public class Result {
		private Vector<Segment> segs;
		private Vector<Point> intersections;
		Result(Vector<Segment> segs, Vector<Point> intersections) {
			this.segs = segs;
			this.intersections = intersections;
		}
		public Vector<Segment> getSegments() {
			return segs;
		}
		public Vector<Point> getIntersections() {
			return intersections;
		}
	}