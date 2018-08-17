package com.rhino.fillblanksedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rhino.fillblanksedittext.view.FillBlanksEditText;

public class MainActivity extends AppCompatActivity {


    private String mFillText = "我是个#_blanks:14_#学生,我有一个梦想，我要成为像#_blanks:-1_#，#_blanks:8_#一样的人.";
    private FillBlanksEditText mFillBlanksEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFillBlanksEditText = findViewById(R.id.FillBlanksEditText);
        mFillBlanksEditText.initFillBlanksText(mFillText);
    }

}
