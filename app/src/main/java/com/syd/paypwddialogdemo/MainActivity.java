package com.syd.paypwddialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnShowPwdDialog;
    private PasswordInputView pwdInputView;

    private List<String> payWayList = new ArrayList<String>(); // 下发的支付方式顺序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        btnShowPwdDialog = (Button) findViewById(R.id.btn_show_pwd_dialog);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        payWayList.add("1");
        payWayList.add("2");
        payWayList.add("3");
        btnShowPwdDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdInputView = new PasswordInputView(MainActivity.this, "100.00", payWayList);
                pwdInputView.setOnPwdInputListener(new OnPwdInputListener() {
                    @Override
                    public void onPwdInput(String password) {
                        Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
