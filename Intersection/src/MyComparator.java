
public class MyComparator implements java.util.Comparator<Segment>  {

	private static final double EPSILON = 1E-12;
	public static final double FORWARD = 1E-11;
	private double y = 0.0;
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public int compare(Segment s1, Segment s2) {
		if (s1.equals(s2))
			return 0;
		
		if(Util.isHorizontal(s1)) {
			if (Util.isHorizontal(s2)) {
				return s1.lower.compareTo(s2.lower) * -1;
			}
			return 1;
		}
		double s1_x = Util.intersection(s1, y + FORWARD);
		double s2_x = Util.intersection(s2, y + FORWARD);
		if (s1_x + EPSILON < s2_x)
			return -1;
		if (s2_x + EPSILON < s1_x)
			return 1;
		
		return 0;
	}

}
