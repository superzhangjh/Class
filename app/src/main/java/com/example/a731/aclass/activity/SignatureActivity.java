package com.example.a731.aclass.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.SignatureAdapter;
import com.example.a731.aclass.data.Signature;
import com.example.a731.aclass.presenter.SignaturePresenter;
import com.example.a731.aclass.presenter.impl.SignaturePresenterImpl;
import com.example.a731.aclass.view.SignatureView;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignatureActivity extends BaseActivity implements SignatureView {

    private TextView groupName;
    private Button btnStartSign;
    private RecyclerView recyclerView;
    private SignatureAdapter adapter;
    private List<Signature> signatures;

    private SignaturePresenter presenter = new SignaturePresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_signature;
    }

    @Override
    public void initView() {
        groupName = (TextView) findViewById(R.id.signature_tv_groupName);
        btnStartSign = (Button) findViewById(R.id.signature_btn_start_signature);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.signature_recycler_content);
        adapter = new SignatureAdapter(this,signatures);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void initListener() {
        btnStartSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupId = getIntent().getStringExtra("groupId");
                Intent intent = new Intent(SignatureActivity.this,SignatureQRCodeActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new SignatureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Signature signature = signatures.get(position);
                Intent intent = new Intent(SignatureActivity.this,SignatureDetailActivity.class);
                intent.putExtra("objectId",signature.getObjectId());
                intent.putExtra("name",signature.getClassName());
                intent.putExtra("time",signature.getCreatedAt());
                intent.putExtra("groupObjectId",getIntent().getStringExtra("groupObjectId"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        String name = getIntent().getStringExtra("groupName");
        groupName.setText(name);
        String groupObjectId = getIntent().getStringExtra("groupObjectId");
        presenter.querySignature(groupObjectId);
    }

    @Override
    public void onGetSignatureSuccess(List<Signature> list) {
        showToast("获取考勤表成功："+list.size());
        signatures = list;
        if (adapter !=null)
            adapter.setListData(signatures);
    }

    @Override
    public void onGetSignatureFail(String message) {
        showToast("获取考勤表失败："+message);
    }
}
