import java.util.Comparator;


/**
 * associe 2 ou 3 etudiants ensemble
 * @author enzo
 *
 */
public class BinomePondere implements Comparable{
	
	
	private Etudiant etudiant1, etudiant2;
	
	/**
	 * poids de l'association 
	 * correspond à la somme des distance 
	 * entre le meilleur choix de l'eleve 1 et la position de l'eleve 2
	 * par exemple: E1 donné la note comme note maximale TRES_BIEN
	 * E1 a donné BIEN à E2 donc la distance sera de 1 
	 * Inversement si la meilleure appréciation de E2 est TRES_BIEN
	 * 	et qu'il a donné TRES_BIEN a E1 la distance est 0
	 * Donc le poids est 1
	 * Plus le poids est petit et plus l'association est optimale
	 */
	private int poids;
	
	/**
	 * @param e1
	 * @param e2
	 * @param poids
	 */
	public BinomePondere (Etudiant e1, Etudiant e2, int poids ){
		this.etudiant1 = e1;
		this.etudiant2 = e2;
		this.poids = poids;
	}

	public  Etudiant getEtudiant1() {
		return etudiant1;
	}

	public Etudiant getEtudiant2() {
		return etudiant2;
	}

	public int getPoids() {
		return poids;
	}
	
	/**
	 * modifie un couple 
	 * @param e1
	 * @param e2
	 * @param poids
	 */
	public void setEtudiant(Etudiant e1, Etudiant e2, int poids){
		this.etudiant1 = e1;
		this.etudiant2 = e2;
		this.poids = poids;
	}
	
	/**
	 * verifie si un eleve appartient au couple 
	 * @param e
	 * @return
	 */
	public boolean appartient(Etudiant e){
		return this.etudiant1.equals(e) || this.etudiant2.equals(e);
	}
	
	/**
	 * Copie les valeurs d'un couple dans le couple actuel 
	 * @param c
	 */
	public void maj(BinomePondere c){
		this.etudiant1 = c.etudiant1;
		this.etudiant2 = c.etudiant2;
		poids = c.poids;
	}
	
	/**
	 * transforme le couple en groupe 
	 * @return Groupe 
	 */
	public Groupe creerGroupe(){
		return new Groupe(this.etudiant1, this.etudiant2);
	}
	
	public String toString(){
		if(this.etudiant1 != null && this.etudiant2 != null){
			return this.etudiant1.getPrenom()+ " "+ this.etudiant1.getNom()
					+ " avec " +etudiant2.getPrenom()+ "  " + this.etudiant2.getNom()
					+ " avec un poids de : " + this.poids;
		}else{
			return "couple incomplet";
		}
		
	}
	
	/**
	 * On regarde la l'appreciation que l'etudiant 1 a donné à l'étudiant 2
	 * on compte pour combien d'élèves l'étudiant 1 à donné la même appreciation
	 * meme chose du coté de l'étudiant 2 
	 * la fonction renvoi le nombre minimal entre les deux nombres
	 * @return
	 */
	public int nbEtuAimeMin(){
		int resultat = 0;
		Appreciation a = etudiant1.noteDonnee(etudiant2);
			// note donnée par etudiant1 à etudiant2
		Appreciation a2 = etudiant2.noteDonnee(etudiant1);
			// note donnée par etudiant2 à etudiant1
		int nb1 = etudiant1.nbEtu(a);
		int nb2 = etudiant1.nbEtu(a2);
		if(nb1 <= nb2)
			resultat = nb1;
		else
			resultat = nb2;
		return resultat;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		int resultat=0;
		BinomePondere c = (BinomePondere) arg0;
		Integer i1 = new Integer(this.poids);
		
		Integer i2 = new Integer(c.poids);
		if(c.poids != this.poids) // si les deux poids sont différents 
		{
			resultat =i1.compareTo(i2);
			
		}
		else {
			if(  !c.appartient(etudiant1) && !c.appartient(etudiant2)){
				
				Integer minBp1 = new Integer(this.nbEtuAimeMin());
				Integer minBp2 = new Integer(c.nbEtuAimeMin());
				resultat = -minBp1.compareTo(minBp2);
			}else{
				if(c.etudiant1.equals(etudiant1)){
					
					Appreciation a = etudiant2.noteDonnee(etudiant1);
					Integer min1 = new Integer(etudiant2.nbEtu(a));
					
					Appreciation a2 = c.etudiant2.noteDonnee(c.etudiant1);
					Integer min2 = new Integer(c.etudiant2.nbEtu(a2));
					
					resultat = min1.compareTo(min2);
					
				}else if(c.etudiant1.equals(etudiant2)){
					Appreciation a = etudiant1.noteDonnee(etudiant2);
					Integer min1 = new Integer(etudiant1.nbEtu(a));
					
					Appreciation a2 = c.etudiant2.noteDonnee(c.etudiant1);
					Integer min2 = new Integer(c.etudiant2.nbEtu(a2));
					
					resultat = min1.compareTo(min2);
					
				}else if(c.etudiant2.equals(etudiant1)){
					System.out.println("True3");
					Appreciation a = etudiant2.noteDonnee(etudiant1);
					Integer min1 = new Integer(etudiant2.nbEtu(a));
					
					Appreciation a2 = c.etudiant1.noteDonnee(c.etudiant2);
					Integer min2 = new Integer(c.etudiant1.nbEtu(a2));
					
					resultat = min1.compareTo(min2);
					
				}else if(c.etudiant2.equals(etudiant2)){
					System.out.println("True4");
					Appreciation a = etudiant1.noteDonnee(etudiant2);
					Integer min1 = new Integer(etudiant1.nbEtu(a));
					
					Appreciation a2 = c.etudiant1.noteDonnee(c.etudiant2);
					Integer min2 = new Integer(c.etudiant1.nbEtu(a2));
					
					resultat = min1.compareTo(min2);
				}
				
			}
		}
			
		return resultat;
			// permet d'être trier en fonction du poids 
	}
	
	
	public boolean equals(Object o){
		BinomePondere bp = (BinomePondere)o;
		return (bp.etudiant1.equals(etudiant1) && bp.etudiant2.equals(etudiant2) ) ||
				(bp.etudiant1.equals(etudiant2) && bp.etudiant2.equals(etudiant1));
		
	}
	
	
	
	
	
}
