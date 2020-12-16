package dgs_software.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class SudokuSolver extends AppCompatActivity {
    public static final int
            ACTIVE_COLOR_BACKGROUND = Color.parseColor("#FF7F50"),
            ACTIVE_COLOR_FONT = Color.parseColor("#6495ED"),
            INACTIVE_COLOR_BACKGROUND = Color.parseColor("#FFFFFF"),
            INACTIVE_COLOR_FONT = Color.parseColor("#000000");

    private Cell activeCell = null;
    private Sudoku sudoku;

    public Cell GetActiveCell() {
        return this.activeCell;
    }

    public void SetActiveCell(Cell cell) {
        this.activeCell = activeCell;
    }

    public Sudoku GetSudoku() {
        return this.sudoku;
    }

    public void SetSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solver);
        GridLayout sudokuGrid = (GridLayout) findViewById(R.id.SudokuGridLayout);

        // Create a new Sudoku object with an all empty field
        Cell[][] field = new Cell[sudokuGrid.getRowCount()][sudokuGrid.getColumnCount()];
        for (int i = 0; i < sudokuGrid.getRowCount(); i++) {
            for (int j = 0; j < sudokuGrid.getColumnCount(); j++) {
                field[i][j] = new Cell();
            }
        }
        sudoku = new Sudoku(field);

        for (int i = 0; i < sudokuGrid.getRowCount(); i++) {
            for (int j = 0; j < sudokuGrid.getColumnCount(); j++) {
                final int row = i;
                final int col = j;

                Button button = new Button(getApplicationContext());
                button.setText("1");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SudokuButtonClickedAction(row, col);
                    }
                });

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.height = CalcDPValue(44, getApplicationContext());
                layoutParams.width = CalcDPValue(44, getApplicationContext());

                layoutParams.setGravity(Gravity.CENTER); // Optional, if you want the text to be centered within the cell
                sudokuGrid.addView(button, i * sudokuGrid.getColumnCount() + j, layoutParams);
                sudoku.GetField()[i][j].SetButton(button);
            }
        }
    }

    // Returns the text of the given button as int
    private static int GetButtonTextAsInteger(Button button) {
        CharSequence sequence = button.getText();
        final StringBuilder sb = new StringBuilder(sequence.length());
        sb.append(sequence);
        return Integer.parseInt(sb.toString());
    }

    public void SudokuButtonClickedAction(int row, int col) {
        int cellValue = GetButtonTextAsInteger(sudoku.GetField()[row][col].GetButton());
        String newText = Integer.toString(++cellValue);
        sudoku.GetField()[row][col].GetButton().setText(newText);
        sudoku.GetField()[row][col].GetButton().setText(newText);
        sudoku.GetField()[row][col].GetButton().setBackgroundColor(ACTIVE_COLOR_BACKGROUND);
        sudoku.GetField()[row][col].GetButton().setTextColor(ACTIVE_COLOR_FONT);
        sudoku.GetField()[row][col].GetButton().setTypeface(null, Typeface.BOLD);
    }

    private static int CalcDPValue(int value, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.getResources().getDisplayMetrics());
    }

    private void RefreshUI() {

    }
}