package dgs_software.sudoku.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.util.Pair;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import dgs_software.sudoku.R;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public abstract class SudokuBaseActivity extends AppCompatActivity {
    public static final int
            ACTIVE_COLOR_FONT = Color.parseColor("#6495ED"),
            INACTIVE_COLOR_BACKGROUND = Color.parseColor("#FFFFFF"),
            INACTIVE_COLOR_FONT_FIXED = Color.parseColor("#000000"),
            INACTIVE_COLOR_FONT_NONFIXED = Color.parseColor("#3CB371"),
            HIGHLIGHTED_COLOR_BACKGROUND = Color.LTGRAY; // TODO: Move to ressource file

    private static int ACTIVE_COLOR_BACKGROUND;

    private static final int STROKE_WIDTH_MID_BORDER = 4;
    private static final int STROKE_WIDTH_BIG_BORDER = STROKE_WIDTH_MID_BORDER * 2;
    private static final int STROKE_WIDTH_SMALL_BORDER = 2;

    private static final int SUDOKU_BORDER_COLOR = Color.BLACK;

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

        // TODO: GET all colors from ressource file
        ACTIVE_COLOR_BACKGROUND = getResources().getColor(R.color.sudoku_button_active_background);

        // SUDOKU GRID
        ViewGroup.LayoutParams gridLayoutParams = sudokuGrid.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels; // Screen width in pixels
        float sudokuGridPixelSize = 0.98f * screenWidth; // Sudoku grid shall fill the screen width
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

                int buttonSize = (int) (sudokuGridPixelSize / 9.0f);
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
        buttonDict.put("1", (Button) findViewById(R.id.userButton1));
        buttonDict.put("2", (Button) findViewById(R.id.userButton2));
        buttonDict.put("3", (Button) findViewById(R.id.userButton3));
        buttonDict.put("4", (Button) findViewById(R.id.userButton4));
        buttonDict.put("5", (Button) findViewById(R.id.userButton5));
        buttonDict.put("6", (Button) findViewById(R.id.userButton6));
        buttonDict.put("7", (Button) findViewById(R.id.userButton7));
        buttonDict.put("8", (Button) findViewById(R.id.userButton8));
        buttonDict.put("9", (Button) findViewById(R.id.userButton9));
        Set<String> keys = buttonDict.keySet();
        for (final String key : keys) {
            Button button = buttonDict.get(key);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputButtonClickedAction(Integer.parseInt(key));
                }
            });

            int buttonSize = (int) (screenWidth / ((float) keys.size()));
            ViewGroup.LayoutParams buttonLayoutParams = button.getLayoutParams();
            buttonLayoutParams.width = buttonSize;
            //buttonLayoutParams.height = buttonSize;
        }

        InstantiateButtons();

        RefreshUI();
    }

    // Sets all values of the User Interface according to the model of the sudoku
    protected void RefreshUI() {
        LinkedList<Pair<Integer, Integer>> cellsToHighlight = new LinkedList<Pair<Integer, Integer>>();
        if (activeCell != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.GetPositionOfCell(activeCell, sudokuModel);
            cellsToHighlight = GetListOfCellsToHighlight(activeCellPosition.first, activeCellPosition.second);
        }
        for (int i = 0; i < sudokuModel.GetField().length; i++) {
            for (int j = 0; j < sudokuModel.GetField()[i].length; j++) {
                Button button = sudokuModel.GetField()[i][j].GetButton();
                int backgroundColor = INACTIVE_COLOR_BACKGROUND;
                int textColor = INACTIVE_COLOR_FONT_FIXED;
                String buttonText = " ";

                if (sudokuModel.GetField()[i][j].GetIsEmpty() == false) {
                    buttonText = Integer.toString(sudokuModel.GetField()[i][j].GetValue());
                }

                if (sudokuModel.GetField()[i][j].GetIsFixedValue() == false) {
                    textColor = INACTIVE_COLOR_FONT_NONFIXED;
                }

                if (Utils.ListContainsElement(cellsToHighlight, new Pair(i,j))) {
                    backgroundColor = HIGHLIGHTED_COLOR_BACKGROUND;
                }

                if (activeCell != null && sudokuModel.GetField()[i][j] == activeCell) {
                    backgroundColor = ACTIVE_COLOR_BACKGROUND;
                    textColor = ACTIVE_COLOR_FONT;
                }

                SetButtonProperties(i, j, backgroundColor, textColor, buttonText);
            }
        }
    }

    private void SetButtonProperties(int row, int col, int backgroundColor, int textColor, String text) {
        Button button = sudokuModel.GetField()[row][col].GetButton();
        if (button != null) {
            button.setTextColor(textColor);
            button.setText(text);

            GradientDrawable drawableInner = new GradientDrawable();
            GradientDrawable drawableBorders = new GradientDrawable();
            drawableInner.setColor(backgroundColor);
            drawableBorders.setColor(SUDOKU_BORDER_COLOR);

            Drawable[] layers = {drawableBorders, drawableInner};
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            int[] borderThicknessValues = GetBorderThicknessValues(row, col);
            layerDrawable.setLayerInset(1, borderThicknessValues[0], borderThicknessValues[1], borderThicknessValues[2], borderThicknessValues[3]);

            button.setBackground(layerDrawable);
        }
    }

    private int[] GetBorderThicknessValues(int row, int col) {
        int[] borders = new int[4];

        // Set Left Border Thickness
        if (col == 0) {
            borders[0] = STROKE_WIDTH_BIG_BORDER;
        } else if (col % 3 == 0) {
            borders[0] = STROKE_WIDTH_MID_BORDER;
        } else {
            borders[0] = STROKE_WIDTH_SMALL_BORDER;
        }

        // Set Top Border Thickness
        if (row == 0) {
            borders[1] = STROKE_WIDTH_BIG_BORDER;
        } else if (row % 3 == 0) {
            borders[1] = STROKE_WIDTH_MID_BORDER;
        } else {
            borders[1] = STROKE_WIDTH_SMALL_BORDER;
        }

        // Set Right Border Thickness
        if (col == 8) {
            borders[2] = STROKE_WIDTH_BIG_BORDER;
        } else if (col % 3 == 2) {
            borders[2] = STROKE_WIDTH_MID_BORDER;
        } else {
            borders[2] = STROKE_WIDTH_SMALL_BORDER;
        }

        // Set Bottom Border Thickness
        if (row == 8) {
            borders[3] = STROKE_WIDTH_BIG_BORDER;
        } else if (row % 3 == 2) {
            borders[3] = STROKE_WIDTH_MID_BORDER;
        } else {
            borders[3] = STROKE_WIDTH_SMALL_BORDER;
        }

        return borders;
    }

    private LinkedList<Pair<Integer, Integer>> GetListOfCellsToHighlight(int row, int col) {
        LinkedList<Pair<Integer, Integer>> list = new LinkedList<Pair<Integer, Integer>>();
        for (int i = 0; i < sudokuModel.GetField().length; i++) {
            for (int j = 0; j < sudokuModel.GetField().length; j++) {
                if (i == row || j == col || (row / 3 == i / 3 && col / 3 == j / 3)) {
                    list.add(new Pair(i, j));
                }
            }
        }
        return list;
    }
}
