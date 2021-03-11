package dgs_software.sudoku.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.SudokuPlayActivity;
import dgs_software.sudoku.config.LanguageConfig;

public class SudokuPlayWrongDialog extends Dialog {
    public SudokuPlayActivity sudokuPlayActivity;
    public Dialog d;
    public Button yes, no;

    public SudokuPlayWrongDialog(SudokuPlayActivity a) {
        super(a);
        d = this;
        this.sudokuPlayActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        LanguageConfig.setAppLanguage(getContext());
        setContentView(R.layout.dialog_sudoku_play_sudokuwrong);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button closeButton = (Button) findViewById(R.id.button_gamewrong_close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudokuPlayActivity.refreshUI();
                d.cancel();
            }
        });
    }
}