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

public class ChooseDifficultyDialog extends Dialog {

    public Activity activity;
    public Dialog d;
    public Button yes, no;

    public ChooseDifficultyDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        setContentView(R.layout.dialog_choose_difficulty);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button easyButton = (Button) findViewById(R.id.button_easy);
        Button normalButton = (Button) findViewById(R.id.button_normal);
        Button hardButton = (Button) findViewById(R.id.button_hard);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickedAction(v.getContext(), Sudoku.Difficulty.EASY);
            }
        });
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickedAction(v.getContext(), Sudoku.Difficulty.MEDIUM);
            }
        });
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickedAction(v.getContext(), Sudoku.Difficulty.HARD);
            }
        });
    }

    public void buttonClickedAction(Context context, Sudoku.Difficulty difficulty) {
        // Create bundle to tell initialize Sudoku Activity with the correct chosen difficulty
        Intent intent = new Intent(context, SudokuPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Sudoku.Difficulty.class.toString(),difficulty.getIntVal());
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}