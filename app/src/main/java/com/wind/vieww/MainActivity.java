package com.wind.vieww;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private HorizontalScrollView mListContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListContainer=findViewById(R.id.hsv_view);
        for (int i=0;i<3;i++){
            ViewGroup layout= (ViewGroup) getLayoutInflater().inflate(R.layout.viewpager,mListContainer,false);
            TextView textView=layout.findViewById(R.id.title);
            textView.setText("page"+(i+1));
            layout.setBackgroundColor(Color.parseColor("#11cd6e"));
            creatList(layout);
            mListContainer.addView(layout);
        }
    }

    private void creatList(ViewGroup layout) {
        ListView listView=layout.findViewById(R.id.list);
        ArrayList<String> datas=new ArrayList<>();
        for (int i=0;i<50;i++){
            datas.add("name"+i);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.item_content,R.id.name,datas);
        listView.setAdapter(adapter);
    }
}
