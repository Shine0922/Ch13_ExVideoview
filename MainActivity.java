package com.example.win7.exvideoview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity
{
    private Button btnVideo1,btnVideo2,btnEnd;
    private VideoView vidVideo;
    private TextView txtVideo;

    //  SD卡路徑
    private final String sdPath = Environment.getExternalStorageDirectory().getPath()+"/";
    //  影片檔名稱
    private String fname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVideo1 = (Button)findViewById(R.id.btnVideo1);
        btnVideo2 = (Button)findViewById(R.id.btnVideo2);
        btnEnd = (Button)findViewById(R.id.btnEnd);
        vidVideo = (VideoView)findViewById(R.id.vidVideo);
        txtVideo = (TextView)findViewById(R.id.txtVideo);

        btnVideo1.setOnClickListener(listener);
        btnVideo2.setOnClickListener(listener);
        btnEnd.setOnClickListener(listener);

        vidVideo.setOnPreparedListener(listenprepare);  //  監聽影片播放中
        vidVideo.setOnCompletionListener(listencomplete);   //  監聽影片結束

        requestStoragePermission();
    }

    private void requestStoragePermission()
    {
        if(Build.VERSION.SDK_INT >=23)  //  安卓6.0以上
        {   //  判斷是否已取得憑證
            int haspermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(haspermission != PackageManager.PERMISSION_GRANTED)
            {   //  未取得權限
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }
        }
        //  已取得授權

    }
    //  requestPermissions 觸發的事件
    @Override
    public void onRequestPermissionsResult(int requestCode,String[]permissions,int[]grantResults)
    {
        if(requestCode==1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)    //  按 允許鈕
            {
                //  看要執行什麼
            }
            else
            {
                Toast.makeText(this,"未取得授權",Toast.LENGTH_LONG).show();
                finish();   //  結束應用程式
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    private Button.OnClickListener listener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btnVideo1:    //  影片一
                    fname = "robot.3gp";
                    playVideo(sdPath+fname);
                    break;
                case R.id.btnVideo2:    //  影片二
                    fname = "post.3gp";
                    playVideo(sdPath+fname);
                    break;
                case R.id.btnEnd:   //  結束
                    finish();
                    break;
            }
        }
    };

    //  播放影片
    private void playVideo(String filePath) //  filePath 是影片的路徑
    {
        if(filePath !="")
        {
            vidVideo.setVideoPath(filePath);    //  設定影片路徑
            //  加入撥放控制軸
            vidVideo.setMediaController(new MediaController(MainActivity.this));
            vidVideo.start();
        }
    }

    //  Onprepare監聽事件
    private MediaPlayer.OnPreparedListener listenprepare = new MediaPlayer.OnPreparedListener()
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            txtVideo.setText(" 影片 : "+ fname);
        }
    };

    //  Complete監聽事件
    private MediaPlayer.OnCompletionListener listencomplete =new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            txtVideo.setText(fname + " 播放完畢 ! ");
        }
    };
}
