package thedezine.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import java.lang.reflect.Field;

import thedezine.android.utils.TypefaceUtil;


public class App extends Application {

    @Override
    public void onCreate() {
      //  TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/OpenSans-Regular.ttf");

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/MavenPro.ttf");
    }


    public static class FontsOverride {


        public static void setDefaultFont(Context context,
                                          String staticTypefaceFieldName, String fontAssetName) {
            final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                    fontAssetName);
            replaceFont(staticTypefaceFieldName, regular);
        }

         static void replaceFont(String staticTypefaceFieldName,
                                          final Typeface newTypeface) {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
