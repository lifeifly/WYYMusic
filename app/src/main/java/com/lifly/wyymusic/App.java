package com.lifly.wyymusic;

import com.lifly.architecture.BaseApplication;
import com.lifly.architecture.utils.Utils;

public class App extends BaseApplication {
    //TODO tip：可借助 Application 来管理一个应用级 的 SharedViewModel，
    // 实现全应用范围内的 生命周期安全 且 事件源可追溯的 视图控制器 事件通知。

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
