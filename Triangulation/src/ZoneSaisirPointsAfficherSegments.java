import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** La classe ZoneSaisirPointsAfficherSegments. */
public class ZoneSaisirPointsAfficherSegments extends JPanel  {

	/** Creation de la zone d'affichage. */
	public ZoneSaisirPointsAfficherSegments()
	{
		// Le canvas d'affichage
		final CanvasSaisirPointsAfficherSegments canvas = new CanvasSaisirPointsAfficherSegments(); 
		
		// Panel des boutons
		JPanel panelBoutons = new JPanel();
		
		
		// Creation du bouton Rand
		final JTextField textNombrePoint = new JTextField("50");
		textNombrePoint.setColumns(5);
		
		// Creation du bouton Effacer
		JButton effacer = new JButton("Effacer");
		
		// Action du bouton Effacer
		effacer.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					// Suppression des points et des segments
					canvas.points.removeAllElements();
					canvas.segments.removeAllElements();
					canvas.repaint();
				}
			}
		);
		// Creation du checkbox showAngle
		JCheckBox showAngle = new JCheckBox("ShowAngle");
		// Action du checkbox showAngle
		showAngle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				AbstractButton abstractButton = (AbstractButton) evt.getSource();
		        boolean selected = abstractButton.getModel().isSelected();
		        if (selected) {
		        	canvas.showAngle = true;
		        }
		        else {
		        	canvas.showAngle = false;
		        }
		        repaint();
				
			}
			
		});
		// Creation 
		JCheckBox showNumber = new JCheckBox("ShowNumber");
		// Action du checkbox showNumber
		showNumber.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				AbstractButton abstractButton = (AbstractButton) evt.getSource();
		        boolean selected = abstractButton.getModel().isSelected();
		        if (selected) {
		        	canvas.showNumber = true;
		        }
		        else {
		        	canvas.showNumber = false;
		        }
		        repaint();
		        
			}
		});
		
		// Creation du Checkbox showLeft/Right
		JCheckBox showLeftRight = new JCheckBox("Show:L/R");
		//Action du checkbox showLeft/Right
		showLeftRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AbstractButton abstractButton = (AbstractButton) evt.getSource();
		        boolean selected = abstractButton.getModel().isSelected();
		        if (selected) {
		        	canvas.showLeftRight = true;
		        }
		        else {
		        	canvas.showLeftRight = false;
		        }
		        repaint();
		        
			}
		});
		// Creation du bouton Rand
		JButton rand = new JButton("Rand");
		
		// Action du bouton Rand
		rand.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					// Suppression des points et des segments
					canvas.points.removeAllElements();
					canvas.segments.removeAllElements();
					int n = Integer.parseInt(textNombrePoint.getText());
					for (int i = 0; i < n; i++)
					{
						canvas.points.addElement(
							new Point(
								2 + Algorithmes.rand(canvas.getWidth()-4),
								2 + Algorithmes.rand(canvas.getHeight()-4)
							)
						);
					}
					canvas.calculer();
					canvas.repaint();
				}
			}
		);
		
		// Ajout des boutons au panel panelBoutons
		panelBoutons.add(effacer);
		panelBoutons.add(rand);
		panelBoutons.add(textNombrePoint);
		panelBoutons.add(showLeftRight);
		panelBoutons.add(showNumber);
		panelBoutons.add(showAngle);
		setLayout(new BorderLayout());
		
		// Ajout du canvas au centre
		add(canvas, BorderLayout.CENTER);
		
		// Ajout des boutons au nord
		add(panelBoutons, BorderLayout.SOUTH);
	}
}

/** La classe CanvasSaisirPointsAfficherSegments. */
class CanvasSaisirPointsAfficherSegments extends JPanel implements MouseListener, MouseMotionListener {
	/** La liste des points affiches. */
	Vector<Point> points;
	
	/** La liste des segments affiches. */
	Vector<Segment> segments;
	
	/** Le numero du point selectionne. */
	private int numSelectedPoint;
	
	/** La couleur d'un point a l'ecran. */
	private final Color pointColor = Color.GRAY;
	
	/** La couleur d'un diagonale a l'ecran. */
	private final Color diagonaleColor = Color.RED;
	
	/** La couleur d'un segment a l'ecran. */
	private final Color segmentColor = Color.BLUE;
	
	/** La couleur d'un point selectionne a l'ecran. */
	private final Color selectedPointColor = Color.RED;

	/** La taille d'un point a l'ecran. */
	private final int POINT_SIZE = 2;
	
	/** boolean si l'information gauche/droite devrait etre afficher */
	boolean showLeftRight = false;
	
	/** boolean si l'information numero du point devrait etre afficher */
	boolean showNumber = false;
	
	/** boolean si l'information Angle resp Superieur/Inferieur devrait etre afficher */
	boolean showAngle = false;
	/** Creation de la zone d'affichage. */
	public CanvasSaisirPointsAfficherSegments()
	{
		
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
			if (showNumber)
				drawPointNumber(g,p);
			if (showLeftRight)
				drawPointLeft_Right(g, p);
			if (showAngle)
				drawPointAngle(g, p);
			g.fillOval((int)(p.x - POINT_SIZE), (int)(p.y - POINT_SIZE), 2 * POINT_SIZE + 1, 2 * POINT_SIZE + 1);
			g.drawOval((int)(p.x - 2 * POINT_SIZE), (int)(p.y - 2 * POINT_SIZE), 2 * 2 * POINT_SIZE,	2 * 2 * POINT_SIZE);
		}
	}
	private void drawPointAngle(Graphics g, Point p) {
		g.drawString(p.angle+"", (int)p.x - 13*POINT_SIZE, (int)p.y - 3*POINT_SIZE);
	}
	private void drawPointNumber(Graphics g, Point p) {
		g.drawString(p.pos+"", (int)p.x - 3*POINT_SIZE, (int)p.y - 3*POINT_SIZE);
	}
	private void drawPointLeft_Right(Graphics g, Point p) {
		if (p.isRight) {
			g.drawString("D", (int) p.x - 8*POINT_SIZE, (int)p.y - 3*POINT_SIZE);
		} else {
			g.drawString("G", (int) p.x - 8*POINT_SIZE, (int)p.y - 3*POINT_SIZE);
		}
	}
	/** Affichage des segments.
	 */
	private void drawSegments(Graphics g) {
		for (int n = 0; n < segments.size(); n++) {
			Segment segment = segments.elementAt(n);
			if (segment.isDiagonale)
				g.setColor(diagonaleColor);
			else
				g.setColor(segmentColor);
			g.drawLine((int)segment.a.x,(int)segment.a.y,(int)segment.b.x,(int)segment.b.y);
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
		repaint();
	}
	
	/** Lance l'algorithme sur l'ensemble de points. */
	public void calculer()
	{
		segments = Algorithmes.algorithme1(points);
	}
	
	public void mouseReleased(MouseEvent evt) {}
	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent evt) {}
	public void mouseClicked(MouseEvent evt) {}
}
