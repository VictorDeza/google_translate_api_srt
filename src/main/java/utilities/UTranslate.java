package utilities;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Builder;
import com.google.api.services.translate.Translate.Translations;
import com.google.api.services.translate.model.TranslationsListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class UTranslate {

    Translate t;
    Translations.List list;
    TranslationsListResponse response;
    public UTranslate(){
        try {
            t = new Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(), null)
                    .setApplicationName("translate_caption").build();
            list = null;
            response = new TranslationsListResponse();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> serviceTranslate(List<String> texts) {
        List<String> rspt = new ArrayList<>();
        try {
            list = t.new Translations().list(texts, "ES");
            list.setKey("keyGoogleTranslaApi"); //Generen su Key Google Cloud
            response = list.execute();
            response.getTranslations().stream().forEach(msj -> rspt.add(msj.getTranslatedText()));
        } catch (IOException e) {
            System.out.println(response);
            e.printStackTrace();
        }
        return rspt;
    }
}
