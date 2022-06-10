package com.example.myapplication11;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtName;
    private EditText mEtPhone;
    private TextView mTvShow;
    private Button add;
    private Button query;
    private Button update;
    private Button delete;
    MyHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        myHelper = new MyHelper(this);
        init();
    }
    private void init(){
        mEtName=findViewById(R.id.editTextTextPersonName);
        mEtPhone=findViewById(R.id.phone);
        mTvShow=findViewById(R.id.textView2);
        add=findViewById(R.id.button);
        query=findViewById(R.id.button2);
        update=findViewById(R.id.button3);
        delete=findViewById(R.id.button4);

        add.setOnClickListener(this);
        query.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String name,phone;
        SQLiteDatabase db;
        ContentValues values;
        switch (view.getId()){
            case R.id.button:
                //添加
                name=mEtName.getText().toString();
                phone=mEtPhone.getText().toString();
                db=myHelper.getWritableDatabase();
                values=new ContentValues();
                values.put("name",name);
                values.put("phone",phone);
                //第二种执行SQL语法的写法
                db.insert("information",null,values);
                Toast.makeText(this, "信息已添加", Toast.LENGTH_SHORT).show();
                db.close();
                break;
            case R.id.button2:
                //查询
                db=myHelper.getReadableDatabase();
                Cursor cursor=db.query("information",null,null,
                        null,null,null,null);
                if(cursor.getCount()==0){
                    mTvShow.setText("");
                    Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                }else{
                    cursor.moveToFirst();
                    mTvShow.setText("Name:"+cursor.getString(1)+"Tel:"+cursor.getString(2));
                }
                while (cursor.moveToNext()){
                    mTvShow.append("\n"+"Name:"+cursor.getString(1)+"Tel:"+cursor.getString(2));
                }
                cursor.close();
                db.close();
                break;
            case R.id.button3:
                //修改
                db=myHelper.getWritableDatabase();
                values=new ContentValues();
                values.put("phone",phone=mEtPhone.getText().toString());
                db.update("information",values,"name=?",new String[]{mEtName.getText().toString()});
                Toast.makeText(this, "信息已修改", Toast.LENGTH_SHORT).show();
                db.close();
                break;
            case R.id.button4:
                //删除
                db=myHelper.getWritableDatabase();
                db.delete("information",null,null);
                Toast.makeText(this, "信息已删除", Toast.LENGTH_SHORT).show();
                mTvShow.setText("");
                db.close();
                break;
            default:
                break;
        }

    }
    class MyHelper extends SQLiteOpenHelper{

        public MyHelper(@Nullable Context context) {
            super(context, "itcast.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY " +
                  "AUTOINCREMENT,name VARCHAR(20),phone VARCHAR(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }
    }
}