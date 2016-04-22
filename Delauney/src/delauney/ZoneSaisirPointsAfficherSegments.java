package delauney;


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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** La classe ZoneSaisirPointsAfficherSegments. */
public class ZoneSaisirPointsAfficherSegments extends JPanel  {

	public JTextField coordinates = new JTextField("");
	/** Creation de la zone d'affichage. */
	public ZoneSaisirPointsAfficherSegments()
	{
		
		
		// Le canvas d'affichage
		final CanvasSaisirPointsAfficherSegments canvas = new CanvasSaisirPointsAfficherSegments(this); 
		
		// Panel des boutons
		JPanel panelBoutons = new JPanel();
		
		JButton load = new JButton("Load");
		load.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				try {
				canvas.points.clear();
				canvas.segments.clear();
				FileReader fr = new FileReader("10PointsDouble.out");
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
			    canvas.calculer();
			    canvas.repaint();
			    br.close();
			    }
			    catch (Exception e) {
			    	e.printStackTrace();
			    }
			    
			}
		}
	);		
		
		coordinates.setColumns(10);
		// Creation du bouton Rand
		final JTextField textNombrePoint = new JTextField("50");
		textNombrePoint.setColumns(5);
		
		// Creation du bouton nombre de repetitions
		final JTextField textNombreRepetition = new JTextField("50");
		textNombreRepetition.setColumns(5);
		
		JButton circles = new JButton("Circles");
		circles.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				// Suppression des points et des segments
				canvas.drawCircles = !canvas.drawCircles;
				canvas.repaint();
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
					canvas.circles.removeAllElements();
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
					int numberOfRepetitions = Integer.parseInt(textNombreRepetition.getText());
					for (int j = 0; j < numberOfRepetitions; j++) {
						canvas.points.clear();
						canvas.segments.clear();
						canvas.circles.clear();
						TreeSet<Point> tset = new TreeSet<>();
					
						
						for (int i = 0; i < n; i++)
						{
							
							
							Point p =	new Point(
									10 + Algorithmes.rand(canvas.getWidth()-4),
									10 + Algorithmes.rand(canvas.getHeight()-4)
								);
								tset.add(p);
							
						}  
						Iterator<Point> it = tset.iterator();
						while(it.hasNext()) {
							canvas.points.add(it.next());
						}
						
						
						Pair mypair = Algorithmes.algorithme1(canvas.points);
						canvas.circles = mypair.circles;
						canvas.segments = mypair.segments;
						if (mypair.isDelauneyTriang == false) {
							break;
							
						}
						
						
						//canvas.calculer();
					}
					canvas.repaint();
				}
			}
		);
		
		// Ajout des boutons au panel panelBoutons
		panelBoutons.add(effacer);
		panelBoutons.add(rand);
		panelBoutons.add(textNombrePoint);
		panelBoutons.add(textNombreRepetition);
		panelBoutons.add(circles);
		panelBoutons.add(coordinates);
		panelBoutons.add(load);
		setLayout(new BorderLayout());
		
		// Ajout du canvas au centre
		add(canvas, BorderLayout.CENTER);
		
		// Ajout des boutons au nord
		add(panelBoutons, BorderLayout.SOUTH);
	}
}

/** La classe CanvasSaisirPointsAfficherSegments. */
class CanvasSaisirPointsAfficherSegments extends JPanel implements MouseListener, MouseMotionListener {
	boolean drawCircles = true;
	/** La liste des points affiches. */
	Vector<Point> points;
	
	/** La liste des segments affiches. */
	Vector<Segment> segments;
	
	/** La liste des cercles circonsrits */
	Vector<Circle> circles = new Vector<Circle>();
	/** Le numero du point selectionne. */
	private int numSelectedPoint;
	
	/** La couleur d'un point a l'ecran. */
	private final Color pointColor = Color.GRAY;
	
	/** La couleur d'un segment a l'ecran. */
	private final Color segmentColor = Color.BLUE;
	
	/** La couleur d'un cercle a l'ecran. */
	private final Color cercleColor = Color.BLACK;
	
	/** La couleur d'un point selectionne a l'ecran. */
	private final Color selectedPointColor = Color.RED;

	/** La taille d'un point a l'ecran. */
	private final int POINT_SIZE = 2;
	
	private ZoneSaisirPointsAfficherSegments zoneSaisirPointsAfficherSegments;
	/** Creation de la zone d'affichage. 
	 * @param zoneSaisirPointsAfficherSegments */
	public CanvasSaisirPointsAfficherSegments(ZoneSaisirPointsAfficherSegments zoneSaisirPointsAfficherSegments)
	{
		this.zoneSaisirPointsAfficherSegments = zoneSaisirPointsAfficherSegments;
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
		
		// dessin des cercles
		drawCircles(g);
	}

	/** Affichage des points. */
	private void drawPoints(Graphics g) {
		for (int n = 0; n < points.size(); n++) {
			Point p = points.elementAt(n);
			
			if ( n == numSelectedPoint ) 
				g.setColor(selectedPointColor);
			else
				g.setColor(pointColor);
			
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
			g.drawLine((int)segment.a.x,(int)segment.a.y,(int)segment.b.x,(int)segment.b.y);
		}
	}
	
	/**
	 * Affichage des cercles
	 */
	private void drawCircles(Graphics g) {
		if (!drawCircles)
			return;
		for (int n = 0; n < circles.size(); n++) {
			Circle c = circles.elementAt(n);
			long radius = Math.round(c.radius);
			long x = Math.round(c.center.x);
			long y = Math.round(c.center.y);
			g.setColor(cercleColor);
			g.drawOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2* radius));
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
				points.addElement(new Point(evt.getX(), evt.getY()));
				calculer();
				repaint();
			}
		}
		else
		{
			if (numSelectedPoint != -1)
			{
				points.removeElementAt(numSelectedPoint);
				numSelectedPoint = getNumSelectedPoint(evt.getX(), evt.getY());
				calculer();
				repaint();
			}
		}
	}
	
	/** Le x et y du point numSelectedPoint est modifie si
	 * 	la souris change de position avec un bouton enfonce.
	 */
	public void mouseDragged(MouseEvent evt) {
		if (numSelectedPoint != -1)
		{
			points.elementAt(numSelectedPoint).x = evt.getX();
			points.elementAt(numSelectedPoint).y = evt.getY();
			calculer();
			repaint();
		}
	}
	
	/** Le numSelectedPoint est calcule si
	 * 	la souris change de position sans bouton enfonce.
	 */
	public void mouseMoved(MouseEvent evt) {
		numSelectedPoint = getNumSelectedPoint(evt.getX(), evt.getY());
		zoneSaisirPointsAfficherSegments.coordinates.setText("x: " + evt.getX() + " y: " + evt.getY());
		repaint();
	}
	
	/** Lance l'algorithme sur l'ensemble de points. */
	public void calculer()
	{
		Pair res = Algorithmes.algorithme1(points);
		segments = res.segments;
		circles = res.circles;
	}
	
	public void mouseReleased(MouseEvent evt) {}
	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent evt) {}
	public void mouseClicked(MouseEvent evt) {}
}
