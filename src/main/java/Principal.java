import com.google.common.io.Files;
import utilities.UTranslate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final String pathName = "directory"; //Example: C:/Pelicula/subtitulos/ *.srt ....
    private static String nameFile;
    private static String nameFileOut;
    private static String[] listNamesFile;

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.nanoTime();
        int c = 0;
        File path = new File(pathName);
        if (path.exists()) {
            File listFiles[] = path.listFiles();
            listNamesFile = new String[listFiles.length];
            for (File tmp : listFiles) {
                String name = tmp.getName();
                if (Files.getFileExtension(tmp.getName()).equals("srt") && !name.contains("_es")) listNamesFile[c++] = tmp.getName();
            }
        }
        for (int x = 0; x < c; x++) {
            nameFile = listNamesFile[x];
            nameFileOut = nameFile.substring(0, nameFile.length() - 4) + "_es.srt";
            UTranslate translate = new UTranslate();
            List<String> list = new ArrayList<>(), listData = new ArrayList<>(), tmpData = new ArrayList<>();
            File inFile = new File(pathName + nameFile), outFile = new File(pathName + nameFileOut);
            if (!outFile.exists()) {
                try {
                    outFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Scanner in = new Scanner(inFile);
            PrintWriter out = new PrintWriter(outFile);
            int cont = 0, indice = 0;
            while (in.hasNextLine()) {
                String tmp = in.nextLine();
                if (cont != 2 && tmp.trim().length() > 0) listData.add(tmp);
                if (cont == 2) {
                    list.add(tmp);
                    cont = -2;
                }
                cont++;
                if (list.size() == 100) {
                    tmpData.addAll(translate.serviceTranslate(list));
                    list.clear();
                }
            }
            tmpData.addAll(translate.serviceTranslate(list));
            for (int i = 0; i < listData.size(); i++) {
                out.println(listData.get(i));
                if (i % 2 == 1) {
                    out.println(tmpData.get(indice++));
                    out.println();
                }
            }
            in.close();
            out.close();
        }
        long endTime = System.nanoTime() - startTime;
        System.out.println(endTime + " nanoSeconds");
        System.out.println(((double) endTime / 1_000_000_000.0) + " seconds.");
    }
}
