/**
 * Fonction pour la creation de trinome 
 * @author enzo
 *
 */
public class TrinomePondere extends BinomePondere {
	private final Etudiant etudiant3;
	
	public TrinomePondere(Etudiant e1, Etudiant e2, Etudiant e3, int poids) {
		super(e1, e2, poids);
		// TODO Auto-generated constructor stub
		this.etudiant3 = e3;
	}
	
	
	public boolean appartient(Etudiant e){
		return super.appartient(e) || e.equals(etudiant3);
	}
	
	public Etudiant getEtudiant3(){
		return this.etudiant3;
	}
	
	
	public String toString(){
		return super.getEtudiant1().getPrenom()+" " +super.getEtudiant1().getNom()+
				" avec " + super.getEtudiant2().getPrenom()+ " "
				+ super.getEtudiant2().getNom() + " et "+
				etudiant3.getPrenom() + " " +
				etudiant3.getNom() + " avec un poids de : "+
				super.getPoids();
	}

}
