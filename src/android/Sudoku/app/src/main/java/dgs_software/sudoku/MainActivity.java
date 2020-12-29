package dgs_software.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playSudokuButton = (Button) findViewById(R.id.PlaySudokuButton);
        Button solveSudokuButton = (Button) findViewById(R.id.solveSudokuButton);

        playSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChooseDifficulty cdd=new DialogChooseDifficulty(MainActivity.this);
                cdd.show();
//                Intent intent = new Intent(v.getContext(), SudokuPlay.class);
//                startActivity(intent);
            }
        });

        solveSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SudokuSolver.class);
                startActivity(intent);
            }
        });
    }
}