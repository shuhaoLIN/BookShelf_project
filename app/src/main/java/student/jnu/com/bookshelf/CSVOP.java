package student.jnu.com.bookshelf;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 2019/6/5.
 */
public class CSVOP {
    File csvfile ;
    //String path = Environment.getRootDirectory().toString() + "/bookshelf/";
    PrintStream ps ;
    String[] dataHeader = {"Title","Authors","Translators","Publisher",
            "PubTime","ISBN","ReadingStatus","Bookshelves","Labels",
            "Notes","Website"
    };
    Settings settings = Settings.getInstance();

    /**
     * 导出
     * @param savePath
     */
    public void exportCSV(String savePath, boolean[] isToSave){
        try {
            //获取到相应的库里面的内容
            List<Book> books = DatabaseOP.selectALLBook();
            //生成保存文件
            File dir;
            if(null != savePath && savePath.length() != 0){
                dir = new File(savePath);
            }else{
                dir = new File(Environment.getExternalStorageDirectory()+"/bookShelf/", "csv");
            }
            //创建目录
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //创建文件
            String time = Calendar.getInstance().getTime().toString().trim();
            csvfile = new File(dir, time + "backup.csv");
            if (!csvfile.exists()) {
                try {
                    csvfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PrintStream ps = new PrintStream(new FileOutputStream(csvfile, true));
            //完成表头
            for(int i=0;i<dataHeader.length;i++){
                if(true == isToSave[i]){
                    ps.print(dataHeader[i]);
                    ps.print(",");
                }
            }
            ps.println();
            for(int i=0;i<books.size();i++){
                if(isToSave[0]) ps.print(books.get(i).getTitle()+",");
                if(isToSave[1]) ps.print(books.get(i).getEditor()+",");
                if(isToSave[2]) ps.print(books.get(i).getTranslator()+",");
                if(isToSave[3]) ps.print(books.get(i).getPublishing()+",");
                if(isToSave[4]) ps.print(books.get(i).getDate()+",");
                if(isToSave[5]) ps.print(books.get(i).getISBN()+",");
                if(isToSave[6]) ps.print(books.get(i).getReadState()+",");
                if(isToSave[7]) ps.print(books.get(i).getShelf()+",");
                if(isToSave[8]) ps.print(books.get(i).getLabels()+",");
                if(isToSave[9]) ps.print(books.get(i).getNotes()+",");
                if(isToSave[10]) ps.print(books.get(i).getOuputURL()+",");
                ps.println();
            }
            ps.flush();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出settings的内容
     */
    public void exportSettings(Settings settings){
        try {
            //获取Settings的实例
            //Settings saveSetting = Settings.getInstance();
            this.settings = settings;
            //生成保存文件
            File dir = new File(settings.getSettingsDir());
            //创建目录
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //创建文件
            csvfile = new File(dir, "settings");
            if (!csvfile.exists()) {
                try {
                    csvfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(csvfile));

            oos.writeObject(settings);
            oos.close();
            Log.d("fuck","重写了！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入settings文件
     */
    public Settings importSettings(){

        //测试一下导入后，直接使用getInstance是新的文件
        try {
            //获取保存文件
            //File dir = new File(fileSetting.getSettingsDir());
            //创建文件
            //csvfile = new File(dir, "settings");
            Settings fileSetting = settings;//Settings.getInstance();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileSetting.getSettingsDir()+"/settings")); //需要使用序列化。

            fileSetting = (Settings) ois.readObject();
            ois.close();
            Log.d("fuck","导入了！");
            return fileSetting;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
