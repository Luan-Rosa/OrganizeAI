package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private DataBaseHandler db;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (isFirstTime()) {

            showFirstTimeDialog();
        }

        db = new DataBaseHandler(this);
        db.openDataBase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        return preferences.getBoolean("firstTime", true);
    }

    private void updateFirstTimeStatus() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstTime", false);
        editor.apply();
    }

    private void showFirstTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bem-vindo!");
        builder.setMessage("Obrigado por baixar o aplicativo pela primeira vez, ao deslizar para a direita você edita a tarefa e para a esquerda exclui");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                updateFirstTimeStatus();
            }
        });

        builder.create().show();
    }
}
