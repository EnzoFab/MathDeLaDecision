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

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		BinomePondere c = (BinomePondere) arg0;
		Integer i1 = new Integer(this.poids);
		
		Integer i2 = new Integer(c.poids);
		
		return i1.compareTo(i2); 
			// permet d'être trier en fonction du poids 
	}
	
	public boolean equals(Object o){
		BinomePondere bp = (BinomePondere)o;
		return (bp.etudiant1.equals(etudiant1) && bp.etudiant2.equals(etudiant2) ) ||
				(bp.etudiant1.equals(etudiant2) && bp.etudiant2.equals(etudiant1));
		
	}
	
	
	
	
	
}
