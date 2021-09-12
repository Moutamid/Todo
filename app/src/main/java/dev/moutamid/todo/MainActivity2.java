package dev.moutamid.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dev.moutamid.todo.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}