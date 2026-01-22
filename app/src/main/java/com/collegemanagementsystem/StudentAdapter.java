package com.collegemanagementsystem;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {

    Context c;
    ArrayList<Student> list;

    public StudentAdapter(Context c, ArrayList<Student> list) {
        this.c = c;
        this.list = list;
    }

    @Override public int getCount() { return list.size(); }
    @Override public Object getItem(int i) { return list.get(i); }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View v, ViewGroup p) {
        if (v == null)
            v = LayoutInflater.from(c).inflate(R.layout.row_student, p, false);

        ((TextView)v.findViewById(R.id.txtName)).setText(list.get(i).name);
        ((TextView)v.findViewById(R.id.txtCourse)).setText(list.get(i).course);
        return v;
    }
}
