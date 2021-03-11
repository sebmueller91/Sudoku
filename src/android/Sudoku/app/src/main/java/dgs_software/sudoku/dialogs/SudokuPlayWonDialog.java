package dgs_software.sudoku.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.MainMenuActivity;
import dgs_software.sudoku.activities.SudokuPlayActivity;
import dgs_software.sudoku.config.LanguageConfig;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public class SudokuPlayWonDialog extends Dialog {
    public SudokuPlayActivity sudokuPlayActivity;
    public Dialog d;
    Context context;
    public Button yes, no;

    public SudokuPlayWonDialog(SudokuPlayActivity a) {
        super(a);
        d = this;
        context = a.getApplicationContext();
        this.sudokuPlayActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        LanguageConfig.setAppLanguage(getContext());
        setContentView(R.layout.dialog_sudoku_play_gamewon);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yesButton = (Button) findViewById(R.id.button_gamewon_yes);
        Button noButton = (Button) findViewById(R.id.button_gamewon_no);

        TextView difficultyTextView = (TextView) findViewById(R.id.dialogGameWon_difficulty);
        TextView timeneededTextView = (TextView) findViewById(R.id.dialogGameWon_timeneeded);

        difficultyTextView.setText(Utils.getDifficultyAsString(context, sudokuPlayActivity.getSudokuModel().getDifficulty()));
        timeneededTextView.setText(Utils.formatSecondsAsTime(sudokuPlayActivity.getSudokuModel().getElapsedSeconds()));

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create bundle to tell initialize Sudoku Activity with the same difficulty as the last game
                Intent intent = new Intent(context, SudokuPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Sudoku.Difficulty.class.toString(), sudokuPlayActivity.getSudokuModel().getDifficulty().getIntVal());
                intent.putExtras(bundle);
                sudokuPlayActivity.startActivity(intent);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete saved sudoku (if any) and go back to main menu
                sudokuPlayActivity.getSaveDataProvider().deleteSudokuPlay_sudoku();
                Intent intent = new Intent(context, MainMenuActivity.class);
                sudokuPlayActivity.startActivity(intent);
            }
        });
    }
}