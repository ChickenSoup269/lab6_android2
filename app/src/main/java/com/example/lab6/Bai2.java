package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Bai2 extends AppCompatActivity {
    // Bài2
    FloatingActionButton fab2;
    TextInputLayout txtInputName;
    TextView txtRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);
        addControls();
        addEvents();
    }

    public void addControls(){
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        txtInputName = (TextInputLayout) findViewById(R.id.txtInputName);
        txtRes = (TextView) findViewById(R.id.txtRes);
    }

    public void addEvents(){
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();}
        });
    }
    private boolean validateName(){
        String name = txtInputName.getEditText().getText().toString().trim();

        if(name.isEmpty()){
            txtInputName.setError("Name không được để trống!");
            return  false;
        }
        else if (name.length() > 15) {
            txtInputName.setError("Tên quá dài!");
            return  false;
        }
        else {
            txtInputName.setError(null);
            return true;
        }
    }

   @Override
    public boolean onTouchEvent(MotionEvent event){
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        return true;
    }

    public void onClickAddDetails(View view){
        if(!validateName()){
            return;
        }
        ContentValues values = new ContentValues();
        values.put(UserProvider.name, txtInputName.getEditText().getText().toString());
        getContentResolver().insert(UserProvider.CONTENT_URI, values);
        Toast.makeText(getBaseContext(), "New Record Inserted", Toast.LENGTH_SHORT).show();

    }
    @SuppressLint("Range")
    public void onClickShowDetails(View view) {
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.tutlane.contentprovider.UserProvider/users"), null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                StringBuilder strBuild = new StringBuilder();
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));

                    // In đậm ID
                    String formattedText = "<b>" + id + "</b> - " + name + "<br>";
                    strBuild.append("\n" + formattedText );

                    cursor.moveToNext();
                }
                txtRes.setText(Html.fromHtml(strBuild.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtRes.setText("No Records Found");
            }
            cursor.close();
        } else {
            txtRes.setText("Error querying data");
        }
    }


}