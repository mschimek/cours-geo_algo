package delauney;

public class Node {
	public Triangle triangle;
	public Node childA;
	public Node childB;
	public Node childC;
	
	public Node(Triangle triangle) {
		this.triangle = triangle;
		triangle.container = this;
	}
	
	private boolean childContainsPoint(Node child, Point p) {
		if (child != null)
			return child.triangle.contains(p);
			
		return false;
	}
	private Node getChild(Point p) {
		if (childContainsPoint(childA, p))
			return childA;
		if (childContainsPoint(childB, p))
			return childB;
		if (childContainsPoint(childC, p))
			return childC;
		
		return null;		
	}
	public Node getNode(Point p) {
		Node child = getChild(p);
		if (child == null) {
			if(triangle.contains(p))
				return this;
			
			return null;
		} 
		return child.getNode(p);
	}
	/**
	 * update all neighbor triangles of 'this' which have a common edge with t
	 * and update t with the neighbors triangles of 'this'
	 * @param t
	 */
	private void updateNeighbours(Triangle t) {
		if (t.contains(triangle.a) && t.contains(triangle.b)) {
			t.updateNeighbour(triangle.ab);
			triangle.ab.updateNeighbour(t);
		}
		if (t.contains(triangle.a) && t.contains(triangle.c)) {
			t.updateNeighbour(triangle.ac);
			triangle.ac.updateNeighbour(t);
		}
		if (t.contains(triangle.b) && t.contains(triangle.c)) {
			t.updateNeighbour(triangle.bc);
			triangle.bc.updateNeighbour(t);
		}
	}
	public void flip(Node a, Node b) {
		childA = a;
		childB = b;
		
	}
	public void split(Triangle a, Triangle b) {
		childA = new Node(a);
		childB = new Node(b);
	}
	public void split(Triangle a, Triangle b, Triangle c) {
		childA = new Node(a);
		childB = new Node(b);
		childC = new Node(c);
		
		updateNeighbours(a);
		a.updateNeighbour(b);
		a.updateNeighbour(c);
		
		updateNeighbours(b);
		b.updateNeighbour(a);
		b.updateNeighbour(c);
		
		updateNeighbours(c);
		c.updateNeighbour(a);
		c.updateNeighbour(b);
	}
	public String toString() {
		return "Triangle: " + triangle + " ChildA: " + (childA!=null) + " childB: " + (childB!=null) + " childC: " + (childC!=null);
	}
}	
