package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;

/**
 * Created by Administrator on 2017/10/21.
 */

public class MyGridAdapter extends BaseAdapter {
    private SharedPreferences spCourseName;
    private SharedPreferences spClassName;
    private SharedPreferences spTeacherName;
    private LayoutInflater inflater;
    private Context context;
    private int newPosition;
    private TextView teacherName,className,courseName;
    private EditText editTextCourseName,editTextClassName,editTextTeacherName;
    public MyGridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        spCourseName = context.getSharedPreferences("showCourseName",context.MODE_PRIVATE);
        spClassName = context.getSharedPreferences("showClassName",context.MODE_PRIVATE);
        spTeacherName = context.getSharedPreferences("showTeacherName",context.MODE_PRIVATE);
    }


    @Override
    public int getCount() {
        return 84;
    }

      @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View contentview, ViewGroup parent) {
        View view = inflater.inflate(R.layout.grid_view_item, null);
        courseName = (TextView) view.findViewById(R.id.course_name);
        className = (TextView) view.findViewById(R.id.class_name);
        teacherName = (TextView) view.findViewById(R.id.teacher_name);
        LinearLayout gridViewItem = (LinearLayout) view.findViewById(R.id.grid_view_item);

        /*courseName.setText(strings[position]);*/

//                接受数据
//            clickItemPosition = new MainActivity();
//            int newPosition = clickItemPosition.getClickItemPosition();
        String changeCourseNames = spCourseName.getString("keyCourseName"+position, "");
        String changeClassNames = spClassName.getString("keyClassName"+position,"");
        String changeTeacherNames = spTeacherName.getString("keyTeacherName"+position,"");
        courseName.setText(changeCourseNames);
        courseName.setTextColor(Color.BLACK);
        className.setText(changeClassNames);
        className.setTextColor(Color.BLACK);
        teacherName.setText(changeTeacherNames);
        teacherName.setTextColor(Color.BLACK);


        int residue = position % 7;
        switch (residue) {
            case 0:
                gridViewItem.setBackgroundColor(Color.MAGENTA);
                courseName.setTextColor(Color.WHITE);
                className.setTextColor(Color.WHITE);
                teacherName.setTextColor(Color.WHITE);
                break;
            case 1:
                gridViewItem.setBackgroundColor(Color.WHITE);
                break;
            case 2:
                gridViewItem.setBackgroundColor(Color.MAGENTA);
                courseName.setTextColor(Color.WHITE);
                className.setTextColor(Color.WHITE);
                teacherName.setTextColor(Color.WHITE);
                break;
            case 3:
                gridViewItem.setBackgroundColor(Color.WHITE);
                break;
            case 4:
                gridViewItem.setBackgroundColor(Color.MAGENTA);
                courseName.setTextColor(Color.WHITE);
                className.setTextColor(Color.WHITE);
                teacherName.setTextColor(Color.WHITE);
                break;
            case 5:
                gridViewItem.setBackgroundColor(Color.WHITE);
                break;
            case 6:
                gridViewItem.setBackgroundColor(Color.MAGENTA);
                courseName.setTextColor(Color.WHITE);
                className.setTextColor(Color.WHITE);
                teacherName.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
        return view;
    }
}
