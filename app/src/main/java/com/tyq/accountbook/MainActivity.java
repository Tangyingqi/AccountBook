package com.tyq.accountbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tyq.greendao.DaoMaster;
import com.tyq.greendao.DaoSession;
import com.tyq.greendao.Person;
import com.tyq.greendao.PersonDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ViewPagerAdapter vpAdapter;
    private ImageButton people_img_btn, setting_img_btn, image_find;
    private List<View> myViews;
    private Button btn_add;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Cursor cursor;
    private ListView listView;
    private EditText et_name, et_money;
    private SimpleCursorAdapter cursorAdapter;
    private ScaleAnimation sa;
    private TextView tv_search,tv_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setUpDatabase();
        getPersonDao();
        addData();
    }

    private void addData() {
        String name = PersonDao.Properties.Name.columnName;
        String money = PersonDao.Properties.Money.columnName;
        cursor = db.query(getPersonDao().getTablename(), getPersonDao().getAllColumns(), null, null, null, null, null);
        String[] from = {name, money};
        int[] to = {R.id.tvName, R.id.tvMoney};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_cell, cursor, from, to);
        listView.setAdapter(cursorAdapter);

    }

    private void setUpDatabase() {
        //通过DaoMaster的内部类DevOpenHelper，可以获得一个SqliteOpenHelper对象
        //注意：默认的DaoMaster.DevOpenHelper会在数据库升级时，删除所有表
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "person-db", null);

        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    private PersonDao getPersonDao() {
        return daoSession.getPersonDao();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        people_img_btn = (ImageButton) findViewById(R.id.people_image_btn);
        setting_img_btn = (ImageButton) findViewById(R.id.setting_image_btn);
        image_find = (ImageButton) findViewById(R.id.imgFind);
        myViews = new ArrayList<>();

        tv_search = (TextView) findViewById(R.id.tvSearch);
        tv_setting = (TextView) findViewById(R.id.tvShezhi);

        sa = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(800);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab1 = mInflater.inflate(R.layout.tab1, null);
        View tab2 = mInflater.inflate(R.layout.tab2, null);

        btn_add = (Button) tab2.findViewById(R.id.btnAdd);
        btn_add.setOnClickListener(this);

        image_find = (ImageButton) tab1.findViewById(R.id.imgFind);
        image_find.setOnClickListener(this);

        listView = (ListView) tab2.findViewById(R.id.listView);

        et_name = (EditText) tab2.findViewById(R.id.edName);
        et_money = (EditText) tab2.findViewById(R.id.edMoney);

        myViews.add(tab1);
        myViews.add(tab2);

        people_img_btn.setOnClickListener(this);
        setting_img_btn.setOnClickListener(this);

        vpAdapter = new ViewPagerAdapter(myViews, this);
        viewPager.setAdapter(vpAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.notice).setMessage(R.string.confirm).setNegativeButton(R.string.quit, null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPersonDao().deleteByKey(id);
                        cursor.requery();
                    }
                }).show();
                return true;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tv_search.setTextColor(Color.BLACK);
                        tv_setting.setTextColor(Color.GRAY);
                        break;
                    case 1:
                        tv_search.setTextColor(Color.GRAY);
                        tv_setting.setTextColor(Color.BLACK);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击下面的ImageButton切换相应的界面
            case R.id.people_image_btn:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.setting_image_btn:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.imgFind:
                getFragmentManager().beginTransaction().replace(R.id.tab1, new FindFragment()).commit();
                v.startAnimation(sa);
                image_find.setVisibility(View.GONE);
                break;
            case R.id.btnAdd:
                if (et_name.getText().toString().equals("") || et_money.getText().toString().equals("")) {
                    return;
                }
                insertData();
                break;
        }
    }

    private void insertData() {
        String name = et_name.getText().toString();
        String money = et_money.getText().toString();

        Person person = new Person(null, name, money);
        getPersonDao().insert(person);
        Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
        et_name.setText("");
        et_money.setText("");
        cursor.requery();
    }
}
