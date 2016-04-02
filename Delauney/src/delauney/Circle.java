package delauney;

public class Circle {
	public double radius;
	public Point center;
	public Circle(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	public boolean contains(Point p) {
		double dist = Util.eucledianDistance(p, center);
		return dist < (radius - Util.EPSILON);
	}
	public boolean onBorder(Point p) {
		double dist = Util.eucledianDistance(p, center);
		return Math.abs(dist - radius) < Util.EPSILON;
	}
	public String toString() {
		return "Center: " + center.toString() + " radius: " + radius;
	}
}
