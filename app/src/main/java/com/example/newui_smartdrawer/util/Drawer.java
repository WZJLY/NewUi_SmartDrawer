package com.example.newui_smartdrawer.util;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class Drawer {
    private int drawerId;
    private int boxId;
    private int drawerSize;
    public String statue;      //“0”启用， “1”禁用


    public Drawer(){
        this.drawerId = -1;
        this.boxId = 1;
        this.drawerSize = 2;
    }

    public Drawer(int dId, int bId, int dS, String drawerStatue){
        this.drawerId = dId;
        this.boxId = bId;
        this.drawerSize = dS;
        this.statue=drawerStatue;
    }
    public int getId(){
        return this.drawerId;
    }
    public int getDrawerSize(){return this.drawerSize;}
    public String getStatue(){return this.statue;}
}
