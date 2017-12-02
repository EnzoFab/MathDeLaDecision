import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVInterpreteur {
	/*public static Etudiant[] listeEtudiantFromCSV(int nbEtudiant, String path, String motif){
		Etudiant [] liste_Etudiant = new Etudiant[nbEtudiant];
		String line = "";
		BufferedReader br = null;
		int i = 0;
		
		try {

            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null && i <nbEtudiant) {

                // use comma as separator
                String[] country = line.split(motif);

                System.out.println(country + " " +i);// test

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		
		
		return liste_Etudiant;
	}*/
	
	
	
	/**
	 * Return a list which contains in each line the data from the csv file 
	 * @param path: String file path
	 * @return
	 * @throws IOException
	 */
	public static List<String> readFile(String path) throws IOException {

        List<String> result = new ArrayList<String>();

        File file = getResource(path);
        
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            result.add(line);
        }

        try {
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        return result;
    }
	
	 private static String getResourcePath(String fileName) {
	       final File f = new File("");
	       final String dossierPath = f.getAbsolutePath() + File.separator + fileName;
	       return dossierPath;
	   }
	
	
	 private static File getResource(String fileName) {
	       final String completeFileName = getResourcePath(fileName);
	       File file = new File(completeFileName);
	       return file;
	   }
	
}
