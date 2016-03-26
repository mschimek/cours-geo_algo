import java.util.Vector;


public class SegmentStore {
	static Segment s1 = new Segment(new Point(208,35), new Point(196,121));
	static Segment s2 = new Segment(new Point(129,3), new Point(233,101));
	static Segment s3 = new Segment(new Point(268,52), new Point(139,123));
	static Segment s4 = new Segment(new Point(49,77), new Point(333,77));
	static Segment s5 = new Segment(new Point(88,55), new Point(231,59));
	static Segment s6 = new Segment(new Point(144,35), new Point(104,128));
	static Segment s7 = new Segment(new Point(213,42), new Point(179,129));
	static Segment s8 = new Segment(new Point(104,86), new Point(298,86));
	static Segment s9 = new Segment(new Point(240,31), new Point(172,116));
	static Segment s10 = new Segment(new Point(157,35), new Point(287,108));
	static Segment s11 = new Segment(new Point(96,86), new Point(342,86));
	static Segment s12 = new Segment(new Point(40,40), new Point(60,60));
	static Segment s13 = new Segment(new Point(40,60), new Point(60,40));
	static Segment s14 = new Segment(new Point(40,30), new Point(60,70));
	static Segment s15 = new Segment(new Point(30,50), new Point(100,50));
	static Segment s16 = new Segment(new Point(50,50), new Point(100,90));
	static Segment s17 = new Segment(new Point(30,10), new Point(50,50));
	
	
	public static Vector<Segment> getSeg1() {
		Vector<Segment> segs = new Vector<>();
		segs.add(s1);
		segs.add(s2);
		segs.add(s3);
		return segs;
	}
	public static Vector<Segment> getSeg2() {
		Vector<Segment> segs = new Vector<>();
		segs.add(s1);
		segs.add(s4);
		return segs;
	}
	public static Vector<Segment> getSeg3() {
		Vector<Segment> segs = new Vector<>();
		segs.add(s5);
		segs.add(s6);
		segs.add(s7);
		segs.add(s8);
		return segs;
	}
	public static Vector<Segment> getSeg4() {
		Vector<Segment> segs = new Vector<>();
		segs.add(s9);
		segs.add(s10);
		segs.add(s11);
		return segs;
	}
	public static Vector<Segment> getSeg5() {
		Vector<Segment> segs = new Vector<>();
		segs.add(s12);
		segs.add(s13);
		segs.add(s14);
		segs.add(s15);
		segs.add(s16);
		segs.add(s17);
		return segs;
	}
	public static Vector<Segment> getSeg6() {
		Vector<Segment> segs = new Vector<>();
		for (int i = 1; i < 501 ; i++) {
			Segment s = new Segment(new Point(1,i),new Point(501,i));
			segs.add(s);
		}
		for (int i = 2; i < 501; i++) {
			Segment s = new Segment(new Point(i,1),new Point(i,501));
			segs.add(s);
		}
		
		


		return segs;
	}
	public static Vector<Point> getPoints(Vector<Segment> seg) {
		Vector<Point> points = new Vector<>();
		for (int i = 0; i < seg.size(); i++) {
			Point u = seg.elementAt(i).upper;
			Point l = seg.elementAt(i).lower;
			points.add(u);
			points.add(l);
		}
		return points;
	}
}
