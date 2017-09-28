package com.example.asionbo.myapplication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView view = new TextView(this);
        view.setText("hello world!");
        view.setGravity(Gravity.CENTER);
        view.setTextColor(getResources().getColor(android.R.color.black));
        Button btn = (Button) findViewById(R.id.btn);
        FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        FloatingActionButton fabNotes = (FloatingActionButton) findViewById(R.id.fab_notes);
        FloatingActionButton fabStop = (FloatingActionButton) findViewById(R.id.fab_stop);
        FloatingActionButton fabPending= (FloatingActionButton) findViewById(R.id.fab_pending);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"hello world",Toast.LENGTH_LONG).show();
            }
        });

        btn.setOnLongClickListener(view1 -> {

            return false;
        });

        fabNotes.setOnClickListener(v1 ->{
            Snackbar.make(v1,"click notes",Snackbar.LENGTH_SHORT).show();
            fabMenu.collapse();
        });
        fabStop.setOnClickListener(v2 -> {
            Snackbar.make(v2,"click stop",Snackbar.LENGTH_SHORT).show();
            fabMenu.collapse();
        });
        fabPending.setOnClickListener(v3 -> {
            Snackbar.make(v3,"click pending",Snackbar.LENGTH_SHORT)
                    .setAction("undo", view12 -> {
                        Toast.makeText(getApplicationContext(),"undo action",Toast.LENGTH_SHORT).show();
                    }).show();
            fabMenu.collapse();
        });
    }
}
