import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author enzo
 * Fait l'association entre une appreciation et une liste d'etudiant 
 */
public class AppreciationEtu {
	
	
	private final Appreciation enTete;
	private ArrayList listEtudiant;
	
	/**
	 * 
	 * @param a
	 */
	public AppreciationEtu(Appreciation a){
		this.enTete = a;
		this.listEtudiant = new ArrayList();
	}
	
	/**
	 * ajoute une nouvel etudiant à  la liste
	 * @param e
	 */
	public void add(Etudiant e){
		this.listEtudiant.add(e);
	}
	
	/**
	 * permet de savoir s'il y a un etudiant dans la liste
	 * @return boolean
	 */
	public boolean isEmpty(){
		return this.listEtudiant.isEmpty();
	}
	

	public final Appreciation getEnTete(){
		return this.enTete;
	}
	
	public boolean contient(Etudiant e){
		return this.listEtudiant.contains(e);
	}
	
	
	public ArrayList getListEtudiant(){
		return this.listEtudiant; 
		// penser à faire une copie de cette liste 
		//pour ne pas supprimer les données qu'elle contient
	}
	
	/**
	 * trie chaque étudiant selon le critère de tri de la classe Etudiant
	 * @see Etudiant
	 */
	public void triEtudiant(){
		Collections.sort(this.listEtudiant);
	}
	
	public int size(){
		return this.listEtudiant.size();
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append(this.enTete.name()+"\n Etudiants:" + listEtudiant.size() +" \n");
		Etudiant e;
		if(listEtudiant.isEmpty()){
			s.append("aucun etudiants");
		}else{
			for(Object o: listEtudiant){
				e= (Etudiant)o;
				s.append(e.getId()+ " " +e.getPrenom()+ " "+e.getNom()+" ");
			}
			
		}
		s.append("\n");
		
		return s.toString();
	}
	
	

}
