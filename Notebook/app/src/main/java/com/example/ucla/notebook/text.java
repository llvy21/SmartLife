package com.example.ucla.notebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ucla on 2017/11/7.
 */

public class text extends Activity {

    private EditText title,content;
    private Button btn;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text1);
        title = (EditText) findViewById(R.id.title);
        btn = (Button) findViewById(R.id.save);
        content = (EditText) findViewById(R.id.content);
        final Intent intent = getIntent();

        Intent intent1 = getIntent();
        String titl = intent1.getStringExtra("title");
        String con = intent1.getStringExtra("content");
        Log.d("test",titl+con);

        if(titl!=null){
            title.setText(titl);
            content.setText(con);
        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putString("title", title.getText().toString());
                bundle.putString("content", content.getText().toString());
                bundle.putInt("position", intent.getIntExtra("position", 0));
                Intent intent = new Intent();
                intent.putExtra("userInfo", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
