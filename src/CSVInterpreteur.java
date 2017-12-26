import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class CSVInterpreteur {
	
	
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
	
	 /**
	  * créé le fichier path et écrit le message à l'interieur 
	  * @param path
	  * @param message
	  */
	 public static void createCSV(String path, String message){
		 Writer writer = null;

		 try {
		     writer = new BufferedWriter(new OutputStreamWriter(
		           new FileOutputStream(path), "utf-8"));
		     writer.write(message);
		 } catch (IOException ex) {
			 System.out.println("Erreur dans la creation du fichier");
		 } finally {
		    try {writer.close();} catch (Exception ex) {/*ignore*/}
		 }
	 }
}
