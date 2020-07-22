

/**
 * Classe PrioritySearchTreeNode,
 * cette classe est la représentation d'un noeud d'un arbre de recherche à priorité .
 * (de manière générale peut-etre vu comme un arbre de recherche à priorité)
 * Elle comporte entre autre une clé (key) , et une valeur auxiliaire (aux).
 * @author Florent
 *
 */
public class PrioritySearchTreeNode{
	public Point key;
	public Point aux;
	public PrioritySearchTreeNode left;
	public PrioritySearchTreeNode right;	
	
	/**
	 * Construit un arbre de recherche a priorité réduit à une feuille vide.
	 */
	public PrioritySearchTreeNode(){
	}
	
	/**
	 * Retourne la taille de l'arbre.
	 * @return la taille de l'arbre
	 */
	public int getSize(){
		if(left == null && right ==null)
			return 1;
		else if(left ==null)
			return 1+ right.getSize();
		else if(right == null)
			return 1+ left.getSize();
		else
			return 1+ right.getSize()+left.getSize();
	}
	
	/**
	 * Retourne l'hauteur de l'arbre.
	 * @return l'hauteur de l'arbre
	 */
	public int getHeight(){
		if(left == null && right ==null)
			return 1;
		else if(left ==null)
			return 1+ right.getHeight();
		else if(right == null)
			return 1+ left.getHeight();
		else
			return 1+ Math.max(right.getHeight(),left.getHeight());
	}
	
	/**
	 * Redéfinition de la méthode toString()
	 */
	public String toString(){
		return ""+aux;
	}
	

	
}
