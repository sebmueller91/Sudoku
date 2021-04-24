package dgs_software.sudoku.activities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.TextViewCompat;

import java.security.InvalidParameterException;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.dialogs.ChooseDifficultyDialog;
import dgs_software.sudoku.dialogs.SudokuPlayRestartDialog;
import dgs_software.sudoku.dialogs.SudokuPlaySettingsDialog;
import dgs_software.sudoku.dialogs.SudokuPlayWonDialog;
import dgs_software.sudoku.dialogs.SudokuPlayWrongDialog;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Logger;
import dgs_software.sudoku.utils.Utils;

public class SudokuPlayActivity extends SudokuBaseActivity {

    // region Attributes
    // region showFaultyCells
    private boolean m_showFaultyCells = GlobalConfig.defaultShowFaultyCells;

    public boolean getShowFaultyCells() {
        return m_showFaultyCells;
    }

    public void setShowFaultyCells(boolean showFaultyCells) {
        this.m_showFaultyCells = showFaultyCells;
        refreshUI();
    }
    // endregion showFaultyCells

    // region highlightCells
    private boolean m_highlightCells = GlobalConfig.defaultHighlightCells;

    public boolean getHighlightCells() {
        return m_highlightCells;
    }

    public void setHighlightCells(boolean highlightCells) {
        this.m_highlightCells = highlightCells;
        refreshUI();
    }

    // region deleteNotes
    private boolean m_deleteNotes = GlobalConfig.defaultHighlightCells;

    public boolean getDeleteNotes() {
        return m_deleteNotes;
    }

    public void setDeleteNotes(boolean deleteNotes) {
        this.m_deleteNotes = deleteNotes;
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

    // region NoteFieldsField
    private TextView[][][] m_noteFieldsField;

    public TextView[][][] getNoteFields() {
        return this.m_noteFieldsField;
    }

    public void setNoteFields(TextView[][][] noteFieldsField) {
        this.m_noteFieldsField = noteFieldsField;
    }
    // endregion NoteFieldsField

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
    // endregion

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add nested grid views for each button the represent the note Buttons
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int row = i;
                final int col = j;

                GridLayout nestedGridLayout = createNestedGridLayout(i, j, getButtonSize());

                // First add nestedGrid (if existent) and then the button to the relative layout wrapper
                if (nestedGridLayout != null) {
                    getFrameLayoutGrid()[i][j].addView(nestedGridLayout);
                }
            }
        }
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
            Logger.LogError("SudokuPlay Activity was called without mandatory bundle parameter " + Sudoku.Difficulty.class.toString());
            throw new InvalidParameterException(this.getClass().toString() + "Cannote create Sudoku Model: No bundle with key " + Sudoku.Difficulty.class.toString() + "found!");
        }

        // Load or create new sudoku
        Sudoku savedSudoku = getSaveDataProvider().loadSudokuPlay_sudoku();
        if (savedSudoku != null && difficulty == Sudoku.Difficulty.RELOAD_EXISTING) {
            setSudokuModel(savedSudoku);
        } else {
            setSudokuModel(new Sudoku(difficulty, getApplicationContext()));
        }

        // Load saved preferences
        setShowFaultyCells(getSaveDataProvider().loadSudokuPlayPreferences_showFaultyCells(GlobalConfig.defaultShowFaultyCells));
        setHighlightCells(getSaveDataProvider().loadSudokuPlayPreferences_highlightCells(GlobalConfig.defaultHighlightCells));
        setDeleteNotes(getSaveDataProvider().loadSudokuPlayPreferences_deleteNotes(GlobalConfig.defaultDeleteNotes));

        // Set text fields in UI
        TextView elapsedTimeTextView = (TextView) findViewById(R.id.ElapsedTimeTextView);
        TextView difficultyTextView = (TextView) findViewById(R.id.DifficultyTextView);

        String difficultyText = Utils.getDifficultyAsString(getResources(), getSudokuModel().getDifficulty());
        difficultyTextView.setText(difficultyText);

        // Create Timer to count seconds
        int secondsToRun = 359999 - getSudokuModel().getElapsedSeconds(); // The will stop at 99:59:59

        if (getTimer() == null) {
            setTimer(ValueAnimator.ofInt(secondsToRun));
            getTimer().setDuration(secondsToRun * 1000).setInterpolator(new LinearInterpolator());
            getTimer().addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

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

    // region Helper Methods

    // Creates the 3x3 grid of noteFields for a given Button
    private GridLayout createNestedGridLayout(final int row, final int col, int buttonSize) {
        int[] borderThicknessValues = getBorderThicknessValues(row, col);
        int nestedGridLayoutWidth = buttonSize - borderThicknessValues[0] - borderThicknessValues[2];
        int nestedGridLayoutHeight = buttonSize - borderThicknessValues[1] - borderThicknessValues[3];

        GridLayout nestedGridLayout = new GridLayout(getApplicationContext());
        nestedGridLayout.setRowCount(3);
        nestedGridLayout.setColumnCount(3);

        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(nestedGridLayoutWidth, nestedGridLayoutHeight);
        nestedGridLayout.setLayoutParams(frameLayoutParams);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.setMargins(borderThicknessValues[0], borderThicknessValues[1], borderThicknessValues[2], borderThicknessValues[3]);

        nestedGridLayout.setLayoutParams(layoutParams);
        nestedGridLayout.setUseDefaultMargins(false);
        nestedGridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        nestedGridLayout.setRowOrderPreserved(false);

        // Fill 3x3 Grid
        int noteFieldWidth = Math.round(((float)nestedGridLayoutWidth) / 3f);
        int noteFieldHeight = Math.round(((float)nestedGridLayoutHeight) / 3f);
        TextView[] noteFields = createNoteFields(row, col);
        for (int k = 0; k < noteFields.length; k++) {
            ViewGroup.LayoutParams noteButtonLayoutParams = new ViewGroup.LayoutParams(noteFieldWidth, noteFieldHeight);

            // Maximize notebutton text and then decrease it by a fixed factor
            TextViewCompat.setAutoSizeTextTypeWithDefaults(noteFields[k], TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            int[] uniformSize = new int[]{(int) (noteFields[k].getTextSize() * GlobalConfig.NOTES_TEXTSIZE)};
            TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(noteFields[k], uniformSize, TypedValue.COMPLEX_UNIT_PX);

            noteFields[k].setGravity(Gravity.CENTER);
            noteFields[k].setTypeface(null, Typeface.BOLD);
            noteFields[k].setStateListAnimator(null);
            noteFields[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sudokuButtonClickedAction(row, col);
                }
            });
            nestedGridLayout.addView(noteFields[k], k, noteButtonLayoutParams);
            nestedGridLayout.bringChildToFront(noteFields[k]);
        }

        return nestedGridLayout;
    }

    // Creates a button array of length 9 to fill the 3x3 nestedGridLayout with the note buttons
    private TextView[] createNoteFields(int row, int col) {
        // If not button Field not yet existent, create it
        if (getNoteFields() == null) {
            setNoteFields(new TextView[9][9][9]);
        }

        // Fill the note button field for the given cell
        for (int k = 0; k < getNoteFields().length; k++) {
            getNoteFields()[row][col][k] = new TextView(getApplicationContext());
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            getNoteFields()[row][col][k].setLayoutParams(layoutParams);
        }

        // Return the note button array only for the given cell
        return getNoteFields()[row][col];
    }
    // endregion

    public void restartGame() {
        getSudokuModel().setElapsedSeconds(0);
        getSudokuModel().deleteNonFixedValues();
        for (int i = 0; i < getSudokuModel().getField().length; i++) {
            for (int j = 0; j < getSudokuModel().getField()[i].length; j++) {
                getSudokuModel().getField()[i][j].setActiveNotes(new boolean[9]);
            }
        }
        refreshUI();
    }

    // region Button OnClickHandlers
    public void makeNoteButtonClickedAction() {
        setMakeNotes(!getMakeNotes());
        Button makeNoteButton = (Button) findViewById(R.id.makeNoteButton);
        if (getMakeNotes()) {
            makeNoteButton.setBackgroundResource(R.drawable.ic_notes_active);
        } else {
            makeNoteButton.setBackgroundResource(R.drawable.ic_notes);
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

    // One of the buttons 1-9 is clicked
    @Override
    public void inputButtonClickedAction(int number) {
        if (getActiveCell() == null) {
            return;
        }

        // Get row and column of active cell
        Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
        int row = activeCellPosition.first, col = activeCellPosition.second;

        if (getMakeNotes() == false) { // Regular number is entered into the cell
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

            // If a number is entered, all notes that are in conflict with this new number get deleted
            if (getDeleteNotes()) {
                for (int i = 0; i < getSudokuModel().getField().length; i++) {
                    for (int j = 0; j < getSudokuModel().getField()[i].length; j++) {
                        if (!(i == row && j == col) && // Check if the cell is in the same row, column or block as the clicked cell, but is not the clicked cell itself
                                (row == i || col == j || (row / 3 == i / 3 && col / 3 == j / 3))) {
                            getSudokuModel().getField()[i][j].getActiveNotes()[number-1] = false;
                        }
                    }
                }
            }


        } else { // Note is entered into the cell
            if (getSudokuModel().getField()[row][col].getIsEmpty() == false) {
                return; // It is not possible to overwrite a filled cell with a note
            }
            getSudokuModel().getField()[row][col].setValue(0);
            boolean[] activeNotes = getSudokuModel().getField()[row][col].getActiveNotes();
            if (activeNotes == null) {
                activeNotes = new boolean[9];
            }
            activeNotes[number - 1] = !activeNotes[number - 1];
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

    // region Refresh UI methods

    // Refresh the note Buttons of the User Interface
    @Override
    public void refreshUI() {
        // If all cells are filled, faulty cells are always displayed - otherwise only according to user preferences
        boolean showFaultyCells = getSudokuModel().isCompletelyFilled() ? true : getShowFaultyCells();

        super.refreshUI(showFaultyCells, getHighlightCells());
        SudokuCellStates[][] cellStates = getCellStates(showFaultyCells, getHighlightCells());

        for (int i = 0; i < getNoteFields().length; i++) {
            for (int j = 0; j < getNoteFields()[i].length; j++) {
                // Only if the cell is empty, the note fields must be drawn
                if (getNoteFields() != null) {
                    boolean[] activeNotes = getSudokuModel().getField()[i][j].getActiveNotes();
                    for (int k = 0; k < getNoteFields()[i][j].length; k++) {
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

                        SetNoteButtonProperties(getNoteFields()[i][j][k], noteButtonText);
                    }
                }
            }
        }
    }

    // Updates all properties of one note-cell in the UI
    private void SetNoteButtonProperties(TextView noteButton, String buttonText) {
        if (noteButton != null) {
            noteButton.setTextColor(getResources().getColor(R.color.sudoku_notebutton_text_color));
            noteButton.setText(buttonText);

            noteButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    // Moves the given view behind other view elements that are at the same place
    private void moveToBack(View myCurrentView) {
        ViewGroup myViewGroup = ((ViewGroup) myCurrentView.getParent());
        int index = myViewGroup.indexOfChild(myCurrentView);
        for (int i = 0; i < index; i++) {
            myViewGroup.bringChildToFront(myViewGroup.getChildAt(i));

        }
        myViewGroup.invalidate();
    }

    // endregion
    // endregion
}