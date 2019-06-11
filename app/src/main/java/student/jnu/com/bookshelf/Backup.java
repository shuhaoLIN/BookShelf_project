package student.jnu.com.bookshelf;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

public class Backup extends AsyncTask<String, Void, Integer>{

    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restroeDatabase";
    private Context mContext;
    private String path;
    private String resortFileName;
    public Backup(Context context, String path){
        this.mContext = context;
        this.path = path;
//        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/bookshelf/",ISBN+".bmp");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//        file.setWritable(true);
//        file.setReadable(true);
    }
    public void setResortFileName(String resortFileName){
        this.resortFileName = resortFileName;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        //读取到了原来手机上面的数据库
        File dbFile = mContext.getDatabasePath("/data/data/student.jnu.com.bookshelf/databases/"+ "BookStore.db");
        File exportDir;
        if(this.path != null && path.length() != 0){
            exportDir = new File(path);
        }
        else{
            exportDir = new File(Environment.getExternalStorageDirectory()+"/bookshelf/",
                    "Backup");
        }
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        Calendar calendar = Calendar.getInstance();
        String stringBuilder = calendar.getTime().toString().trim(); //用时间进行表示备份的文件
        //在exportDir目录下面进行创建数据库
        File backupfile = new File(exportDir, stringBuilder + "book.db"); // 这个是现在备份的数据库

        if(!backupfile.exists())
            try {
                backupfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        String command = strings[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                if(!backupfile.exists())
                    backupfile.createNewFile();
                fileCopy(dbFile, backupfile);
                return Log.d("backupfile", "ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("backupfile", "fail");
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                //这里应该添加一个选择框选择相应的文件名，然后进行还原
//                String[] fileList = exportDir.list();
//                for(String name : fileList){
//                    System.out.println(name);
//                }
                //fileCopy(backupfile, dbFile);
                DatabaseOP.deleteAll();
                fileCopy(new File(exportDir, resortFileName), dbFile);

                //fileCopy(backupfile, dbFile);
                return Log.d("restore", "success");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("restore", "fail");
            }
        } else {
            return null;
        }
    }

    private void fileCopy(File dbFile, File backup) throws Exception{
        // TODO Auto-generated method stub
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

}
