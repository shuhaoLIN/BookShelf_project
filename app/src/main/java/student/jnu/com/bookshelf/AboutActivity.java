package student.jnu.com.bookshelf;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity{
    private static final String TAG = "AboutActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar)findViewById(R.id.bookshelf_about);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //版本跳转
        LinearLayout version = (LinearLayout) findViewById(R.id.version);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView web_open = (WebView)findViewById(R.id.web_open);
                web_open.loadUrl("https://www.baidu.com");
                //web_open.setWebViewClient(new WebViewClient());
            }
        });

        //评分
        LinearLayout rate = (LinearLayout) findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "该应用未加入商店", Toast.LENGTH_SHORT).show();
            }
        });

        //捐赠
        LinearLayout donate = (LinearLayout) findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "不支持支付宝功能", Toast.LENGTH_SHORT).show();
            }
        });

        //开源项目
        LinearLayout open_source = (LinearLayout) findViewById(R.id.open_source);
        open_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView web_open = (WebView)findViewById(R.id.web_open);
                web_open.loadUrl("https://www.baidu.com");
                //web_open.setWebViewClient(new WebViewClient());
            }
        });

        //反馈
        LinearLayout feedback = (LinearLayout) findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView web_open = (WebView)findViewById(R.id.web_open);
                web_open.loadUrl("https://www.baidu.com");
                //web_open.setWebViewClient(new WebViewClient());
            }
        });

        //开源许可
        LinearLayout license = (LinearLayout) findViewById(R.id.license);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                final String[] items = { "com.klinkerapps:sliding-activity:1.5.2"
                        ,"junit:junit:4.12"
                        ,"com.android.support.test:runner:1.0.2"
                        ,"com.android.support.test.espresso:espresso-core:3.0.2"
                        ,"org.litepal.android:java:3.0.0"
                        ,"libs/sun.misc.BASE64Decoder.jar"
                        ,"me.dm7.barcodescanner:zxing:1.9.8" };
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(AboutActivity.this);
                listDialog.setTitle("开源许可");
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which 下标从0开始
                        Toast.makeText(AboutActivity.this,
                                items[which]+"",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                listDialog.show();
            }
        });

        //使用条框
        LinearLayout term_service = (LinearLayout) findViewById(R.id.term_service);
        term_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                final String[] items = { "版权问题","信息收集","其他" };
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(AboutActivity.this);
                listDialog.setTitle("使用条款");
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which 下标从0开始
                        Toast.makeText(AboutActivity.this,
                                ""+items[which],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                listDialog.show();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
