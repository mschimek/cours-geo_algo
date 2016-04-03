package delauney;

import java.util.HashSet;

public class Tree {
	public static void getLeaves(Node root, HashSet<Triangle> set) {
		if (root.childA == null && root.childB == null && root.childC == null) {
			set.add(root.triangle);
			return;
		}
		if (root.childA != null)
			getLeaves(root.childA, set);
		if (root.childB != null)
			getLeaves(root.childB, set);
		if (root.childC != null)
			getLeaves(root.childC, set);
		
		
	}
	public static void splitUpdateNeighbours(Triangle parent1, Triangle child1, Triangle child2, 
									  Triangle parent2, Triangle child3, Triangle child4, Segment border) {
		//parten1 = p0,p1,p2 
	    //child1 = p, p1, p2;
		//child2 = p0,p1,p;
		//parent2 = p0,p2,p3
		//child3 = p3,p,p2
		//child4 = p0,p,p3
		//newPoint = p
		
		assert(border != null);
		
		Point thirdPoint_parent1 = parent1.thirdPoint(border.a, border.b);
		
		// child1
		if (child1.contains(thirdPoint_parent1) && child1.contains(border.a)) {
			child1.updateRightNeighbour(parent1.returnNeigbour(thirdPoint_parent1, border.a));
			parent1.returnNeigbour(thirdPoint_parent1, border.a).updateRightNeighbour(child1);
		}
		else {
			child1.updateRightNeighbour(parent1.returnNeigbour(thirdPoint_parent1, border.b));
			parent1.returnNeigbour(thirdPoint_parent1, border.b).updateRightNeighbour(child1);
		}
		child1.updateRightNeighbour(child3);
		child1.updateRightNeighbour(child2);
		
		
		//child2
		if (child2.contains(thirdPoint_parent1) && child2.contains(border.a)){
			child2.updateRightNeighbour(parent1.returnNeigbour(thirdPoint_parent1, border.a));
			parent1.returnNeigbour(thirdPoint_parent1, border.a).updateRightNeighbour(child2);
		}
		else {
			child2.updateRightNeighbour(parent1.returnNeigbour(thirdPoint_parent1, border.b));
			parent1.returnNeigbour(thirdPoint_parent1, border.b).updateRightNeighbour(child2);
		}
		
		child2.updateRightNeighbour(child4);
		child2.updateRightNeighbour(child1);
		
		Point thirdPoint_parent2 = parent2.thirdPoint(border.a, border.b);
		
		// child3
		if (child3.contains(thirdPoint_parent2) && child3.contains(border.a)) {
			child3.updateRightNeighbour(parent2.returnNeigbour(thirdPoint_parent2, border.a));
			parent2.returnNeigbour(thirdPoint_parent2, border.a).updateRightNeighbour(child3);
		}
		else {
			child3.updateRightNeighbour(parent2.returnNeigbour(thirdPoint_parent2, border.b));
			parent2.returnNeigbour(thirdPoint_parent2, border.b).updateRightNeighbour(child3);
		}
		child3.updateRightNeighbour(child1);
		child3.updateRightNeighbour(child4);
		
		// child4
		if (child4.contains(thirdPoint_parent2) && child4.contains(border.a)) {
			child4.updateRightNeighbour(parent2.returnNeigbour(thirdPoint_parent2, border.a));
			parent2.returnNeigbour(thirdPoint_parent2, border.a).updateRightNeighbour(child4);
		}
		else {
			child4.updateRightNeighbour(parent2.returnNeigbour(thirdPoint_parent2, border.b));
			parent2.returnNeigbour(thirdPoint_parent2, border.b).updateRightNeighbour(child4);
		}
		
		child4.updateRightNeighbour(child2);
		child4.updateRightNeighbour(child3);
	}

	public static void flipUpdateNeighbours(Triangle parent1, Triangle child1, 
			  Triangle parent2, Triangle child2, Segment oldBorder) {
		Point thirdPoint_parent1 = parent1.thirdPoint(oldBorder.a, oldBorder.b);
		Point thirdPoint_parent2 = parent2.thirdPoint(oldBorder.a, oldBorder.b);
		
		child1.updateRightNeighbour(parent1);
		child1.updateRightNeighbour(parent2);
		child1.updateRightNeighbour(child2);
		
		child2.updateRightNeighbour(parent1);
		child2.updateRightNeighbour(parent2);
		child2.updateRightNeighbour(child1);
		
		if (child1.contains(thirdPoint_parent1) && child1.contains(oldBorder.a)) {
			parent1.returnNeigbour(thirdPoint_parent1, oldBorder.a).updateRightNeighbour(child1);
			parent1.returnNeigbour(thirdPoint_parent1, oldBorder.b).updateRightNeighbour(child2);
			parent2.returnNeigbour(thirdPoint_parent2, oldBorder.a).updateRightNeighbour(child1);
			parent2.returnNeigbour(thirdPoint_parent2, oldBorder.b).updateRightNeighbour(child2);
		}
		else {
			parent1.returnNeigbour(thirdPoint_parent1, oldBorder.a).updateRightNeighbour(child2);
			parent1.returnNeigbour(thirdPoint_parent1, oldBorder.b).updateRightNeighbour(child1);
			parent2.returnNeigbour(thirdPoint_parent2, oldBorder.a).updateRightNeighbour(child2);
			parent2.returnNeigbour(thirdPoint_parent2, oldBorder.b).updateRightNeighbour(child1);
		}
		
	}
}
