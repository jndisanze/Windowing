/**
 * Classe Segment,
 * elle permet de modéliser un segment dans un espace cartésien à deux dimensions,
 * c'est à a dire qu'elle sera construite à partir de deux points, et offrira
 * a l'utilisateur certaines informations sur ce segment.
 * @author Ghilain Florent && NDisanze Jean-Paul
 *
 */
public class Segment {

	private Point firstPoint;
	private Point endPoint;
	
	/**
	 * Construit un segment à partir de deux points.
	 * @param a - un point d'extremité du segment
	 * @param b - l'autre point d'extremité du segment
	 */
	public Segment(Point a,Point b){
		firstPoint = a;
		endPoint = b;
		// Traitement
		if(isHorizontal() && a.getX()>b.getX()){
			firstPoint = b;
			endPoint = a;
		}
		if(isVertical() && a.getY()>b.getY()){
			firstPoint = b;
			endPoint = a;
		}
	}
	
	/**
	 * Retourne le point du segment ayant la plus petite abscisse, si
	 * le segment est horizontal, ou ayant la plus petite ordonnée, si il est
	 * vertical.
	 * @return un point
	 */
	public Point getFirstPoint(){
		return firstPoint;
	}
	
	/**
	 * Retourne le point du segment ayant la plus grande abscisse, si le segment
	 * est horizontal, ou ayant la plus grande ordonnée, si il est vertical.
	 * @return un point
	 */
	public Point getEndPoint(){
		return endPoint;
	}
	
	/**
	 * Retourne si il s'agit d'un segment horizontal.
	 * @return oui si le segment est horizontal, non autrement
	 */
	public boolean isHorizontal(){
		return firstPoint.getY() == endPoint.getY();
	}
	
	/**
	 * Retourne si il s'agit d'un segment vertical.
	 * @return oui si le segment est vertical, non autrement
	 */
	public boolean isVertical(){
		return firstPoint.getX() == endPoint.getX();
	}
	
	/**
	 * Retourne si le segment intersecte une ligne horizontale.
	 * @param y - l'ordonnée de la ligne à intersecter
	 * @return oui si le si le segment coupe la ligne, non autrement
	 */
	public boolean intersectHLine(int y){
		return (firstPoint.getY()<=y && endPoint.getY()>=y);
	}
	
	/**
	 * Retourne si le segment intersecte une ligne verticale.
	 * @param x - l'abscisse de la ligne à intersecter
	 * @return oui si le segment coupe la ligne, non autrement
	 */
	public boolean intersectVLine(int x){
		return (firstPoint.getX()<=x && endPoint.getX()>=x);
	}
	
	/**
	 * Redéfinition de la méthode toString()
	 */
	public String toString(){
		return firstPoint + " \t" + endPoint ;
	}
	
	
}
