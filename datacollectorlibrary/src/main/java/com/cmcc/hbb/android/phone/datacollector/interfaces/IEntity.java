package com.cmcc.hbb.android.phone.datacollector.interfaces;

/**
 * Created by zhuxiaoan on 2017/7/4.
 */

public interface IEntity {

    /**
     * 子类必须重写该方法
     *
     * @return 需要记录和发送给服务器的Log内容
     */
    String toContent();

}
