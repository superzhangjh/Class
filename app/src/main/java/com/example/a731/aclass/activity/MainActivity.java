package com.example.a731.aclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.FriendAdapter1;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.fragment.CircleFragment;
import com.example.a731.aclass.fragment.FriendFragment;
import com.example.a731.aclass.fragment.MessFragment;
import com.example.a731.aclass.presenter.MainPresenter;
import com.example.a731.aclass.presenter.impl.MainPresenterImpl;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.MainView;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.a731.aclass.R.id.main_nav_view;
import static com.example.a731.aclass.util.EaseMobUtil.MODIFIED_RESULT;

public class MainActivity extends BaseActivity implements MainView{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private RadioGroup mRadioGroup;

    private MainPresenter mainPresenter;

    private ListView friend_list_view;
    private List<Users> friendList;
    private TextView tvFriendCount;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);


        mainPresenter = new MainPresenterImpl(this);
        initViewPager();
        initNavigationView();
        initToolBar();
        initActionBarDrawerLayout();
        initFriendListView();
    }

    private void initFriendListView() {
        friend_list_view = (ListView) findViewById(R.id.main_nav_right_listview);
        tvFriendCount = (TextView) findViewById(R.id.main_nav_right_count);

        friendList = new ArrayList<>();

        int friendCount = friendList.size();
        if (friendList.size()==0){
            tvFriendCount.setText("通讯录");
        }else {
            tvFriendCount.setText("通讯录(" + friendCount + ")");
        }

        FriendAdapter1 adapter = new FriendAdapter1(getApplicationContext(),R.layout.fragment_friend_item,friendList);
        friend_list_view.setAdapter(adapter);
        friend_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users user = friendList.get(position);
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("isGroupMess",false);
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
        fragmentList.add(new FriendFragment());

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
        NavigationView navView = (NavigationView) findViewById(main_nav_view);
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
        setSupportActionBar(mToolbar);

        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置班圈的头像
        mToolbar.setLogo(R.mipmap.ic_launcher);
        //设置班圈的名称
        mToolbar.setTitle("可改变的名称");
        mToolbar.setTitleTextColor(Color.RED);

        // 导航栏图标显示
        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);

        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        break;
                    case R.id.main_rb_mess:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.main_rb_friend:
                        mViewPager.setCurrentItem(2);
                        break;
                }
            }
        });

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                //好友请求被同意
            }

            @Override
            public void onContactDeleted(String username) {
                //好友请求被拒绝
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                try {
                    EaseMobUtil.acceptFriendRequest(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                //同意邀请时回调此方法

            }

            @Override
            public void onFriendRequestDeclined(String username) {
                //被拒绝时回调此方法
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
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
            case R.id.main_toolbar_search:
                intent.setClass(MainActivity.this,SearchUserActivity.class);
                startActivity(intent);
                break;
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
