import java.util.ArrayList;


public class Etudiant implements Comparable{
	
	/**
	 *  nombre d'étudiant créés
	 */
	private static int nb=0; 
	
	/**
	 * id de l'étudiant 
	 */
	private final int id; 
	private final String nom, prenom;
	
	/**
	 * Mention Majoritaire de l'étudiant
	 * c'est à dire la médiane de toutes les notes qu'il lui ont été données 
	 */
	private MentionMajoritaire mentionMajoritaire; 
	
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
	
	/**
	 * Construits un étudiants 
	 * son id corresponds aux nombre d'étudiants déjà créés 
	 * lorsque celui a été créé
	 * @param n : nom
	 * @param p : prenom
	 */
	public Etudiant(String n, String p){
		this.nom = n;
		this.prenom = p;
		nb++;
		this.id = nb;
		this.mentionMajoritaire  = new MentionMajoritaire((Appreciation.TRES_BIEN), 0);
	
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
	}
	
	/**
	 * Donne le nombre d'étudiants ayant l'appreication a 
	 * dans la liste des étudiants noté par l'étudiant actuel
	 * @param a
	 * @return
	 */
	public int nbEtu(Appreciation a){
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
		
		return classification_appreciation[i].size();
	}
	
	/**
	 * tri chaque étudiant de la liste des etudiants notés par l'étudiant 
	 * en fonction de leur mention majoritaire
	 */
	public void triEtudiant(){
		for(AppreciationEtu ae : this.classification_appreciation)
			ae.triEtudiant();
		
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
	
	
	public MentionMajoritaire getMentionMajoritaire(){
		return this.mentionMajoritaire;
	}
	
	
	/**
	 * Recalcule (ou calcule pour la première fois) la mention majoritaire de l'étudiant)
	 * liste d'appreciation triée par ordre décroissant
	 * @param listeAppreciation : 
	 * @return
	 */
	public void setMentionMajoritaire( ArrayList <Appreciation> listeAppreciation){ 
		/*ATTENTION : méthode à appeler au moins une fois
		 *  pour mettre les bonnes valeurs dans l'attribut mention majoritaire 
		 */
		int n = (int) Math.ceil((double)listeAppreciation.size()/2.00); //pour avoir l'entier supérieur (pour le calcul de la médiane)  
		Appreciation majoritaire = Appreciation.TRES_BIEN ; 
		majoritaire = listeAppreciation.get(n);
		int nombreMention = 0;
		for(int k=0; k<listeAppreciation.size(); k++){
			
			if(k<n){ //du côté TB
				nombreMention = nombreMention+ Appreciation.distance(listeAppreciation.get(k),majoritaire);
			}
			else {
				nombreMention = nombreMention - Appreciation.distance(listeAppreciation.get(k),majoritaire);
			}
		}
		mentionMajoritaire.setMention(majoritaire);
		mentionMajoritaire.setN(nombreMention);
	
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Etudiant e = (Etudiant)o;
		return mentionMajoritaire.compareTo(e.mentionMajoritaire);
	}
	
	
	

}
