package com.sam.scaner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    static Utils instance;
    static String TAG = "SamTam";
    static Toast toast;
    static Activity mContext;
    static String baseUrl = "http://39.100.11.220:8080/";
    MediaPlayer mediaPlayer;
    public static Utils getInstance(Activity context) {
        mContext = context;
        return instance = new Utils();
    }

    //首先必须初始化
    public static  void initBaseUrl(String url){
        baseUrl = url;
    }


    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void toast(Activity context, String msg) {
        context.runOnUiThread(() -> {
            int duration = Toast.LENGTH_SHORT;
            if (toast == null) {
                toast = Toast.makeText(context, msg, duration);
            } else {
                toast.setText(msg);
                toast.setDuration(duration);
            }
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 200);
            toast.show();
        });


    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public  void post(final String url, String jsonData, String token, HTTPCallBack callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                if (!isNetworkConnected(mContext)) {
                    return null;
                }
                try {
                    String tmpUrl = baseUrl + url;
                    URL url = new URL(tmpUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("token", token);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setRequestProperty("accept", "application/json");
                    conn.setConnectTimeout(15000);

                    // 往服务器里面发送数据
                    if (!TextUtils.isEmpty(jsonData)) {
                        byte[] writebytes = jsonData.getBytes();
                        // 设置文件长度
                        conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                        OutputStream outwritestream = conn.getOutputStream();
                        outwritestream.write(jsonData.getBytes());
                        outwritestream.flush();
                        outwritestream.close();
                    }

                    String result = "";
                    BufferedReader reader = null;
                    if (conn.getResponseCode() == 200) {
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        result = reader.readLine();
                        return result;
                    } else {
                        mContext.runOnUiThread(() ->  callback.Fail("err"));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mContext.runOnUiThread(() ->  callback.Fail("err"));
                } catch (IOException e) {
                    e.printStackTrace();
                    mContext.runOnUiThread(() ->  callback.Fail("err"));

                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    mContext.runOnUiThread(() -> callback.Succ(result));

                }
                super.onPostExecute(result);

            }
        }.execute();

    }



    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    public enum AudioEnum{
        pctcInfo("pctcInfo.mp3"), ////请核实快件信息（扫描后）
        placeSucceed("placeSucceed.mp3"),//放置成功（点击确认放置——即确认入库，无问题）
        placeFailed("placeFailed.mp3"),//放置失败（点击确认放置——即确认入库，有问题（没有输入必填信息“运单号、公司、手机号、放置地址”，或者无网络））
        repetition("repetition.mp3"),//重复（点击确认放置——即确认入库，重复扫描快件）
        enterIntoSucceed("enterIntoSucceed.mp3"),//入库成功（点击确认入库，无问题）
        enterIntoFailed("enterIntoFailed.mp3"),//入库失败（点击确认入库，有问题（没有输入必填信息“运单号、公司、手机号”，或者无网络））
        retention("retention.mp3"),//滞留件：
        pvtpInfo("pvtpInfo.mp3"),//请核实取件者信息（在出库详情界面提示）
        goOutSucceed("goOutSucceed.mp3"),//出库成功（点击确认出库，无问题）
        goOutFailed("goOutFailed.mp3"),//出库失败（点击确认出库，无网络）
        alreadyGoOut("alreadyGoOut.mp3"),//已出库（重复扫描）
        handBackSucceed("handBackSucceed.mp3"), //退件成功（点击退件出库，无问题）
        handBackFailed("handBackFailed.mp3"),//退件失败（点击退件出库，无网络）
        DropsSound("DropsSound.mp3"),//滴滴滴
        tipAddress("tipAddress.mp3"),//提示地址
        tipCheckCom("tipCheckCom.mp3"),//提示检查快递公司
        tipCheckInputInfo("tipCheckInputInfo.mp3"),//提示是否输入正确
        ;
        public String mAudioName;
         AudioEnum(String audioName){
            mAudioName = audioName;
        }
    }

    public void play(String filename) {
        //0 播放声音 1 播放滴滴滴 2关闭播放
        int isPlay = SharedPreferencesUtil.getInstance().getIntSP("isPlay");
        if(isPlay == 1){
            filename = AudioEnum.DropsSound.mAudioName;
        }else if(isPlay == 2){
            return;
        }

        try {
            AssetManager assetManager = mContext.getAssets();
            AssetFileDescriptor afd = assetManager.openFd("app/file/audio/"+filename);   //根据文件名找到文件
            //对mediaPlayer进行实例化
            mediaPlayer = new MediaPlayer();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();    //如果正在播放，则重置为初始状态
            }
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());     //设置资源目录
            mediaPlayer.prepare();//缓冲
            mediaPlayer.start();//开始或恢复播放
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("SamTam",e.getLocalizedMessage());
        }
    }
}
