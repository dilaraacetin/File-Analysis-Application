/**
*
* @author Dilara Çetin dilara.cetin2@ogr.sakarya.edu.tr
* @since 13.03.2024
* <p>
* Main sınıfı main fonksiyonun olduğu ve programın ana işlemlerinin gerçekleştiği sınıftır. 
* Burada kullanıcıdan URL istendi, gitHub sınıfının methodları kullanılarak klonlandı ve analiz edildi.
* </p>
*/
package GitHub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import GitHub.gitHub;



public class main {

	public static void main(String[] args) {
		
		String repositoryUrl = null;
		
		System.out.println("GitHub depo linkini girin:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); //Kullanıcıdan istenilen URL'nin okunması
        try {
            repositoryUrl = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gitHub gitHubRepo = new gitHub(repositoryUrl); 
        gitHubRepo.klonla(); //Repository'nin klonlanması
        
        
        
        
        
	}

}
