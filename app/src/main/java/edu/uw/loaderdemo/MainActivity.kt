package edu.uw.loaderdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*



class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //list-model
        val data = arrayOf("Dog", "Cat", "Android", "Inconceivable")

        //list-view
        val listView = findViewById<ListView>(R.id.word_list_view)

        //list-controller
        adapter = ArrayAdapter(this, R.layout.list_item_layout, R.id.txt_item_word, data);
        listView.adapter = adapter


        //handle button input
        val inputText = findViewById<TextView>(R.id.txt_add_word)
        val addButton = findViewById<Button>(R.id.btn_add_word)
        addButton.setOnClickListener {
            val inputWord = inputText.text.toString()
            Log.v(TAG, "To add: $inputWord")
        }

        //handle item clicking
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val word = parent.getItemAtPosition(position) as String
            Log.v(TAG, "Clicked on '$word'")
        }

    }
}
