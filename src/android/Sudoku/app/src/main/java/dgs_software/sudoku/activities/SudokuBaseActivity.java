package dgs_software.sudoku.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.util.Pair;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import dgs_software.sudoku.R;
import dgs_software.sudoku.config.GlobalConfig;
import dgs_software.sudoku.config.LanguageConfig;
import dgs_software.sudoku.data.SaveDataProvider;
import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.model.Sudoku;
import dgs_software.sudoku.utils.Utils;

public abstract class SudokuBaseActivity extends AppCompatActivity {
    // region Enum Declaration
    protected enum SudokuCellStates {
        ACTIVE_FIXED, ACTIVE_NONFIXED,
        INACTIVE_FIXED, INACTIVE_NONFIXED,
        HIGHLIGHTED_FIXED, HIGHLIGHTED_NONFIXED,
        FAULTY_FIXED, FAULTY_NONFIXED;
    }
    // endregion enum declaration

    // region Attributes
    // region ActiveCell
    private Cell m_activeCell = null;

    public Cell getActiveCell() {
        return this.m_activeCell;
    }

    public void setActiveCell(Cell activeCell) {
        this.m_activeCell = activeCell;
    }
    // endregion ActiveCell

    // region sudokuModel
    private Sudoku m_sudokuModel;

    public Sudoku getSudokuModel() {
        return m_sudokuModel;
    }

    public void setSudokuModel(Sudoku sudokuModel) {
        this.m_sudokuModel = sudokuModel;
    }
    // endregion sudokuModel

    // region SaveDataProvider
    private SaveDataProvider m_saveDataProvider;

    public SaveDataProvider getSaveDataProvider() {
        return m_saveDataProvider;
    }

    public void setSaveDataProvider(SaveDataProvider saveDataProvider) {
        this.m_saveDataProvider = saveDataProvider;
    }
    // endregion SaveDataProvider

    // region sudokuButtonArray
    // Used to store a 9x9 array of Buttons that are also contained in the SudokuGrid of the UI
    // This duplicate storage is done because of easier accessibility of the buttons
    private Button[][] m_sudokuButtonArray;

    public Button[][] getSudokuButtonArray() {
        return this.m_sudokuButtonArray;
    }

    public void setSudokuButtonArray(Button[][] sudokuButtonArray) {
        this.m_sudokuButtonArray = sudokuButtonArray;
    }
    // endregion sudokuButtonArray

    // region frameLayoutGrid
    private FrameLayout[][] m_frameLayoutGrid;
    public FrameLayout[][] getFrameLayoutGrid() {
        return m_frameLayoutGrid;
    }
    public void setFrameLayoutGrid(FrameLayout[][] frameLayoutGrid) {
        m_frameLayoutGrid = frameLayoutGrid;
    }
    // endregion frameLayoutGrid

    // region buttonSize
    private int m_buttonSize;
    public int getButtonSize() {
        return m_buttonSize;
    }
    public void setButtonSize(int buttonSize) {
        m_buttonSize = buttonSize;
    }
    // endregion buttonSize
    // endregion Attributes

    //region Abstract method signatures
    protected abstract void refreshUI();

    // Each subclass must set it's own content view
    protected abstract void setContentView();

    // Initialize all subclass-specific Buttons and it's event handlers
    protected abstract void instantiateButtons();

    // Event Handler for the buttons inside the sudoku grid
    public abstract void sudokuButtonClickedAction(int row, int col);

    // Event Handler for the input buttons (1-9)
    public abstract void inputButtonClickedAction(int number);
    //endregion

    // region Methods
    // When back key pressed -> Return to main menu instead of the previous dialogs
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        // Initializations
        super.onCreate(savedInstanceState);
        LanguageConfig.setAppLanguage(this);
        setContentView();
        GridLayout sudokuGrid = (GridLayout) findViewById(R.id.SudokuGridLayout);

        GlobalConfig.applyDisplaySizeToConstants(getResources().getDisplayMetrics());

        // Create Sudoku Grid for the UI
        ViewGroup.LayoutParams gridLayoutParams = sudokuGrid.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels; // Screen width in pixels
        float screenHeight = displayMetrics.heightPixels; // Screen height in pixels
        float sudokuGridPixelSize = GlobalConfig.SUDOKU_GRID_SIZE_PERCENTAGE * Math.min(screenWidth, screenHeight); // Sudoku grid shall fill the screen, either in horizontal or vertical layout
        gridLayoutParams.height = Math.round(sudokuGridPixelSize);
        gridLayoutParams.width = Math.round(sudokuGridPixelSize);
        sudokuGrid.setLayoutParams(gridLayoutParams);
        setFrameLayoutGrid(new FrameLayout[9][9]);
        setButtonSize((int) (sudokuGridPixelSize / 9.0f));

        // Fill Sudoku Grids
        setSudokuButtonArray(new Button[9][9]);
        for (int i = 0; i < sudokuGrid.getRowCount(); i++) {
            for (int j = 0; j < sudokuGrid.getColumnCount(); j++) {
                final int row = i;
                final int col = j;

                // Create Frame Layout (Used as wrapper to place the regular Sudoku Button and the 3x3 note-Buttons above each other
                ViewGroup.LayoutParams frameLayoutWrapperParams = new ViewGroup.LayoutParams(getButtonSize(), getButtonSize());
                //sudokuGrid.addView(relativeLayoutWrapper, i * sudokuGrid.getColumnCount() + j, relativeLayoutWrapperParams);

                // Create FrameLayout params and place Button and nested grid on top of each other
                getFrameLayoutGrid()[i][j] = new FrameLayout(getApplicationContext());
                getFrameLayoutGrid()[i][j].setClipChildren(false);

                sudokuGrid.addView(getFrameLayoutGrid()[i][j], i * sudokuGrid.getColumnCount() + j, frameLayoutWrapperParams);

                // Create button for the current cell
                Button button = new Button(getApplicationContext());
                TextViewCompat.setAutoSizeTextTypeWithDefaults(button, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button, 10,25, 1, TypedValue.COMPLEX_UNIT_DIP);
                button.setTypeface(null, Typeface.BOLD);
                button.setStateListAnimator(null);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sudokuButtonClickedAction(row, col);
                    }
                });
                ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(getButtonSize(), getButtonSize());
                getSudokuButtonArray()[i][j] = button;
                getFrameLayoutGrid()[i][j].addView(button);
            }
        }

        // INPUT BUTTONS: Create and set OnClickedActions
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
                    inputButtonClickedAction(Integer.parseInt(key));
                }
            });

            int buttonSize = (int) (screenWidth / ((float) keys.size()));
            ViewGroup.LayoutParams buttonLayoutParams = button.getLayoutParams();
            buttonLayoutParams.width = buttonSize;
        }

        instantiateButtons();

        setSaveDataProvider(new SaveDataProvider(getApplicationContext()));
    }

    // region RefreshUI Methods

    // Refreshes the complete and updates all values of the User Interface according to the model of the sudoku
    public void refreshUI(boolean markFaultyCells, boolean highlightCells) {
        SudokuCellStates[][] cellStates = getCellStates(markFaultyCells, highlightCells);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Button button = getSudokuButtonArray()[i][j];
                int backgroundColor, textColor;
                String buttonText = getButtonText(i, j);

                switch (cellStates[i][j]) {
                    case INACTIVE_FIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_inactive_background);
                        textColor = getResources().getColor(R.color.sudoku_button_inactive_font_fixed);
                        break;
                    case INACTIVE_NONFIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_inactive_background);
                        textColor = getResources().getColor(R.color.sudoku_button_inactive_font_nonfixed);
                        break;
                    case HIGHLIGHTED_FIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_highlighted_background);
                        textColor = getResources().getColor(R.color.sudoku_button_inactive_font_fixed);
                        break;
                    case HIGHLIGHTED_NONFIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_highlighted_background);
                        textColor = getResources().getColor(R.color.sudoku_button_inactive_font_nonfixed);
                        break;
                    case FAULTY_FIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_error_background);
                        textColor = getResources().getColor(R.color.sudoku_button_inactive_font_fixed);
                        break;
                    case FAULTY_NONFIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_error_background);
                        textColor = getResources().getColor(R.color.sudoku_button_inactive_font_nonfixed);
                        break;
                    case ACTIVE_FIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_active_background);
                        textColor = getResources().getColor(R.color.sudoku_button_active_font);
                        break;
                    case ACTIVE_NONFIXED:
                        backgroundColor = getResources().getColor(R.color.sudoku_button_active_background);
                        textColor = getResources().getColor(R.color.sudoku_button_active_font);
                        break;
                    default:
                        return; // Should not happen
                }

                SetButtonProperties(i, j, button, backgroundColor, textColor, buttonText);
            }
        }
    }

    // Returns the text that needs to be assigned to the given button
    protected String getButtonText(int row, int col) {
        if (getSudokuModel().getField()[row][col].getIsEmpty() == false) {
            return Integer.toString(getSudokuModel().getField()[row][col].getValue());
        } else {
            return " ";
        }
    }

    // Returns a 9x9 list of cell states that assigns each sudoku cell it's current state
    protected SudokuCellStates[][] getCellStates(boolean markFaultyCells, boolean highlightCells) {
        SudokuCellStates[][] cellStates = new SudokuCellStates[9][9];

        LinkedList<Pair<Integer, Integer>> cellsToHighlight = new LinkedList<Pair<Integer, Integer>>();
        LinkedList<Pair<Integer, Integer>> faultyCells = getSudokuModel().getListOfWrongValues();
        if (getActiveCell() != null) {
            Pair<Integer, Integer> activeCellPosition = Utils.getPositionOfCell(getActiveCell(), getSudokuModel());
            cellsToHighlight = getListOfCellsToHighlight(activeCellPosition.first, activeCellPosition.second);
        }

        for (int i = 0; i < getSudokuModel().getField().length; i++) {
            for (int j = 0; j < getSudokuModel().getField()[i].length; j++) {
                // Inactive Cell
                if (getSudokuModel().getField()[i][j].getIsFixedValue()) {
                    cellStates[i][j] = SudokuCellStates.INACTIVE_FIXED;
                } else {
                    cellStates[i][j] = SudokuCellStates.INACTIVE_NONFIXED;
                }

                // Highlight Cells
                if (highlightCells
                        && cellsToHighlight != null
                        && Utils.listContainsElement(cellsToHighlight, new Pair(i, j))) {
                    if (getSudokuModel().getField()[i][j].getIsFixedValue()) {
                        cellStates[i][j] = SudokuCellStates.HIGHLIGHTED_FIXED;
                    } else {
                        cellStates[i][j] = SudokuCellStates.HIGHLIGHTED_NONFIXED;
                    }
                }

                // Mark Faulty Cells
                if (markFaultyCells
                        && faultyCells != null
                        && Utils.listContainsElement(faultyCells, new Pair(i, j))) {
                    if (getSudokuModel().getField()[i][j].getIsFixedValue()) {
                        cellStates[i][j] = SudokuCellStates.FAULTY_FIXED;
                    } else {
                        cellStates[i][j] = SudokuCellStates.FAULTY_NONFIXED;
                    }
                }

                // ActiveCell
                if (getActiveCell() != null && getSudokuModel().getField()[i][j] == getActiveCell()) {
                    if (getSudokuModel().getField()[i][j].getIsFixedValue()) {
                        cellStates[i][j] = SudokuCellStates.ACTIVE_FIXED;
                    } else {
                        cellStates[i][j] = SudokuCellStates.ACTIVE_NONFIXED;
                    }
                }

            }
        }

        return cellStates;
    }

    // Updates all properties of one cell in the UI
    private static void SetButtonProperties(int row, int col, Button button, int backgroundColor, int textColor, String text) {
        if (button != null) {
            button.setTextColor(textColor);
            button.setText(text);

            GradientDrawable drawableInner = new GradientDrawable();
            GradientDrawable drawableBorders = new GradientDrawable();
            drawableInner.setColor(backgroundColor);
            drawableBorders.setColor(GlobalConfig.SUDOKU_BORDER_COLOR);

            Drawable[] layers = {drawableBorders, drawableInner};
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            int[] borderThicknessValues = getBorderThicknessValues(row, col);
            layerDrawable.setLayerInset(1, borderThicknessValues[0], borderThicknessValues[1], borderThicknessValues[2], borderThicknessValues[3]);

            button.setBackground(layerDrawable);
        }
    }

    // Returns an array of the border thickness values for the given cell in the order [left,top,right,bottom]
    protected static int[] getBorderThicknessValues(int row, int col) {
        int[] borders = new int[4];

        // Set Left Border Thickness
        if (col == 0) {
            borders[0] = GlobalConfig.STROKE_WIDTH_BIG_BORDER;
        } else if (col % 3 == 0) {
            borders[0] = GlobalConfig.STROKE_WIDTH_MID_BORDER;
        } else {
            borders[0] = GlobalConfig.STROKE_WIDTH_SMALL_BORDER;
        }

        // Set Top Border Thickness
        if (row == 0) {
            borders[1] = GlobalConfig.STROKE_WIDTH_BIG_BORDER;
        } else if (row % 3 == 0) {
            borders[1] = GlobalConfig.STROKE_WIDTH_MID_BORDER;
        } else {
            borders[1] = GlobalConfig.STROKE_WIDTH_SMALL_BORDER;
        }

        // Set Right Border Thickness
        if (col == 8) {
            borders[2] = GlobalConfig.STROKE_WIDTH_BIG_BORDER;
        } else if (col % 3 == 2) {
            borders[2] = GlobalConfig.STROKE_WIDTH_MID_BORDER;
        } else {
            borders[2] = GlobalConfig.STROKE_WIDTH_SMALL_BORDER;
        }

        // Set Bottom Border Thickness
        if (row == 8) {
            borders[3] = GlobalConfig.STROKE_WIDTH_BIG_BORDER;
        } else if (row % 3 == 2) {
            borders[3] = GlobalConfig.STROKE_WIDTH_MID_BORDER;
        } else {
            borders[3] = GlobalConfig.STROKE_WIDTH_SMALL_BORDER;
        }

        return borders;
    }

    // Returns a list of Cells that need to be highlighted
    // All cells that are in the same row, column or block as the active cell will be highlighted
    private LinkedList<Pair<Integer, Integer>> getListOfCellsToHighlight(int row, int col) {
        LinkedList<Pair<Integer, Integer>> list = new LinkedList<Pair<Integer, Integer>>();
        for (int i = 0; i < getSudokuModel().getField().length; i++) {
            for (int j = 0; j < getSudokuModel().getField().length; j++) {
                if (i == row || j == col || (row / 3 == i / 3 && col / 3 == j / 3)) {
                    list.add(new Pair(i, j));
                }
            }
        }
        return list;
    }
    // endregion
    // endregion Methods
}
