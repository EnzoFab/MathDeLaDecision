


/**
* Profil
*/
public class MentionMajoritaire implements Comparable{

	private Appreciation majoritaire; //correspond à la mention majoritaire
	private int n; //correspond à somme des distances des mentions qui sont différentes de la mention majoritaire
	
	public MentionMajoritaire( Appreciation a, int n){
		this.majoritaire = a;
		this.n = n;
		
	}

	/**
	 * Retourne la mention majoritaire
	 */
	
	public Appreciation getAppreciation(){
		return this.majoritaire;
	}
	

	/**
	 * Retourne le nombre associé à la mention majoritaire
	 */
	public int getNombre(){
		return n;
	}
	
	/**
	 * Modifie la mention majoritaire
	 * @param a
	 */
	public void setMention(Appreciation a){
		this.majoritaire = a;
	}
	
	/**
	 * Modifie le nombre associé à la mention majoritaire
	 * @param nb
	 */
	public void setN( int nb){
		n = nb;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int resultat = 0;
		MentionMajoritaire mm = (MentionMajoritaire)o;
		if(mm.getAppreciation().equals(majoritaire)){
			// les deux mentions sont égale
			Integer i = new Integer(n);
			Integer i2 = new Integer(mm.getNombre());
			resultat = i.compareTo(i2);
		}else{
			resultat = -majoritaire.compareTo(mm.majoritaire);
		}
		return resultat;
	}
	
	
}
