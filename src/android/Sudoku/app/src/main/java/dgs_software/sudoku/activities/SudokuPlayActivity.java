package dgs_software.sudoku.activities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.security.InvalidParameterException;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.dialogs.ChooseDifficultyDialog;
import dgs_software.sudoku.dialogs.SudokuPlayRestartDialog;
import dgs_software.sudoku.dialogs.SudokuPlaySettingsDialog;
import dgs_software.sudoku.dialogs.SudokuPlayWonDialog;
import dgs_software.sudoku.dialogs.SudokuPlayWrongDialog;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public class SudokuPlayActivity extends SudokuBaseActivity {

    // region Attributes
    // region showFaultyCells
    private boolean m_showFaultyCells = true; // TODO Default value
    public boolean getShowFaultyCells() {
        return m_showFaultyCells;
    }

    public void setShowFaultyCells(boolean showFaultyCells) {
        this.m_showFaultyCells = showFaultyCells;
        refreshUI();
    }
    // endregion showFaultyCells

    // region highlightCells
    private boolean m_highlightCells = true; // TODO Default value
    public boolean getHighlightCells() {
        return m_highlightCells;
    }

    public void setHighlightCells(boolean highlightCells) {
        this.m_highlightCells = highlightCells;
        refreshUI();
    }
    // endregion highlightCells

    // region makeNotes
    private boolean m_makeNotes = false;

    public boolean getMakeNotes() {
        return m_makeNotes;
    }

    public void setMakeNotes(boolean makeNotes) {
        m_makeNotes = makeNotes;
    }
    // endregion makeNotes

    // region NoteButtonsField
    private Button[][][] m_noteButtonsField;
    public Button[][][] getNoteButtons() {
        return this.m_noteButtonsField;
    }

    public void setNoteButtons(Button[][][] noteButtonsField) {
        this.m_noteButtonsField = noteButtonsField;
    }
    // endregion NoteButtonsField

    // region Timer
    private ValueAnimator m_timer = null;
    public ValueAnimator getTimer() {
        return m_timer;
    }

    public void setTimer(ValueAnimator timer) {
        m_timer = timer;
    }
    // endregion Timer

    // region lastAnimatedValue: Used to save the lastAnimatedValue of the timer in order to calculate the difference
    private int m_lastAnimatedValue = 0;

    public int getLastAnimatedValue() {
        return m_lastAnimatedValue;
    }

    public void setLastAnimatedValue(int lastAnimatedValue) {
        m_lastAnimatedValue = lastAnimatedValue;
    }

    // endregion lastAnimatedValue
    // endregion Attributes

    // region Methods

    // Create the buttons in the menu bar at the top
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sudoku_play, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Handle button activities of menu bar at the top
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            SudokuPlaySettingsDialog preferencesDialog = new SudokuPlaySettingsDialog(SudokuPlayActivity.this, this);
            preferencesDialog.show();
        } else if (item.getItemId() == R.id.newGameButton) {
            ChooseDifficultyDialog chooseDifficultyDialog = new ChooseDifficultyDialog(this);
            chooseDifficultyDialog.show();
        } else if (item.getItemId() == R.id.restartButton) {
            SudokuPlayRestartDialog restartDialog = new SudokuPlayRestartDialog(this);
            restartDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Read out input Bundle information
        Sudoku.Difficulty difficulty = Sudoku.Difficulty.RELOAD_EXISTING;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            int value = b.getInt(Sudoku.Difficulty.class.toString());
            difficulty = Sudoku.Difficulty.intValToDifficulty(value);
        } else {
            throw new InvalidParameterException(this.getClass().toString() + "Cannote create Sudoku Model: No bundle with key " + Sudoku.Difficulty.class.toString() + "found!");
            // TODO: Log and return
        }

        // Load or create new sudoku
        Sudoku savedSudoku = getSaveDataProvider().loadSudokuPlay_sudoku();
        if (savedSudoku != null && difficulty == Sudoku.Difficulty.RELOAD_EXISTING) {
            setSudokuModel(savedSudoku);
        } else {
            setSudokuModel(new Sudoku(difficulty, getApplicationContext()));
        }

        // Load saved preferences
        setShowFaultyCells(getSaveDataProvider().loadSudokuPlayPreferences_showFaultyCells(true));
        setHighlightCells(getSaveDataProvider().loadSudokuPlayPreferences_highlightCells(true));

        // Set text fields in UI
        TextView elapsedTimeTextView = (TextView) findViewById(R.id.ElapsedTimeTextView);
        TextView difficultyTextView = (TextView) findViewById(R.id.DifficultyTextView);

        difficultyTextView.setText(Utils.getDifficultyAsString(getApplicationContext(), getSudokuModel().getDifficulty()));

        // Create Timer to count seconds
        int secondsToRun = 359999 - getSudokuModel().getElapsedSeconds(); // The will stop at 99:59:59

        if (getTimer() == null) {
            setTimer(ValueAnimator.ofInt(secondsToRun));
            getTimer().setDuration(secondsToRun * 1000).setInterpolator(new LinearInterpolator());
            getTimer().addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {

                    int animatedValueDiff = (int) animation.getAnimatedValue() - getLastAnimatedValue(); // difference between last method call in seconds
                    setLastAnimatedValue(getLastAnimatedValue() + animatedValueDiff);
                    int elapsedSeconds = getSudokuModel().getElapsedSeconds() + animatedValueDiff;
                    getSudokuModel().setElapsedSeconds(elapsedSeconds);

                    TextView elapsedTimeTextView = (TextView) findViewById(R.id.ElapsedTimeTextView);
                    elapsedTimeTextView.setText(Utils.formatSecondsAsTime(elapsedSeconds));
                }
            });
        }

        getTimer().start();

        refreshUI();
    }

    @Override
    protected void onPause() {
        getTimer().pause();
        // Save the game if it is not finished yet
        if (getSudokuModel().isSolved() == false) {
            getSaveDataProvider().saveSudokuPlay_sudoku(getSudokuModel());
        }

        super.onPause();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_sudoku_play);
    }

    @Override
    protected void instantiateButtons() {
        // MAKE NOTES BUTTON
        Button makeNoteButton = (Button) findViewById(R.id.makeNoteButton);
        makeNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNoteButtonClickedAction();
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
    }

    // region CreateNestedGridLayout
    @Override
    protected GridLayout createNestedGridLayout(int row, int col, int buttonSize) {
        GridLayout nestedGridLayout = new GridLayout(getApplicationContext());
        nestedGridLayout.setRowCount(3);
        nestedGridLayout.setColumnCount(3);

        // Fill 3x3 Grid
        Button[] noteButtons = createNoteButtons(row, col);
        for (int k = 0; k < noteButtons.length; k++) {
            ViewGroup.LayoutParams noteButtonLayoutParams = new ViewGroup.LayoutParams(buttonSize, (int) buttonSize);
            noteButtons[k].setTextSize(TypedValue.COMPLEX_UNIT_SP, GlobalConfig.SUDOKU_NOTE_BUTTON_TEXT_SIZE);
            noteButtons[k].setPadding(0, 0, 0, 0);
            noteButtons[k].setTypeface(null, Typeface.BOLD);
            noteButtons[k].setStateListAnimator(null);
            final int rowFinal = row, colFinal = col;
            noteButtons[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sudokuButtonClickedAction(rowFinal, colFinal);
                }
            });
            nestedGridLayout.addView(noteButtons[k], k, noteButtonLayoutParams);
            nestedGridLayout.bringChildToFront(noteButtons[k]);
        }

        return nestedGridLayout;
    }

    // Creates a button array of length 9 to fill the 3x3 nestedGridLayout with the note buttons
    private  Button[] createNoteButtons(int row, int col) {
        // If not button Field not yet existent, create it
        if (getNoteButtons() == null) {
            setNoteButtons(new Button[9][9][9]);
        }

        // Fill the note button field for the given cell
        for (int k = 0; k < getNoteButtons().length; k++) {
            getNoteButtons()[row][col][k] = new Button(getApplicationContext());
        }

        // Return the note button array only for the given cell
        return getNoteButtons()[row][col];
    }
    // endregion CreateNestedGridLayout

    public void restartGame() {
        getSudokuModel().setElapsedSeconds(0);
        getSudokuModel().deleteNonFixedValues();
        refreshUI();
    }

    // region Button OnClickHandlers
    // region OnClickListener methods
    public void makeNoteButtonClickedAction() {
        setMakeNotes(!getMakeNotes());
        Button makeNoteButton = (Button) findViewById(R.id.makeNoteButton);
        if (getMakeNotes()) {
            makeNoteButton.setBackgroundResource(R.drawable.ic_notes_active);
            //makeNoteButton.setBackgroundColor(Color.GREEN);
        } else {
            makeNoteButton.setBackgroundResource(R.drawable.ic_notes);
            //makeNoteButton.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
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

    @Override
    public void inputButtonClickedAction(int number) {
        if (getActiveCell() == null) {
            return;
        }

        // Get row and column of active cell
        Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
        int row = activeCellPosition.first, col = activeCellPosition.second;

        if (getMakeNotes() == false) {
            getSudokuModel().getField()[row][col].setValue(number);
            getSudokuModel().getField()[row][col].setIsFixedValue(false);

            // Check if sudoku is completely filled
            if (getSudokuModel().isCompletelyFilled()) {
                if (getSudokuModel().isSolved()) {
                    getTimer().pause();
                    SudokuPlayWonDialog gameWonDialog = new SudokuPlayWonDialog(this);
                    gameWonDialog.show();
                } else {
                    SudokuPlayWrongDialog sudokuWrongDialog = new SudokuPlayWrongDialog(this);
                    sudokuWrongDialog.show();
                }
            }
        } else {
            if (getSudokuModel().getField()[row][col].getIsEmpty() == false) {
                return; // It is not possible to overwrite a filled cell with a note
            }
            getSudokuModel().getField()[row][col].setValue(0);
            boolean[] activeNotes = getSudokuModel().getField()[row][col].getActiveNotes();
            if (activeNotes == null) {
                activeNotes = new boolean[9];
            }
            activeNotes[number-1] = !activeNotes[number-1];
            getSudokuModel().getField()[row][col].setActiveNotes(activeNotes);
        }
        refreshUI();
    }

    public void clearCellButtonClicked() {
        if (getActiveCell() != null && getSudokuModel() != null && getSudokuModel().getField() != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
            int row = activeCellPosition.first, col = activeCellPosition.second;
            if (getSudokuModel().getField()[row][col].getIsFixedValue() == false) {
                getActiveCell().setValue(0);
                getActiveCell().setIsFixedValue(false);
            }
            getSudokuModel().getField()[row][col].resetActiveNotes();

            refreshUI();
        }
    }
    // endregion Button OnClickHandlers

    // endregion OnClickListener methods

    // region Refresh UI methods

    // Refresh the note Buttons of the User Interface
    @Override
    public void refreshUI() {
        // If all cells are filled, faulty cells are always displayed - otherwise only according to user preferences
        boolean showFaultyCells = getSudokuModel().isCompletelyFilled() ? true : getShowFaultyCells();

        super.refreshUI(showFaultyCells, getHighlightCells());
        SudokuCellStates[][] cellStates = getCellStates(showFaultyCells, getHighlightCells());

        for (int i = 0; i < getNoteButtons().length; i++) {
            for (int j = 0; j < getNoteButtons()[i].length; j++) {
                // Only if the cell is empty, the note fields must be drawn
                if (getNoteButtons() != null) {
                    boolean[] activeNotes = getSudokuModel().getField()[i][j].getActiveNotes();
                    for (int k = 0; k < getNoteButtons()[i][j].length; k++) {
                        String noteButtonText = "";
                        if (activeNotes[k] == true) {
                            noteButtonText = new Integer(k + 1).toString();
                        }

                        int backgroundColor = getResources().getColor(R.color.sudoku_button_inactive_background);
                        if (cellStates[i][j] == SudokuCellStates.HIGHLIGHTED_FIXED || cellStates[i][j] == SudokuCellStates.HIGHLIGHTED_NONFIXED) {
                            backgroundColor = getResources().getColor(R.color.sudoku_button_highlighted_background);
                        } else if (cellStates[i][j] == SudokuCellStates.ACTIVE_FIXED || cellStates[i][j] == SudokuCellStates.ACTIVE_NONFIXED) {
                            backgroundColor = getResources().getColor(R.color.sudoku_button_active_background);
                        }
                        
                        SetNoteButtonProperties(getNoteButtons()[i][j][k], noteButtonText);
                    }
                }
            }
        }
    }

    // Updates all properties of one note-cell in the UI
    private void SetNoteButtonProperties(Button noteButton, String buttonText) {
        if (noteButton != null) {
            noteButton.setTextColor(Color.BLACK); // TODO: Variabel machen
            noteButton.setText(buttonText);

            noteButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void moveToBack(View myCurrentView) {
        ViewGroup myViewGroup = ((ViewGroup) myCurrentView.getParent());
        int index = myViewGroup.indexOfChild(myCurrentView);
        for (int i = 0; i < index; i++) {
            myViewGroup.bringChildToFront(myViewGroup.getChildAt(i));

        }
        myViewGroup.invalidate();
    }

    // endregion Refresh UI methods
    // endregion Methods
}