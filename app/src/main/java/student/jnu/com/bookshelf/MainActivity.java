package student.jnu.com.bookshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Book> bookList = new ArrayList<>();
    private static int curIDofLabel;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static BookAdapter adapter;
    boolean islabel = false;
    private Settings settings;
    private CSVOP op;

    //Scanner
    private int judge = 1;
    public LinearLayout isbn;
    public static List<Book> addbook;
    final int REQUEST_CODE_SCAN = 1;
    public static BookAdapter bookAdapter;
    private FloatingActionMenu mActionAddButton;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //activity加载就初始化数据库
        LitePal.initialize(this);
        addbook=new ArrayList<>();
//        if(addbook.size()==0){
//            Book book=new Book("2",3,"3");
//            addbook.add(book);
//        }
        bookAdapter=new BookAdapter(MainActivity.this,addbook);
        bookAdapter.notifyDataSetChanged();

        bookList = DatabaseOP.getInstance().selectALLBook();
        //update
//        Book updateBook = new Book("update",R.mipmap.ic_launcher,"4");
//        DatabaseOP.getInstance().updateAllBook(updateBook);
        //select
//        if(LitePal.isExist(Book.class)){
//            List<Book> books = LitePal.findAll(Book.class);
//            for(Book book1 : books){
//                Log.d("okok",book1.getTitle());
//            }
//            List<Book> list = DatabaseOP.getInstance().selectBookByShelf(2);
//            for(Book book : list){
//                Log.d("selete by shelf",book.getTitle());
//            }
//            list = DatabaseOP.getInstance().searchByNameOrEditorOrPublishing("林");
//            for(Book book : list){
//                Log.d("selete by 林",book.getTitle());
//            }
//            list = DatabaseOP.getInstance().searchByNameOrEditorOrPublishing("林树豪");
//            for(Book book : list){
//                Log.d("selete by 林树豪",book.getTitle());
//            }
//
//            list = DatabaseOP.getInstance().sortByPublishing();
//            for(Book book : list){
//                Log.d("sort by publishing",book.getPublishing());
//            }
//            list = DatabaseOP.getInstance().selectALLBook();
//            for(Book book : list){
//                Log.d("select all",book.getPublishing());
//            }
//        }else{
//            System.out.println("没有book这个数据库！！！");
//        }


        //delete
//        Book book = new Book("oko",R.mipmap.ic_launcher,"1");
//        DatabaseOP.getInstance().deleteBook(book);
//        List<Book> books = LitePal.findAll(Book.class);
//        for(Book book1 : books){
//            Log.d("okok",book1.getTitle());
//        }

        //restore
//        new Backup(MainActivity.this).execute("restroeDatabase");
//        //backup
//        new Backup(MainActivity.this).execute("backupDatabase");
//        DatabaseOP.getInstance().deleteAll();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookAdapter(this, bookList);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**添加按钮*/
        setFloatingActionButton();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化标签
        op = new CSVOP();
        settings = op.importSettings();
        if (settings == null){
            op.exportSettings(Settings.getInstance());
            settings = op.importSettings();
        }
        curIDofLabel = settings.getCurIDofLabel();
        //Log.d("import",Settings.getInstance().getLabelMaps().size()+"");
        LinkedHashMap<Integer,String> labelMaps = settings.getLabelMaps();
        Log.d("fuck","+"+labelMaps.size());
        //labelMaps.put(1,"fuck");
        if(!labelMaps.isEmpty()){
            Iterator it=labelMaps.keySet().iterator();
            Object key;
            Object value;
            while(it.hasNext()){
                key=it.next();
                value=labelMaps.get(key);
                navigationView.getMenu().add(R.id.g2,(int)key,1,(String)value);//需要获取id的话，id就等于1；
                navigationView.setNavigationItemSelectedListener(this);
            }
        }else{
            Log.d("fuck","空的");
        }
    }

    private void setFloatingActionButton() {
        mActionAddButton = (FloatingActionMenu) findViewById(R.id.fab_menu_add);
        fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_item_1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("fab1", "fab menu item 1 clicked");
                Intent i = new Intent(MainActivity.this, SingleAddActivity.class);
                startActivity(i);
                mActionAddButton.close(true);

            }
        });
        fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_item_2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("fab2", "fab menu item 2 clicked");
                Intent i = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(i);
                mActionAddButton.close(true);
            }
        });
        mActionAddButton.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
        mActionAddButton.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
    }

    /**
     * Main Page's toolbar function.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); //设置相应的顶部toolbar
        MenuItem searchItem = menu.findItem(R.id.search_books);
        //通过MenuItem得到SearchView
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //searchView = (SearchView)findViewById(R.id.search_books);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.setmBookList(bookList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                List<Book> list = DatabaseOP.getInstance().searchByNameOrEditorOrPublishing(s);
                Log.d("fuck","ddddddddd");
                adapter.setmBookList(list);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List<Book> list = DatabaseOP.getInstance().searchByNameOrEditorOrPublishing(s);
                Log.d("fuck","Open");
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(islabel){
            menu.findItem(R.id.search_books).setEnabled(false);
            menu.findItem(R.id.search_books).setVisible(false);
            menu.findItem(R.id.edit_label).setEnabled(true);
            menu.findItem(R.id.edit_label).setVisible(true);
        }else{
            menu.findItem(R.id.search_books).setEnabled(true);
            menu.findItem(R.id.search_books).setVisible(true);
            menu.findItem(R.id.edit_label).setEnabled(false);
            menu.findItem(R.id.edit_label).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sorting_books) {
            showSortingDialog();
            return true;
        }else if(id==R.id.bookisbn){
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            isbn=(LinearLayout)getLayoutInflater().inflate(R.layout.dialoglayout,null);
            dialog.setTitle("手动添加").setMessage("请输入书籍的ISBN码（10或13位数字)").setView(isbn);
            dialog.setPositiveButton("取消",new okclick() );
            dialog.setNegativeButton("添加",new cancleclick());
            dialog.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public class okclick implements DialogInterface.OnClickListener{
        @Override
        public  void onClick(DialogInterface dialog,int which){
            dialog.cancel();
        }
    }
    public  class  cancleclick implements  DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog,int which){
            dialog.cancel();
        }
    }

    /**
     * 显示选择排序的对话框
     */
    int sortingChoice;

    private void showSortingDialog() {
        final String[] items = {"标题", "作者", "出版社", "出版时间"};
        AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        myDialog.setTitle("排序依据");
        myDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sortingChoice = which;
            }
        });
        myDialog.setPositiveButton("排序", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sortingChoice != -1) {
                    if (which == 0)
                        bookList = DatabaseOP.sortByTitle();
                    if (which == 1)
                        bookList = DatabaseOP.sortByEditor();
                    if (which == 2)
                        bookList = DatabaseOP.sortByPublishing();
                    if (which == 3)
                        bookList = DatabaseOP.sortByDate();

                    adapter.setmBookList(bookList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        myDialog.show();
    }

    /**
     * 这个是设置左边的拉出框
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.all_books:
                //Toast.makeText(MainActivity.this, "所有书籍", Toast.LENGTH_SHORT).show();
                adapter.setmBookList(bookList);
                adapter.notifyDataSetChanged();
                islabel = false;
                invalidateOptionsMenu();
                break;
            case R.id.search:
                searchView.setIconified(false);
                break;
            case R.id.add_label:
                alert_edit(MainActivity.this);

                break;
            case R.id.setting:
                Intent intent1 = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.about_app:
                Intent intent2 = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent2);
                break;

            default:

                LinkedHashMap<Integer,String> labelMaps = settings.getLabelMaps();

                List<Book> nowBookList =  DatabaseOP.selectBookByLabel(id);
                adapter.setmBookList(nowBookList);
                adapter.notifyDataSetChanged();
                islabel = true;
                invalidateOptionsMenu();
                if(!labelMaps.isEmpty()){
                    Iterator it=labelMaps.keySet().iterator();
                    Object key;
                    Object value;
                    while(it.hasNext()){
                        key=it.next();
                        value=labelMaps.get(key);
                        Toast.makeText(MainActivity.this,key+":"+value,Toast.LENGTH_SHORT).show();
                    }
                }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alert_edit(MainActivity view){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("标签名")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        String label_name = et.getText().toString();
                        curIDofLabel++;//1
                        settings.setCurIDofLabel(curIDofLabel);
                        settings.labelMapsAddNew(curIDofLabel,label_name);
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                        op.exportSettings(settings);

                        navigationView.getMenu().add(R.id.g2,curIDofLabel,Menu.NONE,label_name);//需要获取id的话，id就等于1；
                        navigationView.setNavigationItemSelectedListener(MainActivity.this);

                    }
                }).setNegativeButton("取消",null).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookList = DatabaseOP.getInstance().selectALLBook();
        adapter.setmBookList(bookList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
