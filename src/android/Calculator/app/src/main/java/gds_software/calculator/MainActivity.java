package gds_software.Calculator;

import java.util.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public enum Operation
    {
        ADD, SUB, MUL, DIV
    }

    EditText crunchifyEditText;
    double mValueOne, mValueTwo;

    Operation curOperation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hashtable<String, Button> buttonDict = new Hashtable<String, Button>();
        buttonDict.put("0",(Button) findViewById(R.id.button0));
        buttonDict.put("1",(Button) findViewById(R.id.button1));
        buttonDict.put("2",(Button) findViewById(R.id.button2));
        buttonDict.put("3",(Button) findViewById(R.id.button3));
        buttonDict.put("4",(Button) findViewById(R.id.button4));
        buttonDict.put("5",(Button) findViewById(R.id.button5));
        buttonDict.put("6",(Button) findViewById(R.id.button6));
        buttonDict.put("7",(Button) findViewById(R.id.button7));
        buttonDict.put("8",(Button) findViewById(R.id.button8));
        buttonDict.put("9",(Button) findViewById(R.id.button9));
        buttonDict.put(".",(Button) findViewById(R.id.buttondot));
        buttonDict.put("+",(Button) findViewById(R.id.buttonadd));
        buttonDict.put("-",(Button) findViewById(R.id.buttonsub));
        buttonDict.put("*",(Button) findViewById(R.id.buttonmul));
        buttonDict.put("/",(Button) findViewById(R.id.buttondiv));
        buttonDict.put("c",(Button) findViewById(R.id.buttonC));
        buttonDict.put("=",(Button) findViewById(R.id.buttoneql));

        crunchifyEditText = (EditText) findViewById(R.id.edt1);

        Set<String> keys = buttonDict.keySet();
        for (final String key: keys) {
            Button button = buttonDict.get(key);
            if (key.equals("+")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OperationKeyClicked(Operation.ADD);
                    }
                });
            } else if (key.equals("-")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OperationKeyClicked(Operation.SUB);
                    }
                });
            } else if (key.equals("*")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OperationKeyClicked(Operation.MUL);
                    }
                });
            } else if (key.equals("/")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OperationKeyClicked(Operation.DIV);
                    }
                });
            } else if (key.equals("=")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EqualKeyClicked();
                    }
                });
            } else if (key.equals("c")) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CKeyClicked();
                    }
                });
            } else {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberKeyClicked(key);
                    }
                });
            }
        }
    }

    public void NumberKeyClicked(String number) {
        crunchifyEditText.setText(crunchifyEditText.getText() + number);
    }

    public void CKeyClicked() {
        crunchifyEditText.setText("");
        curOperation = null;
    }

    public void EqualKeyClicked() {
        if (curOperation == null) {
            return;
        }

        float[] numberToSet = new float[]{0};
        if (!TryParseDouble(crunchifyEditText, numberToSet)) {
            return;
        }
        mValueTwo = numberToSet[0];

        double result = -1;
        switch (curOperation) {
            case ADD:
                result = mValueOne + mValueTwo;
                break;
            case SUB:
                result = mValueOne - mValueTwo;
                break;
            case MUL:
                result = mValueOne * mValueTwo;
                break;
            case DIV:
                result = mValueOne / mValueTwo;
                break;
        }
        crunchifyEditText.setText(Double.toString(result));
        curOperation = null;
    }


    public void OperationKeyClicked(Operation operation) {
        if (crunchifyEditText == null) {
            crunchifyEditText.setText("");
        } else {
            float[] numberToSet = new float[]{0};
            if (!TryParseDouble(crunchifyEditText, numberToSet)) {
                return;
            }
            mValueOne = numberToSet[0];
            curOperation = operation;
            crunchifyEditText.setText("");
        }
    }

    private boolean TryParseDouble(EditText text, float[] numberToSet) {
        float tmp = 0;
        try {
            tmp = Float.parseFloat(text.getText() + "");
        } catch (Exception e) {
            return false;
        }
        numberToSet[0] = tmp;
        return true;
    }
}