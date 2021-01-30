package dgs_software.sudoku.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.SudokuPlayActivity;
import dgs_software.sudoku.data.SaveDataProvider;
import dgs_software.sudoku.model.Sudoku;

public class SudokuPlayPreferencesDialog extends Dialog {

    // region Attributes
    private SudokuPlayActivity m_activity;
    public SudokuPlayActivity getActivity() {
        return this.m_activity;
    }

    public void setActivity(SudokuPlayActivity activity) {
        this.m_activity = activity;
    }
    // endregion Attributes

    public SudokuPlayPreferencesDialog(@NonNull Context context, SudokuPlayActivity activity) {
        super(context);
        setActivity(activity);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sudoku_play_preferences);

        final Switch showFaultyCellsSwitch = (Switch) findViewById(R.id.showFaultyCellsSwitch);
        final Switch highlightCellsSwitch = (Switch) findViewById(R.id.highlightCellsSwitch);

        showFaultyCellsSwitch.setChecked(getActivity().getShowFaultyCells());
        highlightCellsSwitch.setChecked(getActivity().getHighlightCells());

        showFaultyCellsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setShowFaultyCells(!getActivity().getShowFaultyCells());
                showFaultyCellsSwitch.setChecked(getActivity().getShowFaultyCells());
                getActivity().getSaveDataProvider().saveSudokuPlayPreferences_showFaultyCells(getActivity().getShowFaultyCells());
            }
        });

        highlightCellsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setHighlightCells(!getActivity().getHighlightCells());
                highlightCellsSwitch.setChecked(getActivity().getHighlightCells());
                getActivity().getSaveDataProvider().saveSudokuPlayPreferences_highlightCells(getActivity().getHighlightCells());
            }
        });
    }

}
