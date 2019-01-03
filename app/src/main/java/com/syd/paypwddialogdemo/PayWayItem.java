package com.syd.paypwddialogdemo;

public class PayWayItem {

    private int payWayIcon;
    private String payWayName;
    private boolean isSelected;
    private int payOrder;

    public PayWayItem(int payWayIcon, String payWayName, boolean isSelected, int payOrder) {
        this.payWayIcon = payWayIcon;
        this.payWayName = payWayName;
        this.isSelected = isSelected;
        this.payOrder = payOrder;
    }

    public int getPayWayIcon() {
        return payWayIcon;
    }

    public void setPayWayIcon(int payWayIcon) {
        this.payWayIcon = payWayIcon;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(int payOrder) {
        this.payOrder = payOrder;
    }
}
