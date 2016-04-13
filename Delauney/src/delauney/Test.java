package delauney;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class Test {

	public static void main(String[] args) {
		Vector<Point> points = new Vector<Point>();
		Set<Point> set = new TreeSet<Point>();
		try {
			points.clear();
		
			FileReader fr = new FileReader("10PointsDouble.out");
		    BufferedReader br = new BufferedReader(fr);
		    String line = br.readLine();
		    while(line != null) {
		    	String[] numbers = line.split(",");
		    	int x = Integer.parseInt(numbers[0]);
		    	int y = Integer.parseInt(numbers[1]);
		    	Point p = new Point(x,y);
		    	
		    	System.out.println(set.contains(p));
		    	if (!set.contains(p))
		    		set.add(p);
		    	line = br.readLine();
		    }
		   
		    br.close();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    }
			for(Point p : set)
				points.add(p);
			
			for (Point p : points)
				System.out.println(p);

	}

}
