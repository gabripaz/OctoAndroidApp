package model;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speaker {
    private static TextToSpeech engine;


    public static void speak(Context context, String text){
        engine = new TextToSpeech(context,
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i == TextToSpeech.SUCCESS){
                            int res = engine.setLanguage(Locale.US);
                            switch (res){
                                case TextToSpeech.LANG_MISSING_DATA:
                                case TextToSpeech.LANG_NOT_SUPPORTED:
                                    Log.e("TTS","Language not supported");
                                    break;
                            }
                        } else{
                            Log.e("TTS","Initialization Failed");
                        }
                    }
                });
        speak(text);
        destroy();

    }

    private static void speak(String text) {
        engine.setPitch(1f);
        engine.setSpeechRate(1f);
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    protected static void destroy() {
        if (engine != null) {
            engine.stop();
            engine.shutdown();
        }
    }
}

