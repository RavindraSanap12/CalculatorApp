package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calculator.databinding.ActivityMainBinding;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    boolean lastNumeric = false;
    boolean stateError = false;
    boolean lastDot = false;

    private Expression expression;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClearClick(View view) {
        binding.dataTv.setText("");
        lastNumeric = false;
    }

    public void onBackClick(View view) {
        String text = binding.dataTv.getText().toString();
        if (!text.isEmpty()) {
            // Check if there are enough characters to remove
            if (text.length() > 1) {
                binding.dataTv.setText(text.substring(0, text.length() - 1));
                try {
                    char lastChar = text.charAt(text.length() - 2); // Access second last character
                    if (Character.isDigit(lastChar)) {
                        // If the last two characters are digits, remove them from resultTv
                        String resultText = binding.resultTv.getText().toString();
                        if (resultText.length() >= 2) {
                            binding.resultTv.setText(resultText.substring(0, resultText.length() - 2));
                        }
                        onEqual();
                    }
                } catch (Exception e) {
                    binding.resultTv.setText("");
                    binding.resultTv.setVisibility(View.GONE);
                    Log.e("last char error", e.toString());
                }
            } else {
                // If there's only one character, just remove it
                binding.dataTv.setText("");
                // Clear resultTv when dataTv becomes empty
                binding.resultTv.setText("");
                binding.resultTv.setVisibility(View.GONE);
                // Handle any other operations you may need
            }
        }
    }



    public void onOperatorClick(View view) {

        if(!stateError && lastNumeric)
        {
            TextView dataTV = binding.dataTv;
            Button button = (Button) view;
            binding.dataTv.append(button.getText());
            lastDot =false;
            lastNumeric = false;

            onEqual();

        }
    }

    public void onEqualClick(View view) {
        onEqual();
        binding.dataTv.setText(binding.resultTv.getText().toString().substring(1));
    }

    public void onAllClearClick(View view) {
        binding.dataTv.setText("");
        binding.resultTv.setText("");
        stateError = false;
        lastDot = false;
        lastNumeric = false;
        binding.resultTv.setVisibility(view.GONE);
    }

    public void onDigitClick(View view) {
        if(stateError)
        {

            TextView dataTV = binding.dataTv;
            Button button = (Button) view;
            dataTV.setText(button.getText());
            stateError = false;
        }
        else
        {
            TextView dataTV = binding.dataTv;
            Button button = (Button) view;
            dataTV.append(button.getText());
        }
        lastNumeric = true;
        onEqual();
    }

    public void onEqual()
    {
        if (lastNumeric && !stateError) {
            String txt = binding.dataTv.getText().toString();

            expression = new ExpressionBuilder(txt).build();

            try {
                double result = expression.evaluate();
                binding.resultTv.setVisibility(View.VISIBLE);
                binding.resultTv.setText("=" + result);
            } catch (Exception ex) {
                Log.e("Evaluate Error", ex.toString());
                binding.resultTv.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }

    }
}