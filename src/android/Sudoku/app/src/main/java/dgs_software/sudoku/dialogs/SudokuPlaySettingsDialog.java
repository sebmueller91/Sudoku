package dgs_software.sudoku.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.SudokuPlayActivity;
import dgs_software.sudoku.config.LanguageConfig;

public class SudokuPlaySettingsDialog extends Dialog {

    // region Attributes
    private SudokuPlayActivity m_activity;
    public SudokuPlayActivity getActivity() {
        return this.m_activity;
    }

    public void setActivity(SudokuPlayActivity activity) {
        this.m_activity = activity;
    }
    // endregion Attributes

    public SudokuPlaySettingsDialog(@NonNull Context context, SudokuPlayActivity activity) {
        super(context);
        setActivity(activity);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageConfig.setAppLanguage(getContext());
        setContentView(R.layout.dialog_sudoku_play_settings);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Switch showFaultyCellsSwitch = (Switch) findViewById(R.id.showFaultyCellsSwitch);
        final Switch highlightCellsSwitch = (Switch) findViewById(R.id.highlightCellsSwitch);
        final Switch deleteNotesSwitch = (Switch) findViewById(R.id.deleteNotesSwitch);

        showFaultyCellsSwitch.setChecked(getActivity().getShowFaultyCells());
        highlightCellsSwitch.setChecked(getActivity().getHighlightCells());
        deleteNotesSwitch.setChecked(getActivity().getDeleteNotes());

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

        deleteNotesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setDeleteNotes(!getActivity().getDeleteNotes());
                deleteNotesSwitch.setChecked(getActivity().getDeleteNotes());
                getActivity().getSaveDataProvider().saveSudokuPlayPreferences_deleteNotes(getActivity().getDeleteNotes());
            }
        });
    }

}
