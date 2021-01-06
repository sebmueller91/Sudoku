package dgs_software.sudoku.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import dgs_software.sudoku.R;
import dgs_software.sudoku.activities.SudokuPlayActivity;

public class ChooseDifficultyDialog extends Dialog {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public ChooseDifficultyDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_difficulty);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button easyButton = (Button) findViewById(R.id.button_easy);
        Button normalButton = (Button) findViewById(R.id.button_normal);
        Button hardButton = (Button) findViewById(R.id.button_hard);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SudokuPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("difficulty",1);
                intent.putExtras(bundle);
                c.startActivity(intent);
            }
        });
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SudokuPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("difficulty",2);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                c.startActivity(intent);
            }
        });
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SudokuPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("difficulty",3);
                intent.putExtras(bundle);
                c.startActivity(intent);
            }
        });
    }
}