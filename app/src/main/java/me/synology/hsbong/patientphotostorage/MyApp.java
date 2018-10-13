package me.synology.hsbong.patientphotostorage;

import android.app.Application;


/**
 * Created by bongh on 2018-10-13.
 */

public class MyApp extends Application {
    private MemberInfoItem memberInfoItem;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public MemberInfoItem getMemberInfoItem() {
        if (memberInfoItem == null) memberInfoItem = new MemberInfoItem();

        return memberInfoItem;
    }

    public  void setMemberInfoItem(MemberInfoItem item) {
        this.memberInfoItem = item;
    }
}
