package dgs_software.sudoku.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dgs_software.sudoku.data.SaveDataProvider;
import dgs_software.sudoku.dialogs.ChooseDifficultyDialog;
import dgs_software.sudoku.R;
import dgs_software.sudoku.dialogs.InfoDialog;
import dgs_software.sudoku.model.Sudoku;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button playSudokuButton = (Button) findViewById(R.id.PlaySudokuButton);
        Button solveSudokuButton = (Button) findViewById(R.id.solveSudokuButton);
        Button infoButton = (Button) findViewById(R.id.infoButton);

        playSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataProvider saveDataProvider = new SaveDataProvider(getApplicationContext());
                Sudoku savedSudoku = saveDataProvider.loadSudokuPlay_sudoku();
                if (savedSudoku == null) {
                    // No saved sudoku available -> bring on "Select Difficulty" dialog
                    ChooseDifficultyDialog chooseDifficultyDialog = new ChooseDifficultyDialog(MainMenuActivity.this);
                    chooseDifficultyDialog.show();
                } else {
                    // There is a saved sudoku -> Start SudokuPlay activity directly
                    Intent intent = new Intent(getApplicationContext(), SudokuPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Sudoku.Difficulty.class.toString(), Sudoku.Difficulty.RELOAD_EXISTING.getIntVal());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        solveSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SudokuSolverActivity.class);
                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialog infoDialog = new InfoDialog(MainMenuActivity.this);
                infoDialog.show();
            }
        });
    }
}