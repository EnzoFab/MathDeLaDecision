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
			liste_Etudiant,
			liste_trinomeIntermediaires;
	private boolean passer = false; // pour passer toutes les explication
	private String nomFichierSortie;
	
	
	public Promo(String csv, String fichier){
		
		liste_Groupe = new ArrayList();// les groupes finaux 
		liste_couples_intermediaires = new ArrayList();
				// les couples qui ne sont pas encore deja créés mais pas qui peuvent changer 
		liste_Couple_Affecte = new ArrayList();// les couples fixés 
		liste_Etudiant =new ArrayList();
		liste_trinomeIntermediaires= new ArrayList();
		
		try {
			csvResultat = (ArrayList) CSVInterpreteur.readFile(csv); // "preferences.csv"
			// recuperation de toute les données du cvs 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Fichier non trouvé ");
		}
		
		initEtudiant();
		creerAssociationEtu();
		setMentionMajoritaireEtu();
		
		
		nomFichierSortie = fichier;
		
		
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
	 * met à jour les mentions majoritaires pour chaque etudiant
	 */
	private void setMentionMajoritaireEtu(){
		Etudiant actuel, observe;
		ArrayList <Appreciation> listAppreciation = new ArrayList<Appreciation>();
		for(Object o : this.liste_Etudiant){
			actuel = (Etudiant) o;
			listAppreciation.clear();
			for(Object o2: this.liste_Etudiant){
				if(!actuel.equals(o2)){
					observe = (Etudiant)o2;
					listAppreciation.add(observe.noteDonnee(actuel));
				}
			}
			Collections.sort(listAppreciation);
			actuel.setMentionMajoritaire(listAppreciation);
		}
		Collections.sort(this.liste_Etudiant); 
			// trie la liste des étudiants en fonction de leurs mention majoritaire
		
		
		for(Object o: liste_Etudiant){
			Etudiant e = (Etudiant)o;
			e.triEtudiant(); // tri les etudiants notés par e 
			System.out.println(e.getPrenom()+" " +e.getNom()+" mention majoritaire : " +
					e.getMentionMajoritaire().getAppreciation()+ " "+
					e.getMentionMajoritaire().getNombre());
		}
		
		pause();

	}
	
	
	/**
	 * creer des trinomes à partir des trinomes restants 
	 * @param etuRestant
	 */
	private void repartitionTrinomeV2(ArrayList etuRestant){
		
		Collections.sort(etuRestant);
		ArrayList listeTrinome = new ArrayList(); 
		Etudiant actuel; 
		BinomePondere bObserve; 
		TrinomeIntermediaire triInt;
		//ListIterator iterateur = etuRestant.listIterator();
		
		for(int i =0; i< etuRestant.size(); i++){
		//	actuel =(Etudiant) iterateur.next();
			
			actuel =(Etudiant)etuRestant.get(i);
	
			ArrayList listTrinomePossibles = new ArrayList(); 
				// liste des trinomes possibles pour l'etudiant 
			
			for(Object o: this.liste_couples_intermediaires){
				bObserve = (BinomePondere) o;
				listTrinomePossibles.add(new TrinomeIntermediaire(actuel, bObserve));
					// ajout du trinome 
			}
			Collections.sort(listTrinomePossibles);// on trie en fonction du poids des couples
			System.out.println("\nTrinomes possibles pour " + 
					actuel.getPrenom() + " (trier en fonction du poids ):\n");
			for(Object o : listTrinomePossibles)
				System.out.println(o);
			
			System.out.println();
			
			for(Object o : listTrinomePossibles){
				triInt = (TrinomeIntermediaire)o;
				
				TrinomeIntermediaire ancienTri; // parcours la liste des 
				if( (ancienTri = 
						existeTrinome(liste_trinomeIntermediaires,triInt.getBinome()))  != null){
					if(triInt.getPoids() < ancienTri.getPoids()){
						System.out.println(ancienTri.getEtudiant().getPrenom() + " n'a plus de groupe");
						//iterateur.add(ancienTri.getEtudiant());
						etuRestant.add(ancienTri.getEtudiant());
						liste_trinomeIntermediaires.remove(ancienTri);
						liste_trinomeIntermediaires.add(triInt);
						System.out.println("Nouveau trinomes "+ triInt.affichageSimple());
						break;
					}
				}else{
					liste_trinomeIntermediaires.add(triInt);
					System.out.println("Nouveau trinome "+ triInt.affichageSimple());
					break;
				}
				
			}
			
		}
		System.out.println("etudiant restant "+ etuRestant.size());
		for(Object o: liste_trinomeIntermediaires){
			TrinomeIntermediaire trinome = (TrinomeIntermediaire)o;
			if(this.liste_couples_intermediaires.contains(trinome.getBinome()) ){
				liste_couples_intermediaires.remove(trinome.getBinome());
				liste_couples_intermediaires.add(trinome.getTrinome());
			}
		}
		Collections.sort(this.liste_couples_intermediaires);
		
		
	}
	
	

	
	/**
	 * crée les groupes d'étudiants en fonction des appréciations de chacun
	 */
	public void repartitionGroupe(){
		System.out.println("Debut de la répartion");
		passer = false;
		
		ArrayList liste_etudiant_a_coupler = new ArrayList(this.liste_Etudiant);
		// copie de la liste des etudiants;
		
		ArrayList liste_binome = new ArrayList();
			
		/*Collections.shuffle(liste_etudiant_a_coupler); 
			// on trie aléatoirement la liste des étudiants */
		Etudiant actuel, observe;
		ListIterator iterateur = liste_etudiant_a_coupler.listIterator();
		
		
		int iterationBoucle = 0;
		
		// repartition des binomes 
		while(iterationBoucle < liste_etudiant_a_coupler.size()
				&& iterationBoucle<liste_Etudiant.size() ||
				liste_couples_intermediaires.size() < 18 ){
			//actuel = (Etudiant) iterateur.next(); // etudiant suivant
			actuel = (Etudiant) liste_etudiant_a_coupler.get(iterationBoucle);
			iterationBoucle++;
			System.out.println(actuel.getPrenom()+" "
					+ actuel.getNom() 
					+ "...\n");
			
			
			ArrayList liste_binomePossible = new ArrayList();
			
			// creation de tous les couples les binomes possibles pour l'etudiant actuel
			for(AppreciationEtu ae : actuel.getClassification_appreciation()){
				// amelioration: trier les etudiants selon leurs profils 
				for(Object o : ae.getListEtudiant()){
					observe = (Etudiant) o;
					
				
					int poids = actuel.getPoids(observe);
					
					BinomePondere c = new BinomePondere(actuel, observe, poids);
					liste_binomePossible.add(c); // ajout du couple aux binomes possibles
				}
			}
			
			Collections.sort(liste_binomePossible);// on trie en fonction du poids des binomes
			System.out.println("\nBinome possible pour " + 
					actuel.getPrenom() + " (trier en fonction du poids ):\n");
			for(Object o : liste_binomePossible)
				System.out.println(o);
			
			System.out.println();
			
	
			// on parcours la liste des binomes possibles pour l'etudiant
			for(Object o: liste_binomePossible){
				BinomePondere c = (BinomePondere) o;
				BinomePondere ancienCouple, ancienCouple2;
				
				if( (ancienCouple = dejaPresent(c.getEtudiant1()) )  !=null &&
						(ancienCouple2 = dejaPresent(c.getEtudiant2()) ) !=null  ){
					/*cas n°1 
					 * les deux etudiants sont deja dans un groupe 
					 * Premier cas les deux étudiants formant le nouveau couple
					 * sont deja dans un couple,
					 * on regarde alors le poids de ce nouveau couple comparé 
					 * aux poids des deux anciens couple 
					 * si jamais le poids est inférieur on casse les deux anciens couples et 
					 * on y ajoute le nouveau, les deux élèves 
					 * n'ayant plus de groupes sont remis dans la liste des elèves à grouper 
					 */
					if(c.getPoids() < ancienCouple.getPoids() &&
							c.getPoids() < ancienCouple2.getPoids() 
							||
							c.getPoids() <= ancienCouple.getPoids() &&
							c.getPoids() < ancienCouple2.getPoids() 
							||
							c.getPoids() < ancienCouple.getPoids() &&
							c.getPoids() <= ancienCouple2.getPoids() 
							||
							(c.getPoids() == ancienCouple.getPoids() 
								&& c.getPoids() == ancienCouple2.getPoids() 
								&& c.nbEtuAimeMin() <=ancienCouple.nbEtuAimeMin()
								&& c.nbEtuAimeMin() <= ancienCouple2.nbEtuAimeMin())
							){
						/*
						 * le poids du nouveau couple est inferieur 
						 * aux poids des deux anciens couples
						 * ou 
						 * Le poids du nouveau couples est inférieur au poids d'un couple 
						 * et inferieur ou egal au poids du deuxieme 
						 * ou
						 * les 3 poids poids sont égaux 
						 * on va donc créer le couple avec les etudiants 
						 * qui aiment le moinds de monde 
						 * càd le binome avec le nbEtuAimeMin le plus bas sera créé
						 */
						if(c.getEtudiant1().equals(ancienCouple.getEtudiant1())){
							liste_etudiant_a_coupler.add(ancienCouple.getEtudiant2());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
								+" n'a plus de groupe ");
						}else if(c.getEtudiant1().equals(ancienCouple.getEtudiant2())){
							liste_etudiant_a_coupler.add(ancienCouple.getEtudiant1());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
								+" n'a plus de groupe ");
						}
						liste_couples_intermediaires.remove(ancienCouple);
						
						if(c.getEtudiant2().equals(ancienCouple2.getEtudiant1())){
							liste_etudiant_a_coupler.add(ancienCouple2.getEtudiant2());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple2.getEtudiant2().getPrenom()
								+" n'a plus de groupe ");
						}else if(c.getEtudiant2().equals(ancienCouple2.getEtudiant2())){
							liste_etudiant_a_coupler.add(ancienCouple2.getEtudiant1());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple2.getEtudiant1().getPrenom()
								+" n'a plus de groupe ");
						}
						liste_couples_intermediaires.remove(ancienCouple2);
						//ancienCouple.maj(c); // on  met a jour l'ancien couple 
						System.out.println(c.getEtudiant1().getPrenom() + 
								" est maintenant en binome avec "+
								c.getEtudiant2().getPrenom()+"\n");
						liste_couples_intermediaires.add(c);
						
					}
				}
				
				/*Cas n°2  
				 * L'étudiant actuel (c.getEtudiant1() c'est la meme chose )
				 *  est déja dans un binome, on regarde alors le poids d'un nouveau couple 
				 *  si jamais le poids de c est inférieur on casse l'ancien couple 
				 *  l'ancien partenaire de l'étudiant actuel est remis dans la liste des étudiants
				 *  à coupler et on ajoute le couple c
				 */
				else if(( ancienCouple = dejaPresent(c.getEtudiant1()) ) != null){
					if(c.getPoids() < ancienCouple.getPoids()){
						if(c.getEtudiant1().equals(ancienCouple.getEtudiant1())){
							// l'etudiant qui se retrouve tout seul est le deuxieme 
							
							liste_etudiant_a_coupler.add(ancienCouple.getEtudiant2());
								// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
									+" n'a plus de groupe ");
							
							
							
						}else if (c.getEtudiant1().equals(ancienCouple.getEtudiant2())){
							liste_etudiant_a_coupler.add(ancienCouple.getEtudiant1());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
									+" n'a plus de groupe ");
							
						}
						
						
						
						
						liste_couples_intermediaires.remove(ancienCouple);
						//ancienCouple.maj(c); // on  met a jour l'ancien couple 
						System.out.println(c.getEtudiant1().getPrenom() + 
								" est maintenant en binome avec "+
								c.getEtudiant2().getPrenom()+"\n");
						liste_couples_intermediaires.add(c);
						
					}
		
				}else if((ancienCouple =  dejaPresent(c.getEtudiant2()) ) != null){
					if(c.getPoids() < ancienCouple.getPoids()
							||
							(c.getPoids() == ancienCouple.getPoids() 
								&&  c.nbEtuAimeMin() < ancienCouple.nbEtuAimeMin() ) ){
						if(c.getEtudiant2().equals(ancienCouple.getEtudiant1())){
							// l'etudiant qui se retrouve tout seul est le deuxieme 
							
							liste_etudiant_a_coupler.add(ancienCouple.getEtudiant2());
								// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant2().getPrenom()
									+" n'a plus de groupe ");
							
							
							
						}else if (c.getEtudiant2().equals(ancienCouple.getEtudiant2())){
							liste_etudiant_a_coupler.add(ancienCouple.getEtudiant1());
							// on le réajoute à la liste des étudiants à coupler 
							System.out.println(ancienCouple.getEtudiant1().getPrenom()
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
					if(liste_couples_intermediaires.size() < 18 ){
						System.out.println(c.getEtudiant1().getPrenom() + 
								" est maintenant en binome avec "+
								c.getEtudiant2().getPrenom()+"\n");
						liste_couples_intermediaires.add(c);
					}else{
						liste_etudiant_a_coupler.add(actuel);
					}
					
					break;
					
				}
			}
			pause();
		} // fin du while 
		
		System.out.println("\nEtudiant restants à répartir "+etudiantRestant().size());
		
		for(Object o : etudiantRestant()){
			Etudiant e = (Etudiant)o;
			System.out.println(e.getPrenom() + " "+ e.getNom());
		}
		System.out.println( iterationBoucle +"\nFin des repartions des binomes \n ");
		passer = false;// on force la pause 
		// repartie les eleves restants dans les groupes déjà crées 
		pause();
		//repartitionTrinome(etudiantRestant());
		this.repartitionTrinomeV2(etudiantRestant());
		System.out.println("\nFin de répartition\n");
		
		CSVInterpreteur.createCSV(nomFichierSortie, transformationCSV());
		
	}

	/**
	 * Transforme les groupes créés en une chaine de caracteres représentant un CSV
	 * @return
	 */
	private String transformationCSV(){
		String csv ="nom";
		Etudiant actuel, observe; 
		ListIterator iterateur = this.liste_Etudiant.listIterator();
		while(iterateur.hasNext()){
			csv+=",";
			actuel = (Etudiant)iterateur.next();
			csv+=actuel.getPrenom()+ " "+actuel.getNom();
		} /* ajout de la premiere ligne de la forme : 
		actuel.prenom actuel.nom, etudSuiv.prenom, etudSuiv.nom, ... 
		*/
		csv+="\n";
		iterateur = this.liste_Etudiant.listIterator();
		BinomePondere bp = null;
		boolean appartient;
		int i;
		while(iterateur.hasNext()){
			actuel = (Etudiant)iterateur.next();
			csv+=actuel.getPrenom()+ " "+actuel.getNom();
			appartient = false;
			i = 0;
			while(!appartient && i< liste_couples_intermediaires.size()){
				bp = (BinomePondere)this.liste_couples_intermediaires.get(i);
				appartient = bp.appartient(actuel);
				i++;
			}
			for(Object o: liste_Etudiant){
				observe = (Etudiant)o;
				if(observe.equals(actuel))
					csv+=",-1";
				else if(bp.appartient(observe))
					csv+=",1";
				else
					csv+=",0";
					
				
				
			}
			
			csv+="\n";
		}
		return csv;
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
	
	/**
	 * regarde si un binome est présent dans un trinome 
	 * @param l: liste de trinome 
	 * @param bp
	 * @return
	 */
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
	
	/**
	 * Vrai si un etudiant est présent dans un trinome 
	 * @param l
	 * @param e
	 * @return
	 */
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
	 * Vrai si un etudiant est présent dans un trinome 
	 * si oui renvoie ce trinome 
	 * @param l
	 * @param e
	 * @return
	 */
	private TrinomeIntermediaire existeTrinome(ArrayList l, BinomePondere bp){
		TrinomeIntermediaire trInt = null;
		int i = 0;
		while(trInt == null && i < l.size()){
			TrinomeIntermediaire tr = (TrinomeIntermediaire) l.get(i);
			if(tr.getBinome().equals(bp)){
				trInt = tr;
			}
			i++;
		}
		
		return trInt; 
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
		Collections.sort(retour);
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
		if(!passer){
			do{
				System.out.println("Continuer ?\nOui/Non Liste (afficher liste des binomes)/ " +
						"passer (ne plus s'arreter)");
				str = sc.nextLine();
				if(str.equals("Liste")){
					System.out.println("Etudiants deja affectés : "+
							liste_couples_intermediaires.size());
					for(Object o: this.liste_couples_intermediaires){
						System.out.println(o);
					}
				}else if(str.equals("passer")){
					passer = true;
				}
			}while(!str.equals("Oui") && !str.equals("oui") && !str.equals("o") 
					&& !str.equals("O") && !str.equals("passer"));
		}
		
		 
		
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//CSVInterpreteur.listeEtudiantFromCSV(20, "preferences.csv", ",");
		String csv= "";
		String sortie="";
		try
	    {
	      csv = args[0];
	      
	    }
	    catch(ArrayIndexOutOfBoundsException e){
	    	csv = "preferences.csv";
	    }
		try
	    {
			sortie = args[1];
	      
	    }
	    catch(ArrayIndexOutOfBoundsException e){
	    	sortie = "resultatRepartition.csv";
	    }
		
		Promo p = new Promo(csv,sortie);
		p.repartitionGroupe();
		/*System.out.println(p.liste_Etudiant.get(13));
		System.out.println(p.liste_Etudiant.get(19));
		System.out.println(p.liste_Etudiant.get(22));
		System.out.println(p.liste_Etudiant.get(26));*/
		for(Object o : p.liste_couples_intermediaires){
			System.out.println(o);
		}
		

	}

}
