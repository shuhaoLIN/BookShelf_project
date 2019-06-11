package student.jnu.com.bookshelf;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Settings implements Serializable {
    private static final long serialVersionUID = 1L;
    //private String currentBookshelf = "all";
    //private String currentLabel = "asd";
    private String service = "http://119.29.3.47:9001/book/worm/isbn?isbn=";
    private String backupPath = "/storage/emulated/0/bookshelf/backups";
    private String[] service_items = { "DouBan.com","OpenLibrary.org"};
    private boolean[] service_choosed_set ={true,false};
    private String[] articles_items = {"标题","作者","译者","出版社",
            "出版时间","ISBN","状态","书架","标签",
            "笔记","网址"};
    private String[] readState = {"未读", "阅读中", "已读"};
    private boolean[] articles_choosed_set ={true,true,true,true,true,true,true,true,true,true,true};
    private String restoreFileName = "";
    private int curIDofLabel = 0;
    private LinkedHashMap<Integer, String> labelMaps = new LinkedHashMap<Integer, String>() {
    };
    //书架
    private ArrayList<String> shelfs = new ArrayList<String>();
    public void addNewShelf(String shelf){
        shelfs.add(shelf);
    }
    public ArrayList<String> getShelfs(){
        return shelfs;
    }

    //存放图片地址的
    private String pictrueDir = "/storage/emulated/0/bookshelf";
    //存放settings的导出文件的
    private String settingsDir = "/storage/emulated/0/bookshelf/settings";



    private static Settings instance;

    private Settings(){}
    public static Settings getInstance(){
        if(null == instance ){
            instance = new Settings();
            instance.shelfs.add("默认书架");
        }
        return instance;
    }

    public int getCurIDofLabel() {
        return curIDofLabel;
    }

    public void setCurIDofLabel(int curIDofLabel) {
        this.curIDofLabel = curIDofLabel;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;

    }

    public String[] getService_items() {
        return service_items;
    }

    public void setService_items(String[] service_items) {
        this.service_items = service_items;

    }

    public boolean[] getService_choosed_set() {
        return service_choosed_set;
    }

    public void setService_choosed_set(boolean[] service_choosed_set) {
        this.service_choosed_set = service_choosed_set;

    }

    public String[] getArticles_items() {
        return articles_items;
    }

    public void setArticles_items(String[] articles_items) {
        this.articles_items = articles_items;
    }

    public boolean[] getArticles_choosed_set() {
        return articles_choosed_set;
    }

    public void setArticles_choosed_set(boolean[] articles_choosed_set) {
        this.articles_choosed_set = articles_choosed_set;

    }

    public String getRestoreFileName() {
        return restoreFileName;
    }

    public void setRestoreFileName(String restoreFileName) {
        this.restoreFileName = restoreFileName;

    }

//    public static int getIDOfLabelAndAddOne() {
//        int temp =  IDOfLabel;
//        IDOfLabel++;
//
//        return temp;
//    }

    public String getPictrueDir() {
        return pictrueDir;
    }

    public void labelMapsAddNew(int id, String labelName) {
        this.labelMaps.put(id, labelName);
    }

    public String getSettingsDir() {
        return settingsDir;
    }
    public LinkedHashMap<Integer, String> getLabelMaps() {
        return labelMaps;
    }

    public String[] getReadState() {
        return readState;
    }

    public void setReadState(String[] readState) {
        this.readState = readState;
    }
}
