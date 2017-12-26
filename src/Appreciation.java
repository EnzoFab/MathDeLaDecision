
public enum Appreciation {
	TRES_BIEN,
	BIEN,
	ASSEZ_BIEN,
	PASSABLE,
	INSUFFISANT,
	A_REJETER;
	
	/**
	 * donne la distance entre deux appreciation
	 * @param a1
	 * @param a2
	 * @return
	 */
	static int distance(Appreciation a1, Appreciation a2){
		return Math.abs(a2.ordinal() - a1.ordinal()) ; // valeur absolue
	}
	
	/**
	 * Traduit un string en une appreciation
	 * @param s
	 * @return
	 */
	static Appreciation appreciationFromString(String s){
		Appreciation a= Appreciation.PASSABLE;// par defaut ni bon ni mauvais
		switch(s.toUpperCase()){// on mets en majuscule au cas ou 
		case "TB":
			 a= Appreciation.TRES_BIEN;
			break;
		case "B":
			a= Appreciation.BIEN;
			break;
		case "AB":
			a= Appreciation.ASSEZ_BIEN;
			break;
		case "P":
			a= Appreciation.PASSABLE;
			break;
		case "I":
			a= Appreciation.INSUFFISANT;
			break;
		case "AR":
			a= Appreciation.A_REJETER;
			break;
		}
		return a;
	
		
	}
	
	 

}
