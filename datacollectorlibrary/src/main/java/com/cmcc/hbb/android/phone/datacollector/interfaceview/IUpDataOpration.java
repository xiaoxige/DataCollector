package com.cmcc.hbb.android.phone.datacollector.interfaceview;

import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;

import java.util.List;

/**
 * Created by 小稀革 on 2017/7/2.
 * 上传服务器接口
 */

public interface IUpDataOpration {

    void upCollectorDataToServer(IEntity entity);

    void upCollectorDataToServer(List<IEntity> entity);
}
