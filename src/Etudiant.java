
public class Etudiant {
	
	private static int nb=0; // nombre d'étudiant créés
	private final int id;
	private final String nom, prenom;
	
	/**
	 * tableau des appreciation de l'etudiant a donné aux autres eleve 
	 * chaque case du tableau contient zéro ou plusieurs étudiants 
	 */
	private AppreciationEtu [] classification_appreciation = 
		{
			new AppreciationEtu(Appreciation.TRES_BIEN),
			new AppreciationEtu(Appreciation.BIEN),
			new AppreciationEtu(Appreciation.ASSEZ_BIEN),
			new AppreciationEtu(Appreciation.PASSABLE),
			new AppreciationEtu(Appreciation.INSUFFISANT),
			new AppreciationEtu(Appreciation.A_REJETER)
		};
	
	public Etudiant(String n, String p){
		this.nom = n;
		this.prenom = p;
		nb++;
		this.id = nb;
	
	}
	
	/**
	 * Ajoute un Etudiant a un AppreciationEtu en fonction de l'appreciation reçu
	 * @param a
	 * @param e
	 */
	public void ajouterEtudiant(Appreciation a, Etudiant e){
		int i=0;
		switch(a){
			case TRES_BIEN:
				i=0;
				break;
			case BIEN:
				i=1;
				break;
			case ASSEZ_BIEN:
				i=2;
				break;
			case PASSABLE:
				i=3;
				break;
			case INSUFFISANT:
				i=4;
				break;
			case A_REJETER:
				i=5;
				break;
		}
		classification_appreciation[i].add(e);
		/*Appreciation appr = classification_appreciation[i].getEnTete();
		while(i< classification_appreciation.length && appr != a ){
			appr = classification_appreciation[i].getEnTete();
			System.out.print(i +" ");
			if(classification_appreciation[i].getEnTete() == (a)){
				classification_appreciation[i].add(e);
			}else{
				i++;
			}
			
		}
		System.out.print("\n");*/
		
		
	}
	
	/**
	 * retourne la meilleure appréciation que l'étudiant ait donnée
	 * correspond à la tête de case du tableau classification_appreciation 
	 * avec le plus petit indice et dont la liste n'est pas vide 
	 * @return
	 */
	public Appreciation getMeilleureNote(){
		int i = 0;

		while(classification_appreciation[i].isEmpty() && i < classification_appreciation.length)
			i++;
		return classification_appreciation[i].getEnTete();
		
	}
	
	/**
	 * Donne la note donnee à l'étudiant 
	 * @param e : un etudiant 
	 * @return appreciation que l'élève a donné à cet étudiant
	 */
	public Appreciation noteDonnee(Etudiant e){
		Appreciation a = null;
		int i = 0;
		while (i < classification_appreciation.length && a == null){
			if(classification_appreciation[i].contient(e) ) // si l'eleve appartient a la liste 
				a = classification_appreciation[i].getEnTete();
				// alors on peut connaitre la note qu'on lui a donné
			i++;
		}
		return a;
	}
	
	
	
	public final int getId() {
		return id;
	}

	public final String getNom() {
		return nom;
	}

	public final String getPrenom() {
		return prenom;
	}

	public final AppreciationEtu[] getClassification_appreciation() {
		return classification_appreciation;
	}

	
	public String toString(){
		String s = "Etudiant n°"+this.id+" prenom: "+this.prenom +
				" nom: "+this.nom + "\nAppreciation";
		for(Object o: classification_appreciation){
			s+= o.toString()+"\n";
		}
		return s;
	}
	
	public boolean equals(Object o){
		Etudiant e = (Etudiant) o;
		return e.id == this.id;
		
	}
	
	/**
	 * le poids que creerai une assiocation entre
	 * 	l'etudiant actuel et l'etudiant e
	 * @param e
	 * @return
	 */
	public int getPoids(Etudiant e){
		int p = 0;
		Appreciation a = this.noteDonnee(e);
		p += Appreciation.distance(getMeilleureNote(), a);
		p+= Appreciation.distance(e.getMeilleureNote(), e.noteDonnee(this));
		return p;
	}
	
	
	

}
