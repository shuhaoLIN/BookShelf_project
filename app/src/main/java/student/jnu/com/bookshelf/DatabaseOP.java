package student.jnu.com.bookshelf;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by lenovo on 2019/5/25.
 */
public class DatabaseOP {
    //static SQLiteDatabase db = LitePal.getDatabase();;
    static DatabaseOP op;

    private DatabaseOP() {
    }

    public static DatabaseOP getInstance() {
        if (op == null)
            op = new DatabaseOP();
        return op;
    }

    //插入操作
    public static void insertBook(Book book) {
        book.save();
    }

    //更新操作：
    public static void updateAllBook(Book book) {
        book.saveOrUpdate("ISBN=?", book.getISBN());
    }

    //查询
    public static List<Book> selectALLBook(){
        return LitePal.findAll(Book.class);
    }
    //按照书架查询
    public static List<Book> selectBookByShelf(String shelf) {
        return LitePal.where("shelf = ?", String.valueOf(shelf)).find(Book.class);
    }
    //按照标签查询
    public static List<Book> selectBookByLabel(int label) {
        return LitePal.where("labels LIKE ?", "%"+String.valueOf(1)+"%").find(Book.class);
    }
    //搜索按钮
    public static List<Book> searchByNameOrEditorOrPublishing(String search) {
        return LitePal.where("name like '%"+search+"%' or editor like '%"+search+"%' or publishing like '%"+search+"%'").find(Book.class);
    }

    //排序
    public static List<Book> sortByTitle(){
        return LitePal.order("title asc").find(Book.class);
    }
    public static List<Book> sortByEditor(){
        return LitePal.order("editor asc").find(Book.class);
    }
    public static List<Book> sortByPublishing(){
        return LitePal.order("publishing desc").find(Book.class);
    }
    public static List<Book> sortByDate(){
        return LitePal.order("date asc").find(Book.class);
    }

    //删除
    public static void deleteBook(Book book){
        LitePal.deleteAll(Book.class,"ISBN = ?", book.getISBN());
    }
    public static void deleteAll(){
        LitePal.deleteAll(Book.class);
    }
}
