package model;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener {
    private static TextToSpeech textToSpeech ;
    public static void SpeakThis(Context context, String message){
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if(i == TextToSpeech.SUCCESS){ //validates the initialization
                        int res = textToSpeech.setLanguage(Locale.ENGLISH);
                        switch (res){
                            case TextToSpeech.LANG_MISSING_DATA:
                            case TextToSpeech.LANG_NOT_SUPPORTED:
                                Log.e("Text to Speech", "Language not supported");
                                break;
                        }
                    }else{
                        Log.e("Text to Speech", "Initialization Failed");
                    }
                }
            });
        }
        try{
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }catch (Exception e)
        {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }


    }

    //just for security
    public static void Destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.GERMAN);
            textToSpeech.setPitch(3);
            textToSpeech.speak("TESTIN 123 TESTING", TextToSpeech.QUEUE_FLUSH, null,null);
            while(textToSpeech.isSpeaking());
        }
    }
}

