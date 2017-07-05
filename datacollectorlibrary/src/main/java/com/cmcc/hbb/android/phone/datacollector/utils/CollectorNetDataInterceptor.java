//package com.cmcc.hbb.android.phone.datacollector.utils;
//
//
//import com.cmcc.hbb.android.phone.datacollector.NetEntity;
//
//import java.io.IOException;
//
//
///**
// * Created by zhuxiaoan on 2017/7/5.
// */
//
//public class CollectorNetDataInterceptor implements Interceptor {
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        String url = request.url().toString();
//
//
//        String method = request.method();
//        Headers headers = request.headers();
//
//        NetEntity entity = new NetEntity();
//        entity.logType = "netReq";
//        entity.desc = "method: " + method +
//                ", url: " + url +
//                ", headers: " + headers.toString();
//        com.cmcc.hbb.android.phone.common_data.datacollector.utils.CollectorUtil.getInstance().commitNormal(entity);
//
//
//        Response proceed = chain.proceed(request);
//        String responseString = new String(proceed.body().bytes());
//
//
//        entity.time = System.currentTimeMillis();
//        entity.logType = "netRes";
//        entity.desc = responseString;
//        com.cmcc.hbb.android.phone.common_data.datacollector.utils.CollectorUtil.getInstance().commitNormal(entity);
//
//        return proceed.newBuilder()
//                .body(ResponseBody.create(proceed.body().contentType(), responseString))
//                .build();
//    }
//
//}
