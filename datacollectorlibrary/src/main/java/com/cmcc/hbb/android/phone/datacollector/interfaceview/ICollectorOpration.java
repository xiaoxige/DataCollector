package com.cmcc.hbb.android.phone.datacollector.interfaceview;

import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;

import java.util.List;

/**
 * Created by 小稀革 on 2017/7/3.
 */

public interface ICollectorOpration {
    void commitNormalData(List<IEntity> entitys);

    void commitNormalData(IEntity entity);

    void commitErrorData(List<IEntity> entitys);

    void commitErrorData(IEntity entity);
}
