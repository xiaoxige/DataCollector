package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * 判断网络情况工具类
 *
 * @author Administrator
 */
public class NetworkUtils {


    public enum NetworkStatus {
        UNAVAILABLE,//无网络
        UNKNOWN,//未知网络
        NETWORK_2G,
        NETWORK_3G,
        NETWORK_4G,
        NETWORK_WIFI
    }

    /**
     * wifi 是否连接或正在连接
     *
     * @return true是, false不是
     */
    public static boolean isWifi(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo == null ? false
                : (networkInfo.getType() == ConnectivityManager.TYPE_WIFI ? true : false);
    }

    /**
     * 判断连接的网络是否是2g/3g/4g
     *
     * @return true是, false不是
     */
    public static boolean isMobile(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo == null ? false
                : (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE ? true : false);
    }

    /**
     * 判定是否使用GPRS
     *
     * @return true是, false不是
     */
    public static boolean isGprs(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null) return false;
        switch (networkInfo.getSubtype()) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return true;
            default:
                return false;
        }
    }

//    /**
//     * 判定网络是否可用
//     *
//     * @return true是, false不是
//     */
//    public static boolean isNetworkAvailable(Context context) {
//        try {
//            return getNetworkInfo(context).isAvailable();
//        }catch (Exception e){
//            return false;
//        }
//    }

    /**
     * 获取可用网络信息
     *
     * @return 当前可以使用的网络信息对象
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        return context == null ? null : ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 是否打开WIFI
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取网络链接类型
     *
     * @param context
     * @return
     */
    public static NetworkStatus getNetworkType(Context context) {
        NetworkStatus status = NetworkStatus.UNKNOWN;

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                status = NetworkStatus.NETWORK_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String subTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        status = NetworkStatus.NETWORK_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        status = NetworkStatus.NETWORK_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        status = NetworkStatus.NETWORK_4G;
                        break;
                    default:
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase("CDMA2000")) {
                            status = NetworkStatus.NETWORK_3G;
                        }
                        break;
                }
            }
        } else {
            status = NetworkStatus.UNAVAILABLE;
        }

        return status;
    }


    /**
     * 获取WIFI信号强度
     *
     * @param context
     * @return
     */
    public static String getWifiRssi(Context context) {
        int asu = 85;
        try {
            final NetworkInfo network = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        asu = wifiInfo.getRssi();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asu + "dBm";
    }

    /**
     * 获取无线路由的名称
     *
     * @param context
     * @return
     */
    public static String getWifiSsid(Context context) {
        String ssid = "";
        try {
            final NetworkInfo network = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        ssid = wifiInfo.getSSID();
                        if (ssid == null) {
                            ssid = "";
                        }
                        ssid = ssid.replaceAll("\"", "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssid;
    }


}
