package dgs_software.sudoku.activities;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import dgs_software.sudoku.R;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public class SudokuSolverActivity extends SudokuBaseActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_sudoku_solver);
    }

    @Override
    protected void onPause() {
        getSaveDataProvider().saveSudokuSolver_sudoku(getSudokuModel());

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Sudoku savedSudoku = getSaveDataProvider().loadSudokuSolver_sudoku();
        if (savedSudoku == null) {
            Cell[][] field = new Cell[9][9];
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    field[i][j] = new Cell(0);
                }
            }
            setSudokuModel(new Sudoku(field));
        } else {
            setSudokuModel(savedSudoku);
        }

        refreshUI();
    }

    @Override
    protected void instantiateButtons() {
        // SOLVE SUDOKU BUTTON
        Button solveSudokuButton = (Button) findViewById(R.id.solveSudokuButton);
        solveSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveSudokuButtonClicked();
            }
        });

        // CLEAR CELL BUTTON
        ImageButton clearCellButton = (ImageButton) findViewById(R.id.clearCellButton);
        clearCellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCellButtonClicked();
            }
        });

        // RESET SOLUTION BUTTON
        Button resetSolutionButton = (Button) findViewById(R.id.resetSolutionButton);
        resetSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSolutionButtonClicked();
            }
        });

        // RESET SOLUTION BUTTON
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getSudokuModel().getField().length; i++) {
                    for (int j = 0; j < getSudokuModel().getField()[i].length; j++) {
                        getSudokuModel().getField()[i][j].setValue(0);
                        getSudokuModel().getField()[i][j].setIsFixedValue(false);
                    }
                }
                refreshUI();
            }
        });
    }

    public void sudokuButtonClickedAction(int row, int col) {
        if (getSudokuModel().getField() == null || getSudokuModel().getField()[row][col] == null) {
            return;
        }
        if (getActiveCell() != null && getSudokuModel().getField()[row][col].equals(getActiveCell())) {
            setActiveCell(null);
        } else {
            setActiveCell(getSudokuModel().getField()[row][col]);
        }

        refreshUI();
    }

    public void inputButtonClickedAction(int number) {
        getSudokuModel().deleteNonFixedValues();
        if (getActiveCell() != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
            int row = activeCellPosition.first, col = activeCellPosition.second;

            getSudokuModel().getField()[row][col].setValue(number);
            getSudokuModel().getField()[row][col].setIsFixedValue(true);
        }
        refreshUI();
    }

    public void solveSudokuButtonClicked() {
        getSudokuModel().getSolution();
        refreshUI();
    }

    public void clearCellButtonClicked() {
        if (getActiveCell() != null) {
            getActiveCell().setValue(0);
            getActiveCell().setIsFixedValue(false);
            getSudokuModel().deleteNonFixedValues();
            refreshUI();
        }
    }

    public void resetSolutionButtonClicked() {
        getSudokuModel().deleteNonFixedValues();
        refreshUI();
    }

    @Override
    protected void refreshUI() {
        super.refreshUI(true,true);
    }
}