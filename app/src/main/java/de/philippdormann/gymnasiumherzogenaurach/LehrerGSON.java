package de.philippdormann.gymnasiumherzogenaurach;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LehrerGSON extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lehrer);

        final RelativeLayout addLayout = findViewById(R.id.addLehrerLayout);
        final RelativeLayout lehrerListLayout = findViewById(R.id.lehrerListLayout);
        final FloatingActionButton floatingActionButton = findViewById(R.id.fabAddLehrer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lehrerListLayout.setVisibility(View.INVISIBLE);
                addLayout.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("GYMH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //String lehrerJSONsaved = sharedPreferences.getString("LEHRER", "");

        Gson gson = new Gson();
        List<LehrerObject> lehrer = new ArrayList<>();
        lehrer.add(new LehrerObject("Basti", "Ansorge"));
        lehrer.add(new LehrerObject("Astrid", "Dobler"));
        lehrer.add(new LehrerObject("Rolf", "Kraus"));
        LehrerArray lehrerArray = new LehrerArray(lehrer);
        String obj2json = gson.toJson(lehrerArray);
        editor.putString("LEHRER", obj2json);
        editor.apply();

        final List<String> lehrerNamen = new ArrayList<>();
        for (LehrerObject object : lehrerArray.lehrerArray) {
            System.out.println(object.vorname + " " + object.nachname);
            lehrerNamen.add(object.vorname + " " + object.nachname);
        }
        ListView listView = findViewById(R.id.lehrerList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lehrerNamen);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("LOGGER", String.valueOf(position));
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Löschen")
                        .setMessage("Willst du wirklich &quot;" + lehrerNamen.get(position) + "&quot; löschen")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getApplicationContext(), "Yaay", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    class LehrerObject {
        final String vorname;
        final String nachname;

        LehrerObject(String vorname, String nachname) {
            this.vorname = vorname;
            this.nachname = nachname;
        }
    }

    class LehrerArray {
        final List<LehrerObject> lehrerArray;

        LehrerArray(List<LehrerObject> lehrerArray) {
            this.lehrerArray = lehrerArray;
        }
    }
}