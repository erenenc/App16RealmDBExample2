package com.example.app16realmdbexample2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app16realmdbexample2.model.Student;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private EditText name, mark;
    private TextView display;
    private Button saveButton;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        mark = findViewById(R.id.mark);
        display = findViewById(R.id.display);
        saveButton = findViewById(R.id.saveButton);


        realm = Realm.getDefaultInstance();

        // asagidaki 3 satir her uygulama başlangıcında datayı sıfırlar
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
                readData();

            }
        });

    }

    private void saveData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {
                                              Student student = realm.createObject(Student.class);
                                              student.setName(name.getText().toString().trim());
                                              student.setMark(Integer.parseInt(mark.getText().toString().trim()));
                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              Log.e("onSuccess", "Data Written Successfully");
                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              Log.e("onError", "Error Occured When Data Written");
                                          }
                                      }

        );

    }

    private void readData(){

        RealmResults<Student> students = realm.where(Student.class).findAll();
        display.setText("");
        String data = "";

        for (Student student:students){
            try {

                data = data + "\n" + student.toString();

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        display.setText(data);

    }
}