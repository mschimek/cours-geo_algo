package delauney;

import java.util.Vector;

public class MyPair {
	public Vector<Triangle> triangles;
	public Vector<Circle> circles;
	public MyPair(Vector<Triangle> triangles, Vector<Circle> circles) {
		this.triangles = triangles;
		this.circles = circles;
	}
}
