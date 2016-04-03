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
	private void updateNeighbours(Triangle t) {
		if (t.contains(triangle.a) && t.contains(triangle.b)) {
			t.updateRightNeighbour(triangle.ab);
			triangle.ab.updateRightNeighbour(t);
		}
		if (t.contains(triangle.a) && t.contains(triangle.c)) {
			t.updateRightNeighbour(triangle.ac);
			triangle.ac.updateRightNeighbour(t);
		}
		if (t.contains(triangle.b) && t.contains(triangle.c)) {
			t.updateRightNeighbour(triangle.bc);
			triangle.bc.updateRightNeighbour(t);
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
		a.updateRightNeighbour(b);
		a.updateRightNeighbour(c);
		
		updateNeighbours(b);
		b.updateRightNeighbour(a);
		b.updateRightNeighbour(c);
		
		updateNeighbours(c);
		c.updateRightNeighbour(a);
		c.updateRightNeighbour(b);
	}
	public String toString() {
		return "Triangle: " + triangle + " ChildA: " + (childA!=null) + " childB: " + (childB!=null) + " childC: " + (childC!=null);
	}
}	
