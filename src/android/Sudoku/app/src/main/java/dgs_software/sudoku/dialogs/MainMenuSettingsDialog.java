package dgs_software.sudoku.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.Locale;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.MainMenuActivity;
import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.config.LanguageConfig;
import dgs_software.sudoku.data.SaveDataProvider;
import dgs_software.sudoku.utils.Logger;

public class MainMenuSettingsDialog extends Dialog {

    // region Attributes
    private MainMenuActivity m_activity;
    public MainMenuActivity getActivity() {
        return this.m_activity;
    }
    public void setActivity(MainMenuActivity activity) {
        this.m_activity = activity;
    }
    // endregion Attributes

    public MainMenuSettingsDialog(@NonNull Context context, MainMenuActivity activity) {
        super(context);
        setActivity(activity);
    } // This is a meaningless comment

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageConfig.setAppLanguage(getContext());
        setContentView(R.layout.dialog_mainmenu_settings);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Create SaveDataProvider
        final SaveDataProvider saveDataProvider = new SaveDataProvider(getContext());

        // Get OS language and user language preference
        String osLanguageString = Locale.getDefault().getDisplayLanguage();
        final LanguageConfig.SUPPORTED_LANGUAGES osLanguage =
                (osLanguageString.equals("de")) ?
                        LanguageConfig.SUPPORTED_LANGUAGES.GERMAN :
                        LanguageConfig.SUPPORTED_LANGUAGES.ENGLISH; // English is treated as default if any not supported language is set
        final LanguageConfig.SUPPORTED_LANGUAGES languageOverride = saveDataProvider.load_languageOverride();


        // Create items for spinner with the currently selected language on top
        String[] items;
        if (languageOverride == LanguageConfig.SUPPORTED_LANGUAGES.GERMAN
                || (languageOverride == null && osLanguage == LanguageConfig.SUPPORTED_LANGUAGES.GERMAN)) {
            items = new String[]{getActivity().getResources().getString(R.string.language_german), getActivity().getResources().getString(R.string.language_english)};
        } else {
            items = new String[]{getActivity().getResources().getString(R.string.language_english), getActivity().getResources().getString(R.string.language_german)};
        }

        // Create language spinner
        Spinner dropdown = (Spinner) findViewById(R.id.languageSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                String selectedItemText = (String) adapterView.getItemAtPosition(position);
                LanguageConfig.SUPPORTED_LANGUAGES selectedLanguage = LanguageConfig.stringToSupportedLanguage(selectedItemText); // selected language in the dropdown
                LanguageConfig.SUPPORTED_LANGUAGES languageOverride = saveDataProvider.load_languageOverride(); // currently active language override

                if (selectedLanguage != null
                        && ((languageOverride !=null && selectedLanguage != languageOverride)
                        || (languageOverride ==null && selectedLanguage != osLanguage))) {
                    if (selectedLanguage.equals(osLanguage) == false) {
                        saveDataProvider.save_languageOverride(selectedLanguage);
                    } else {
                        saveDataProvider.delete_LanguageOverride();
                    }
                    LanguageConfig.setAppLanguage(getContext());

                    // Restart main manu activity to reload language texts
                    restartActivity(getActivity());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Logger.LogWarning( "MainMenuSettingsDialog: onNothingSelected called on LanguageDialog which should not happen!");
            }
        });
    }

    // Restarts the given activity
    private static void restartActivity(Activity activity){

        Intent intent=new Intent();
        intent.setClass(activity, activity.getClass());
        activity.startActivity(intent);
        activity.finish();
    }
}
