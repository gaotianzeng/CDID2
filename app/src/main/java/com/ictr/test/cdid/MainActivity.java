package com.ictr.test.cdid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ictr.android.cdid.CDIDSdk;
import com.ictr.android.cdid.config.CDIDConfig;
import com.ictr.android.cdid.impl.CDIDCallback;


/**
 * @author gaotianzeng
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 众拍对应服务器地址
     */
//    private static final String CDID_SERVER_URL = "https://crowdphoto.ictr.cn/CrowdphotoWeb/WebService";
    private static final String CDID_SERVER_URL = "http://10.20.4.134:8083";
    private final String TAG = MainActivity.class.getSimpleName();
    private TextView mShowCDID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mGetCDID = findViewById(R.id.btn_get_main);
        mShowCDID = findViewById(R.id.tv_show_main);
        mGetCDID.setOnClickListener(this);

        //打开sdk log
        CDIDConfig.isOpenLog(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_get_main) {
            CDIDSdk.getCDID(this, CDID_SERVER_URL, new CDIDCallback() {
                @Override
                public void onSuccess(String cdid) {
                    Log.i(TAG, "onSuccess:" + cdid);
                    mShowCDID.setText(cdid);
                }

                @Override
                public void onError(int errorCode) {
                    //errorCode
                    //-100:参数为空
                    //-101:查找CDID失败
                    //-102:注册失败
                    //-1001:网络异常
                    Log.i(TAG, "onError:" + errorCode);
                }
            });
        }
    }
}
