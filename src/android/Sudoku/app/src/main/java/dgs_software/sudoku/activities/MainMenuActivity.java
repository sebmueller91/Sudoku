package dgs_software.sudoku.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dgs_software.sudoku.dialogs.ChooseDifficultyDialog;
import dgs_software.sudoku.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button playSudokuButton = (Button) findViewById(R.id.PlaySudokuButton);
        Button solveSudokuButton = (Button) findViewById(R.id.solveSudokuButton);

        playSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDifficultyDialog cdd=new ChooseDifficultyDialog(MainMenuActivity.this);
                cdd.show();
            }
        });

        solveSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SudokuSolverActivity.class);
                startActivity(intent);
            }
        });
    }
}