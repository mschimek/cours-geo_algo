package delauney;

import java.util.HashSet;
import java.util.Vector;

public class Tree {
	public static void getLeaves(Node root, HashSet<Triangle> set) {
		root.visited = true;
		Vector<Triangle> triangles = new Vector<Triangle>();
		if (root.childA == null && root.childB == null && root.childC == null) {
			set.add(root.triangle);
			//System.out.println("got leaf");
			//return triangles;
		}
		if (root.childA != null && !root.childA.visited)
			getLeaves(root.childA, set);
		if (root.childB != null && !root.childB.visited)
			getLeaves(root.childB, set);
		if (root.childC != null && !root.childC.visited)
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
		
		Point thirdParent1 = parent1.thirdPoint(border.a, border.b);
		Point thirdParent2 = parent2.thirdPoint(border.a, border.b);
		Point commonPointA = border.a;
		Point commonPointB = border.b;
		
		child1.updateNeighbour(child2);
		child2.updateNeighbour(child1);
		
		child1.updateNeighbour(child3);
		child3.updateNeighbour(child1);
		
		child2.updateNeighbour(child4);
		child4.updateNeighbour(child2);
		
		child3.updateNeighbour(child4);
		child4.updateNeighbour(child3);
		
		if (!child1.contains(commonPointA)) {
			Point tmp = commonPointA;
			commonPointA = commonPointB;
			commonPointB = tmp;
		}
		
		// child1
		Triangle neighbour = parent1.returnNeigbour(thirdParent1, commonPointA);
		child1.updateNeighbour(neighbour);
		neighbour.updateNeighbour(child1);
		
		//child2
		neighbour = parent1.returnNeigbour(thirdParent1, commonPointB);
		child2.updateNeighbour(neighbour);
		neighbour.updateNeighbour(child2);
		
		//child3
		neighbour = parent2.returnNeigbour(thirdParent2, commonPointA);
		child3.updateNeighbour(neighbour);
		neighbour.updateNeighbour(child3);
		
		//child4
		neighbour = parent2.returnNeigbour(thirdParent2, commonPointB);
		child4.updateNeighbour(neighbour);
		neighbour.updateNeighbour(child4);
		
		Node n1 = new Node(child1);
		Node n2 = new Node(child2);
		Node n3 = new Node(child3);
		Node n4 = new Node(child4);
		
		parent1.container.childA = n1;
		parent1.container.childB = n2;
		parent2.container.childA = n3;
		parent2.container.childB = n4;
			
	}

	public static void flipUpdateNeighbours(Triangle parent1, Triangle child1, 
			  Triangle parent2, Triangle child2, Segment oldBorder) {
		Point commonPointA = oldBorder.a;
		Point commonPointB = oldBorder.b;
		Point thirdParent1 = parent1.thirdPoint(oldBorder.a, oldBorder.b);
		Point thirdParent2 = parent2.thirdPoint(oldBorder.a, oldBorder.b);
		
		child1.updateNeighbour(child2);
		child2.updateNeighbour(child1);
		
		if (!child1.contains(commonPointA)) {
			Point tmp = commonPointA;
			commonPointA = commonPointB;
			commonPointB = tmp;
			
		}
			
		// update child1
		Triangle neighbourParent1 = parent1.returnNeigbour(thirdParent1, commonPointA);
		Triangle neighbourParent2 = parent2.returnNeigbour(thirdParent2, commonPointA);
				
			// first neighbour
			child1.updateNeighbour(neighbourParent1);
			neighbourParent1.updateNeighbour(child1);
				
			// second neighbour
			child1.updateNeighbour(neighbourParent2);
			neighbourParent2.updateNeighbour(child1);
				
		//update child2
		neighbourParent1 = parent1.returnNeigbour(thirdParent1, commonPointB);
		neighbourParent2 = parent2.returnNeigbour(thirdParent2, commonPointB);
					
			// first neighbour
			child2.updateNeighbour(neighbourParent1);
			neighbourParent1.updateNeighbour(child2);
					
			// secondneighbour
			child2.updateNeighbour(neighbourParent2);
			neighbourParent2.updateNeighbour(child2); 
		
	}
}
