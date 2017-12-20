package com.example.sysdevns.livedatahandler;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    static final int MAX = 100;

    private ProgressBar progressBar;
    private Button startButton;
    private Button resetButton;
    private Button stopButton;
    private TextView counterTextView;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        findViews();
        addOnClickListeners();
        listenToViewModel();
    }

    private void findViews() {
        counterTextView = findViewById(R.id.hello);
        startButton = findViewById(R.id.start_button);
        resetButton = findViewById(R.id.reset);
        stopButton = findViewById(R.id.stop_button);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(MAX);
    }

    private void addOnClickListeners() {
        startButton.setOnClickListener(view -> viewModel.startCounter());
        resetButton.setOnClickListener(view -> viewModel.resetCounter());
        stopButton.setOnClickListener(view -> viewModel.stopCounter());
    }

    private void listenToViewModel() {
        viewModel.getCounter().observe(this, integer -> {
            if (integer != null) {
                counterTextView.setText(getString(R.string.count_value, integer));
                progressBar.setProgress(integer);
            }
        });

        viewModel.getStartButtonEnabled().observe(this, enabled -> {
            if (enabled != null) {
                startButton.setEnabled(enabled);
            }
        });

        viewModel.getResetButtonEnabled().observe(this, enabled -> {
            if (enabled != null) {
                resetButton.setEnabled(enabled);
            }
        });

        viewModel.getStopButtonEnabled().observe(this, enabled -> {
            if (enabled != null) {
                stopButton.setEnabled(enabled);
            }
        });

        viewModel.getStartNotRestart().observe(this, start -> {
            if (start != null) {
                startButton.setText(start ? "START" : "RESTART");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + counterTextView.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + counterTextView.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + counterTextView.getText().toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + counterTextView.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + counterTextView.getText().toString());
    }
}