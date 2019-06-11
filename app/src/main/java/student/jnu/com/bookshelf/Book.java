package student.jnu.com.bookshelf;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Calendar;

public class Book extends LitePalSupport implements Serializable{
    private int id;
    private String output; //来源
    private String name; //书名
    private String editor; //作者
    private String publishing;//出版社
    private String date; //出版时间
    private String ISBN; //ISBN
    private int readState; //阅读状态 要不设置为枚举变量也是可以的
    private String shelf; //书架
    private String notes; //笔记
    private String labels; //标签
    private String ouputURL; //URL
    private Calendar addTime; //添加时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String translator;

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }



    /**
     * For test
     * */
    private String title;
    private String imageId;

    public Book(String title, int imageId, String ISBN){
        this.ISBN = ISBN;
        this.title = title;
        this.editor = title;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getPublishing() {
        return publishing;
    }

    public void setPublishing(String publishing) {
        this.publishing = publishing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



    public String getLabels() {
        return labels;
    }

    public void AddLabels(int label) {
        if(null == this.labels){
            this.labels = label + ",";
        }
        else this.labels += label+",";
    }
    public void deleteLabels(int label){
        int loc = this.labels.indexOf(label+",");
        String[] two = new String[2];
        two = this.labels.split(label+",");
        this.labels = two[0] + two[1];
        System.out.println("main的book"+this.labels);
    }



    public String getOuputURL() {
        return ouputURL;
    }

    public void setOuputURL(String ouputURL) {
        this.ouputURL = ouputURL;
    }

    public Calendar getAddTime() {
        return addTime;
    }

    public void setAddTime(Calendar addTime) {
        this.addTime = addTime;
    }


}