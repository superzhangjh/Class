package com.example.a731.aclass.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.MyGridAdapter;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView gridView;
    private MyGridAdapter adapter;
    private SharedPreferences coursePreferences;
    private SharedPreferences classPreferences;
    private SharedPreferences spSaveClassPreferences;
    private SharedPreferences teacherPreferences;
    private SharedPreferences spSaveTeacherPreferences;
    private SharedPreferences schoolPreferences;
    private SharedPreferences spSaveCoursePreferences;
    private SharedPreferences newSchoolPreferences,newClassTextPreferences;
    SharedPreferences.Editor courseEditor,classEditor,teacherEditor;
    private TextView schoolText,classText;
    private TextView changeCourseName;
    private LayoutInflater inflater;
    private EditText editTextCourseName,editTextClassName,editTextTeacherName;
    private EditText edit,editClass;
    private int itemId;


//    显示
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_menu,menu);
        return true;
    }
//    事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ScheduleActivity.this);
                dialog.setTitle("帮助说明");
                dialog.setMessage("(1)、长按未有课程处，可添加课程；\n" +
                        "(2)、长按已有课程处，可修改课程；\n" +
                        "(3)、可添加您的学校和班级；\n" +
                        "(4)、使用愉快");
//                dialog.setCancelable(false);
                dialog.setPositiveButton("懂了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.create().show();
                break;
            case R.id.finish:
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);




        gridView = (GridView) findViewById(R.id.grid_view);
        spSaveCoursePreferences = getSharedPreferences("showCourseName", getApplicationContext().MODE_PRIVATE);
        spSaveClassPreferences = getSharedPreferences("showClassName",getApplicationContext().MODE_PRIVATE);
        spSaveTeacherPreferences = getSharedPreferences("showTeacherName",getApplicationContext().MODE_PRIVATE);
        adapter = new MyGridAdapter(this);
        gridView.setAdapter(adapter);

        schoolText = (TextView) findViewById(R.id.textXuexiao);
        newSchoolPreferences = getSharedPreferences("newSName", getApplicationContext().MODE_PRIVATE);
        final String newSchoolName = newSchoolPreferences.getString("newSName", "您的学校");
        schoolText.setText(newSchoolName);

        classText = (TextView) findViewById(R.id.textClassName);
        newClassTextPreferences = getSharedPreferences("newYCName",getApplicationContext().MODE_PRIVATE);
        final String newClassTextName = newClassTextPreferences.getString("newYCName","您的班级");
        classText.setText(newClassTextName);


//        表格中的子项的长按事件，以修改课程
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                            View view, final int position, long id) {
                final View myDialogView = (LinearLayout)getLayoutInflater().inflate(
                        R.layout.edit_text_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
//                inflater = LayoutInflater.from(getApplicationContext().this);
//                //final EditText sit = new EditText(getApplicationContext().this);
//                view.clearAnimation();
//                view = inflater.inflate(R.layout.edit_text_dialog,null);
                builder.setTitle("课程信息修改");
                builder.setMessage("编辑和添加课程信息");
                builder.setView(myDialogView);

//                添加Textview中已有的数据到弹窗
                EditText getEditTextCourse = (EditText) myDialogView.findViewById(R.id.course_edit);
                EditText getEditTextClass = (EditText) myDialogView.findViewById(R.id.class_edit);
                EditText getEditTextTeacher = (EditText) myDialogView.findViewById(R.id.teacher_edit);
                String cName = getSharedPreferences("showCourseName",MODE_PRIVATE).
                        getString("keyCourseName"+position,"");
                    if(!cName.equals("")) {
                        getEditTextCourse.setText(cName);
                        getEditTextCourse.setTextColor(Color.RED);
                    }
                String cClass = getSharedPreferences("showClassName",MODE_PRIVATE).getString("keyClassName"+position,"");
                    if(!cClass.equals("")) {
                        getEditTextClass.setText(cClass);
                        getEditTextClass.setTextColor(Color.RED);
                    }
                String cTeacher = getSharedPreferences("showTeacherName",MODE_PRIVATE).getString("keyTeacherName"+position,"");
                    if(!cTeacher.equals("")) {
                        getEditTextTeacher.setText(cTeacher);
                        getEditTextTeacher.setTextColor(Color.RED);
                    }

//                EditText editTextCourse = (EditText) findViewById(R.id.course_edit);
//                EditText editTextClass = (EditText) findViewById(R.id.class_edit);
//                EditText editTextTeacher = (EditText) findViewById(R.id.teacher_edit);
//
//                editTextCourse.setHint("请输入课程");
//               editTextClass.setHint("请输入上课课室");
//               editTextTeacher.setHint("请输入任课老师");
//                builder.setView(R.layout.edit_text_dialog);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                       clickItemPosition = position;
                        EditText editTextCourse = (EditText) myDialogView.findViewById(R.id.course_edit);
                        EditText editTextClass = (EditText) myDialogView.findViewById(R.id.class_edit);
                        EditText editTextTeacher = (EditText) myDialogView.findViewById(R.id.teacher_edit);

                        String newCourseNames = editTextCourse.getText().toString().trim();
                        String newClassName = editTextClass.getText().toString().trim();
                        String newTeacherName = editTextTeacher.getText().toString().trim();

//                        添加进程报告
                        if(newCourseNames.equals("") && newClassName.equals("") && newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课程，课室,老师均未添加",
                                    Toast.LENGTH_LONG).show();
                        }
                        else if(!newCourseNames.equals("") && newClassName.equals("") && newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课程添加成功；\n 课室,老师未添加",
                                    Toast.LENGTH_LONG).show();
                        }else if(newCourseNames.equals("") && !newClassName.equals("") && newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课室添加成功； \n 课程,老师未添加",
                                    Toast.LENGTH_LONG).show();
                        }else if(newCourseNames.equals("") && newClassName.equals("") && !newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"老师添加成功； \n 课程,课室未添加",
                                    Toast.LENGTH_LONG).show();
                        }else if(!newCourseNames.equals("") && !newClassName.equals("") && newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课程,课室添加成功； \n 老师未添加",
                                    Toast.LENGTH_LONG).show();
                        }else if(!newCourseNames.equals("") && newClassName.equals("") && !newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课程,老师添加成功； \n 课室未添加",
                                    Toast.LENGTH_LONG).show();
                        }else if(newCourseNames.equals("") && !newClassName.equals("") && !newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课室,老师添加成功； \n 课程未添加",
                                    Toast.LENGTH_LONG).show();
                        }else if(!newCourseNames.equals("") && !newClassName.equals("") && !newTeacherName.equals("")){
                            Toast.makeText(getApplicationContext(),"课程，课室，老师均已添加成功",
                                    Toast.LENGTH_LONG).show();
                        }

                            //                            新建文件夹储存输入的数据
                            //                            存到saveCourseName.xml
                        itemId = position;
                        coursePreferences = getSharedPreferences("showCourseName", getApplicationContext().MODE_PRIVATE);
                        courseEditor = coursePreferences.edit();
                        courseEditor.putString("keyCourseName"+position, newCourseNames);
                        courseEditor.commit();

                        classPreferences = getSharedPreferences("showClassName", getApplicationContext().MODE_PRIVATE);
                        classEditor = classPreferences.edit();
                        classEditor.putString("keyClassName" + position, newClassName);
                        classEditor.commit();

                        teacherPreferences = getSharedPreferences("showTeacherName", getApplicationContext().MODE_PRIVATE);
                        teacherEditor = teacherPreferences.edit();
                        teacherEditor.putString("keyTeacherName"+position, newTeacherName);
                        teacherEditor.commit();
//                            TextView newCoursename = (TextView) findViewById(R.id.course_name);
//                            newCoursename.setText(newCourseNames);
                            //view一定不能少，不然不能进行表格中准确位置数据的修改
                        adapter.notifyDataSetChanged();


                    }
                });
                    builder.setNegativeButton("清空本节课信息", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {//                创建
                            SharedPreferences clearCoursePreferences = getSharedPreferences("showCourseName",getApplicationContext().MODE_PRIVATE);
                            SharedPreferences clearClassPreferences = getSharedPreferences("showClassName",getApplicationContext().MODE_PRIVATE);
                            SharedPreferences clearTeacherPreferences = getSharedPreferences("showTeacherName",getApplicationContext().MODE_PRIVATE);
//                编辑状态
                            SharedPreferences.Editor clearCourseEditor = clearCoursePreferences.edit();
                            SharedPreferences.Editor clearClassEditor = clearClassPreferences.edit();
                            SharedPreferences.Editor clearTeacherEditor = clearTeacherPreferences.edit();
//                存
                            clearCourseEditor.putString("keyCourseName"+position,"");
                            clearClassEditor.putString("keyClassName"+position,"");
                            clearTeacherEditor.putString("keyTeacherName"+position,"");
//                交
                            clearCourseEditor.commit();
                            clearClassEditor.commit();
                            clearTeacherEditor.commit();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                return false;
            }
        });



//    an钮点击事件

        schoolText = (TextView) findViewById(R.id.textXuexiao);
        classText = (TextView) findViewById(R.id.textClassName);
        schoolText.setOnClickListener(this);
        classText.setOnClickListener(this);


    }


    @Override
    public void onClick(View view){
            switch (view.getId()) {
                case R.id.textXuexiao:
                    schoolText.setClickable(true);
                    final View viewSchool = (LinearLayout)getLayoutInflater().
                            inflate(R.layout.edit_school,null);

                    AlertDialog.Builder dialogSchool = new AlertDialog.Builder(ScheduleActivity.this);
                    dialogSchool.setTitle("学校");
                    dialogSchool.setMessage("您就读学校的名字");
                    dialogSchool.setView(viewSchool);
                    String schoolNameStar = getSharedPreferences(
                            "newSName",getApplicationContext().MODE_PRIVATE).getString("newSName","");

                    if(!schoolNameStar.equals("")){
                        EditText slName = (EditText) viewSchool.findViewById(R.id.edit_school);
                        slName.setText(schoolNameStar);
                        slName.setTextColor(Color.RED);
                    }
                    dialogSchool.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                        收到的数据储存
                            EditText edit = (EditText) viewSchool.findViewById(R.id.edit_school);
                            String school = edit.getText().toString().trim();
                            schoolPreferences = getSharedPreferences("newSName", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = schoolPreferences.edit();
                            editor.putString("newSName", school);
                            editor.commit();
                            TextView schoolText1 = (TextView)findViewById(R.id.textXuexiao);
                            schoolText1.setText(school);
                        }
                    });
                    dialogSchool.setNegativeButton("清空", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences clearSchoolPreferences = getSharedPreferences(
                                    "newSName",getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor clearSchoolEditor = clearSchoolPreferences.edit();
                            clearSchoolEditor.putString("newSName","").commit();
                        }
                    });
                    dialogSchool.create().show();
                    break;

                case R.id.textClassName:
                    classText.setClickable(true);
                    final View viewClass = (LinearLayout)getLayoutInflater().inflate(R.layout.edit_class,null);

                    AlertDialog.Builder dialogClass = new AlertDialog.Builder(ScheduleActivity.this);
                    dialogClass.setTitle("班级");
                    dialogClass.setMessage("您所在的班级");
                    dialogClass.setView(viewClass);
                    String classNameStar = getSharedPreferences(
                            "newYCName",getApplicationContext().MODE_PRIVATE).getString("newYCName","");
                    if(!classNameStar.equals("")){
                        EditText csName = (EditText) viewClass.findViewById(R.id.edit_class);
                        csName.setText(classNameStar);
                        csName.setTextColor(Color.RED);
                    }
                    dialogClass.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                        收到的数据储存
                            EditText editClass = (EditText) viewClass.findViewById(R.id.edit_class);
                            String yourClass = editClass.getText().toString().trim();
                            classPreferences = getSharedPreferences("newYCName", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor classTextEditor = classPreferences.edit();
                            classTextEditor.putString("newYCName", yourClass);
                            classTextEditor.commit();
                            TextView classText1 = (TextView)findViewById(R.id.textClassName);
                            classText1.setText(yourClass);
                        }
                    });
                    dialogClass.setNegativeButton("清空", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences clearClassPreferences = getSharedPreferences(
                                    "newYCName",getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor clearClassEditor = clearClassPreferences.edit();
                            clearClassEditor.putString("newYCName","").commit();
                        }
                    });
                    dialogClass.create().show();
                    break;


                default:
                    break;
            }
        }

//        学校编辑
//        设置textview的clickable属性

}
