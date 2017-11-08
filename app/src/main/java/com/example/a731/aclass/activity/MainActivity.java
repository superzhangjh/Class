package com.example.a731.aclass.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.EMContactListenerAdapter;
import com.example.a731.aclass.adapter.EMGroupChangeListenerAdapter;
import com.example.a731.aclass.adapter.FriendAdapter;
import com.example.a731.aclass.adapter.GroupAdapter;
import com.example.a731.aclass.data.BasicMessage;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.fragment.CircleFragment;
import com.example.a731.aclass.fragment.MessFragment;
import com.example.a731.aclass.presenter.MainPresenter;
import com.example.a731.aclass.presenter.impl.MainPresenterImpl;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.util.customView.NoScrollViewPager;
import com.example.a731.aclass.view.MainView;
import com.example.a731.aclass.zxing.activity.CaptureActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.a731.aclass.R.id.main_nav_view;
import static com.example.a731.aclass.util.EaseMobUtil.MODIFIED_RESULT;

public class MainActivity extends BaseActivity implements MainView{

    private static String TITLE_GROUP_NAME = "";//班圈名称
    private static String TITLE_USER_NAME = " ";//用户名称

    //动态权限申请列表
    private static final String[] PERMISSION = new String[]{
            Manifest.permission.CAMERA,//相机
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
    };

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private NavigationView navRight;
    private ActionBarDrawerToggle mDrawerToggle;
    private NoScrollViewPager mViewPager;
    private Toolbar mToolbar;
    private RadioGroup mRadioGroup;
    private ImageView imgSearchFriend;
    private MenuItem itemFriend;
    private MenuItem itemScan;
    private MenuItem itemCreateGroup;
    private MenuItem itemJoinIn;
    private TextView tvTitle;
    private TextView tvCheckPhoneNumber;
    private LinearLayout tvUserinfo;
    private LinearLayout tvGroupinfo;

    private EMContactListenerAdapter contactListener;
    private EMGroupChangeListenerAdapter groupChangeListener;

    private RadioButton rbGroup;
    private RadioButton rbMess;

    private MainPresenter mainPresenter;

    private ListView friend_list_view,group_list_view;
    private List<Users> friendList;
    private TextView tvFriendCount;
    private CircleImageView imgToolbarClassHead;
    private CircleImageView imgToolbalUserHead;
    private View navHeaderView;
    private ImageView headImg;
    private TextView name,phoneNum;
    private Users mUser;

    private EMConnectionListener connectionListener;
    private List<Group> groupList;

    private String presentGroupId;

    private FriendAdapter friendAdapter;
    private GroupAdapter groupAdapter;

    @Override
    protected int getLayoutRes() {

        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setPermissions();
        tvTitle = (TextView) findViewById(R.id.main_tv_title);
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        mainPresenter = new MainPresenterImpl(this);
        imgToolbalUserHead = (CircleImageView) findViewById(R.id.main_userhead);
        initViewPager();
        initNavigationView();
        initToolBar();
        initActionBarDrawerLayout();
        initFriendListView();
        initGroupListView();
    }

    private void initGroupListView() {
        rbGroup = (RadioButton) findViewById(R.id.main_rb_group);
        rbMess = (RadioButton) findViewById(R.id.main_rb_mess);
        group_list_view = (ListView) findViewById(R.id.main_nav_right_list_group);
        groupList = new ArrayList<>();
        mainPresenter.getGroup();
        groupAdapter = new GroupAdapter(this,R.layout.fragment_group_item,groupList);
        group_list_view.setAdapter(groupAdapter);
        group_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = groupList.get(position);
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("chatToId",group.getGroupId());
                intent.putExtra("chatType",EaseMobUtil.CHATTYPE_GROUP);
                startActivity(intent);
            }
        });

    }

    private void initFriendListView() {
        friend_list_view = (ListView) findViewById(R.id.main_nav_right_listview);
        tvFriendCount = (TextView) findViewById(R.id.main_nav_right_count);
        imgSearchFriend = (ImageView) findViewById(R.id.main_nav_right_search);

        imgSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SearchUserActivity.class);
                startActivity(intent);
            }
        });

        friendList = new ArrayList<>();
        mainPresenter.getFriendList();



        friendAdapter = new FriendAdapter(getApplicationContext(),R.layout.fragment_friend_item,friendList);
        friend_list_view.setAdapter(friendAdapter);
        friend_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users user = friendList.get(position);
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("chatToId",user.getUsername());
                intent.putExtra("chatType",EaseMobUtil.CHATTYPE_PERSONAL);
                startActivity(intent);
            }
        });
    }

    private void initViewPager() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setCurrentItem(4);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CircleFragment());
        fragmentList.add(new MessFragment());

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
    }

    //个人资料侧滑栏
    private void initNavigationView() {
        mUser = BmobUser.getCurrentUser(Users.class);
        if (mUser.getName()!=null){
            TITLE_USER_NAME = mUser.getName();
        }else{
            TITLE_USER_NAME = mUser.getUsername();
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navView = (NavigationView) findViewById(main_nav_view);
        navRight = (NavigationView) findViewById(R.id.main_nav_right);

        navHeaderView = navView.getHeaderView(0);
        headImg = (ImageView) navHeaderView.findViewById(R.id.main_nav_head);
        name = (TextView) navHeaderView.findViewById(R.id.main_nav_name);
        phoneNum = (TextView) navHeaderView.findViewById(R.id.main_nav_phoneNum);
        tvCheckPhoneNumber = (TextView) navHeaderView.findViewById(R.id.main_nav_phoneCheck);
        if (mUser.getHeadImg()!=null)
            Glide.with(this).load(mUser.getHeadImg()).crossFade().error(R.drawable.cross86).into(headImg);
        phoneNum.setText(mUser.getMobilePhoneNumber()==null?"":mUser.getMobilePhoneNumber());
        name.setText(mUser.getName()==null?"未设置":mUser.getName());
        if (mUser.getMobilePhoneNumber()!=null){
            tvCheckPhoneNumber.setVisibility(View.GONE);
        }else {
            tvCheckPhoneNumber.setVisibility(View.VISIBLE);
        }

        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });

        navView.setNavigationItemSelectedListener(  new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){ //跳转到个人主页
                    case R.id.main_nav_menu_userinfo:
                        Intent intent = new Intent(MainActivity.this,UserInfoActivity.class);
                        String username = BmobUser.getCurrentUser(Users.class).getUsername();
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
                    case R.id.main_nav_menu_groupinfo://跳转到班圈主页
                        if (presentGroupId==null){
                            showToast("你还未设置班圈");
                            finish();
                        }
                        Intent groupInfoIntent = new Intent(MainActivity.this,GroupInfoActivity.class);
                        groupInfoIntent.putExtra("groupId",presentGroupId);
                        startActivity(groupInfoIntent);
                        break;
                    case R.id.main_nav_menu_QRCode:
                        Intent QRintent = new Intent(MainActivity.this,QRCodeActivity.class);
                        QRintent.putExtra("qrCode",mUser.getQRCode());
                        QRintent.putExtra("headImage",mUser.getHeadImg());
                        if (mUser.getName()!=null){
                            QRintent.putExtra("name",mUser.getName());
                        }else{
                            QRintent.putExtra("name",mUser.getUsername());
                        }
                        startActivity(QRintent);
                        break;
                    case R.id.main_nav_menu_logOut:
                        new AlertDialog.Builder(MainActivity.this).setTitle("你真的要离开了吗/(ㄒoㄒ)/~~")
                                .setPositiveButton("还是算了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                }).setNegativeButton("狠心离开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mainPresenter.logOut();
                            }
                        }).show();
                        break;
                    case R.id.main_nav_menu_setting:
                        //TODO:软件设置
                        break;
                    case R.id.main_nav_menu_modified:
                        Intent modifiedIntent = new Intent(MainActivity.this,ModifiedUserDataActivity.class);
                        startActivityForResult(modifiedIntent,MODIFIED_RESULT);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void showPhotoDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();//创建一个AlertDialog对象
        dialog.show();//一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了\
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_select_photo);
        window.setGravity(Gravity.CENTER);//设置对话框在界面底部显示
        dialog.setCanceledOnTouchOutside(true);
        TextView tvTakePhoto = (TextView) window.findViewById(R.id.dialog_select_photo_take);
        TextView tvSelectPhoto = (TextView) window.findViewById(R.id.dialog_select_photo_album);

        //拍照
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(getImageByCamera, ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE);
                }
                else {
                    showToast("请确认已经插入SD卡");
                }
                dialog.cancel();
            }
        });
        //选择照片
        tvSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                dialog.cancel();
            }
        });
    }

    private void initToolBar() {
        //设置toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.RED);
        setSupportActionBar(mToolbar);

        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置班圈的名称

        // 导航栏图标显示
        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);

        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置班圈头像
        imgToolbarClassHead = (CircleImageView) findViewById(R.id.main_classhead);
        imgToolbarClassHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presentGroupId!=null){
                    Intent intent = new Intent(getApplicationContext(), GroupInfoActivity.class);
                    intent.putExtra("groupId",presentGroupId);
                    startActivity(intent);
                }else{
                    showToast("你还未加入任何班圈");
                }

            }
        });
        //设置个人头像
        Glide.with(this).load(BmobUser.getCurrentUser(Users.class).getHeadImg()).crossFade().error(R.drawable.cross86).into(imgToolbalUserHead);
        imgToolbalUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserInfoActivity.class);
                String username = BmobUser.getCurrentUser(Users.class).getUsername();
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }

    private void initActionBarDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,0,0){};
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public void initListener() {
        //手机号验证
        tvCheckPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:联网设置用户手机号
                showToast("未实现该功能");
            }
        });

        //点击单选时，ViewPager显示对应的子界面
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.main_rb_group://圈按钮
                        rbGroup.setTextColor(Color.BLACK);
                        rbMess.setTextColor(Color.GRAY);
                        mViewPager.setCurrentItem(0);
                        itemFriend.setVisible(false);
                        itemScan.setVisible(true);
                        itemCreateGroup.setVisible(true);
                        itemJoinIn.setVisible(true);
                        tvTitle.setText(TITLE_GROUP_NAME);
                        imgToolbarClassHead.setVisibility(View.VISIBLE);
                        imgToolbalUserHead.setVisibility(View.GONE);
                        break;

                    case R.id.main_rb_mess://消息按钮
                        rbGroup.setTextColor(Color.GRAY);
                        rbMess.setTextColor(Color.BLACK);
                        mViewPager.setCurrentItem(1);
                        itemFriend.setVisible(true);
                        itemScan.setVisible(false);
                        itemCreateGroup.setVisible(false);
                        itemJoinIn.setVisible(false);
                        tvTitle.setText(TITLE_USER_NAME);
                        imgToolbarClassHead.setVisibility(View.GONE);
                        imgToolbalUserHead.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0://选中圈按钮
                        rbGroup.setTextColor(Color.BLACK);
                        rbMess.setTextColor(Color.GRAY);
                        itemFriend.setVisible(false);
                        itemScan.setVisible(true);
                        itemCreateGroup.setVisible(true);
                        itemJoinIn.setVisible(true);
                        tvTitle.setText(TITLE_GROUP_NAME);
                        imgToolbarClassHead.setVisibility(View.VISIBLE);
                        imgToolbalUserHead.setVisibility(View.GONE);
                        break;
                    case 1://选中消息
                        rbGroup.setTextColor(Color.GRAY);
                        rbMess.setTextColor(Color.BLACK);
                        itemFriend.setVisible(true);
                        itemScan.setVisible(false);
                        itemCreateGroup.setVisible(false);
                        itemJoinIn.setVisible(false);
                        tvTitle.setText(TITLE_USER_NAME);
                        imgToolbarClassHead.setVisibility(View.GONE);
                        imgToolbalUserHead.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //监听环信连接状态
        connectionListener = new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int errorCode) {
                if (errorCode == EMError.USER_REMOVED) {
                    showToast("帐号已经被移除");
                    // 显示帐号已经被移除
                } else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    showToast("帐号在其他设备登录");
                    // 显示帐号在其他设备登录
                    mainPresenter.logOut();
                } else {
                    if (NetUtils.hasNetwork(MainActivity.this)) {
                        showToast("连接不到聊天服务器");
                    }
                    //连接不到聊天服务器
                    else {
                        showToast("当前网络不可用，请检查网络设置");
                    }
                    //当前网络不可用，请检查网络设置
                }
            }
        };

        EMClient.getInstance().addConnectionListener(connectionListener);

        contactListener = new EMContactListenerAdapter(){
            @Override
            public void onContactAdded(String username) {
                //好友请求被同意
                mainPresenter.getFriendList();
            }
            @Override
            public void onFriendRequestAccepted(String username) {
                //同意邀请时回调此方法
                mainPresenter.getFriendList();
            }
        };

        EMClient.getInstance().contactManager().setContactListener(contactListener);

        groupChangeListener = new EMGroupChangeListenerAdapter(){
            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
                mainPresenter.getGroup();
            }
        };

        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar,menu);
        itemFriend = menu.findItem(R.id.main_toolbar_friend);
        itemScan = menu.findItem(R.id.main_toolbar_scan);
        itemCreateGroup = menu.findItem(R.id.main_toolbar_create_group);
        itemJoinIn = menu.findItem(R.id.main_toolbar_join_in);
        itemFriend.setVisible(false);

        //扫一扫功能
        itemScan.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(getApplicationContext(), CaptureActivity.class));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.main_toolbar_create_group:
                intent.setClass(MainActivity.this,CreateGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.main_toolbar_join_in:
                intent.setClass(MainActivity.this,JoinGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.main_toolbar_friend:
                mDrawerLayout.openDrawer(navRight);
        }
        return true;
    }


    /*--------------------------------事务处理回调方法-------------------------------------*/
    @Override
    public void onLogoutFail(String error) {
        showToast("退出失败:"+error);
    }

    @Override
    public void onLogoutSuccess() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetFriendsSuccess(List<Users> friendList) {
        this.friendList = friendList;
        for (Users users:friendList){

            List<BasicMessage> messages = DataSupport.where("userId = ?",users.getUsername()).find(BasicMessage.class);
            if (messages.size() == 0){
                BasicMessage message = new BasicMessage();
                message.setType(BasicMessage.GROUP);
                message.setUserId(users.getUsername());
                message.setName(users.getName());
                message.setHeadImg(users.getHeadImg());
                message.save();
            }

            else{
                ContentValues values = new ContentValues();
                values.put("type",BasicMessage.GROUP);
                values.put("name",users.getName());
                values.put("headImg",users.getHeadImg());
                DataSupport.updateAll(BasicMessage.class,values,"userId = ?",users.getUsername()+"");
            }
        }
        if (friendAdapter!=null)
            friendAdapter.onDataChanged(friendList);
        int friendCount = friendList.size();
        tvFriendCount.setText("通讯录("+ friendCount +")");
        //showToast("获取好友列表成功:"+friendList.size());
    }

    @Override
    public void onGetFriendsFail(String s) {
        showToast("获取好友列表失败："+s);
    }

    @Override
    public void onGetGroupFail(String message) {
        showToast("获取班圈列表失败"+message);
    }

    @Override
    public void onGetGroupSuccess(List<Group> groupList) {
        if (groupList.size()==0){
            return;
        }
        //showToast("获取班圈列表成功："+groupList.size());
        this.groupList = groupList;
        if (groupAdapter!=null)
            groupAdapter.onDataChanged(groupList);
        if (SharedPreferencesUtil.lodaDataFromSharedPreferences(mUser.getUsername(),this)==null && groupList.size()>0){
            presentGroupId = groupList.get(0).getGroupId();
            SharedPreferencesUtil.saveDataToSharedPreferences(mUser.getUsername(),presentGroupId,this);
        }else if (SharedPreferencesUtil.lodaDataFromSharedPreferences(mUser.getUsername(),this)!=null){
            presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(mUser.getUsername(),this);
        }


        for (Group group:groupList){
            if (group.getGroupId().equals(presentGroupId)){
                Glide.with(this).load(group.getHeadImg()).crossFade().error(R.drawable.cross86).into(imgToolbarClassHead);
                TITLE_GROUP_NAME = group.getName();
                tvTitle.setText(group.getName());
            }

            List<BasicMessage> messages = DataSupport.where("userId = ?",group.getGroupId()).find(BasicMessage.class);
            if (messages.size() == 0){
                BasicMessage message = new BasicMessage();
                message.setType(BasicMessage.GROUP);
                message.setUserId(group.getGroupId());
                message.setName(group.getName());
                message.setHeadImg(group.getHeadImg());
                message.save();
            }
            else{
                ContentValues values = new ContentValues();
                values.put("type",BasicMessage.GROUP);
                values.put("name",group.getName());
                values.put("headImg",group.getHeadImg());
                DataSupport.updateAll(BasicMessage.class,values,"userId = ?",group.getGroupId()+"");
            }

        }
    }

    @Override
    public void onUpdateUserHeadImgSuccess(String imgPath) {
        Glide.with(this).load(imgPath).crossFade().error(R.drawable.cross86).into(headImg);
        showToast("更新头像数据成功");
    }

    @Override
    public void onUpdateUserHeadImgFail(String message) {
        showToast("更新头像数据失败："+message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        if (requestCode == MODIFIED_RESULT){

            name.setText(data.getStringExtra("name") == null?mUser.getName():data.getStringExtra("name"));
            showToast("更新数据成功");
        }
        if(requestCode == ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE){
            Uri uri = data.getData();
            headImg.setImageURI(uri);
            String gHeadImgPath = ImageLoderUtil.getRealPathFromUri(this,uri);
            mainPresenter.updateUserHeadImg(gHeadImgPath);
        }else if(requestCode == ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE){

            if (resultCode == Activity.RESULT_OK) {
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                FileOutputStream b = null;
                File file = new File("/sdcard/group_circle/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/group_circle/"+name;
                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        try {
                            b.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mainPresenter.updateUserHeadImg(fileName);
            }
        }
    }

    public void setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(this,PERMISSION,1);
        }else{
            //Log.i(TAG,"权限申请ok");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().groupManager().removeGroupChangeListener(groupChangeListener);
        EMClient.getInstance().contactManager().removeContactListener(contactListener);
    }
}
