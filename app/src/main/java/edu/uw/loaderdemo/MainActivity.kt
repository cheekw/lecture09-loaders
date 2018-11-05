package edu.uw.loaderdemo

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.provider.UserDictionary
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val TAG = "MainActivity"

    private lateinit var adapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //example: query words
        val projection = arrayOf(UserDictionary.Words.WORD, UserDictionary.Words.FREQUENCY, UserDictionary.Words._ID)
        val cursor = contentResolver.query(UserDictionary.Words.CONTENT_URI, projection, null, null, null)

        cursor.moveToFirst() //move to the first item
        val field0 = cursor.getString(0) //get the first field (column you specified) as a String
        val word = cursor.getString(cursor.getColumnIndexOrThrow("word")) //get the "word" field as a String
        cursor.moveToNext() //go to the next item
        Log.v(TAG, word)


        //list-model
        val data = arrayOf("Dog", "Cat", "Android", "Inconceivable")

        //list-view
        val listView = findViewById<ListView>(R.id.word_list_view)

        //list-controller
        adapter = SimpleCursorAdapter(
                this,
                R.layout.list_item_layout, //item to inflate
                null, //cursor to show
                arrayOf(UserDictionary.Words.WORD, UserDictionary.Words.FREQUENCY), //fields to display
                intArrayOf(R.id.txt_item_word, R.id.txt_item_freq), //where to display them
                0 //flags
        )
        listView.adapter = adapter

        //load the data
        supportLoaderManager.initLoader(0, null, this)


        //handle button input
        val inputText = findViewById<TextView>(R.id.txt_add_word)
        val addButton = findViewById<Button>(R.id.btn_add_word)
        addButton.setOnClickListener {
            val inputWord = inputText.text.toString()
            Log.v(TAG, "To add: $inputWord")

            addWord(null)
        }

        //handle item clicking
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as Cursor //item we clicked on
            val word = item.getString(item.getColumnIndexOrThrow(UserDictionary.Words.WORD))
            val freq = item.getInt(item.getColumnIndexOrThrow(UserDictionary.Words.FREQUENCY))
            Log.v(TAG, "Clicked on '$word' ($freq)")

            setFrequency(id, freq + 1)
        }

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        val projection = arrayOf(UserDictionary.Words.WORD, UserDictionary.Words.FREQUENCY, UserDictionary.Words._ID)

        //create the CursorLoader
        val loader = CursorLoader(
                this,
                UserDictionary.Words.CONTENT_URI,
                projection, null, null, null
        )

        return loader
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        //replace the data
        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        //empty the data
        adapter.swapCursor(null)
    }

    //adds (creates) a word to the list
    fun addWord(v: View?) {
        val inputText = findViewById<TextView>(R.id.txt_add_word)

        val newValues = ContentValues()
        newValues.put(UserDictionary.Words.WORD, inputText.text.toString())
        newValues.put(UserDictionary.Words.FREQUENCY, 100)
        newValues.put(UserDictionary.Words.APP_ID, "edu.uw.loaderdemo")
        newValues.put(UserDictionary.Words.LOCALE, "en_US")

        val newUri = contentResolver.insert(
                UserDictionary.Words.CONTENT_URI, // the user dictionary content URI!
                newValues                   // the values to insert
        )
        Log.v(TAG, "New word at: " + newUri!!)
    }

    //sets (updates) the frequency of the word with the given id
    fun setFrequency(id: Long, newFrequency: Int) {
        val newValues = ContentValues()
        newValues.put(UserDictionary.Words.FREQUENCY, newFrequency)

        contentResolver.update(
                ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI, id),
                newValues, null, null //no selection
        )
    }
}
