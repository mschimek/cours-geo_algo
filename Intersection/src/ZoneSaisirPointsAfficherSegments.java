import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** La classe ZoneSaisirPointsAfficherSegments. */
public class ZoneSaisirPointsAfficherSegments extends JPanel  {

	JTextField coordinates = new JTextField("x: y:");
	
	
	/** Creation de la zone d'affichage. */
	public ZoneSaisirPointsAfficherSegments()
	{
		// Le canvas d'affichage
		final CanvasSaisirPointsAfficherSegments canvas = new CanvasSaisirPointsAfficherSegments(this); 
		
		// Panel des boutons
		JPanel panelBoutons = new JPanel();
		
		
		// Creation du bouton Rand
		final JTextField textNombrePoint = new JTextField("50");
		textNombrePoint.setColumns(5);
		
		
		final JTextField points = new JTextField("");
		coordinates.setColumns(10);
		
		JButton calculer = new JButton("Calculer");
		
		calculer.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				// Suppression des points et des segments
				//canvas.segments = SegmentStore.getSeg7();
				
				canvas.calculer();
				//canvas.points = SegmentStore.getPoints(canvas.segments);
				
			}
		}
	);
		
		JButton load = new JButton("Load");
		load.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				try {
				canvas.points.clear();
				canvas.segments.clear();
				FileReader fr = new FileReader("50Points2.out");
			    BufferedReader br = new BufferedReader(fr);
			    String line = br.readLine();
			    while(line != null) {
			    	String[] numbers = line.split(",");
			    	int x = Integer.parseInt(numbers[0]);
			    	int y = Integer.parseInt(numbers[1]);
			    	Point p = new Point(x,y);
			    	
			    	canvas.points.add(p);
			    	line = br.readLine();
			    }
			    canvas.segments = canvas.construireSegment(canvas.points);
			    canvas.calculer();
			    br.close();
			    }
			    catch (Exception e) {
			    	e.printStackTrace();
			    }
			    
			}
		}
	);		
		// Creation du bouton Effacer
		JButton effacer = new JButton("Effacer");
		
		
		
		// Action du bouton Effacer
		effacer.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					// Suppression des points et des segments
					canvas.points.removeAllElements();
					canvas.segments.removeAllElements();
					canvas.drawingLine = false;
					canvas.firstPoint = null;
					canvas.secondPoint = null;
					canvas.repaint();
				}
			}
		);
		
		// Creation du bouton Rand
		JButton rand = new JButton("Rand");
		
		// Action du bouton Rand
		rand.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					// Suppression des points et des segments
					canvas.points.removeAllElements();
					canvas.segments.removeAllElements();
					int n = Integer.parseInt(textNombrePoint.getText());
					if (n % 2 == 1)
						n++;
					int j = 0;
					boolean b = false;
					Vector<Point> givenPoints = new Vector<Point>();
					do {
						givenPoints.clear();
						canvas.points.clear();
						canvas.segments.clear();
						j++;
						System.out.print(" dj = " + j);
						HashSet<Point> pointSet = new HashSet<Point>();
						for (int i = 0; i < n; i++)
						{
							
							Point p = new Point(
									2 + Algorithme.rand(canvas.getWidth()-4),
									2 + Algorithme.rand(canvas.getHeight()-4));
							
							if (pointSet.contains(p)) 
								continue;
							
							pointSet.add(p);
							canvas.points.addElement(
								p
							);
							
						}
						if (canvas.points.size() % 2 == 1)
							canvas.points.remove(canvas.points.size()-1);
						givenPoints.addAll(canvas.points);

						 
						canvas.segments = canvas.construireSegment(canvas.points);
						b = j < 10000 && Algorithme.bruteForce(canvas.segments).getIntersections().size() == Algorithme.algorithme1(canvas.segments).getIntersections().size();
						if(!b) {
							for (int k = 0; k < givenPoints.size(); k++) {
								System.out.println(givenPoints.elementAt(k));
							}
						}
						canvas.calculer();
			
						System.out.print(" fj= " + j);
						
					}
					while(b);
								
					canvas.repaint();
		
				}
			}
		);
		
		// Ajout des boutons au panel panelBoutons
		panelBoutons.add(calculer);
		panelBoutons.add(effacer);
		panelBoutons.add(rand);
		panelBoutons.add(textNombrePoint);
		panelBoutons.add(load);
		panelBoutons.add(coordinates);
		
		setLayout(new BorderLayout());
		
		// Ajout du canvas au centre
		add(canvas, BorderLayout.CENTER);
		
		// Ajout des boutons au nord
		add(panelBoutons, BorderLayout.SOUTH);
	}
	
	
	
}

/** La classe CanvasSaisirPointsAfficherSegments. */
class CanvasSaisirPointsAfficherSegments extends JPanel implements MouseListener, MouseMotionListener {
	Point firstPoint = null;
	Point secondPoint = null;
	boolean drawingLine = false;
	Result res1, res;
	/** La liste des points affiches. */
	Vector<Point> points;
	
	/** La liste des segments affiches. */
	Vector<Segment> segments;
	
	/** Le numero du point selectionne. */
	private int numSelectedPoint;
	
	/** La couleur d'un point a l'ecran. */
	private final Color pointColor = Color.GRAY;
	/** La couleur d'un intersection a l'ecran. */
	private final Color intersectionColor = Color.BLACK;
	
	/** La couleur d'un segment a l'ecran. */
	private final Color segmentColor = Color.BLUE;
	
	/** La couleur d'un point selectionne a l'ecran. */
	private final Color selectedPointColor = Color.RED;

	/** La taille d'un point a l'ecran. */
	private final int POINT_SIZE = 2;
	
	private final ZoneSaisirPointsAfficherSegments ref;
	/** Creation de la zone d'affichage. */
	public CanvasSaisirPointsAfficherSegments(ZoneSaisirPointsAfficherSegments ref)
	{
		this.ref = ref;
		// Creation du vecteur de points
		points = new Vector<Point>();
		
		// Creation du vecteur de segments
		segments = new Vector<Segment>();
		
		// Initialisation du point selectionne
		numSelectedPoint = -1;
		
		// Initialisation de la couleur de fond
		setBackground(Color.WHITE);
		
		// Ajout de la gestion des actions souris
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	/** Dessin de la zone d'affichage. */
	public void paint(Graphics g) {
		// Efface le fond
		g.clearRect(0,0,getWidth(),getHeight());
		
		// Dessin des segments 
		drawSegments(g);
		
		// Dessin des points
		drawPoints(g);
	}

	/** Affichage des points. */
	private void drawPoints(Graphics g) {
		for (int n = 0; n < points.size(); n++) {
			Point p = points.elementAt(n);
			
			if ( n == numSelectedPoint ) 
				g.setColor(selectedPointColor);
			else
				g.setColor(pointColor);
			
			if (p.intersection)
				g.setColor(intersectionColor);
			
			g.fillOval((int)(p.x - POINT_SIZE), (int)(p.y - POINT_SIZE), 2 * POINT_SIZE + 1, 2 * POINT_SIZE + 1);
			g.drawOval((int)(p.x - 2 * POINT_SIZE), (int)(p.y - 2 * POINT_SIZE), 2 * 2 * POINT_SIZE,	2 * 2 * POINT_SIZE);
		}
	}
	
	/** Affichage des segments.
	 */
	private void drawSegments(Graphics g) {
		for (int n = 0; n < segments.size(); n++) {
			Segment segment = segments.elementAt(n);
			g.setColor(segmentColor);
			g.drawLine((int)segment.upper.x,(int)segment.upper.y,(int)segment.lower.x,(int)segment.lower.y);
		}
	}
	
	/** Retourne le numero du point situe en (x,y). */
	private int getNumSelectedPoint(int x, int y) {
		for(int n = 0; n < points.size(); n++)
		{
			Point p = points.elementAt(n);
			if
			(
				p.x > x - 2 * POINT_SIZE && 
				p.x < x + 2 * POINT_SIZE &&
				p.y > y - 2 * POINT_SIZE && 
				p.y < y + 2 * POINT_SIZE
			)
				return n;
		}
		return -1;
	}

	/** Un point est ajoute si on presse le bouton de gauche
	 * 	et si aucun point n'est selectionne.
	 * 	Un point est suuprime si on presse un autre bouton
	 * 	et si un point est selectionne.
	 */
	public void mousePressed(MouseEvent evt) {
		if ( evt.getButton() == 1 )
		{
			if (numSelectedPoint == -1)
			{
				numSelectedPoint = points.size();
				Point p = new Point(evt.getX(), evt.getY());
				if (drawingLine) {
					secondPoint = p;
				}
				else {
					firstPoint = p;
				}
				
				points.addElement(p);
				
				repaint();
			}
			else {
				Point p = points.elementAt(numSelectedPoint);
				
				//System.out.println("Seclect by click "+ numSelectedPoint);
				if (drawingLine) {
					secondPoint = p;
				}
				else {
					firstPoint = p;
				}
				
			}
		}
		/*else
		{
			if (numSelectedPoint != -1)
			{
				Point p = points.elementAt(numSelectedPoint);
				for (int i = 0; i < segments.size(); i++) {
					Point upper = segments.elementAt(i).upper;
					Point lower = segments.elementAt(i).lower;
					if (upper.compareTo(p) == 0 || lower.compare)
				}
				points.removeElementAt(numSelectedPoint);
				numSelectedPoint = getNumSelectedPoint(evt.getX(), evt.getY());
				if (drawingLine) {
					secondPoint = new Point(evt.getX(), evt.getY());
				}
				else {
					firstPoint = new Point(evt.getX(), evt.getY());
				}
				calculer();
				repaint();
			}
		} */
	}
	
	/** Le x et y du point numSelectedPoint est modifie si
	 * 	la souris change de position avec un bouton enfonce.
	 */
	public void mouseDragged(MouseEvent evt) {
		/*if (numSelectedPoint != -1)
		{
			double x = points.elementAt(numSelectedPoint).x;
			double y = points.elementAt(numSelectedPoint).y;
			Point p = new Point(x,y);
			points.elementAt(numSelectedPoint).x = evt.getX();
			points.elementAt(numSelectedPoint).y = evt.getY();
			Point new_p = points.elementAt(numSelectedPoint);
			for (int i = 0; i < segments.size(); i++) {
				Segment s = segments.elementAt(i);
				Point upper = s.upper;
				Point lower = s.lower;
				if (s.upper.compareTo(p) == 0) {
					segments.remove(i);
					s = new Segment(new_p,lower);
				}
				if (s.lower.compareTo(p) == 0) {
					segments.remove(i);
					s = new Segment(upper,new_p);
				}
				segments.add(s);
			}
			if (drawingLine) {
				secondPoint = new Point(evt.getX(), evt.getY());
			}
			else {
				firstPoint = new Point(evt.getX(), evt.getY());
			}
			calculer();
			repaint();
		}*/
	}
	
	/** Le numSelectedPoint est calcule si
	 * 	la souris change de position sans bouton enfonce.
	 */
	public void mouseMoved(MouseEvent evt) {
		numSelectedPoint = getNumSelectedPoint(evt.getX(), evt.getY());
		//System.out.println("selected Point " + numSelectedPoint);
		ref.coordinates.setText("x:" + evt.getX() + "; y:" + evt.getY());
		repaint();
	}
	
	/** Lance l'algorithme sur l'ensemble de points. */
	public void calculer()
	{
		
		
		System.out.println("new calculation: segmentSize " + segments.size());
		
		
		
		Result res1 = Algorithme.bruteForce(segments);
		Result res = Algorithme.algorithme1(segments);

		System.out.println("intersection bruteForce:" + res1.getIntersections().size());
		segments = res.getSegments();
		points.addAll(res.getIntersections());
		repaint();
		
				
	}
	/** Calcule les segments à partir d'une liste de points de nombre pair
	 * Exemple: la liste
	 * {p1,p2,p3,p4}
	 * donne les segments
	 * [p1,p2] et [p3,p4]
	 */
	public Vector<Segment> construireSegment(Vector<Point> points) {
		if (points == null)
			return new Vector<Segment>();
		
		int n = points.size();
		Vector<Segment> seg = new Vector<Segment>(n / 2);
		
		if (n % 2 == 1)
			n--;
		
		//System.out.println("Construction of segments" + points.size() );
		for (int i = 0; i < n - 1; i += 2){
			
			Point p1 = points.elementAt(i);
			Point p2 = points.elementAt(i + 1);
			//System.out.println(p1);
			//System.out.println(p2);
			int comp = p1.compareTo(p2);
			if (comp == 0)
				continue;
			Segment curSeg = new Segment(p1, p2);
			p1.seg = curSeg;
			p2.seg = curSeg;
			if (comp < 1)
				p1.upperPoint = true;
			else
				p2.upperPoint = true;
			seg.add(curSeg);
		} 
		return seg;
	}
	public void mouseReleased(MouseEvent evt) {
		if (drawingLine) {
			Segment seg = new Segment(firstPoint, secondPoint);
			segments.add(seg);
			drawingLine = false;
			//calculer();
			
		}
		else {
			drawingLine = true;
		}
		
		repaint();
		
	}
	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent evt) {}
	public void mouseClicked(MouseEvent evt) {}
}
