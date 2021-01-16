package dgs_software.sudoku.config;

import android.graphics.Color;

public class Constants {
    public static final float SUDOKU_GRID_SIZE_PERCENTAGE = 0.98f;
    public static final int SUDOKU_BUTTON_TEXT_SIZE = 20;
    public static final int SUDOKU_NOTE_BUTTON_TEXT_SIZE = 8;

    public static final int STROKE_WIDTH_MID_BORDER = 4;
    public static final int STROKE_WIDTH_BIG_BORDER = STROKE_WIDTH_MID_BORDER * 2; // Inner lines are drawn from two sides - thus the outer lines must be twice as thick
    public static final int STROKE_WIDTH_SMALL_BORDER = 2;
    public static final int SUDOKU_BORDER_COLOR = Color.BLACK;
}
