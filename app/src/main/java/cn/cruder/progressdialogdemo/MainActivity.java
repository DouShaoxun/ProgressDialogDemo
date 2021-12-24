package cn.cruder.progressdialogdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;


import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNormalDialog, btnLinearDialog, btnLiearDialogWithUpdateProgress;

    private int currentProgress = 0;
    private int add = 0;
    private ProgressDialog pd1 = null;
    private ProgressDialog pd2 = null;
    private final static int MAXVALUE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        componentInit();
        componentAddOnClickListener();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                pd2.setProgress(currentProgress);
            }
            if (currentProgress >= MAXVALUE) {
                pd2.dismiss();
            }
        }
    };

    public void componentInit() {
        btnNormalDialog = findViewById(R.id.main_btnNormalDialog);
        btnLinearDialog = findViewById(R.id.main_btnLinearDialog);
        btnLiearDialogWithUpdateProgress = findViewById(R.id.main_btnLinearDialogWithUpdateProgress);
    }

    public void componentAddOnClickListener() {
        btnNormalDialog.setOnClickListener(this);
        btnLinearDialog.setOnClickListener(this);
        btnLiearDialogWithUpdateProgress.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btnNormalDialog: {
                //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                ProgressDialog.show(MainActivity.this, "资源加载中", "资源加载中,请稍后..."
                        , false, true);
                break;
            }
            case R.id.main_btnLinearDialog: {
                pd1 = new ProgressDialog(MainActivity.this);
                //依次设置标题,内容,是否用取消按钮关闭,是否显示进度
                pd1.setTitle("软件更新中");
                pd1.setMessage("软件正在更新中,请稍后...");
                pd1.setCancelable(true);
                //这里是设置进度条的风格,HORIZONTAL是水平进度条,SPINNER是圆形进度条
                pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd1.setIndeterminate(true);
                //调用show()方法将ProgressDialog显示出来
                pd1.show();
                break;
            }
            case R.id.main_btnLinearDialogWithUpdateProgress: {
                //初始化属性
                currentProgress = 0;
                add = 0;
                //依次设置一些属性
                pd2 = new ProgressDialog(MainActivity.this);
                pd2.setMax(MAXVALUE);
                pd2.setTitle("文件读取中");
                pd2.setMessage("文件加载中,请稍后...");
                //这里设置为不可以通过按取消按钮关闭进度条
                pd2.setCancelable(false);
                pd2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //这里设置的是是否显示进度,设为false才是显示的哦！
                pd2.setIndeterminate(false);
                pd2.show();
                //这里的话新建一个线程,重写run()方法,
                new Thread() {
                    @Override
                    public void run() {
                        while (currentProgress < MAXVALUE) {
                            //这里的算法是决定进度条变化的,可以按需要写
                            currentProgress = 2 * usetime();
                            //把信息码发送给handle让更新界面
                            handler.sendEmptyMessage(123);
                        }
                    }
                }.start();
                break;
            }
            default:
                break;
        }

    }

    /**
     * 这里设置一个耗时的方法
     *
     * @return
     */
    private int usetime() {
        add++;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return add;
    }
}