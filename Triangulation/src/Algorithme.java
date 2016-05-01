import java.util.Random;
import java.util.Stack;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {

	/**
	 * Algorithme qui prend un ensemble de points et qui retourne un ensemble de
	 * segments.
	 */
	static Vector<Segment> algorithme1(Vector<Point> points) {
		// Creation de la liste des segments
		Vector<Segment> segments = new Vector<Segment>();
		Point premierPt = points.elementAt(0);
		Point dernierPt = points.elementAt(points.size() - 1);

		// Ajout d'un segment entre chaque paire de points consecutifs
		for (int n1 = 0; n1 < points.size() - 1; n1++) {

			Point p1 = points.elementAt(n1);
			Point p2 = points.elementAt(n1 + 1);
			segments.addElement(new Segment(p1, p2));
		}
		segments.addElement(new Segment(premierPt, dernierPt));
		
		marquagePoint(points);
		Vector<Point> trie = marquerOrdre(points);
		marquerAngle(trie);
		Vector<Segment> diagonale = triangulation(trie);
		segments.addAll(diagonale);
		return segments;
	}

	private static void marquagePoint(Vector<Point> points) {
		for (int i = 0; i < points.size() - 1; i++) {
			Point p1 = points.elementAt(i);
			Point p2 = points.elementAt(i + 1);
			if (p2.y < p1.y)
				p2.isRight = false;
			else
				p2.isRight = true;

		}
	}

	/**
	 * renvoie les vector à droite
	 * @param points
	 * @return
	 */
	private static Vector<Point> getVectorDroit(Vector<Point> points) {
		Vector<Point> res = new Vector<>();
		for (int i = 0; i < points.size(); i++) {
			Point p = points.elementAt(i);
			if (p.isRight)
				res.add(p);
		}
		return res;
	}

	/**
	 * renvoie les points à gauche
	 * @param points
	 * @return
	 */
	private static Vector<Point> getVectorGauche(Vector<Point> points) {
		Vector<Point> res = new Vector<>();
		for (int i = 0; i < points.size(); i++) {
			Point p = points.elementAt(i);
			if (!p.isRight)
				res.add(p);
		}
		return res;
	}

	/**
	 * inverse Vector
	 * @param points
	 * @return
	 */
	private static Vector<Point> inverser(Vector<Point> points) {
		Vector<Point> res = new Vector<Point>();
		for (int i = 0; i < points.size(); i++) {
			Point p = points.elementAt(points.size() - 1 - i);
			res.add(p);
		}
		return res;
	}

	static Vector<Point> trierParFusion(Vector<Point> v1, Vector<Point> v2) {
		int cur1 = 0;
		int cur2 = 0;
		int cur = 0;
		Vector<Point> res = new Vector<>();
		while (cur1 < v1.size() && cur2 < v2.size()) {
			Point point1 = v1.elementAt(cur1);
			Point point2 = v2.elementAt(cur2);
			Point pointToAdd = null;
			if (point1.y < point2.y) {
				cur1++;
				pointToAdd = point1;
			} else {
				cur2++;
				pointToAdd = point2;
			}
			pointToAdd.pos = cur++;
			res.add(pointToAdd);
		}
		for (int i = cur1; i < v1.size(); i++) {
			Point p = v1.elementAt(i);
			p.pos = cur++;
			res.add(p);
		}
		for (int i = cur2; i < v2.size(); i++) {
			Point p = v2.elementAt(i);
			p.pos = cur++;
			res.add(p);
		}

		return res;
	}

	static Vector<Point> marquerOrdre(Vector<Point> points) {
		if (points == null || points.size() == 0)
			return null;
		Vector<Point> droit = getVectorDroit(points);
		Vector<Point> gauche = getVectorGauche(points);
		gauche = inverser(gauche);

		return trierParFusion(droit, gauche);
	}

	// points déjà triés par y croissant
	static void marquerAngle(Vector<Point> points) {
		if (points == null)
			return;
		if (points.size() <= 3)
			return;
		Vector<Point> vecDroit = getVectorDroit(points);
		Vector<Point> vecGauche = getVectorGauche(points);

		if (vecDroit.size() > 1) {
			vecDroit.elementAt(0).angle = Point.Label.INDETERMINE;
			vecDroit.elementAt(vecDroit.size() - 1).angle = Point.Label.INDETERMINE;
		}
		for (int i = 1; i < vecDroit.size() - 1; i++) {
			Point p1 = vecDroit.elementAt(i - 1);
			Point p2 = vecDroit.elementAt(i);
			Point p3 = vecDroit.elementAt(i + 1);
			if (produitVectoriel(p1, p2, p3) < 0)
				p2.angle = Point.Label.INFERIEUR;
			else
				p2.angle = Point.Label.SUPERIEUR;
		}
		for (int i = 0; i < vecGauche.size(); i++) {
			Point p1;
			Point p2 = vecGauche.elementAt(i);
			Point p3;
			if (i == 0)
				p1 = vecDroit.firstElement();
			else
				p1 = vecGauche.elementAt(i - 1);

			if (i == vecGauche.size() - 1)
				p3 = vecDroit.lastElement();
			else
				p3 = vecGauche.elementAt(i + 1);

			if (produitVectoriel(p1, p2, p3) > 0)
				p2.angle = Point.Label.INFERIEUR;
			else
				p2.angle = Point.Label.SUPERIEUR;
		}

	}

	// points doivent etre deja tries
	static Vector<Segment> triangulation(Vector<Point> points) {
		Vector<Segment> diagonales = new Vector<>();
		
		if (points == null || points.size() <= 3)
			return diagonales;	
		
		Stack<Point> stack = new Stack<>();

		stack.add(points.elementAt(0));
		stack.add(points.elementAt(1));

		for (int i = 2; i < points.size() - 1; i++) {
			Point curPoint = points.elementAt(i);
			showStack(stack, curPoint);
			if (curPoint.isRight != stack.peek().isRight) {
				for (int j = stack.size(); j > 1; j--) {
					Point topStack = stack.pop();
					Segment seg = new Segment(curPoint, topStack);
					seg.isDiagonale = true;
					diagonales.add(seg);
				}
				stack.pop();
				stack.push(points.elementAt(i - 1));
				stack.push(curPoint);
			} else {
				
				Point topStack = stack.pop();
				Point secondStack = stack.peek();
				stack.push(topStack);
				while (diagonaleOK(secondStack, topStack, curPoint)) {
					stack.pop();
					showStack(stack, curPoint);
					Segment seg = new Segment(curPoint, secondStack);
					seg.isDiagonale = true;
					diagonales.add(seg);
					if (stack.size() < 2)
						break;
					else {
						topStack = stack.pop();
						secondStack = stack.peek();
						stack.add(topStack);
					}

				}
				stack.push(curPoint);
			}
		}
		if (stack.size() > 1) {
			stack.pop();
			while (stack.size() > 1) {
				Point topStack = stack.pop();
				Segment seg = new Segment(points.lastElement(), topStack);
				seg.isDiagonale = true;
				diagonales.add(seg);
			}
		}
		return diagonales;

	}

	static void showStack(Stack<Point> stack, Point curPoint) {
		Stack<Point> copy = new Stack<Point>();
		System.out.println();
		System.out.print("curPOINT: " + curPoint.pos + "   ");
		while (!stack.empty()) {
			Point cur = stack.pop();
			copy.add(cur);
			System.out.print("POINT: " + cur.pos + ";  ");
		}
		System.out.println();
		while (!copy.empty()) {
			stack.add(copy.pop());
		}
	}

	/**
	 * p2 et p3 doit appartenir au meme cote
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	static boolean diagonaleOK(Point p1, Point p2, Point p3) {
		if (p2.isRight != p3.isRight)
			throw new IllegalArgumentException(
					"p2 et p3 n'appartiennent pas au meme cote");

		if (p2.isRight)
			return produitVectoriel(p1, p2, p3) < 0;

		return produitVectoriel(p1, p2, p3) > 0;
	}

	static int produitVectoriel(Point p1, Point p2, Point p3) {
		double x1 = p1.x - p2.x;
		double y1 = p1.y - p2.y;

		double x2 = p3.x - p2.x;
		double y2 = p3.y - p2.y;

		double produit = x1 * y2 - y1 * x2;
		return produit > 0 ? 1 : -1;
	}



	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n) {
		int r = new Random().nextInt();

		if (r < 0)
			r = -r;

		return r % n;
	}


}
