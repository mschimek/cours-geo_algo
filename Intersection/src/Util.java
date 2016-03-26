
public class Util {
	/**
	 * returns point of intersection if s1 and s2 intersect and null otherwise
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static Point intersection(Segment s1, Segment s2, boolean withoutEndpointCheck) {
			
		if (s1 == null || s2 == null)
			return null;
		
		double x1 = s1.upper.x;
		double x2 = s1.lower.x;
		double x3 = s2.upper.x;
		double x4 = s2.lower.x;
		double y1 = s1.upper.y;
		double y2 = s1.lower.y;
		double y3 = s2.upper.y;
		double y4 = s2.lower.y;
		
		double determinant = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (determinant == 0) 
			return null;
		   
		double inter_x = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/determinant;
		double inter_y = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/determinant;
		    
		Point intersection = new Point(inter_x, inter_y);
		
		if (withoutEndpointCheck)
			return intersection;
		
		double s1_min_x = s1.upper.x < s1.lower.x ? s1.upper.x : s1.lower.x;
		double s2_min_x = s2.upper.x < s2.lower.x ? s2.upper.x : s2.lower.x;
		double s1_min_y = s1.upper.y < s1.lower.y ? s1.upper.y : s1.lower.y;
		double s2_min_y = s2.upper.y < s2.lower.y ? s2.upper.y : s2.lower.y;
		double s1_max_x = s1.upper.x > s1.lower.x ? s1.upper.x : s1.lower.x;
		double s2_max_x = s2.upper.x > s2.lower.x ? s2.upper.x : s2.lower.x;
		double s1_max_y = s1.upper.y > s1.lower.y ? s1.upper.y : s1.lower.y;
		double s2_max_y = s2.upper.y > s2.lower.y ? s2.upper.y : s2.lower.y;
		if (intersection.x < s1_min_x || intersection.x < s2_min_x)
			return null;
		if (intersection.x > s1_max_x || intersection.x > s2_max_x)
			return null;
		if (intersection.y < s1_min_y || intersection.y < s2_min_y)
			return null;
		if (intersection.y > s1_max_y || intersection.y > s2_max_y)
			return null;
		
		if (intersection.compareTo(s1.upper) == 0 || intersection.compareTo(s1.lower) == 0) {
			if (intersection.compareTo(s2.upper) == 0 || intersection.compareTo(s2.lower) == 0)
				return null;
		}
	    
		return intersection;
	}
	
	public static boolean isHorizontal(Segment s) {
		return (s.upper.y == s.lower.y) && (s.upper.y == 0);
	}
	public static Double intersection(Segment s, double y) {
		Segment y_seg = new Segment(new Point(Integer.MIN_VALUE, y), new Point(Integer.MAX_VALUE, y));
		//System.out.println("s " + s + "  y_seg " + y_seg);
		Point res = intersection(s, y_seg, true);
		if (res == null)
			return null;
		Double x = res.x;
		return x;
		
	}
	public static Point formula(Segment s1) {
		Point upper = s1.upper;
		Point lower = s1.lower;
		double c = 0;
		double m = 0;
		if (s1.lower.compareTo(s1.upper) == 0)
			return null;
		
		if (upper.x == 0) {
			 c = upper.y;
			 m = (lower.y - c) / lower.x;
		}
		else if (lower.x == 0) {
			c = lower.y;
			m = (upper.y -c) / upper.x;
		}
		else {
			m = (lower.y -upper.y) / (upper.x - lower.x);
			c = lower.y - lower.x * m; 
		     
		}
		return new Point(m,c);
		
	}
}
