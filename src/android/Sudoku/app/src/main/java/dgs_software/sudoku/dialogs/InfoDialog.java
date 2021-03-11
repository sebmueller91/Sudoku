package dgs_software.sudoku.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.LanguageConfig;

public class InfoDialog extends Dialog {

    public Activity activity;

    public InfoDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageConfig.setAppLanguage(getContext());
        setContentView(R.layout.dialog_mainmenu_info);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
