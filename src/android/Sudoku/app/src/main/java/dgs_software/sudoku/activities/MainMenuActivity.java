package dgs_software.sudoku.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.LanguageConfig;
import dgs_software.sudoku.data.SaveDataProvider;
import dgs_software.sudoku.dialogs.ChooseDifficultyDialog;
import dgs_software.sudoku.dialogs.InfoDialog;
import dgs_software.sudoku.dialogs.MainMenuSettingsDialog;
import dgs_software.sudoku.model.Sudoku;

public class MainMenuActivity extends AppCompatActivity {

    // region Methods
    // When back key pressed -> Return to home screen instead of the previous acitities
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    // Create the buttons in the menu bar at the top
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle button activities of menu bar at the top
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.infoButton) {
            InfoDialog infoDialog = new InfoDialog(MainMenuActivity.this);
            infoDialog.show();
        } else if (item.getItemId() == R.id.settings_button) {
            MainMenuSettingsDialog settingsDialog = new MainMenuSettingsDialog(MainMenuActivity.this, this);
            settingsDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageConfig.setAppLanguage(this);

        setContentView(R.layout.activity_main_menu);

        Button playSudokuButton = (Button) findViewById(R.id.PlaySudokuButton);
        Button solveSudokuButton = (Button) findViewById(R.id.solveSudokuButton);

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
    }
}