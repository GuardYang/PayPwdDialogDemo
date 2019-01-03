package com.syd.paypwddialogdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PasswordInputView implements View.OnClickListener {

    private Context context;
    private String payMoney; // 交易金额
    private List<String> payWayList; // 下发的支付方式顺序

    private LinearLayout llyPwdInputView;
    private BottomSheetDialog payPwdDialog;
    private ImageView ivDefaultPayWayIcon;
    private TextView tvDefaultPayWayName;
    private PasswordEditText etPwd; // 密码输入框
    private TextWatcher textWatcher;
    private GridView gvKeyboard; // 密码键盘
    private ArrayList<Map<String, String>> numList; // 数字按键序列
    private String password = ""; // 输入的密码
    private OnPwdInputListener onPwdInputListener;

    private LinearLayout llyPayWaySelect;
    private ListView lvPayWaySelect;
    private List<PayWayItem> payWayItemList;

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case Constants.KEYBOARD_INPUT:
                    int position = (int) msg.obj;
                    if (position < 11 && position != 9) {
                        // 点击0-9按键
                        password = etPwd.getText().append(numList.get(position).get("num")).toString();
                        etPwd.setText(password);
                    } else {
                        if (position == 11) {
                            // 点击退格键
                            if (!TextUtils.isEmpty(password) && !password.equals("")) {
                                password = etPwd.getText().delete(password.length() - 1, password.length()).toString();
                                etPwd.setText(password);
                            }
                        }
                    }
                    break;
            }
        }
    };

    public PasswordInputView(Context context, String payMoney, List<String> payWayList) {
        this.context = context;
        this.payMoney = payMoney;
        this.payWayList = payWayList;

        payPwdDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_pwd, null, false);
        initStep1(view);
        initStep2(view);

        llyPwdInputView = (LinearLayout) view.findViewById(R.id.lly_pwd_input_view);
        llyPayWaySelect = (LinearLayout) view.findViewById(R.id.lly_pay_way_select);
        showStep1(); // 显示第一页
    }

    /**
     * 初始化第一页
     * @param view
     */
    private void initStep1(View view) {
        view.findViewById(R.id.iv_close_dialog).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_pay_money)).setText(payMoney);
        view.findViewById(R.id.lly_pay_way_change).setOnClickListener(this);
        ivDefaultPayWayIcon = (ImageView) view.findViewById(R.id.iv_default_pay_way_icon);
        tvDefaultPayWayName = (TextView) view.findViewById(R.id.tv_default_pay_way_name);

        etPwd = (PasswordEditText) view.findViewById(R.id.et_password_InputView);
        etPwd.setEnabled(false); // 设置输入框不可编辑，防止系统键盘弹出
//        etPwd.setBorderColor(context.getResources().getColor(R.color.colorAccent));
//        etPwd.setPasswordColor(context.getResources().getColor(R.color.colorAccent));
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPwd.getText().length() == 6) {
                    onPwdInputListener.onPwdInput(etPwd.getText().toString());
                }
            }
        };
        etPwd.addTextChangedListener(textWatcher);
        initPayWay(view);
        gvKeyboard = (GridView) view.findViewById(R.id.gv_keyboard);
        initKeyboard();

        payPwdDialog.setContentView(view);
        payPwdDialog.show();
    }

    private void initStep2(View view) {
        view.findViewById(R.id.iv_back_dialog).setOnClickListener(this);
        lvPayWaySelect = (ListView) view.findViewById(R.id.lv_pay_way_select);
        // 根据下发的支付方式顺序显示
        payWayItemList = new ArrayList<>();
        for (int i = 0; i < payWayList.size(); i++) {
            if (Integer.parseInt(payWayList.get(i)) == 1) {
                // 现金支付
                PayWayItem item = new PayWayItem(R.drawable.icon_pay_cash, "现金支付", false, 1);
                payWayItemList.add(item);
            } else if (Integer.parseInt(payWayList.get(i)) == 2) {
                // 沃支付
                PayWayItem item = new PayWayItem(R.drawable.icon_pay_wo, "沃支付", false, 2);
                payWayItemList.add(item);
            } else if (Integer.parseInt(payWayList.get(i)) == 3) {
                // 银联支付
                PayWayItem item = new PayWayItem(R.drawable.icon_pay_unionpay, "银联支付", false, 3);
                payWayItemList.add(item);
            }
        }
        final PayWayAdapter payWayAdapter = new PayWayAdapter(context, R.layout.listitem_pay_way, payWayItemList);
        lvPayWaySelect.setAdapter(payWayAdapter);

        lvPayWaySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < payWayItemList.size(); i++) {
                    if (i == position) {
                        payWayItemList.get(i).setSelected(true);
                        // 更新第一页的支付方式
                        switch (payWayItemList.get(i).getPayOrder()) {
                            case 1:
                                ivDefaultPayWayIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pay_cash));
                                tvDefaultPayWayName.setText("现金支付");
                                break;
                            case 2:
                                ivDefaultPayWayIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pay_wo));
                                tvDefaultPayWayName.setText("沃支付");
                                break;
                            case 3:
                                ivDefaultPayWayIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pay_unionpay));
                                tvDefaultPayWayName.setText("银联支付");
                                break;
                        }
                    } else {
                        payWayItemList.get(i).setSelected(false);
                    }
                }
                payWayAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化默认的支付方式
     * @param view
     */
    private void initPayWay(View view) {
        int firstIndex = -1; // 默认支付方式
        boolean hasCash = false; // 是否含有现金支付
        for (int i = 0; i < payWayList.size(); i++) {
            firstIndex = Integer.parseInt(payWayList.get(i));
            if (firstIndex != 3 && firstIndex != 5) {
                hasCash = true;
                break;
            }
        }
        if (!hasCash) {
            payWayList.add("1");
            firstIndex = 1;
        }
        switch (firstIndex) {
            case 1:
                // 现金支付
                ivDefaultPayWayIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pay_cash));
                tvDefaultPayWayName.setText("现金支付");
                break;
            case 2:
                // 沃支付
                ivDefaultPayWayIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pay_wo));
                tvDefaultPayWayName.setText("沃支付");
                break;
            case 3:
                // 银联支付
                ivDefaultPayWayIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pay_unionpay));
                tvDefaultPayWayName.setText("银联支付");
                break;
        }
    }

    /**
     * 初始化密码键盘
     */
    private void initKeyboard() {
        final int number = 10;
        int[] keys = new int[number];
        for (int i = 0; i < 10; i++) {
            keys[i] = i;
        }
        // 随机生成键盘数字
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int p = random.nextInt(number);
            int tmp = keys[i];
            keys[i] = keys[p];
            keys[p] = tmp;
        }

        numList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 9) {
                map.put("num", String.valueOf(keys[i]));
            } else if (i == 9) {
                map.put("num", "");
            } else if (i == 10) {
                map.put("num", String.valueOf(keys[9]));
            } else if (i == 11) {
                map.put("num", "");
            }
            numList.add(map);
        }
        KeyAdapter keyAdapter = new KeyAdapter(context, numList, handler);
        gvKeyboard.setAdapter(keyAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog:
                if (payPwdDialog.isShowing()) {
                    payPwdDialog.dismiss(); // 关闭对话框
                }
                break;
            case R.id.lly_pay_way_change:
                showStep2(); // 选择支付方式
                break;
            case R.id.iv_back_dialog:
                showStep1(); // 返回输入密码
                break;
        }
    }

    private void showStep1() {
        llyPwdInputView.setVisibility(View.VISIBLE);
        llyPayWaySelect.setVisibility(View.GONE);
    }

    private void showStep2() {
        llyPwdInputView.setVisibility(View.GONE);
        llyPayWaySelect.setVisibility(View.VISIBLE);
    }

    public void setOnPwdInputListener(OnPwdInputListener listener) {
        this.onPwdInputListener = listener;
    }

//    private void closeInputMethod() {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            Toast.makeText(context, "系统键盘隐藏成功", Toast.LENGTH_SHORT).show();
//            imm.hideSoftInputFromWindow(
//                    etPwd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
}
