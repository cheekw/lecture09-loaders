package edu.uw.loaderdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //list-model
        String[] data = {"Dog","Cat","Android","Inconceivable"};

        //list-view
        AdapterView listView = (AdapterView)findViewById(R.id.word_list_view);

        //list-controller
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, R.id.txt_list_item, data);
        listView.setAdapter(adapter);


        //handle button input
        final TextView inputText = (TextView)findViewById(R.id.txt_add_word);
        Button addButton = (Button)findViewById(R.id.btn_add_word);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputWord = inputText.getText().toString();
                Log.v(TAG, "To add: "+inputWord);
            }
        });
    }
}
