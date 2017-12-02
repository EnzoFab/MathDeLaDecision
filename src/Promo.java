import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Set;


public class Promo {

	
	/**
	 * @see CSVInterpreteur
	 * contiendra toute les lignes du csv
	 */
	private ArrayList csvResultat;
	
	private ArrayList liste_Groupe,
			liste_couples_intermediaires,
			liste_Couple_Affecte,
			liste_Etudiant;
	
	
	public Promo(){
		
		liste_Groupe = new ArrayList();// les groupes finaux 
		liste_couples_intermediaires = new ArrayList();
				// les couples qui ne sont pas encore deja créés mais pas qui peuvent changer 
		liste_Couple_Affecte = new ArrayList();// les couples fixés 
		liste_Etudiant =new ArrayList();
		
		try {
			csvResultat = (ArrayList) CSVInterpreteur.readFile("preferences.csv");
			// recuperation de toute les données du cvs 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initEtudiant();
		creerAssociationEtu();
		Collections.shuffle(this.liste_Etudiant); // on trie les etudiants aléatoirement
		
		
	}
	
	
	
	/**
	 * creer tous les etudiants de la promo
	 */
	private void initEtudiant(){
		String ligne_EudiantCSV = csvResultat.get(0).toString(); 
			// la liste des etudiants est sur la premiere ligne
		String etudiants [] = ligne_EudiantCSV.split(",");
		/*i=1 car la premiere case du tableau contient 'nom' 
					et on ne le compte pas comme un etudiant*/
		Etudiant e;
		for(int i =1; i< etudiants.length; i++){
			String [] etudiant = etudiants[i].split(" "); // recuperation du nom et du prenom
			String prenom =etudiant[0];
			String nom = etudiant[1];
			e = new Etudiant(nom, prenom);
			this.liste_Etudiant.add(e);
		}
	}
	
	/**
	 * trie les association poour chaque etudiants
	 */
	private void creerAssociationEtu(){
		int i = 1; // 1 car on a deja recuperer les noms et prenoms 
		int j;
		String line;
		String [] lineSplit;
		Appreciation a;
		Etudiant courant,// l'etudiant sur lequel on trie les appreciation donnee
			observe; //
		while(i < csvResultat.size()){
			courant = (Etudiant)this.liste_Etudiant.get(i-1);
				// on commence a i à 1 or les indices commencent à 0 
			j=1;// on remets j à 1 
			
			
			line = csvResultat.get(i).toString();
				// recuperation d'une ligne du CSV
			lineSplit = line.split(",");
			while(j< lineSplit.length){
				a = Appreciation.appreciationFromString(lineSplit[j]);
					// traduit l'association du csv en vrai associatin java 
				observe = (Etudiant)this.liste_Etudiant.get(j-1); 
				
					// voire au dessus 
				if(courant.getId() != observe.getId()){ 
					/* l'etudiant observe est differant du courant 
					 * car on un etudiant ne peut pas se noter lui meme 
					*/
					courant.ajouterEtudiant(a, observe);
				}
				j++;
				
			}
			i++;
		}
	}
	
	

	
	/**
	 * crée les groupes d'étudiants en fonction des appréciations de chacun
	 */
	/*public void repartitionGroupe(){
		System.out.println("Debut de la répartion");
		// premiere amélioration prendre les étudiants aléatoirement
		ArrayList coupleTemporaire =new ArrayList();
		
		Etudiant courant, observe;
		final int DISTANCE_MAX = 10; // la distance max que peut atteindre un couple 
			// 5 + 5 (5 car il y a au max 5 entre TB et AR)
		for(Object o: this.liste_Etudiant){
		
			courant = (Etudiant) o;
			System.out.println(courant.getPrenom()+"...");
			BinomePondere c = new BinomePondere(courant,null,DISTANCE_MAX); 
				// creation d'un couple 
			Appreciation a = courant.getMeilleureNote();// meilleure note donnée
			AppreciationEtu [] appreciation_donnee = courant.getClassification_appreciation();
			
			for(int i= a.ordinal(); i < appreciation_donnee.length; i++){ 
				// on part de la meilleure note donnée et on parcours la listes des étudiants 
				AppreciationEtu ae = appreciation_donnee[i];
				
				Collections.shuffle(ae.getListEtudiant()); // trie aléatoirement
				
				for(Object o2 : ae.getListEtudiant()){
					
					
					observe = (Etudiant)o2; // 
					int distanceEtuCourant = Appreciation.distance(a, courant.noteDonnee(observe));
					// recuperation de la distance entre la meilleure note et
					//la note donnée à l'etudiant
					int distanceEtuObserve = Appreciation.distance(
							observe.getMeilleureNote(), 
							observe.noteDonnee(courant)
						);
				
					
					boolean dejacouple = false; 
					BinomePondere c2;
							// on va regarder si l'étudiant est déjà dans un groupe 
					Iterator ite = this.liste_couples_intermediaires.iterator();
					
					while(!dejacouple && ite.hasNext()){
						c2 = (BinomePondere) ite.next();
						if(c2.appartient(observe)){
							dejacouple = true;
						}else if(c2.appartient(courant)){
								dejacouple = true;
							}
						}
					if(distanceEtuCourant +distanceEtuObserve < c.getPoids() ){
						if(!dejacouple){
							c.setEtudiant(courant, observe, distanceEtuCourant +distanceEtuObserve);
							// on mets a jour le couple 
							//si jamais on trouve une meilleure association
						}else{
							// il faut qu'on casse une association
							// se souvienne de l'etudiant qui a perdu son partenaire
							//pour le rajouter à un autre groupe 
							c.setEtudiant(courant, observe, distanceEtuCourant +distanceEtuObserve);
						}
					
					}
				}
				
			}
			this.liste_couples_intermediaires.add(c); // on ajoute aux couples potentiel
		}
		System.out.println("Fin de la répartion");
	}*/
	
	public void repartitionGroupe(){
		System.out.println("Debut de la répartion");
		
		ArrayList liste_etudiant_a_coupler = new ArrayList(this.liste_Etudiant);
		ArrayList liste_binome = new ArrayList();
			// copie de la liste des etudiants;
		Collections.shuffle(liste_etudiant_a_coupler); 
			// on trie aléatoirement la liste des étudiants 
		Etudiant actuel, observe;
		ListIterator iterateur = liste_etudiant_a_coupler.listIterator();
		
		final int DISTANCE_MAX = 10; // la distance max que peut atteindre un couple 
		// 5 + 5 (5 car il y a au max 5 entre TB et AR)
		
		
		// repartition des binomes 
		while(iterateur.hasNext() && liste_couples_intermediaires.size() < 18){
			actuel = (Etudiant) iterateur.next(); // etudiant suivant
			System.out.println(actuel.getPrenom()+" "
					+ actuel.getNom() 
					+ "...\n");
			//System.out.println(actuel.getPrenom()+"...");
			
			ArrayList liste_binomePossible = new ArrayList();
			
			Appreciation a = actuel.getMeilleureNote();// meilleure note donnée
			AppreciationEtu [] appreciation_donnee = actuel.getClassification_appreciation();
			
			// creation de tout les couples les binomes possible pour l'etudiant actuel
			for(AppreciationEtu ae : actuel.getClassification_appreciation()){
				for(Object o : ae.getListEtudiant()){
					observe = (Etudiant) o;
					
				
					int poids = actuel.getPoids(observe);
					
					BinomePondere c = new BinomePondere(actuel, observe, poids);
					liste_binomePossible.add(c); // ajout du couple aux binomes possibles
				}
			}
			
			Collections.sort(liste_binomePossible);// on trie en fonction du poids des couples
			System.out.println("\nBinome possible pour " + 
					actuel.getPrenom() + " (trier en fonction du poids ):\n");
			for(Object o : liste_binomePossible)
				System.out.println(o);
			
			System.out.println();
			
			/*for(Object o: liste_binomePossible)
				System.out.println(o);
				*/
			// on parcours la liste des binomePossible pour l'etudiant
			for(Object o: liste_binomePossible){
				BinomePondere c = (BinomePondere) o;
				BinomePondere ancienCouple;
				if(( ancienCouple = dejaPresent(c.getEtudiant1()) ) != null){
					// l'étudiant actuel est déjà présent dans un couple 
					
					if(c.getPoids() < ancienCouple.getPoids()){
						// le nouveau couple est meilleur 
						if(c.getEtudiant2().equals(ancienCouple.getEtudiant1())){
							// l'etudiant qui se retrouve tout seul est le deuxieme 
							
							iterateur.add(ancienCouple.getEtudiant1());
								// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
									+" n'a plus de groupe ");
							
							
							
							
						}else if (c.getEtudiant2().equals(ancienCouple.getEtudiant2())){
							iterateur.add(ancienCouple.getEtudiant2());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
									+" n'a plus de groupe ");
							
						}else if (c.getEtudiant1().equals(ancienCouple.getEtudiant2())){
							iterateur.add(ancienCouple.getEtudiant2());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
									+" n'a plus de groupe ");
							
						}else if (c.getEtudiant2().equals(ancienCouple.getEtudiant1())){
							iterateur.add(ancienCouple.getEtudiant2());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
									+" n'a plus de groupe ");
							
						}
						
						
						
						liste_couples_intermediaires.remove(ancienCouple);
						//ancienCouple.maj(c); // on  met a jour l'ancien couple 
						System.out.println(c.getEtudiant1().getPrenom() + 
								" est maintenant en binome avec "+
								c.getEtudiant2().getPrenom()+"\n");
						liste_couples_intermediaires.add(c);
						
					}else{
						// le poids est egal  il faut regarder si le couple est le meme 
						// sinon on regarde l'histoire de profil 
					}
					
				}else if((ancienCouple =  dejaPresent(c.getEtudiant2()) ) != null){
					if(c.getPoids() < ancienCouple.getPoids()){
						if(c.getEtudiant2().equals(ancienCouple.getEtudiant1())){
							// l'etudiant qui se retrouve tout seul est le deuxieme 
							
							iterateur.add(ancienCouple.getEtudiant2());
								// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
									+" n'a plus de groupe ");
							
							
							
						}else if (c.getEtudiant2().equals(ancienCouple.getEtudiant2())){
							iterateur.add(ancienCouple.getEtudiant1());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
									+" n'a plus de groupe ");
							
						}else if (c.getEtudiant1().equals(ancienCouple.getEtudiant2())){
							iterateur.add(ancienCouple.getEtudiant1());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
									+" n'a plus de groupe ");
							
						}else if (c.getEtudiant1().equals(ancienCouple.getEtudiant1())){
							iterateur.add(ancienCouple.getEtudiant2());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
									+" n'a plus de groupe ");
							
						}
						
						
						
						liste_couples_intermediaires.remove(ancienCouple);
						//ancienCouple.maj(c); // on  met a jour l'ancien couple 
						System.out.println(c.getEtudiant1().getPrenom() + 
								" est maintenant en binome avec "+
								c.getEtudiant2().getPrenom()+"\n");
						liste_couples_intermediaires.add(c);
						
					}else{
						// le poids est egal  il faut regarder si le couple est le meme 
						// sinon on regarde l'histoire de profil 
					}
					
					
				}else{
					System.out.println(c.getEtudiant1().getPrenom() + 
							" est maintenant en binome avec "+
							c.getEtudiant2().getPrenom()+"\n");
					liste_couples_intermediaires.add(c);
					break;
					
				}
			}
			pause();
		} // fin du while 
		
		System.out.println("\nEtutudiant restants à répartir "+etudiantRestant().size());
		
		for(Object o : etudiantRestant()){
			Etudiant e = (Etudiant)o;
			System.out.println(e.getPrenom() + " "+ e.getNom());
		}
		System.out.println("\nFin des repartions des binomes \n ");
		// repartie les eleves restants dans les groupes déjà crées 
		pause();
		repartitionTrinome(etudiantRestant());
		
		System.out.println("\nFin de répartition\n");
		
		
	}

	
	
	/**
	 * repartie les etudiants en trinomes 
	 * @param listEtudiant
	 */
	private void repartitionTrinome (ArrayList listEtudiant){
		ArrayList <ArrayList> binomeParEtudiant = new ArrayList<ArrayList>();
			// ArrayList d'arrayList sur une ligne toujours le meme etudiant 
			// sur une colonne le meme binome 
		Etudiant actuel; 
		BinomePondere bp;
		ArrayList <TrinomeIntermediaire> trinomIntermediaire ;
		for(Object o : listEtudiant){
			actuel = (Etudiant) o;
			trinomIntermediaire = new ArrayList();
			for(Object o1 : this.liste_couples_intermediaires){
				bp = (BinomePondere) o1;
				trinomIntermediaire.add(new TrinomeIntermediaire(actuel, bp) );
			}
			binomeParEtudiant.add(trinomIntermediaire);
		}  // produit cartésien 
		System.out.println("\n Produit cartésien \n");
		for(ArrayList l : binomeParEtudiant){
			System.out.println("\n  Etudiants suivants \n");
			for(int i= 0 ; i < l .size() ; i++){
				System.out.println(l.get(i));
			}
			
		}
		System.out.println("\n");
		pause();
		
		//TrinomeIntermediaire ti;
		//int i ;
		ArrayList <ArrayList> copieBPE, // copie d'arrayList binomeParEtudiant
				touteCombinaisons ; // toutes les combinaisons possibles 
		touteCombinaisons = new ArrayList <ArrayList> ();
		for(ArrayList  <TrinomeIntermediaire> l: binomeParEtudiant){
			ArrayList <TrinomeIntermediaire> trPossibles = new ArrayList();
			for(int i =0 ; i <l.size(); i++){
				//trPossibles.add( l.get(i) ); 
				// on ajoute le trinome à la liste des trinome possibles
				copieBPE = new ArrayList<ArrayList>( binomeParEtudiant);
				//copieBPE.remove(l); // liste nouvelle liste sans les trinome de l'etudiant i
				Collections.shuffle(copieBPE); // trie aléatoirement 
				for(ArrayList  <TrinomeIntermediaire> l2 : copieBPE){
					for(TrinomeIntermediaire ti: l2){
						if(! estPresent(trPossibles , ti.getBinome())  &&
								!estPresent(trPossibles, ti.getEtudiant())){
							// si le binome n'est pas deja présent dans la liste des binomes 
							trPossibles.add(ti);
							
							break;
						}
					}
					
				}
			}
			touteCombinaisons.add(trPossibles);
		} // toutes les combinaisons possibles de trinomes 
		
		for(ArrayList l2:  touteCombinaisons){
			for(int i = 0 ; i < l2.size() ; i++){
				System.out.println(l2.get(i));
			}
			System.out.println("\nCombinaison suivante\n");
		}
		
		
		ArrayList <TrinomeIntermediaire> l = touteCombinaisons.get(0);
		ArrayList <TrinomeIntermediaire> meilleureCombinaion = l ;
		
		
		int poidsTotalMin, poidsTotal;
		poidsTotalMin = 0;
		poidsTotal = 0;
		for(int i = 0 ; i < l.size(); i++){
			poidsTotalMin += l.get(i).getPoids();
		}  // pour avoir une reference 
		
		for(ArrayList<TrinomeIntermediaire> combi :touteCombinaisons ){
			for(int i = 0 ; i < combi.size(); i++){
				poidsTotal += combi.get(i).getPoids();
			}  // pour avoir une reference 
			if(poidsTotal < poidsTotalMin){ // on a trouvé une meilleure combinaison 
				meilleureCombinaion = combi; 
				poidsTotalMin = poidsTotal;
			}
		}// on  obtient la combinaison avec un poids minimal 
		
		System.out.println("\nMeilleures Trinomes\n");
		for(TrinomeIntermediaire trinome: meilleureCombinaion){
			System.out.println(trinome);
		}
		System.out.println("\n\n");
		
		for(TrinomeIntermediaire trinome: meilleureCombinaion){
			if(this.liste_couples_intermediaires.contains(trinome.getBinome()) ){
				liste_couples_intermediaires.remove(trinome.getBinome());
				liste_couples_intermediaires.add(trinome.getTrinome());
			}
		}
		
		
		
	}
	
	
	private boolean estPresent(ArrayList l, BinomePondere bp){
		boolean estPresent = false;
		int i = 0;
		while(! estPresent && i < l.size()){
			TrinomeIntermediaire tr = (TrinomeIntermediaire) l.get(i);
			if(tr.getBinome().equals(bp)){
				estPresent = true;
				break;
			}
			i++;
		}
		
		return estPresent; 
	}
	
	private boolean estPresent(ArrayList l, Etudiant e){
		boolean estPresent = false;
		int i = 0;
		while(! estPresent && i < l.size()){
			TrinomeIntermediaire tr = (TrinomeIntermediaire) l.get(i);
			if(tr.getEtudiant().equals(e)){
				estPresent = true;
				break;
			}
			i++;
		}
		
		return estPresent; 
	}
	
	
	/**
	 * retourne la liste des etudiants n'ayant pas de binome 
	 * @return
	 */
	private ArrayList etudiantRestant(){
		ArrayList retour = new ArrayList();
		for(Object o : this.liste_Etudiant){
			Etudiant e = (Etudiant) o;
			boolean appartient = false;
			int i =0;
			while(i < this.liste_couples_intermediaires.size() && !appartient){
				BinomePondere b = (BinomePondere) liste_couples_intermediaires.get(i);
				if(b.appartient(e)){
					appartient = true;
				}
				i++;
			}
			if(!appartient)
				retour.add(e);
		}
		return retour;
	}
	
	/**
	 * regarde si un etudiant fait deja partie de la liste des couples 
	 * @param e
	 * @return
	 */
	public BinomePondere dejaPresent(Etudiant e){
		BinomePondere present = null;
		Iterator i = this.liste_couples_intermediaires.iterator();
		while(i.hasNext() && present == null){
			BinomePondere c = (BinomePondere) i.next();
			if(c.appartient(e)){
				present = c;
			}
		}
		return present;
	}
	
	
	
	/**
	 * mets le programme en pause pour voir les resultats 
	 */
	private void pause(){
		Scanner sc = new Scanner(System.in);
		String str ="";
		
		
		
			System.out.println("Continuer ?\nOui/Non (Liste Binome)");
			str = sc.nextLine();
			
			if(str.equals("Liste")){
				System.out.println("Etudiants deja affectés");
				for(Object o: this.liste_couples_intermediaires){
					System.out.println(o);
				}
			}
		while(!str.equals("Oui") && !str.equals("oui") && !str.equals("o") 
					&& !str.equals("O")){
			System.out.println("Continuer ?\nOui/Non (Liste Binome)");
			str = sc.nextLine();
			if(str.equals("Liste")){
				System.out.println("Etudiants deja affectés");
				for(Object o: this.liste_couples_intermediaires){
					System.out.println(o);
				}
			}
		}
		 
		
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//CSVInterpreteur.listeEtudiantFromCSV(20, "preferences.csv", ",");
		Promo p = new Promo();
		p.repartitionGroupe();
		//System.out.print(p.liste_Etudiant.get(34));
		for(Object o : p.liste_couples_intermediaires){
			System.out.println(o);
		}

	}

}
