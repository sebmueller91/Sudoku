package dgs_software.sudoku.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;
import java.util.Set;

import dgs_software.sudoku.R;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;

public abstract class SudokuBaseActivity extends AppCompatActivity {
    public static final int
            ACTIVE_COLOR_BACKGROUND = Color.parseColor("#ffffcc"),
            ACTIVE_COLOR_FONT = Color.parseColor("#6495ED"),
            INACTIVE_COLOR_BACKGROUND = Color.parseColor("#FFFFFF"),
            INACTIVE_COLOR_FONT_FIXED = Color.parseColor("#000000"),
            INACTIVE_COLOR_FONT_NONFIXED = Color.parseColor("#3CB371"),
            INACTIVE_COLOR_FONT_EMPTY = Color.parseColor("#A9A9A9"); // TODO: Move to ressource file

    protected Cell activeCell = null;
    protected Sudoku sudokuModel;

    protected abstract void SetContentView();
    protected abstract Sudoku CreateSudokuModel();
    protected abstract void InstantiateButtons();
    public abstract void SudokuButtonClickedAction(int row, int col);
    public abstract void InputButtonClickedAction(int number);

    // When back key pressed -> Return to main menu instead of dialogs
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetContentView();
        GridLayout sudokuGrid = (GridLayout) findViewById(R.id.SudokuGridLayout);

        // CREATE SUDOKU MODEL
        sudokuModel = CreateSudokuModel();

        // SUDOKU GRID
        ViewGroup.LayoutParams gridLayoutParams = sudokuGrid.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels; // Screen width in pixels
        float sudokuGridPixelSize = 0.98f*screenWidth; // Sudoku grid shall fill the screen width
        gridLayoutParams.height = (int) sudokuGridPixelSize;
        gridLayoutParams.width = (int) sudokuGridPixelSize;
        sudokuGrid.setLayoutParams(gridLayoutParams);

        for (int i = 0; i < sudokuGrid.getRowCount(); i++) {
            for (int j = 0; j < sudokuGrid.getColumnCount(); j++) {
                final int row = i;
                final int col = j;

                Button button = new Button(getApplicationContext());
                button.setText("");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SudokuButtonClickedAction(row, col);
                    }
                });

                int buttonSize = (int) (sudokuGridPixelSize/9.0f);
                ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(buttonSize, buttonSize);
                sudokuGrid.addView(button, i * sudokuGrid.getColumnCount() + j, buttonLayoutParams);
                sudokuModel.GetField()[i][j].SetButton(button);
                //button.setBackgroundResource();
                button.setText("");
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            }
        }

        // INPUTBUTTONS: Create and set OnClickedActions
        Hashtable<String, Button> buttonDict = new Hashtable<String, Button>();
        buttonDict.put("1",(Button) findViewById(R.id.userButton1));
        buttonDict.put("2",(Button) findViewById(R.id.userButton2));
        buttonDict.put("3",(Button) findViewById(R.id.userButton3));
        buttonDict.put("4",(Button) findViewById(R.id.userButton4));
        buttonDict.put("5",(Button) findViewById(R.id.userButton5));
        buttonDict.put("6",(Button) findViewById(R.id.userButton6));
        buttonDict.put("7",(Button) findViewById(R.id.userButton7));
        buttonDict.put("8",(Button) findViewById(R.id.userButton8));
        buttonDict.put("9",(Button) findViewById(R.id.userButton9));
        Set<String> keys = buttonDict.keySet();
        for (final String key: keys) {
            Button button = buttonDict.get(key);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputButtonClickedAction(Integer.parseInt(key));
                }
            });

            int buttonSize = (int) (screenWidth/((float) keys.size()));
            ViewGroup.LayoutParams buttonLayoutParams = button.getLayoutParams();
            buttonLayoutParams.width = buttonSize;
            //buttonLayoutParams.height = buttonSize;
        }

        InstantiateButtons();

        RefreshUI();
    }

    // Sets all values of the User Interface according to the model of the sudoku
    protected void RefreshUI() {
        for (int i = 0; i < sudokuModel.GetField().length; i++) {
            for (int j = 0; j < sudokuModel.GetField()[i].length; j++) {
                Button button = sudokuModel.GetField()[i][j].GetButton();

                if (sudokuModel.GetField()[i][j].GetIsEmpty()) {
                    button.setText(" ");
                    button.setTextColor(INACTIVE_COLOR_FONT_EMPTY);
                } else {
                    button.setText(Integer.toString(sudokuModel.GetField()[i][j].GetValue()));
                }
                button.setBackgroundColor(INACTIVE_COLOR_BACKGROUND);

                if (sudokuModel.GetField()[i][j].GetIsFixedValue()) {
                    button.setTextColor(INACTIVE_COLOR_FONT_FIXED);
                } else if (sudokuModel.GetField()[i][j].GetIsEmpty()) { // TODO: DELETE CASE
                    button.setTextColor(INACTIVE_COLOR_FONT_EMPTY);
                } else {
                    button.setTextColor(INACTIVE_COLOR_FONT_NONFIXED);
                }

                Drawable buttonDrawable = getApplicationContext().getResources().getDrawable(R.drawable.sudoku_grid_button);
                button.setBackground(buttonDrawable);
            }
        }
        if (activeCell != null) {
            Button button = activeCell.GetButton();
            button.setBackgroundColor(ACTIVE_COLOR_BACKGROUND);
            button.setTextColor(ACTIVE_COLOR_FONT);
        }
    }
}
