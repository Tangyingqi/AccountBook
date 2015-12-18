package com.tyq.accountbook;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.tyq.greendao.DaoMaster;
import com.tyq.greendao.DaoSession;
import com.tyq.greendao.Person;
import com.tyq.greendao.PersonDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyq on 2015/12/17.
 */
public class FindFragment extends Fragment {
    private Button btn_search;
    private EditText et_search_name;
    private ListView listView;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private MyAdapter adpter;

    private List<Person> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.find_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpDatabase();
        getPersonDao();
        btn_search = (Button) getActivity().findViewById(R.id.btnSearch);
        et_search_name = (EditText) getActivity().findViewById(R.id.edFindName);
        listView = (ListView) getActivity().findViewById(R.id.listView);
        list = new ArrayList<Person>();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(et_search_name.getText().toString().equals(""))) {
                    refreshView();
                    et_search_name.setText("");
                }
            }
        });
    }

    private void refreshView() {

        list = getPersonDao().queryBuilder().where(PersonDao.Properties.Name.like("%" + et_search_name.getText().toString() + "%")).list();
        if (list.size() == 0) {
            Toast.makeText(getActivity(), "没有查询到结果", Toast.LENGTH_SHORT).show();
        }
        adpter = new MyAdapter(list, getActivity());
        listView.setAdapter(adpter);
    }

    private void setUpDatabase() {
        //通过DaoMaster的内部类DevOpenHelper，可以获得一个SqliteOpenHelper对象
        //注意：默认的DaoMaster.DevOpenHelper会在数据库升级时，删除所有表
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "person-db", null);

        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }
    private PersonDao getPersonDao() {
        return daoSession.getPersonDao();
    }
}
