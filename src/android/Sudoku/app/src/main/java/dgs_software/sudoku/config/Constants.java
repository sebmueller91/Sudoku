package dgs_software.sudoku.config;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

public class Constants {
    public static final float SUDOKU_GRID_SIZE_PERCENTAGE = 0.98f;
    public static int SUDOKU_BUTTON_TEXT_SIZE = 20;
    public static int SUDOKU_NOTE_BUTTON_TEXT_SIZE = 8;

    public static int STROKE_WIDTH_MID_BORDER = 4;
    public static int STROKE_WIDTH_BIG_BORDER = STROKE_WIDTH_MID_BORDER * 2; // Inner lines are drawn from two sides - thus the outer lines must be twice as thick
    public static int STROKE_WIDTH_SMALL_BORDER = 2;
    public static final int SUDOKU_BORDER_COLOR = Color.BLACK;

    private static final float SUDOKU_BUTTON_TEXT_SIZE_FACTOR = 7.6f;
    private static final float SUDOKU_NOTE_BUTTON_TEXT_SIZE_FACTOR = 3.3f;
    private static final float STROKE_WIDTH_MID_BORDER_FACTOR = 1.63f;
    private static final float STROKE_WIDTH_SMALL_BORDER_FACTOR = 0.81f;

    public static void applyDisplaySizeToConstants(DisplayMetrics displayMetrics) {
        // Calculate factor that is directly proportional to the physical width of the device
        float displayWidthFactor = ((float) displayMetrics.widthPixels) / displayMetrics.densityDpi;

        SUDOKU_BUTTON_TEXT_SIZE = (Math.round(SUDOKU_BUTTON_TEXT_SIZE_FACTOR * displayWidthFactor));
        SUDOKU_NOTE_BUTTON_TEXT_SIZE = (Math.round(SUDOKU_NOTE_BUTTON_TEXT_SIZE_FACTOR * displayWidthFactor));
        STROKE_WIDTH_SMALL_BORDER = (Math.round(STROKE_WIDTH_SMALL_BORDER_FACTOR * displayWidthFactor));
        STROKE_WIDTH_MID_BORDER = (Math.round(STROKE_WIDTH_MID_BORDER_FACTOR * displayWidthFactor));
        STROKE_WIDTH_BIG_BORDER = STROKE_WIDTH_MID_BORDER * 2;
    }
}
