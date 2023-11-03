package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class Bai1 extends AppCompatActivity {
    // Bài1
    public static final int REQUEST_READ_CONTACTS = 79;
    FloatingActionButton fab1;
    ListView lstViewDanhBa;
    ArrayList mobileArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1);
        addControls();
        addEvents();
    }

    public void addControls(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllContacts();
        }
        else {
            requestPermission();
        }
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        lstViewDanhBa = findViewById(R.id.lstViewDanhBa);
    }

    public void addEvents(){
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();}
        });
        // Lấy danh sách liên lạc
        ArrayList<HashMap<String, String>> contacts = getAllContacts();

        // Tạo adapter từ custom layout
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                contacts,
                R.layout.custom_contact_item,
                new String[]{"Name", "Phone", "Email"},
                new int[]{R.id.textName, R.id.textPhone, R.id.textEmail}
        );

        // Đặt Adapter cho listView
        lstViewDanhBa.setAdapter(adapter);
    }

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            // Show UI
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_READ_CONTACTS:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllContacts();
                } else{
                    // permission denied, Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @SuppressLint("Range")
    private ArrayList<HashMap<String, String>> getAllContacts() {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                HashMap<String, String> contact = new HashMap<>();
                 String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.put("Name", name);

                // Lấy danh sách số điện thoại
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null
                    );

                    StringBuilder phoneNumbers = new StringBuilder();
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNumbers.append(phoneNo).append(", ");
                    }
                    pCur.close();

                    // Xóa phẩy và cách
                    if (phoneNumbers.length() > 0) {
                        phoneNumbers.setLength(phoneNumbers.length() - 2);
                    }

                    contact.put("Phone", phoneNumbers.toString());
                }

                // Lấy email
                Cursor emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );

                StringBuilder emailAddresses = new StringBuilder();
                while (emailCur.moveToNext()) {
                    String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    emailAddresses.append(email).append(", ");
                }
                emailCur.close();

                // Remove trailing comma and space
                if (emailAddresses.length() > 0) {
                    emailAddresses.setLength(emailAddresses.length() - 2);
                }

                contact.put("Email", emailAddresses.toString());

                // Thêm contact vào danh sách
                contactList.add(contact);
            }
            cur.close();
        }

        return contactList;
    }

}
