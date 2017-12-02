/**
 * association entre une etudiant et un binome 
 * @author enzo
 *
 */
public class TrinomeIntermediaire {
	private Etudiant e;
	private BinomePondere bp;
	private int poids ;
	
	
	public TrinomeIntermediaire(Etudiant e, BinomePondere bp) {
		super();
		this.e = e;
		this.bp = bp;
		
		poids = bp.getPoids();
		poids += e.getPoids(bp.getEtudiant1());
		poids += e.getPoids(bp.getEtudiant2());
		
		this.poids = poids;
	}


	public final Etudiant getEtudiant() {
		return e;
	}


	public final BinomePondere getBinome() {
		return bp;
	}


	public final int getPoids() {
		return poids;
	}
	
	public String toString(){
		return e.getPrenom()+" " +e.getNom()+
				" avec " + bp.getEtudiant1().getPrenom()+ " "
				+ bp.getEtudiant1().getNom() + " et "+
				bp.getEtudiant2().getPrenom() + " " +
				bp.getEtudiant2().getNom() + " avec un poids de : "+
				this.poids;
	}
	
	
	public TrinomePondere getTrinome(){
		return new TrinomePondere(this.e, this.bp.getEtudiant1(),
				this.bp.getEtudiant2(), poids);
	}
	
	
}
