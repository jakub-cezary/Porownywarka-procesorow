package com.example.vilq.porownywarka_procesorow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button compare = (Button) findViewById(R.id.button);

        //String[] processor_family={"Intel Atom", "Intel Celeron","Intel Pentium","Intel Core M","Intel Core i3","Intel Core i5","Intel Core i7","Intel Xeon", "AMD A", "AMD FX"};

        List<String> listaIdProc = new ArrayList<String>();
        List<String> listaProc = new ArrayList<String>();
        List<String> rodzinaProc = new ArrayList<String>();
        List<String> atomProc = new ArrayList<String>();
        String polaTabeli[]={"id_procesora","nazwa_procesora"};
        String polaTabeli2[]={"nazwa_rodziny"};
        mDBHelper = new DatabaseHelper(this);

        //Check exists database
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SQLiteDatabase myDatabase;
        myDatabase = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDatabase.query("lista_procesorow", polaTabeli, null, null, null, null, null);

        cursor.moveToFirst();

        int lengthCursor = cursor.getCount();

        for(int i=0;i<lengthCursor;i++)
        {
            listaIdProc.add(cursor.getString(0));
            listaProc.add(cursor.getString(1));
            cursor.moveToNext();
        }
        myDatabase.close();

        SQLiteDatabase myDatabase2;
        myDatabase2 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor2 = myDatabase2.query("rodziny_procesorow",polaTabeli2,null,null,null,null,null);
        cursor2.moveToFirst();
        int lengthCursor2 = cursor2.getCount();
        for(int i=0;i<lengthCursor2;i++)
        {
            rodzinaProc.add(cursor2.getString(0));
            cursor2.moveToNext();
        }
        myDatabase2.close();

        SQLiteDatabase myDatabase3;
        myDatabase3 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor3 = myDatabase3.rawQuery("select nazwa_procesora from lista_procesorow where nazwa_procesora like '%Atom%'",null);
        cursor3.moveToFirst();
        int lengthCursor3 = cursor3.getCount();
        for(int i=0;i<lengthCursor3;i++)
        {
            atomProc.add(cursor3.getString(0));
            cursor3.moveToNext();
        }
        myDatabase3.close();

        final ArrayAdapter<String> stringArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, rodzinaProc);
        final Spinner spinner = (Spinner)  findViewById(R.id.spinner);
        spinner.setAdapter(stringArrayAdapter);
        final Spinner spinner3 = (Spinner)  findViewById(R.id.spinner3);
        spinner3.setAdapter(stringArrayAdapter);

        final ArrayAdapter<String> stringArrayAdapter1= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaProc);
        final Spinner spinner2 = (Spinner)  findViewById(R.id.spinner2);
        final Spinner spinner4 = (Spinner)  findViewById(R.id.spinner4);
        spinner4.setAdapter(stringArrayAdapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                TextView textView = (TextView) findViewById(R.id.textView);
                String text = spinner.getSelectedItem().toString();

                List<String> wybranaRodzina = new ArrayList<String>();

                SQLiteDatabase myDatabase4;
                myDatabase4 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor4 = myDatabase4.rawQuery("select nazwa_procesora from lista_procesorow where nazwa_procesora like '%"+ text +"%'",null);
                cursor4.moveToFirst();
                int lengthCursor4 = cursor4.getCount();

                for(int i=0;i<lengthCursor4;i++)
                {
                    wybranaRodzina.add(cursor4.getString(0));
                    cursor4.moveToNext();
                }
                myDatabase4.close();

                ArrayAdapter<String> stringArrayAdapter2 = new ArrayAdapter<String>(mDBHelper.mContext,android.R.layout.simple_spinner_dropdown_item, wybranaRodzina);
                spinner2.setAdapter(stringArrayAdapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //TextView textView = (TextView) findViewById(R.id.textView);
                String text = spinner3.getSelectedItem().toString();

                List<String> wybranaRodzina2 = new ArrayList<String>();

                SQLiteDatabase myDatabase5;
                myDatabase5 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor5 = myDatabase5.rawQuery("select nazwa_procesora from lista_procesorow where nazwa_procesora like '%"+ text +"%'",null);
                cursor5.moveToFirst();
                int lengthCursor5 = cursor5.getCount();

                for(int i=0;i<lengthCursor5;i++)
                {
                    wybranaRodzina2.add(cursor5.getString(0));
                    cursor5.moveToNext();
                }
                myDatabase5.close();

                ArrayAdapter<String> stringArrayAdapter3 = new ArrayAdapter<String>(mDBHelper.mContext,android.R.layout.simple_spinner_dropdown_item, wybranaRodzina2);
                spinner4.setAdapter(stringArrayAdapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        /*spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //TextView textView = (TextView) findViewById(R.id.textView);
                String text = spinner2.getSelectedItem().toString();

                String wybranyProc1 = new String();

                SQLiteDatabase myDatabase7;
                myDatabase7 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor7 = myDatabase7.rawQuery("select id_procesora from lista_procesorow where nazwa_procesora like '%"+ text +"%'",null);
                cursor7.moveToFirst();
                int lengthCursor7 = cursor7.getCount();

                for(int i=0;i<lengthCursor7;i++)
                {
                    wybranyProc1 = cursor7.getString(0);
                    cursor7.moveToNext();
                }
                myDatabase7.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //TextView textView = (TextView) findViewById(R.id.textView);
                String text = spinner4.getSelectedItem().toString();

                String wybranyProc = new String();

                SQLiteDatabase myDatabase6;
                myDatabase6 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor6 = myDatabase6.rawQuery("select id_procesora from lista_procesorow where nazwa_procesora like '%"+ text +"%'",null);
                cursor6.moveToFirst();
                int lengthCursor6 = cursor6.getCount();

                for(int i=0;i<lengthCursor6;i++)
                {
                    wybranyProc = cursor6.getString(0);
                    cursor6.moveToNext();
                }
                myDatabase6.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = spinner2.getSelectedItem().toString();

                String wybranyProc1 = new String();

                SQLiteDatabase myDatabase7;
                myDatabase7 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor7 = myDatabase7.rawQuery("select id_procesora from lista_procesorow where nazwa_procesora like '%"+ text +"%'",null);
                cursor7.moveToFirst();
                int lengthCursor7 = cursor7.getCount();

                for(int i=0;i<lengthCursor7;i++)
                {
                    wybranyProc1 = cursor7.getString(0);
                    cursor7.moveToNext();
                }
                myDatabase7.close();

                String text2 = spinner4.getSelectedItem().toString();

                String wybranyProc = new String();

                SQLiteDatabase myDatabase6;
                myDatabase6 = SQLiteDatabase.openDatabase(DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor6 = myDatabase6.rawQuery("select id_procesora from lista_procesorow where nazwa_procesora like '%"+ text2 +"%'",null);
                cursor6.moveToFirst();
                int lengthCursor6 = cursor6.getCount();

                for(int i=0;i<lengthCursor6;i++)
                {
                    wybranyProc = cursor6.getString(0);
                    cursor6.moveToNext();
                }
                myDatabase6.close();

                TextView textView = (TextView) findViewById(R.id.textView);

                if(wybranyProc.equals(wybranyProc1)){
                    textView.setText("!!!To ten sam procesor!!!");
                    textView.setTextColor(Color.RED);
                    textView.setTextSize(20);
                }
                else if(Integer.parseInt(wybranyProc) > Integer.parseInt(wybranyProc1)){
                    textView.setText("Wybrałbym " + text);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(20);
                }
                else if(Integer.parseInt(wybranyProc) < Integer.parseInt(wybranyProc1)){
                    textView.setText("Wybrałbym " + text2);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(20);
                }

            }
        });
        //String text = spinner.getSelectedItem().toString();
        //TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setText(text);
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
