package dgs_software.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;
import java.util.Set;

public class SudokuPlay extends AppCompatActivity {
    public static final int
            ACTIVE_COLOR_BACKGROUND = Color.parseColor("#ffffcc"),
            ACTIVE_COLOR_FONT = Color.parseColor("#6495ED"),
            INACTIVE_COLOR_BACKGROUND = Color.parseColor("#FFFFFF"),
            INACTIVE_COLOR_FONT_FIXED = Color.parseColor("#000000"),
            INACTIVE_COLOR_FONT_NONFIXED = Color.parseColor("#3CB371"),
            INACTIVE_COLOR_FONT_EMPTY = Color.parseColor("#A9A9A9");

    private Cell activeCell = null;
    private Sudoku sudokuModel;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_play);
        GridLayout sudokuGrid = (GridLayout) findViewById(R.id.SudokuGridLayout);

        // CREATE SUDOKU MODEL
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("difficulty");
        Sudoku.Difficulty difficulty = null;
        if (value == 1) {
            difficulty = Sudoku.Difficulty.EASY;
        } else if (value == 2) {
            difficulty = Sudoku.Difficulty.NORMAL;
        } else {
            difficulty = Sudoku.Difficulty.HARD;
        }
        sudokuModel = new Sudoku(difficulty, getApplicationContext());

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

        // SOLVE SUDOKU BUTTON
        Button displayErrorsButton = (Button) findViewById(R.id.displayErrorsButton);
        displayErrorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayErrorsButtonClicked();
            }
        });

        // CLEAR CELL BUTTON
        Button clearCellButton = (Button) findViewById(R.id.clearCellButton);
        clearCellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCellButtonClicked();
            }
        });

        // RESET SOLUTION BUTTON
        Button checkSolutionButton = (Button) findViewById(R.id.checkSolutionButton);
        checkSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSolutionButtonClicked();
            }
        });

        RefreshUI();
    }

    public void SudokuButtonClickedAction(int row, int col) {
        /*int cellValue = GetButtonTextAsInteger(sudoku.GetField()[row][col].GetButton());
        String newText = Integer.toString(++cellValue);
        sudoku.GetField()[row][col].GetButton().setText(newText);
        sudoku.GetField()[row][col].GetButton().setText(newText);
        sudoku.GetField()[row][col].GetButton().setBackgroundColor(ACTIVE_COLOR_BACKGROUND);
        sudoku.GetField()[row][col].GetButton().setTextColor(ACTIVE_COLOR_FONT);
        sudoku.GetField()[row][col].GetButton().setTypeface(null, Typeface.BOLD);*/
        if (sudokuModel.GetField() == null || sudokuModel.GetField()[row][col] == null) {
            return;
        }
        if (activeCell != null && sudokuModel.GetField()[row][col].equals(activeCell)) {
            activeCell = null;
        } else {
            activeCell = sudokuModel.GetField()[row][col];
        }

        RefreshUI();
    }

    public void InputButtonClickedAction(int number) {
        if (activeCell != null) {
            int row = 0, col = 0;
            for (int i = 0; i < sudokuModel.GetField().length; i++) {
                for (int j = 0; j < sudokuModel.GetField()[i].length; j++) {
                    if (sudokuModel.GetField()[i][j].equals(activeCell)) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            sudokuModel.GetField()[row][col].SetValue(number);
            sudokuModel.GetField()[row][col].SetIsFixedValue(false);
        }
        RefreshUI();
    }

    public void DisplayErrorsButtonClicked() {
        //TODO
    }

    public void ClearCellButtonClicked() {
        if (activeCell != null) {
            activeCell.SetValue(0);
            activeCell.SetIsFixedValue(false);
        }
        RefreshUI();
    }

    public void checkSolutionButtonClicked() {
        if (sudokuModel.SudokuIsValid() && sudokuModel.SudokuIsCompletelyFilled()) {
            // TODO: Dialog: Du hast gewonnen!
        } else {
            // TODO: Dialog: Du hast Verloren!
        }
    }

    private void DeleteNonFixedValues() {
        for (int i = 0; i < sudokuModel.GetField().length; i++) {
            for (int j = 0; j < sudokuModel.GetField()[i].length; j++) {
                if (sudokuModel.GetField()[i][j].GetIsFixedValue() == false) {
                    sudokuModel.GetField()[i][j].SetValue(0);
                }
            }
        }
    }

    // Sets all values of the User Interface according to the model of the sudoku
    private void RefreshUI() {
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