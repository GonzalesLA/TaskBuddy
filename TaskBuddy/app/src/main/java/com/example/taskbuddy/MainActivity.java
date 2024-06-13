package com.example.taskbuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button add;
    Button navigateButton;
    AlertDialog dialog;
    LinearLayout layout;

    SharedPreferences sharedPreferences;
    Set<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("TaskBuddyPrefs", MODE_PRIVATE);
        tasks = sharedPreferences.getStringSet("tasks", new HashSet<>());

        add = findViewById(R.id.add);
        navigateButton = findViewById(R.id.inst_button);
        layout = findViewById(R.id.container);

        buildDialog();
        loadTasks();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, instructions.class);
                startActivity(intent);
            }
        });
    }

    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter your Task")
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskName = name.getText().toString();
                        addCard(taskName);
                        saveTask(taskName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addCard(final String name) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                deleteTask(name);
            }
        });

        layout.addView(view);
    }

    private void saveTask(String task) {
        tasks.add(task);
        sharedPreferences.edit().putStringSet("tasks", tasks).apply();
    }

    private void deleteTask(String task) {
        tasks.remove(task);
        sharedPreferences.edit().putStringSet("tasks", tasks).apply();
    }

    private void loadTasks() {
        for (String task : tasks) {
            addCard(task);
        }
    }
}
