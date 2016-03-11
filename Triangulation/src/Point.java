/** La classe Point. */
public class Point implements Comparable
{	
	/** Enum pour la Libelle pour indique si l'angle est < ou >= 180°C*/
	public static enum Label { 
		INFERIEUR("I"),
		SUPERIEUR("S"),
		INDETERMINE("-");
		
		private String s;
		Label(String s) {
			this.s = s;
		}
		public String toString() {
			return s;
		}
	};
	/** La valeur de x. */
	public double x;
	
	/** La valeur de y. */
	public double y;
	
	public boolean isRight = true;
	
	/** position du point dans l'ecran **/
	public int pos;
	/** libelle pour indiquer si l'angle est < ou >= 180C°*/
	public Label angle = Label.INDETERMINE;
	
	/** Constructeur avec initialisation de x et y. */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/** Constructeur sans initialisation. */
	public Point(){}

	@Override
	public int compareTo(Object arg0) {
		
		return 0;
	}
}