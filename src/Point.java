import java.util.ArrayList;
/**
 * Classe Point,
 * cette classe permet de créer un point dans un espace à deux dimensions,
 * de plus elle permet d'associer à ce point un ensemble d'autres points qui
 * seront maintenu sous la forme d'une liste de segments associés à ce point.
 * 
 * @author Ghilain Florent & Ndizanse Jean-Paul
 */

public class Point implements Comparable<Point>  {
	private ArrayList<Segment> mySegments= new ArrayList<Segment>();
	private double x;
	private double y;
	
	/**
	 * Construit une instance d'objet Point dont les coordonnées dans un espace cartésien
	 * à deux dimension sont données par les paramètres x et y.
	 * @param x - l'abscisse du point
	 * @param y - l'ordonnée du point
	 */
	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Permet d'ajouter un segment à la liste des segments déja associés au point.
	 * @param p - le point qui deviendra une extremité du segment ajouté
	 */
	public void addSegment(Point p){
		Segment s = new Segment(this,p);
		mySegments.add(s);
	}
	
	/**
	 * Retourne l'ensemble des segments associés au point.
	 * @return une liste de segments
	 */
	public ArrayList<Segment> getAllSegment(){
		return mySegments;
	}
	
	/**
	 * Retourne un segment particulier de la liste des segments associés au point,
	 * sur base d'un indice de position.
	 * @param i - l'indice de position dans la liste du segment à retourner
	 * @return un segment
	 */
	public Segment getSegment(int i){
		return this.mySegments.get(i);
	}
	
	/**
	 * Retourne l'abscisse du point.
	 * @return un double représentant l'abscise du point
	 */
	public double getX(){
		return x;
	}
		
	/**
	 * Retourne l'ordonnée du point.
	 * @return un double représentant l'ordonnée du point
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Retourne l'ordonnée du point composite du point.
	 * @return un point étant l'ordonnée du point composite 
	 */
	public Point getCompositeX(){
		return new Point(x,y);
	}
	
	/**
	 * Retourne l'abscisse du point composite du point.
	 * @return un point étant l'abscisse du point composite
	 */
	public Point getCompositeY(){
		return new Point(y,x);
	}
	
	/**
	 * Redéfinition de la méthode compareTo() basé sur la notion
	 * de nombre composite.
	 */
	public int compareTo(Point b){
		if(this.x < b.getX() || ( this.x == b.getX() && this.y<b.getY()))
			return -1;
		else if(this.x> b.getX() || (this.x == b.getX() && this.y>b.getY()))
			return 1;	
		else		
			return 0;
	}
	
	/**	
	 * Permet de comparer deux points par rapport à leur abscisse,
	 * suivant la notion de nombre composite.
	 */
	public int compareToX(Point b){
		if(this.x < b.getX() || ( this.x == b.getX() && this.y<b.getY()))
			return -1;
		else if(this.x> b.getX() || (this.x == b.getX() && this.y>b.getY()))
			return 1;	
		else		
			return 0;
	}
	
	/**	
	 * Permet de comparer deux points par rapport à leur ordonnée,
	 * suivant la notion de nombre composite.
	 */
	public int compareToY(Point b){
		if(this.y < b.getY() || ( this.y == b.getY() && this.x<b.getX()))
			return -1;
		else if(this.y> b.getY() || (this.y == b.getY() && this.x>b.getX()))
			return 1;	
		else		
			return 0;
	}
		
	/**
	 * Redéfinition de la méthode toStrign()
	 */
	public String toString(){
		return "("+x+","+y+")";
	}
	

	
}
