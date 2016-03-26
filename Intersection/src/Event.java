import java.util.ArrayList;
import java.util.List;


public class Event {
	public final double EPSILON = 1E-12;
	public double x;
	public double y;
	public List<Segment> upper;
	public List<Segment> lower;
	public List<Segment> cut;
	public Event(double x, double y) {
		this.x = x;
		this.y = y;
		this.upper = new ArrayList<>();
		this.lower = new ArrayList<>();
		this.cut = new ArrayList<>();
	}
	public boolean equals(Object ob) {
		System.out.println("bin gerade hier");
		if (ob instanceof Event) {
			Event obj = (Event) ob;
			if (Math.abs(this.x - obj.x) < EPSILON && Math.abs(this.y - obj.y) < EPSILON)
				return true;
		}
		return false;
	}
}
