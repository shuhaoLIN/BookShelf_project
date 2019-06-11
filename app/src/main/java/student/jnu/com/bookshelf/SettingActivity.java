package student.jnu.com.bookshelf;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lib.folderpicker.FolderPicker;


/**
 * settings activity
 */

public class SettingActivity extends AppCompatActivity implements WiperSwitch.OnChangedListener{
    private static final int FOLDERPICKER_CODE = 1;

    private static String[] service_items;
    private static boolean[] service_choosed_set;
    private static String[] articles_items;
    private static boolean[] articles_choosed_set;
    private static String backupPath;

    private static String restoreFileName;
    private static int yourChoice;
    CSVOP op;
    Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LitePal.initialize(this);
        op = new CSVOP();
        settings = op.importSettings();
        service_items = settings.getService_items();
        service_choosed_set = settings.getService_choosed_set();
        articles_items = settings.getArticles_items();
        articles_choosed_set = settings.getArticles_choosed_set();
        backupPath = settings.getBackupPath();
        restoreFileName = settings.getRestoreFileName();


        Toolbar toolbar = (Toolbar)findViewById(R.id.bookshelf_setting);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        /**
         * 选择默认爬虫接口
         * 获得选择的爬虫接口之后，这里要存储到数据库*/
        LinearLayout webService = (LinearLayout) findViewById(R.id.web_service);
        webService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceDialog();

            }
        });

        /**
         * 实例化自动完成开关按钮
         * 同样，这里也要存储到数据库*/
        WiperSwitch wiperSwitch = (WiperSwitch)findViewById(R.id.wiperSwitch1);
        wiperSwitch.setChecked(false);//设置初始状态为false
        wiperSwitch.setOnChangedListener(this);//设置监听

        /**
         * 设置备份路径
         * 当设置完备份路径后，要存到数据库*/
        LinearLayout backupLoc = (LinearLayout) findViewById(R.id.backup_location);
        backupLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里应该获取默认的存储路径
                Intent intent = new Intent(SettingActivity.this, FolderPicker.class);
                startActivityForResult(intent, FOLDERPICKER_CODE);
            }
        });

        /**
         * 备份书架*/
        LinearLayout backup = (LinearLayout) findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Backup backup =  new Backup(SettingActivity.this, backupPath);
                    backup.execute("backupDatabase");
                    Toast.makeText(SettingActivity.this,
                            "备份成功",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        /**
         * 恢复书架
         * 恢复失败，未完成备份功能*/
        LinearLayout restore = (LinearLayout) findViewById(R.id.restore_bookshelf);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                File file = new File(backupPath);
                String[] items = file.list();

                restoreDialog(items);
            }
        });

        /**
         * 导出书架为csv文件
         * */
        LinearLayout export = (LinearLayout) findViewById(R.id.export_bookshelf);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导出书架，首先调用dialog询问要导出的选择
                exportDialog();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

            String folderLocation = intent.getExtras().getString("data");
            TextView backupPathView = (TextView)findViewById(R.id.backup_path);
            backupPathView.setText(folderLocation);
            backupPath = folderLocation;
            settings.setBackupPath(backupPath);
            op.exportSettings(settings);
        }
    }


    @Override
    public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
        if (checkState){
            //当自动完成网页域
            Toast.makeText(SettingActivity.this,"开关开启了",Toast.LENGTH_SHORT).show();
        }else if (!checkState){
            Toast.makeText(SettingActivity.this,"开关关闭了",Toast.LENGTH_SHORT).show();
        }
    }

    //Service选择框
    private void webServiceDialog() {

        new AlertDialog.Builder(this)
        .setTitle("Web Services")
        .setMultiChoiceItems(service_items, service_choosed_set,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            service_choosed_set[which] = true;
                        } else {
                            service_choosed_set[which] = false;
                        }
                    }
                })
        .setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settings.setService_choosed_set(service_choosed_set);
                        op.exportSettings(settings);
                    }
                })
        .show();
    }

    //export标签导出框
    private void exportDialog() {
        new AlertDialog.Builder(this)
        .setTitle("Web Services")
        .setMultiChoiceItems(articles_items, articles_choosed_set,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            articles_choosed_set[which] = true;
                        } else {
                            articles_choosed_set[which] = false;
                        }
                    }
                })
        .setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 点击确定后，要实现数据库写入*/
                        op.exportCSV("/storage/emulated/0/bookshelf/csv",
                                articles_choosed_set);
                        settings.setArticles_choosed_set(articles_choosed_set);
                        op.exportSettings(settings);
                    }
                })
        .show();
    }

    //恢复备份单选择框
    private void restoreDialog(final String[] items){
        yourChoice = -1;
        new AlertDialog.Builder(SettingActivity.this)
        .setTitle("备份文件")
        .setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                })
        .setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            Toast.makeText(SettingActivity.this,
                                    "你选择了" + items[yourChoice],
                                    Toast.LENGTH_SHORT).show();
                            restoreFileName = items[yourChoice];
                        }

                        try{
                            Backup backup =  new Backup(SettingActivity.this, backupPath);
                            backup.setResortFileName(restoreFileName);
                            backup.execute("restroeDatabase");
                            settings.setRestoreFileName(restoreFileName);
                            op.exportSettings(settings);
                            Toast.makeText(SettingActivity.this,
                                    "恢复备份成功,请重新启动应用",Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                })
        .show();
    }
}

