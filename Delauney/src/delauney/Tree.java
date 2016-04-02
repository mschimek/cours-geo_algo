package delauney;

public class Tree {
	public Triangle node;
	public Tree childA;
	public Tree childB;
	public Tree childC;
	
	public Tree(Triangle node) {
		this.node = node;
		node.container = this;
	}
	
	private boolean childContainsPoint(Tree child, Point p) {
		if (childA != null)
			return childA.node.contains(p);
			
		return false;
	}
	public Tree getChild(Point p) {
		if (childContainsPoint(childA, p))
			return childA;
		if (childContainsPoint(childB, p))
			return childB;
		if (childContainsPoint(childC, p))
			return childC;
		
		return null;		
	}
	public Tree getTriangle(Point p) {
		Tree child = getChild(p);
		if (child == null) {
			if(node.contains(p))
				return this;
			
			return null;
		} 
		return child.getTriangle(p);
	}
	private void updateNeighbours(Triangle t) {
		if (t.contains(node.a) && t.contains(node.b)) {
			t.updateRightNeighbour(node.ab);
			node.ab.updateRightNeighbour(t);
		}
		if (t.contains(node.a) && t.contains(node.c)) {
			t.updateRightNeighbour(node.ac);
			node.ac.updateRightNeighbour(t);
		}
		if (t.contains(node.b) && t.contains(node.c)) {
			t.updateRightNeighbour(node.bc);
			node.bc.updateRightNeighbour(t);
		}
	}
	public void split(Triangle a, Triangle b, Triangle c) {
		childA = new Tree(a);
		childB = new Tree(b);
		childC = new Tree(c);
		
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
}	
