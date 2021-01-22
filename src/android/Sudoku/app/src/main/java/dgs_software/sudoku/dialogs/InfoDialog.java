package dgs_software.sudoku.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import dgs_software.sudoku.R;

public class InfoDialog extends Dialog {

    public Activity activity;

    public InfoDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);
    }

}
