/**
*
* @author Dilara Çetin dilara.cetin2@ogr.sakarya.edu.tr
* @since 13.03.2024
* <p>
*Kullanıcıdan alınan URL için klonlama methodu ve sınıfları analiz esnasında kullanılıcak methodlar mevcut.
* </p>
*/
package GitHub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class gitHub {
	
	private String githubRepo;

    public gitHub(String githubRepo) { //Yapıcı fonksiyon
        this.githubRepo = githubRepo;
    }

    public void klonla() { //Klonlama işlemi
        File hedefDizin = new File("klon");
        if (hedefDizin.exists()) {
            dosyaSilme(hedefDizin); //Eğer dosya zaten doluysa bulunan dosyayı silme işlemi
        }
        hedefDizin.mkdir();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("git", "clone", githubRepo, "klon");

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); 
            if (exitCode == 0) {
                System.out.println("GitHub deposu klonlandı.");
                javaMi(hedefDizin); //Klonlanan dosyadaki .java uzantılı dosyaları bulma
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("GitHub deposu klonlanırken bir hata oluştu.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void dosyaSilme(File directory) {
        if (directory.isDirectory()) {
            File[] children = directory.listFiles();
            if (children != null) {
                for (File child : children) {
                    dosyaSilme(child);
                }
            }
        }
        directory.delete();
    }
    
    private void javaMi(File dizin) { //Dosyanın .java uzantılı olup olmadığını bulan rekürsif fonksiyon
        if (dizin.isDirectory()) {
            File[] files = dizin.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                    	javaMi(file); 
                    } else {
                        if (file.getName().endsWith(".java")) {
                        	try {
                                BufferedReader reader = new BufferedReader(new FileReader(file));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    if (line.contains("class")) { //Dosyada class olup olmadığını kontrol ediyor
                                        System.out.println("Class Adı:" + file.getName());
                                        hesapla(file);
                                    }
                                }
                                reader.close(); 
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void hesapla(File dosya) { //Sınıfın hesaplama methodlarını çağırarak sonuçları ekrana yazdıran fonksiyon
    	//System.out.println("Sınıf: " + dosya.getName());
    	javaDocSayisi=0;
    	yorumSayisi=0;
    	kodSatirSayisi=0;
    	fonksiyonSayisi=0;
    	LOC=0;
    	yuzde=0;
    	try (BufferedReader readerJavaDoc = new BufferedReader(new FileReader(dosya))) {
    		System.out.println("JavaDoc Satır Sayısı: "+ javaDocSayisiHesapla(readerJavaDoc));
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	try (BufferedReader readerYorum = new BufferedReader(new FileReader(dosya))) {
    		System.out.println("Yorum Satır Sayısı: "+ yorumSayisiHesapla(readerYorum));
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	try (BufferedReader readerKod = new BufferedReader(new FileReader(dosya))) {
    		System.out.println("Kod Satır Sayısı: "+ kodSatirSayisiHesapla(readerKod));
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	try (BufferedReader readerLOC = new BufferedReader(new FileReader(dosya))) {
    		System.out.println("LOC: "+ LOChesapla(readerLOC));
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	try (BufferedReader readerFonk = new BufferedReader(new FileReader(dosya))) {
    		System.out.println("Fonksiyon Sayısı: "+ fonksiyonSayisiBul(readerFonk));
    	} catch (IOException e) {
            e.printStackTrace();
        }
    
    	System.out.println("Yorum Sapma Yüzdesi: "+ sapmaYuzdesi());
    	System.out.println("---------------------------------");
    }
    
    int javaDocSayisi;
    private int javaDocSayisiHesapla (BufferedReader reader) throws IOException {
    	String line;
    	boolean javaDocMu = false;
    	while ((line = reader.readLine()) != null) {
    		
    		line = line.trim();
    		
    	    if (line.startsWith("/**")) {
    	        javaDocMu = true;
    	    }
    	    else if (javaDocMu && !line.endsWith("*/")) {
    	    	javaDocSayisi++;
    	    } 
    	    if (line.endsWith("*/")) 
    	    {
    	        javaDocMu = false;
    	    }
    	}
        return javaDocSayisi;
    }
    
    int yorumSayisi;
    private int yorumSayisiHesapla (BufferedReader reader) throws IOException {
    	
    	String line;
    	boolean yorumBasladi = false;
    	
    	while ((line = reader.readLine()) != null) {
    		line = line.trim();
    	    if (line.startsWith("/*")&&!(line.startsWith("/**")))
    	    {
    	    	yorumBasladi = true;
    	    	yorumSayisi--;
    	    } 
    	    else if(yorumBasladi && line.startsWith("*"))
    	    {
    	    	yorumSayisi++;
    	    }
    	    else if(line.contains("//"))
    	    {
    	    	yorumSayisi++;
    	    }
    	    
    	    if (line.contains("*/")) 
    	    {
    	    	yorumBasladi = false;
    	    }
    	}
       
        return yorumSayisi;
    }
    
    int kodSatirSayisi;
    private int kodSatirSayisiHesapla (BufferedReader reader) throws IOException {
    	String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("//") && !line.startsWith("/*") && !line.startsWith("*") && !line.startsWith("*/")) {
                kodSatirSayisi++;
            }
        }
        return kodSatirSayisi;
    }
    
    int LOC ;
    private int LOChesapla(BufferedReader reader) throws IOException {
        
        String line;
        while ((line = reader.readLine()) != null) {
            LOC++;
        }
        return LOC;
    }
    
    int fonksiyonSayisi; 
    private int fonksiyonSayisiBul(BufferedReader reader) throws IOException {
        
        String line;
        boolean fonksiyonIcerisinde = false;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.contains("{")) {
                fonksiyonIcerisinde = true;
            }
            if (fonksiyonIcerisinde && line.contains("}")) {
                fonksiyonIcerisinde = false;
                fonksiyonSayisi++;
            }
        }
        return fonksiyonSayisi;
    }
    
    double yuzde;
    private double sapmaYuzdesi() {
    	
    	double YG = ((javaDocSayisi + yorumSayisi) * (0.8))/ fonksiyonSayisi;
        double YH = ((double)kodSatirSayisi / (double)fonksiyonSayisi) * (0.3);
        
        yuzde= ((100 * YG) / YH) - 100;
        return yuzde;
    }
    
}
