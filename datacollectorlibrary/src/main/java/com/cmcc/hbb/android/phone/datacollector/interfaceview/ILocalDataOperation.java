package com.cmcc.hbb.android.phone.datacollector.interfaceview;

import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;

import java.util.List;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public interface ILocalDataOperation {

    boolean save(IEntity entity);

    boolean save(List<IEntity> entities);

    List<IEntity> getAllEntity();

    boolean del(IEntity entity);

    boolean del(List<IEntity> entities);

    boolean delAll();

    long getLines();

}
