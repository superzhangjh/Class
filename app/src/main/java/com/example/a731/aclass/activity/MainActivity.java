package com.example.a731.aclass.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.FriendAdapter;
import com.example.a731.aclass.adapter.GroupAdapter;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.fragment.CircleFragment;
import com.example.a731.aclass.fragment.MessFragment;
import com.example.a731.aclass.presenter.MainPresenter;
import com.example.a731.aclass.presenter.impl.MainPresenterImpl;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.MainView;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.a731.aclass.R.id.main_nav_view;
import static com.example.a731.aclass.util.EaseMobUtil.MODIFIED_RESULT;
import static org.litepal.LitePalApplication.getContext;

public class MainActivity extends BaseActivity implements MainView{

    private static final String TITLE_GROUP_NAME = "班圈名称";
    private static final String TITLE_USER_NAME = " 用户名称";

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private NavigationView navRight;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private RadioGroup mRadioGroup;
    private ImageView imgSearchFriend;
    private MenuItem itemFriend;
    private MenuItem itemScan;
    private MenuItem itemCreateGroup;
    private MenuItem itemJoinIn;
    private TextView tvTitle;

    private MainPresenter mainPresenter;

    private ListView friend_list_view,group_list_view;
    private List<Users> friendList;
    private TextView tvFriendCount;
    private CircleImageView imgClasshead;
    private List<Group> groupList;

    private FriendAdapter friendAdapter;
    private GroupAdapter groupAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        tvTitle = (TextView) findViewById(R.id.main_tv_title);
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        mainPresenter = new MainPresenterImpl(this);
        initViewPager();
        initNavigationView();
        initToolBar();
        initActionBarDrawerLayout();
        initFriendListView();
        initGroupListView();
    }

    private void initGroupListView() {
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
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navView = (NavigationView) findViewById(main_nav_view);
        navRight = (NavigationView) findViewById(R.id.main_nav_right);

        navView.setNavigationItemSelectedListener(  new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_nav_menu_logOut:
                        mainPresenter.logOut();
                        break;
                    case R.id.main_nav_menu_setting:
                        //TODO:软件设置
                        break;
                    case R.id.main_nav_menu_modified:
                        Intent intent = new Intent(getApplicationContext(),ModifiedUserDataActivity.class);
                        startActivityForResult(intent,MODIFIED_RESULT);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
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

        imgClasshead = (CircleImageView) findViewById(R.id.main_classhead);
        imgClasshead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (groupList.size()>0){
                Intent intent = new Intent(getApplicationContext(), GroupInfoActivity.class);
                intent.putExtra("groupId",groupList.get(0).getGroupId());
                startActivity(intent);
                /*}else{
                    showToast("你还未加入任何班圈");
                }*/

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
        //点击单选时，ViewPager显示对应的子界面
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.main_rb_class:
                        mViewPager.setCurrentItem(0);
                        itemFriend.setVisible(false);
                        itemScan.setVisible(true);
                        itemCreateGroup.setVisible(true);
                        itemJoinIn.setVisible(true);
                        tvTitle.setText(TITLE_GROUP_NAME);
                        break;
                    case R.id.main_rb_mess:
                        mViewPager.setCurrentItem(1);
                        itemFriend.setVisible(true);
                        itemScan.setVisible(false);
                        itemCreateGroup.setVisible(false);
                        itemJoinIn.setVisible(false);
                        tvTitle.setText(TITLE_USER_NAME);
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
                    case 0:itemFriend.setVisible(false);
                        itemScan.setVisible(true);
                        itemCreateGroup.setVisible(true);
                        itemJoinIn.setVisible(true);
                        tvTitle.setText(TITLE_GROUP_NAME);
                        break;
                    case 1:
                        itemFriend.setVisible(true);
                        itemScan.setVisible(false);
                        itemCreateGroup.setVisible(false);
                        itemJoinIn.setVisible(false);
                        tvTitle.setText(TITLE_USER_NAME);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        itemFriend = menu.findItem(R.id.main_toolbar_friend);
        itemScan = menu.findItem(R.id.main_toolbar_scan);
        itemCreateGroup = menu.findItem(R.id.main_toolbar_create_group);
        itemJoinIn = menu.findItem(R.id.main_toolbar_join_in);
        itemFriend.setVisible(false);
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
        if (friendList!=null)
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
        showToast("获取班圈列表成功："+groupList.size());
        this.groupList = groupList;
        if (groupAdapter!=null)
            groupAdapter.onDataChanged(groupList);
    }



    @Override
    public void onDisconnected(int errorCode) {
        if(errorCode == EMError.USER_REMOVED){
            showToast("帐号已经被移除");
            // 显示帐号已经被移除
        }else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            showToast("帐号在其他设备登录");
            // 显示帐号在其他设备登录
        } else {
            if (NetUtils.hasNetwork(MainActivity.this)){
                showToast("连接不到聊天服务器");
            }
            //连接不到聊天服务器
            else{
                showToast("当前网络不可用，请检查网络设置");
            }
            //当前网络不可用，请检查网络设置
        }
    }
}
