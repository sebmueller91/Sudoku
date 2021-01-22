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

        final Switch switch1 = (Switch) findViewById(R.id.switch1);
        final Switch switch2 = (Switch) findViewById(R.id.switch2);

        switch1.setChecked(getActivity().getShowFaultyCells());
        switch2.setChecked(getActivity().getHighlightCells());

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setShowFaultyCells(!getActivity().getShowFaultyCells());
                switch1.setChecked(getActivity().getShowFaultyCells());
            }
        });

        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setHighlightCells(!getActivity().getHighlightCells());
                switch2.setChecked(getActivity().getHighlightCells());
            }
        });
        // TODO:
    }

}
