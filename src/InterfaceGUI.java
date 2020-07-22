import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;




@SuppressWarnings("serial")
public class InterfaceGUI extends JFrame{
	// Tableau des points du fichier.
	private ArrayList<Point> pointsHori = new ArrayList<Point>();
	private ArrayList<Point> pointsVert = new ArrayList<Point>();
	private ArrayList<Point> pointsHoriCopy = new ArrayList<Point>();
	private ArrayList<Point> pointsVertCopy = new ArrayList<Point>();
	// Tableau des points reportes apres requete.
	private ArrayList<Point> pointsHoriReported = new ArrayList<Point>();
	private ArrayList<Point> pointsVertReported = new ArrayList<Point>();
	// Requete querie
	private Rectangle2D.Double rectangleRange = new Rectangle2D.Double();
	// Arbres de recherche a priorite. Hori et Verti
	private PrioritySearchTreeNode treeHori = new PrioritySearchTreeNode();
	private PrioritySearchTreeNode treeVert = new PrioritySearchTreeNode();
	// Objet de pretraitement
	Preprocessing preprocessing = new Preprocessing();
	// Composants graphiques
	private Windowing window;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu menuZoom;
	private JMenuItem item_1;
	private JMenuItem item_2;
	private JMenuItem item_3;
	private JFrame frame;
	private double scale = 1.;
	private JMenuItem item_zoom_2;
	private JMenuItem item_zoom_1;
	// Instance servant au rendu graphique 
	private int x_translate = 0;
	private int y_translate = 0;
	private boolean newrequete = true;
	private boolean displayquerie = false;
	private boolean retouchegraphique = true;
	public static int XMAX;
	public static int YMAX;
	public static int XMIN;
	public static int YMIN;
	// Police graphique
	private  Font font = new Font("courier", Font.PLAIN,25);
	// Instance servant au design singleton pour la fenetre de creation de requete.
	private JPanel instancePanel;
	//
	private JFrame reportedSegment;
	
	public InterfaceGUI(){
		frame = this;
		frame.setBackground(Color.white);
		window = new Windowing(getToolkit().getScreenSize());
		frame.getContentPane().add(window);
		frame.setSize(frame.getToolkit().getScreenSize());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void initialize(){
		menuBar =  new JMenuBar();
		menu = new JMenu("Fichier");
		menuZoom = new JMenu("Affichage");
		item_1 = new JMenuItem("Charger un fichier de données");
		item_2 = new JMenuItem("Effectuer une nouvelle requete");
		item_3 = new JMenuItem("Quitter l'application");
		item_zoom_1 = new JMenuItem("Zoom avant");
		item_zoom_2 = new JMenuItem("Zoom arriere");
		item_1_M();
		item_2_M();
		item_3_M();
		itemZoom_1_M();
		itemZoom_2_M();
		
		menuBar.add(menu);
		menuBar.add(menuZoom);
		setJMenuBar(menuBar);
	}
	public void item_1_M(){
		menu.add(item_1);
		item_1.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				pointsHori = new ArrayList<Point>();
				pointsVert = new ArrayList<Point>();
				String fichier= "";
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) 
			    	fichier = fichier + chooser.getSelectedFile().getAbsolutePath();
			    rectangleRange = Preprocessing.loadFile(fichier, pointsHori,pointsVert);
			    newrequete = true;
			    frame.repaint();
			    
			    
			    // Tri sur Y
			    preprocessing.setArrayToSort(pointsHori);
			    preprocessing.getSortedByQS(false);
			   // Tri sur X
			    preprocessing.setArrayToSort(pointsVert);
			    preprocessing.getSortedByQS(true);
			    pointsHoriCopy = (ArrayList<Point>) pointsHori.clone();
			    pointsVertCopy = (ArrayList<Point>) pointsVert.clone();
			    
			    treeHori = Preprocessing.PrioritySearchTree(pointsHoriCopy);
			    treeVert =  Preprocessing.PrioritySearchTreeII(pointsVertCopy);
			}});}
	
	public void item_2_M(){
		menu.add(item_2);
		item_2.addActionListener(new ActionListener(){
			

			public void actionPerformed(ActionEvent arg0) {
				newrequete = true;
				frame.repaint();
				JFrame pref_frame = new JFrame();
				pref_frame.setTitle("Creation d'une requete");
				pref_frame.setSize(new Dimension(420,440));
				pref_frame.setResizable(false);
				pref_frame.setLocationRelativeTo(null);              
						        
		        //On lie notre panel de requete a la fenetre de creation de requete.
				if(instancePanel==null)
					instancePanel = new RequetePanel();
		        pref_frame.setContentPane(instancePanel);
		        pref_frame.setVisible(true);
		       }				
		});
	}
	
	public void item_3_M(){
		menu.add(item_3);
		item_3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
			}
		});
	}
	
	public void itemZoom_1_M(){
		menuZoom.add(item_zoom_1);
		item_zoom_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				scale = scale + 0.3;
				frame.repaint();
			}});
	}
	
	public void itemZoom_2_M(){
		menuZoom.add(item_zoom_2);
		item_zoom_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				scale = scale - 0.3;
				frame.repaint();
			}});
	}
	
	class RequetePanel extends JPanel{
		private JButton fix_querie;
		private JButton compute_querie;
		private CheckboxGroup groupeRadio;
		private Checkbox interval_1;
		private Checkbox interval_2;
		private Checkbox interval_3;
		private Checkbox interval_4;
		private Checkbox interval_5;
		private Checkbox querie;
		private JPanel panel;
		private JPanel inter_1;
		private JPanel inter_2;
		private JPanel inter_3;
		private JPanel inter_4;
		private JPanel inter_5;
		private JPanel label_p;
		private JTextField x1_text;
		private JTextField x2_text;
		private JTextField y1_text;
		private JTextField y2_text;
		private String inf;
		private Checkbox rendugraphique;
		
		public RequetePanel(){
			panel = this;
			panel.setBackground(Color.white);
			panel.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			// Petit label
			JLabel label = new JLabel("Choisissez votre type de requete.");
			label_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			label_p.add(label);
			label_p.setBackground(Color.white);
			
			DecimalFormatSymbols symbol = new DecimalFormatSymbols();
			inf = symbol.getInfinity();
						
			groupeRadio = new CheckboxGroup();
			// Classe implementant l'ecouteur de bouton radio
			class Radio implements  ItemListener{
				public void itemStateChanged(ItemEvent arg0) {
					Checkbox temp_c = (Checkbox)arg0.getSource();
					x1_text.setText("");
					x2_text.setText("");
					y1_text.setText("");
					y2_text.setText("");
					if(temp_c.equals(interval_2))
						y2_text.setText("+"+inf);
					else if(temp_c.equals(interval_3))
						y1_text.setText("-"+inf);
					else if(temp_c.equals(interval_4))
						x2_text.setText("+"+inf);
					else if(temp_c.equals(interval_5))
						x1_text.setText("-"+inf);
				}
			}
			Radio ecouteradio = new Radio();
			interval_1 = new Checkbox("[x:x']x[y:y']",groupeRadio,true);
			interval_1.addItemListener(ecouteradio);
			interval_2 = new Checkbox("[x:x']x[y:+"+inf+"[",groupeRadio,false);
			interval_2.addItemListener(ecouteradio);
			interval_3 = new Checkbox("[x:x']x]-"+inf+":y']",groupeRadio,false);
			interval_3.addItemListener(ecouteradio);
			interval_4 = new Checkbox("[x:"+inf+"[x[y:y']",groupeRadio,false);
			interval_4.addItemListener(ecouteradio);
			interval_5 = new Checkbox("]-"+inf+":x']x[y:y']",groupeRadio,false);
			interval_5.addItemListener(ecouteradio);
			
			// Declaration et ecouteur de la case � cocher retouche graphique.
			rendugraphique = new Checkbox("Retouche graphique");
			rendugraphique.setState(retouchegraphique);
			rendugraphique.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent arg0) {
					boolean display = (arg0.getStateChange() == ItemEvent.SELECTED);
					if(display)
						retouchegraphique = true;
					else
						retouchegraphique = false;
					frame.repaint();
				}
			});
			// Ecouteur du bouton Affichage de l'interrogation.
			querie = new Checkbox("Affichage de l'interrogation");
			querie.setState(displayquerie);
			querie.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent arg0) {
					boolean display = (arg0.getStateChange() == ItemEvent.SELECTED);
					if(display)
						displayquerie = true;
					else
						displayquerie = false;
					frame.repaint();
				}
			});
			//Declaration des Labels et Panels initialisant une requete.
			inter_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			inter_1.add(interval_1);
			inter_1.setBackground(Color.white);
			// Champ de la requete
			JLabel x1 = new JLabel(" x= ");
			x1_text = new JTextField(3);
			x1_text.setText(""+rectangleRange.getMinX());
			//
			JLabel x2 = new JLabel(" x'= ");
			x2_text = new JTextField(3);
			x2_text.setText(""+rectangleRange.getMaxX());
			//
			JLabel y1 = new JLabel(" y= ");
			y1_text = new JTextField(3);
			y1_text.setText(""+-rectangleRange.getMaxY());	
			//
			JLabel y2 = new JLabel(" y'= ");
			y2_text = new JTextField(3);
			y2_text.setText(""+-rectangleRange.getMinY());
			//
			inter_1.add(x1);
			inter_1.add(x1_text);
			inter_1.add(x2);
			inter_1.add(x2_text);
			inter_1.add(y1);
			inter_1.add(y1_text);
			inter_1.add(y2);
			inter_1.add(y2_text);
			// Declaration des panels.
			inter_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			inter_2.add(interval_2);
			inter_2.setBackground(Color.white);
			inter_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			inter_3.add(interval_3);
			inter_3.setBackground(Color.white);
			inter_4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			inter_4.add(interval_4);
			inter_4.setBackground(Color.white);
			inter_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			inter_5.add(interval_5);
			inter_5.setBackground(Color.white);
			JPanel queriepanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			queriepanel.add(querie);
			queriepanel.setBackground(Color.white);
			// boutton querie et ecouteur des boutons radios asoocies.
			fix_querie = new JButton("Fixer une nouvelle requete");
			fix_querie.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					newrequete = true;
					frame.repaint();
					try{
						if(interval_1.getState()){
							double a = Double.parseDouble(x1_text.getText());
							double b = Double.parseDouble(x2_text.getText());
							double c = Double.parseDouble(y1_text.getText());
							double d = Double.parseDouble(y2_text.getText());
							rectangleRange = new Rectangle2D.Double(a,-d,b-a,d-c);	
						}
						else if(interval_2.getState()){
							if(y2_text.getText().equals("+"+inf)){
								double a = Double.parseDouble(x1_text.getText());
								double b = Double.parseDouble(x2_text.getText());
								double c =Double.parseDouble(y1_text.getText());
								rectangleRange = new Rectangle2D.Double(a,-YMAX,b-a,YMAX-c);
							}
							else
								JOptionPane.showMessageDialog(null, "Veuillez n'utiliser que des chiffres, et" +
								" selectionner le bon type de requete.");
						}	
						else if(interval_3.getState()){
							if(y1_text.getText().equals("-"+inf)){
								double a = Double.parseDouble(x1_text.getText());
								double b = Double.parseDouble(x2_text.getText());
								double d = Double.parseDouble(y2_text.getText());
								rectangleRange = new Rectangle2D.Double(a,-d,b-a,d-YMIN);	
							}
							else
								JOptionPane.showMessageDialog(null, "Veuillez n'utiliser que des chiffres, et" +
								" selectionner le bon type de requete.");
						}	
						else if(interval_4.getState()){
							if(x2_text.getText().equals("+"+inf)){
								double a = Double.parseDouble(x1_text.getText());
								double c = Double.parseDouble(y1_text.getText());
								double d = Double.parseDouble(y2_text.getText());
								rectangleRange = new Rectangle2D.Double(a,-d,XMAX-a,d-c);	
							}
							else
								JOptionPane.showMessageDialog(null, "Veuillez n'utiliser que des chiffres, et" +
								" selectionner le bon type de requete.");
						}	
						else if(interval_5.getState()){
							if(x1_text.getText().equals("-"+inf)){
								double b = Double.parseDouble(x2_text.getText());
								double c = Double.parseDouble(y1_text.getText());
								double d = Double.parseDouble(y2_text.getText());
								rectangleRange = new Rectangle2D.Double(XMIN,-d,b-XMIN,d-c);	
							}
							else
								JOptionPane.showMessageDialog(null, "Veuillez n'utiliser que des chiffres, et" +
								" selectionner le bon type de requete.");
						}	
					}
					catch(Exception e){
						JOptionPane.showMessageDialog(null, "Veuillez n'utiliser que des chiffres, et" +
								" selectionner le bon type de requete.");
					}
				}
			});
			// Declaration et ecouteur du bouton Windowing
			compute_querie = new JButton("Windowing");
			compute_querie.addActionListener(new ActionListener(){
			

				public void actionPerformed(ActionEvent arg0) {
					newrequete = false;
					pointsHoriReported =Preprocessing.queryPrioSearchTree(treeHori, rectangleRange.getMinX(),
							rectangleRange.getMaxX(),-rectangleRange.getMaxY(),-rectangleRange.getMinY());
					pointsVertReported =Preprocessing.queryPrioSearchTreeII(treeVert, rectangleRange.getMinX(),
							rectangleRange.getMaxX(),-rectangleRange.getMaxY(),-rectangleRange.getMinY());
					frame.repaint();
					// fenetre des segments reportés
					if(reportedSegment == null){
						reportedSegment =new ReportedSegment(pointsHoriReported,pointsVertReported);
						reportedSegment.setVisible(true);
					}
					else{
						reportedSegment.dispose();
						reportedSegment =new ReportedSegment(pointsHoriReported,pointsVertReported);
						reportedSegment.setVisible(true);
					}
				
				}	
			});
			// Ajout des bouttons et d'une case a cocher
			queriepanel.add(fix_querie);
			queriepanel.add(rendugraphique);
			queriepanel.add(compute_querie);
			// Ajout des differents panels.
			panel.add(label_p);
			panel.add(inter_1);
			panel.add(inter_2);
			panel.add(inter_3);
			panel.add(inter_4);
			panel.add(inter_5);
			panel.add(queriepanel);
		}
	}

	class ReportedSegment extends JFrame{
		// Permet de créer une fenetre ou l'on liste les segments qui ont été reportés.
		JScrollPane myScrollPane;
		JPanel container;
		public ReportedSegment(ArrayList<Point> pointsHori,ArrayList<Point> pointsVert){
			super();
			container = new JPanel();
			container.setBackground(Color.white);
			container.setPreferredSize(new Dimension(200,400));
			myScrollPane = new JScrollPane(container);
			//setVisible(true);
			setSize(new Dimension(250,400));
			add(myScrollPane,BorderLayout.CENTER);
			container.add(new JLabel("Segments horizontaux reportés"));
			for(Point p:pointsHori){
				container.add(new JLabel(""+p.getSegment(0)));
			}
			container.add(new JLabel("Segments verticaux reportés"));
			for(Point p:pointsVert){
				container.add(new JLabel(""+p.getSegment(0)));
			}
		}
	}
	
	class Windowing extends JPanel{
		private int x_init;
		private int y_init;
		private Dimension dim_f;
		private int t_x ; // permet de translater le panel dynamiquement
		private int t_y ; // permet de translater le panel dynamiquement
		
		public Windowing(Dimension d){
			dim_f = d;
			addMouseListener(new Mouse());
		 	t_x =  (dim_f.width / 2) + x_translate;
		 	t_y = (dim_f.height / 2) + y_translate;
		 
		}
		protected void paintComponent(Graphics g) {
	    	Graphics2D g2 = (Graphics2D) g;
	    	// Antialiasing
	    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    			RenderingHints.VALUE_ANTIALIAS_ON);
	    	// Translation de l'origine au centre de la fenetre.
	    	t_x = t_x + x_translate;
	    	t_y = t_y + y_translate;
	    	x_translate = 0;
		 	y_translate = 0;
	    	g.translate(t_x , t_y );
	    		    	
	        int w = dim_f.width;
	        int h = dim_f.height;
	        int leftW = w / 2;
	        int topH = h / 2;
	        // Nomme l'axe x
	        g.setColor(Color.BLACK);
	        g.setFont(font);
	        g.drawString("Axe X",leftW-100,-10);
	        // Nomme l'axe y
	        g.drawString("Axe y",10,-topH+50);
	        // Mise a l'echelle
	        AffineTransform ct = g2.getTransform();
	        ct.scale(scale,scale);
	        g2.setTransform(ct);
	        g.setColor(Color.BLACK);    
	        // Trace l'axe x
	    	g.drawLine(-leftW,0 , leftW, 0);
	    	// Trace quelques reperes
	    	for(int i=1;i<=leftW/10;i++){
	    		g2.draw(new Line2D.Double(10*i,-.5,10*i,.5));
	    		g2.draw(new Line2D.Double(-10*i,-.5,-10*i,.5));
	    	}
	    	for(int i=1;i<=topH/10;i++){
	    		g2.draw(new Line2D.Double(-.5,10*i,.5,10*i));
	    		g2.draw(new Line2D.Double(-.5,-10*i,.5,-10*i));
	    	}
	    	//Trace l'axe y
	        g.drawLine(0,-topH , 0, topH);
	        // Trace la requete
	        g2.setColor(new Color(39,153,255));
	        if(displayquerie)
	        	g2.draw(rectangleRange);
	        // Trace les segemnts extraits d'un fichier
	        if(newrequete){
	        	// les horizontaux
	        	g2.setColor(new Color(165,98,42)); // couleur brune hori
	        	for(int i = 0 ; i <pointsHori.size();i++){
	        		Point first_p = pointsHori.get(i);
	        		Segment temp_s = first_p.getSegment(0);
	        		first_p = temp_s.getFirstPoint();
	        		Point  last_p= temp_s.getEndPoint();
	        		
	        		Line2D.Double ligne;
	        		ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
	        				last_p.getX(),-last_p.getY());
	        		g2.draw(ligne);
	        	}
	        	//les verticaux
	        	g2.setColor(new Color(128,128,128)); // couleur grise verti
	        	for(int i = 0 ; i <pointsVert.size();i++){
	        		Point first_p = pointsVert.get(i);
	        		Segment temp_s = first_p.getSegment(0);
	        		first_p = temp_s.getFirstPoint();
	        		Point  last_p= temp_s.getEndPoint();
	        		
	        		Line2D.Double ligne;
	        		ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
        				last_p.getX(),-last_p.getY());
	        		g2.draw(ligne);
	        	}
	        	
	        }
	       
	        // Trace les points reporte apres execution d'une requete
	        else{
	        	//Min x requete
	        	double a = rectangleRange.getMinX();
	        	// Max x requete
	        	double b = rectangleRange.getMaxX();
	        	// Max y requete
	        	double c = -rectangleRange.getMinY();
	        	// Min y requete
	        	double d = -rectangleRange.getMaxY();
	        	// On s'occupe des segments horizontaux
	        	Line2D.Double ligne;
	        	g2.setColor(new Color(165,98,42)); // couleur brune hori
	        	for(int i = 0 ; i <pointsHoriReported.size();i++){
	        		Point first_p = pointsHoriReported.get(i);
	        		Segment temp_s = first_p.getSegment(0);
	        		first_p = temp_s.getFirstPoint();
	        		Point  last_p= temp_s.getEndPoint();
	        		if(retouchegraphique){
	        		//Permet de couper les bouts inutiles
	        		if(first_p.getX()<=a){
	        			if(last_p.getX()<=b)
	        			ligne = new Line2D.Double(a,-first_p.getY(),
		        				last_p.getX(),-last_p.getY());
	        			else
	        				ligne = new Line2D.Double(a,-first_p.getY(),
			        				b,-last_p.getY());
	        		}
	        		else{
	        			if(last_p.getX()<=b)
		        			ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
			        				last_p.getX(),-last_p.getY());
		        			else
		        				ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
				        				b,-last_p.getY());
	        			
	        		}
	        		
	        	
	        		g2.draw(ligne);
	        		}
	        		else{
	        		ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
	        		last_p.getX(),-last_p.getY());
	        		g2.draw(ligne);
	        		}
	        	}
	        	// On s'occupe des verticaux
	        	g2.setColor(new Color(128,128,128)); // couleur grise verti
	        	for(int i = 0 ; i <pointsVertReported.size();i++){
	        		Point first_p = pointsVertReported.get(i);
	        		Segment temp_s = first_p.getSegment(0);
	        		first_p = temp_s.getFirstPoint();
	        		Point  last_p= temp_s.getEndPoint();
	        		if(retouchegraphique){
	        		//Permet de couper les bouts inutiles
	        		if(first_p.getY()<=d){
	        			if(last_p.getY()<=c)
	        			ligne = new Line2D.Double(first_p.getX(),-d,
		        				last_p.getX(),-last_p.getY());
	        			else
	        				ligne = new Line2D.Double(first_p.getX(),-d,
			        				last_p.getX(),-c);
	        		}
	        		else{
	        			if(last_p.getY()<= c)
		        			ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
			        				last_p.getX(),-last_p.getY());
		        			else
		        				ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
				        				last_p.getX(),-c);
	        			
	        		}
	        		g2.draw(ligne);
	        		}
	        		else{
	        			ligne = new Line2D.Double(first_p.getX(),-first_p.getY(),
	        					last_p.getX(),-last_p.getY());
	        			g2.draw(ligne);
	        		}
	        			
	        	}
	        	
	        }
		}
		class Mouse implements MouseListener{
		public void mouseClicked(MouseEvent arg0) {
		}
		public void mouseEntered(MouseEvent arg0) {
		}
		public void mouseExited(MouseEvent arg0) {
		}
		public void mousePressed(MouseEvent arg0) {
			x_init = arg0.getX();
			y_init = arg0.getY();
		}
		public void mouseReleased(MouseEvent arg0) {
			int x_final = arg0.getX();
			int y_final = arg0.getY();
			x_translate =  x_final- x_init ;
			y_translate = y_final - y_init;
			frame.repaint();
		}
	}
	}
}
