package com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.app.core.ApiCallBack;
import com.app.core.QString;
import com.app.core.WebMan;
import com.app.rum.R;
import com.app.sugarorm.Book;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_testing);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btTest1)
    public void onClick_btTest1() {
        Intent a = new Intent(TestActivity.this, MainActivity.class);
        startActivity(a);
    }

    @OnClick(R.id.btTest2)
    public void onClick_btTest2() {

        // List all books
        List<Book> books = Book.listAll(Book.class);
        for (int i = 0; i < books.size(); i++) {
            log("" + books.get(i).getId(), books.get(i).getTitle(), books.get(i).getEdition());
        }

        // Add a book
        Book book = new Book("Dummy", "Test Record : " + books.size());
        book.save();
    }

    @OnClick(R.id.btTest3)
    public void onClick_btTest3() {

        QString q = new QString();
        // build Q

        WebMan.getJsonFromApi(this, q, new ApiCallBack() {
            @Override
            public void onReceiveJSON(JSONObject json) {
                // POJO pojo = new Gson().fromJson(json.toString(), POJO.class);
            }

            @Override
            public void onResponse(int type, String resp) {
                log("onResponse TYPE = " + type);
            }
        });

    }

    @OnClick(R.id.btTest4)
    public void onClick_btTest4() {
    }

    @OnClick(R.id.btTest5)
    public void onClick_btTest5() {
    }

    @OnClick(R.id.btTest6)
    public void onClick_btTest6() {
    }

    @BindView(R.id.btTest1)
    Button btTest1;
    @BindView(R.id.btTest2)
    Button btTest2;
    @BindView(R.id.btTest3)
    Button btTest3;
    @BindView(R.id.btTest4)
    Button btTest4;
    @BindView(R.id.btTest5)
    Button btTest5;
    @BindView(R.id.btTest6)
    Button btTest6;
}
