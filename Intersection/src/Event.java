import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Event {
	public double x;
	public double y;
	public HashSet<Segment> upper;
	public HashSet<Segment> lower;
	public HashSet<Segment> cut;
	public Event(double x, double y) {
		this.x = x;
		this.y = y;
		this.upper = new HashSet<>();
		this.lower = new HashSet<>();
		this.cut = new HashSet<>();
	}
	public boolean equals(Object ob) {
		System.out.println("bin gerade hier");
		if (ob instanceof Event) {
			Event obj = (Event) ob;
			if (Math.abs(this.x - obj.x) < MyComparator.EPSILON && Math.abs(this.y - obj.y) < MyComparator.EPSILON)
				return true;
		}
		return false;
	}
	public String toString() {
		return "x:" + x + " y: " + y + " upper: " + upper.size() + " lower: " + lower.size() + " cut: " + cut.size() + "\n"; 
	}
}
