import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;	 

/**
 * Classe Preprocessing,
 * cette classe s'occupe de tous les prétraitements, ainsi que des méthodes utiles comme 
 * la recherche d'un minimum ou d'un maximum, la construction des arbres de recherche à priorité,
 * des méthodes d'interrogations de ces arbres,
 * lecture de données dans les fichiers, les tris de données.
 * 
 * @author Florent
 *
 */



public class Preprocessing {
	private ArrayList<Point> arraysorted;
	
	/**
	 * Permet de lire dans un fichier de données, et d'en extraire les coordonnées des points qui s'y trouvent, et
	 * de les ajouter à deux listes de points différentes données en paramètre.
	 * @param fichier - le nom du fichier
	 * @param horiPoints - la réference d'une liste de points destinée à contenir des points de segments horizontaux
	 * @param verPoints - la réference d'une liste de points destinée à contenir des points de segments verticaux
	 * @return le rectangle symbolisant la requête, donnée en premiere ligne de chaque fichier
	 */
	public static Rectangle2D.Double loadFile(String fichier,ArrayList<Point> horiPoints,ArrayList<Point> verPoints){
		Rectangle2D.Double retour = new Rectangle2D.Double();
		int isMinX;
		int isMaxX;
		int isMinY;
		int isMaxY;
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			StringTokenizer tokenizer;
			double x1;
			double y1;
			double x2;
			double y2;
			int i=0;
			while ((ligne=br.readLine())!=null){
				if(i==0){
					//traitement de la 1 ere ligne
					//enregistrement du range querie
					tokenizer = new StringTokenizer(ligne);
					double a = Double.parseDouble(tokenizer.nextToken());
					double b= Double.parseDouble(tokenizer.nextToken());
					double c = Double.parseDouble(tokenizer.nextToken());
					double d = Double.parseDouble(tokenizer.nextToken());
					retour = new Rectangle2D.Double(a,-d,b-a, d-c);
				}
				else{
					//traitement des autres lignes
					//stockage des points
					tokenizer = new StringTokenizer(ligne);
					x1 = Double.parseDouble(tokenizer.nextToken());
					y1 = Double.parseDouble(tokenizer.nextToken());
					x2 = Double.parseDouble(tokenizer.nextToken());
					y2 = Double.parseDouble(tokenizer.nextToken());
					Point a= new Point(x1,y1);
					Point b =new Point(x2,y2);
					a.addSegment(b);
					b.addSegment(a);
					Segment temp_s = a.getSegment(0);
					// Initialisation des max et min
					if(i==1){
							InterfaceGUI.XMAX = (int) temp_s.getEndPoint().getX() + 1000;
							InterfaceGUI.XMIN = (int) (temp_s.getFirstPoint().getX()) -1000;
							InterfaceGUI.YMAX = (int) (temp_s.getEndPoint().getY()) + 1000;
							InterfaceGUI.YMIN = (int)( temp_s.getEndPoint().getX()) -1000 ;
					}
					
					if(temp_s.isHorizontal()){
						horiPoints.add(a);
						horiPoints.add(b);
						
						// Maj des max et min hori
						isMaxX = (int) temp_s.getEndPoint().getX() + 1000;
						isMinX = (int) (temp_s.getFirstPoint().getX()) -1000;
						if(isMaxX>InterfaceGUI.XMAX)
							InterfaceGUI.XMAX = isMaxX;
						if(isMinX<InterfaceGUI.XMIN)
							InterfaceGUI.XMIN = isMinX;
						// Maj des max et min verti
						isMaxY = (int) temp_s.getFirstPoint().getY() + 1000;
						isMinY = (int) (temp_s.getEndPoint().getY()) -1000;
						if(isMaxY>InterfaceGUI.YMAX)
							InterfaceGUI.YMAX = isMaxY;
						if(isMinY<InterfaceGUI.YMIN)
							InterfaceGUI.YMIN = isMinY;
						
					}
					else{
						verPoints.add(a);
						verPoints.add(b);
						// Maj des max et min verti
						isMaxY = (int) temp_s.getFirstPoint().getY() + 1000;
						isMinY = (int) (temp_s.getEndPoint().getY()) -1000;
						if(isMaxY>InterfaceGUI.YMAX)
							InterfaceGUI.YMAX = isMaxY;
						if(isMinY<InterfaceGUI.YMIN)
							InterfaceGUI.YMIN = isMinY;
						// Maj des max et min hori
						isMaxX = (int) temp_s.getEndPoint().getX() + 1000;
						isMinX = (int) (temp_s.getFirstPoint().getX()) -1000;
						if(isMaxX>InterfaceGUI.XMAX)
							InterfaceGUI.XMAX = isMaxX;
						if(isMinX<InterfaceGUI.XMIN)
							InterfaceGUI.XMIN = isMinX;
					}
				}
				i++;
			}
			br.close(); 
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(null, "Erreur lors " +
					"de l'ouverture du fichier.");
		}
		return retour;
	
	}
	
	/**
	 * Algortihme QuickSort modifié,
	 * de telle sorte qu'il puisse trié de deux manières différentes.
	 * Il triera d'une part des points en fonction de leur abscisse,
	 * ou bien alors en fonction de leur ordonnée.
	 * @param from - l'indice du tableau du départ du tri
	 * @param to - l'indice du tableau de fin du tri
	 * @param b - boolean vrai pour un tri par rapport au abscisse, faux pour un tri sur les ordonnées.
	 */
	public void quickSort(int from,int to,boolean b ){
		if(from>=to)return;
		int p = partition(from,to,b);
		quickSort(from,p,b);
		quickSort(p+1,to,b);
	}
	
	public int partition(int from,int to,boolean b){
		Point pivot = arraysorted.get(from);
		int i = from -1;
		int j = to+1;
		while(i<j){
			if(b){
				i++;while(arraysorted.get(i).compareToX(pivot)==-1)
					i++;
				j--;while(arraysorted.get(j).compareToX(pivot)==1)
					j--;
				if(i<j) swap(i,j);
			}
			else{
				i++;while(arraysorted.get(i).compareToY(pivot)==-1)
					i++;
				j--;while(arraysorted.get(j).compareToY(pivot)==1)
					j--;
				if(i<j) swap(i,j);
				
			}
		}
		return j;
	}
	
	public void swap(int i, int j){
		Point temp = arraysorted.get(i);
		arraysorted.set(i, arraysorted.get(j)); 
		arraysorted.set(j, temp);
	}
	
	/**
	 * Permet de modifier le tableau que l'on désire trier.
	 * @param array - le tableau que l'on veut trier
	 */
	public void setArrayToSort(ArrayList<Point> array){
		arraysorted = array;
	}
	
	/**
	 * Permet d'appeller le tri du tableau par la méthode quickSort en spécifiant si le tri
	 * porte sur les abscisses ou les ordonnées des points du tableau.
	 * @param sortedbyx - vrai si l'ordre est sur les abscisses, faux pour trier en fonction des ordonnées
	 * @return le tableau trié
	 */
	public ArrayList<Point> getSortedByQS(boolean sortedbyx){
		quickSort(0,arraysorted.size()-1,sortedbyx);
		return arraysorted;
	}
	
	/**
	 * Retourne la position du point d'abscisse minimum d'un tableau de point.
	 * On utilise une fois encore la notion de nombre composite.
	 * @param array - le tableau duquel on cherche le minimum
	 * @return l'indice du point d'abscisse minimum
	 */
	public static int getMinX(ArrayList<Point> array){
		int aux= 0;
		Point min = array.get(0);
		for(int i =1; i<array.size();i++){
			if(min.compareToX(array.get(i))==1){
				min = array.get(i);
				aux = i;
			}
		}
		return aux;
	}

	/**
	 * Retourne la position du point d'abscisse minimum d'un tableau de point.
	 * On utilise une fois encore la notion de nombre composite.
	 * @param array - le tableau duquel on cherche le minimum
	 * @return l'indice du point d'abscisse minimum
	 */
	public static int getMinY(ArrayList<Point> array){
		int aux= 0;
		Point min = array.get(0);
		for(int i =1; i<array.size();i++){
			if(min.compareToY(array.get(i))==1){
				min = array.get(i);
				aux = i;
			}
		}
		return aux;
	}
	
	/**
	 * Copie un tableau dans un autre, jusqu'à un certain indice donné en paramètre.
	 * @param a - le tableau à partir duquel on va copier chacun des elements
	 * @param b - le tablau que l'on va remplir des copies
	 * @param median - l'indice d'arret de la copie ( rempli de l'indice 0 à l'indice median)
	 */
	public static void reportPointBelow(ArrayList<Point> a, ArrayList<Point> b,int median){
		for(int i=0;i<=median;i++)
			b.add(a.get(i));
	}
	
	/**
	 * Copie un tableau dans un autre, à partir d'un certain indice donné en paramètre.
	 * @param a -  le tableau à partir duquel on va copier chacun des elements
	 * @param b - le tablau que l'on va remplir des copies
	 * @param median - l'indice à partir duquel la copie commence ( rempli de l'indice median à la fin du tableau)
	 */
	public static void reportPointAbove(ArrayList<Point> a, ArrayList<Point> b,int median){
		for(int i=median+1;i<a.size();i++)		
			b.add(a.get(i));
	}
	
	/**Methode qui questionne un point p pour savoir si il se trouve dans le plan[x1:x2]x[y1:y2].
	 * 
	 * @param p - le point que l'on questionne
	 * @param x1 - borne inferieure de l'interval [x1:x2]
	 * @param x2 - borne superieure de l'interval [x1:x2]
	 * @param y1 - borne inferieure de l'interval [y1:y2]
	 * @param y2 - borne superieure de l'interval [y1:y2]
	 * @return boolean : vrai ou faux
	 */
	public static boolean lieInRange(Point p,double x1,double x2,double y1,double y2){
		double pX = p.getX();   
		double pY = p.getY();
		return ((pX>= x1) && (pX <=x2) && (y1 <= pY) && (y2 >=pY));
	}
		
	/**Creation d'un arbre de recherche a priorite a partir d'un tableau de points provenant de segments horizontaux.
	 * Cette arbre est construit de tel sorte à pouvoir répondre éfficacement à des requetes
	 * de la forme [-inf:x]x[y1:y2]
	 * @param array -le tableau de points
	 * @return root - la racine de l'arbre crée
	*/ 
	public static PrioritySearchTreeNode PrioritySearchTree(ArrayList<Point> array){
		PrioritySearchTreeNode root = new PrioritySearchTreeNode();
		if(array.size()==0) 
			return null;
		else if(array.size()==1){
			root.aux = array.get(0);
			root.key = root.aux.getCompositeY();
		}
		else{
			// calcul du Point d'x minimum
			int i_min =  getMinX(array); 
			root.aux = array.get(i_min);
			array.remove(i_min);
			// calcul de la median des y
			int i_median = (array.size()-1)/2;
			root.key = array.get(i_median).getCompositeY();
			// construction recursive avec les points restants
			ArrayList<Point> p_below = new ArrayList<Point>();
			ArrayList<Point> p_above = new ArrayList<Point>();
			reportPointBelow(array,p_below,i_median);
			reportPointAbove(array,p_above,i_median);
			root.left = PrioritySearchTree(p_below);
			root.right= PrioritySearchTree(p_above);
		}
		return root;
	}
	

	 
	 /**Methode qui va reporter tous les points de segments horizontaux
	 * d'un arbre de recherche a priorite, compris
	 * ou entre-coupant le plan [x1:x2]x[y1:y2], dans une liste.
	 * 
	 * @param t - l'arbre de recherche a priorite
	 * @param x1 - borne inferieure de l'interval [x1:x2]
	 * @param x2 - borne superieure de l'interval [x1:x2]
	 * @param y1 - borne inferieure de l'interval [y1:y2]
	 * @param y2 - borne superieure de l'interval [y1:y2]
	 * @return pointReported - la liste des points reportes
	 */
	 public static ArrayList<Point> queryPrioSearchTree(PrioritySearchTreeNode t,double x1,double x2,double y1,double y2
			 	){
		 double min = Double.MIN_VALUE;
		 double max = Double.MAX_VALUE;
		ArrayList<Point> pointReported = new ArrayList<Point>();
		// Necessaire d'avoir au prealable traiter les donnees.
		// Avoir acces aux elements min,max en x
		// Variables tampons
		PrioritySearchTreeNode splitNode = t;
		Point splitPoint= null;
		// Points composites en y
		Point compY1 = new Point(y1,min);
		Point compY2 = new Point(y2,max);
		// Recherche du noeud split
		while((splitNode != null) && 
				!((compY1.compareTo(splitNode.key)<=0) && (splitNode.key.compareTo(compY2) <= 0))){
			// Affectation du noeud a traiter
			splitPoint = splitNode.aux;
			if(lieInRange(splitPoint,x1,x2,y1,y2))
				pointReported.add(splitPoint);// Points avec extremite a l'interieur.
			else if(lieInRange(splitPoint,min,x1,y1,y2))
				if(splitNode.aux.getSegment(0).getEndPoint().getX()>x2)
					pointReported.add(splitNode.aux);// Points sans extremite a l'interieur.
			//rien ne sert de continuer la recherche
			if(splitPoint.getX()> x2)
				splitNode = null;
			// Continue recursivement a chercher le noeud split
			else if(splitNode.key.compareTo(compY1)>0)
				splitNode = splitNode.left;
			else
				splitNode = splitNode.right;
		}
		// Recherche a partir du noeud split
		if(splitNode!= null){
			// Noeud split trouve. Maj du tampon
			splitPoint = splitNode.aux;
			if(lieInRange(splitPoint,x1,x2,y1,y2))
				pointReported.add(splitPoint);// Points avec extremite a l'interieur.
			else if(lieInRange(splitPoint,min,x1,y1,y2))
				if(splitNode.aux.getSegment(0).getEndPoint().getX()>x2)
					pointReported.add(splitNode.aux);// Points sans extremite a l'interieur.
			// Parcourt gauche depuis le noeud split vers y1
			PrioritySearchTreeNode leftnode = splitNode.left;
			while(leftnode != null ){
				if(lieInRange(leftnode.aux,x1,x2,y1,y2))
					pointReported.add(leftnode.aux);// Points avec extremite a l'interieur.
				// Points sans extremite a l'interieur.
				else if(lieInRange(leftnode.aux,min,x1,y1,y2))
					if(leftnode.aux.getSegment(0).getEndPoint().getX()>x2)
						pointReported.add(leftnode.aux);
				// Continue l'exploration vers y1
				if(leftnode.key.compareTo(compY1)>= 0){
					reportInSubtree(leftnode.right,x1,x2,pointReported);
					leftnode = leftnode.left;
				}
				else
					leftnode = leftnode.right;
			}
			// Parcourt droit depuis le noeud split vers y2
			PrioritySearchTreeNode rightnode = splitNode.right;
			while(rightnode != null){
				if(lieInRange(rightnode.aux,x1,x2,y1,y2))
					pointReported.add(rightnode.aux);// Points avec extremite a l'interieur.
				// Points sans extremite a l'interieur.
				else if(lieInRange(rightnode.aux,min,x1,y1,y2))
					if(rightnode.aux.getSegment(0).getEndPoint().getX()>x2)
						pointReported.add(rightnode.aux);
				// Continue l'exploration vers y2
				if(rightnode.key.compareTo(compY2)<0){
					reportInSubtree(rightnode.left,x1,x2,pointReported);
					rightnode = rightnode.right;
				}
				else
					rightnode = rightnode.left;
			}
		}
		return pointReported;
	}

	/**Methode traitant de maniere recursive les sous arbres d'un arbre de recherche a priorite, tel que
	 * l'on reporte les points de segments horizontaux compris dans l'interval ou entre-coupant l'interval [x1:x2],
	 *  au depart d'un 'split' noeud, dans une liste.
	 * 
	 * @param t PrioritySearchTreeNode -la racine d'un arbre de recherche a priorite
	 * @param x1 - borne inferieure de l'interval [x1:x2]
	 * @param x2 - borne superieure de l'interval [x1:x2]
	 * @param pointReported ArrayList<Point> - réference de la liste auquelle on ajoutera les points a reporter
	 */
	 public static void reportInSubtree(PrioritySearchTreeNode t, double x1,double x2,ArrayList<Point> pointReported){
	if(t != null && t.aux.getX()<= x2){
		if(t.aux.getX()>= x1) // Points avec extremite a l'interieur.
			pointReported.add(t.aux);
		else // Points sans extremite a l'interieur.
			if(t.aux.getSegment(0).getEndPoint().getX()>x2)
				pointReported.add(t.aux);
		// On continue a traiter les sous arbres de maniere recursive.
		if(t.left!= null && t.right != null ){
		reportInSubtree(t.left,x1,x2,pointReported);
		reportInSubtree(t.right,x1,x2,pointReported);
		}
		else if(t.left != null)
			reportInSubtree(t.left,x1,x2,pointReported);
		else if(t.right != null)
			reportInSubtree(t.right,x1,x2,pointReported);
	}
}
	 
	 /**Creation d'un arbre de recherche a priorite a partir d'un tableau de points provenant de segments verticaux.
		 * Cette arbre est construit de tel sorte à pouvoir répondre éfficacement à des requetes
		 * de la forme [x1:x2]x[-inf:y2]
		 * @param array -le tableau de points
		 * @return root - la racine de l'arbre crée
		*/ 
	 public static PrioritySearchTreeNode PrioritySearchTreeII(ArrayList<Point> array){
		 	
			PrioritySearchTreeNode root;
			if(array.size()==0) 
				return null;
			else if(array.size()==1){
				root = new PrioritySearchTreeNode();
				root.aux = array.get(0);
				root.key = root.aux.getCompositeX();
			}
			else{
				root = new PrioritySearchTreeNode();
				// calcul du Point d'x minimum
				int i_min =  getMinY(array); 
				root.aux = array.get(i_min);
				array.remove(i_min);
				// calcul de la median des y
				int i_median = (array.size()-1)/2;
				root.key = array.get(i_median).getCompositeX();
				// construction recursive avec les points restants
				ArrayList<Point> p_below = new ArrayList<Point>();
				ArrayList<Point> p_above = new ArrayList<Point>();
				reportPointBelow(array,p_below,i_median);
				reportPointAbove(array,p_above,i_median);
				root.left = PrioritySearchTreeII(p_below);
				root.right= PrioritySearchTreeII(p_above);
			}
			return root;
		}
		 
	 /**Methode qui va reporter tous les points de segments verticaux
		 * d'un arbre de recherche a priorite, compris
		 * ou entre-coupant le plan [x1:x2]x[y1:y2], dans une liste.
		 * 
		 * @param t - l'arbre de recherche a priorite
		 * @param x1 - borne inferieure de l'interval [x1:x2]
		 * @param x2 - borne superieure de l'interval [x1:x2]
		 * @param y1 - borne inferieure de l'interval [y1:y2]
		 * @param y2 - borne superieure de l'interval [y1:y2]
		 * @return pointReported - la liste des points à reporter
		 */
	 public static ArrayList<Point> queryPrioSearchTreeII(PrioritySearchTreeNode T,double x1,double x2,double y1,double y2
			 	){
		 double min = Double.MIN_VALUE;
		 double max = Double.MAX_VALUE;
			ArrayList<Point> pointReported = new ArrayList<Point>();
			// Necessaire d'avoir au prealable traiter les donnees.
			// Avoir acces aux elements min,max en y
			// Variables tampons
			PrioritySearchTreeNode splitNode = T;
			Point splitPoint = null;
			// Points composites en x
			Point compX1 = new Point(x1,min);
			Point compX2 = new Point(x2,max);
			// Recherche du noeud split
			while(splitNode != null && 
					!((compX1.compareTo(splitNode.key)<=0) && (splitNode.key.compareTo(compX2) <= 0))){
				// Affectation du noeud a traiter
				splitPoint = splitNode.aux;
				if(lieInRange(splitPoint,x1,x2,y1,y2))
					pointReported.add(splitPoint);// Points avec extremite a l'interieur.
				else if(lieInRange(splitPoint,x1,x2,min,y1))
					if(splitNode.aux.getSegment(0).getEndPoint().getY()>y2)
						pointReported.add(splitNode.aux);// Points sans extremite a l'interieur.
				//rien ne sert de continuer la recherche
				if(splitPoint.getY()> y2)
					splitNode = null;
				// Continue recursivement a chercher le noeud split
				else if(splitNode.key.compareTo(compX1)>0)
					splitNode = splitNode.left;
				else
					splitNode = splitNode.right;
			}
			
			// Recherche a partir du noeud split
			if(splitNode!= null){
				// Noeud split trouve. Maj du tampon
				splitPoint = splitNode.aux;
				if(lieInRange(splitPoint,x1,x2,y1,y2))
					pointReported.add(splitPoint);// Points avec extremite a l'interieur.
				else if(lieInRange(splitPoint,x1,x2,min,y1))
					if(splitNode.aux.getSegment(0).getEndPoint().getY()>y2)
						pointReported.add(splitNode.aux);// Points sans extremite a l'interieur.
				// Parcourt gauche depuis le noeud split vers y1
				PrioritySearchTreeNode leftnode = splitNode.left;
				while(leftnode != null ){
					if(lieInRange(leftnode.aux,x1,x2,y1,y2))
						pointReported.add(leftnode.aux);// Points avec extremite a l'interieur.
					// Points sans extremite a l'interieur.
					else if(lieInRange(leftnode.aux,x1,x2,min,y1))
						if(leftnode.aux.getSegment(0).getEndPoint().getY()>y2)
							pointReported.add(leftnode.aux);
					// Continue l'exploration vers y1
					if(leftnode.key.compareTo(compX1)>= 0){
						reportInSubtreeII(leftnode.right,y1,y2,pointReported);
						leftnode = leftnode.left;
					}
					else
						leftnode = leftnode.right;
				}
				// Parcourt droit depuis le noeud split vers y2
				PrioritySearchTreeNode rightnode = splitNode.right;
				while(rightnode != null){
					if(lieInRange(rightnode.aux,x1,x2,y1,y2))
						pointReported.add(rightnode.aux);// Points avec extremite a l'interieur.
					// Points sans extremite a l'interieur.
					else if(lieInRange(rightnode.aux,x1,x2,min,y1))
						if(rightnode.aux.getSegment(0).getEndPoint().getY()>y2)
							pointReported.add(rightnode.aux);
					// Continue l'exploration vers y2
					if(rightnode.key.compareTo(compX2)<0){
						reportInSubtreeII(rightnode.left,y1,y2,pointReported);
						rightnode = rightnode.right;
					}
					else
						rightnode = rightnode.left;
				}
			}
			return pointReported;
		}

		/**Methode traitant de maniere recursive les sous arbres d'un arbre de recherche a priorite, tel que
		 * l'on reporte les points de segments horizontaux compris dans l'interval ou entre-coupant l'interval [y1:y2],
		 *  au depart d'un 'split' noeud.
		 * 
		 * @param t PrioritySearchTreeNode -la racine d'un arbre de recherche a priorite
		 * @param y1 - borne inferieure de l'interval [y1:y2]
		 * @param y2 - borne superieure de l'interval [y1:y2]
		 * @param pointReported ArrayList<Point> - réference de la liste auquelle on ajoutera les points a reporter
		 */
		 public static void reportInSubtreeII(PrioritySearchTreeNode t, double y1,double y2,ArrayList<Point> pointReported){
		if(t != null && t.aux.getY()<= y2){
			if(t.aux.getY()>= y1) // Points avec extremite a l'interieur.
				pointReported.add(t.aux);
			else // Points sans extremite a l'interieur.
				if(t.aux.getSegment(0).getEndPoint().getY()>y2)
					pointReported.add(t.aux);
			// On continue a traiter les sous arbres de maniere recursive.
			if(t.left!= null && t.right != null ){
			reportInSubtreeII(t.left,y1,y2,pointReported);
			reportInSubtreeII(t.right,y1,y2,pointReported);
			}
			else if(t.left != null)
				reportInSubtreeII(t.left,y1,y2,pointReported);
			else if(t.right != null)
				reportInSubtreeII(t.right,y1,y2,pointReported);
		}
	}
	 
}