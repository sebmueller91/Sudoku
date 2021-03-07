package dgs_software.sudoku.config;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import dgs_software.sudoku.data.SaveDataProvider;

public class LanguageConfig {
    // region Definition of supported languages
    public enum SUPPORTED_LANGUAGES {GERMAN, ENGLISH}
    public static SUPPORTED_LANGUAGES stringToSupportedLanguage(String string) {
        if (string.toUpperCase().equals("ENGLISH") || string.toUpperCase().equals("ENGLISCH")) {
            return SUPPORTED_LANGUAGES.ENGLISH;
        } else if (string.toUpperCase().equals("GERMAN") || string.toUpperCase().equals("DEUTSCH")) {
            return SUPPORTED_LANGUAGES.GERMAN;
        } else {
            return null;
        }
    }
    // endregion Definition of supported languages

    public static void setAppLanguage(Context context) {
        SaveDataProvider saveDataProvider = new SaveDataProvider(context);
        SUPPORTED_LANGUAGES languageOverride = saveDataProvider.load_languageOverride();

        Locale locale;
        if (languageOverride == SUPPORTED_LANGUAGES.GERMAN) {
            locale = Locale.GERMAN;
        } else {
            locale = Locale.ENGLISH;
        }

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
