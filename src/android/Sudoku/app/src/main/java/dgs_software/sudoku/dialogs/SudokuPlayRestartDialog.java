package dgs_software.sudoku.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.SudokuPlayActivity;
import dgs_software.sudoku.model.Sudoku;

public class SudokuPlayRestartDialog extends Dialog {
    public SudokuPlayActivity sudokuPlayActivity;
    public Dialog d;
    public Button yes, no;

    public SudokuPlayRestartDialog(SudokuPlayActivity a) {
        super(a);
        d = this;
        this.sudokuPlayActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        setContentView(R.layout.dialog_sudoku_play_restart);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yesButton = (Button) findViewById(R.id.button_restart_yes);
        Button noButton = (Button) findViewById(R.id.button_restart_no);


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudokuPlayActivity.restartGame();
                d.cancel();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });
    }
}