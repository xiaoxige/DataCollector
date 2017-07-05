package cn.xiaoxige.datacollector;

import android.util.Log;

import com.cmcc.hbb.android.phone.datacollector.interfaces.IResult;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IUpServer;


/**
 * Created by zhuxiaoan on 2017/7/4.
 */

public class UpCollector implements IUpServer {

    @Override
    public void upServer(String upMsg, IResult result) {
        Log.e("TAG", "upMsg = " + upMsg);
        result.error();
    }

}
