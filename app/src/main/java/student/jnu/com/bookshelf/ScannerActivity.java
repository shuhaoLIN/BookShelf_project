package student.jnu.com.bookshelf;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.List;

public class ScannerActivity extends AppCompatActivity {
    private TabLayout tablayout;
    public static boolean mFlash = false;
    public  static List<Book> mbook;
    private ViewPager viewPager;
    public  static  String is;
    public  boolean fragmentchange=true;
    public static String[] titles = {"                     扫描                     ", "                      已添加                     "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        viewPager.setAdapter(myPagerAdapter);

        tablayout.setupWithViewPager(viewPager);

        LinearLayout linearLayout = (LinearLayout) tablayout.getChildAt(0);

        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("toolbar");
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }



    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.multiscanmenu, menu);


        return true;

    }

    private Handler multiHandl = new Handler(){
        /**
         * handleMessage接收消息后进行相应的处理
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Bundle bundle=new Bundle();
                bundle=msg.getData();
                Book book=(Book)bundle.getSerializable("book");

                Log.i("book",book.getName());
                ScannerActivity.mbook.add(book);

                Log.i("booklist",String.valueOf(ScannerActivity.mbook.size()));
                //ScannerView.resumeCameraPreview(scanfragment.this);



            }
        }
    };
   @Override
   public boolean  onPrepareOptionsMenu(Menu menu){
       if(mFlash){
           menu.findItem(R.id.flash1).setIcon(R.drawable.ic_flash_on);
       }
       else menu.findItem(R.id.flash1).setIcon(R.drawable.ic_flash_off);

       if(fragmentchange){
           menu.findItem(R.id.flash1).setVisible(true);

       }
      else{
           menu.findItem(R.id.flash1).setVisible(false);
       }

       return true;
   }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
            case R.id.flash1:
                mFlash = !mFlash;
                if (mFlash) {

                    item.setIcon(R.drawable.ic_flash_on);
                } else {

                }
                scanfragment.mScannerView.setFlash(mFlash);
                break;

            case R.id.bookdone:

                for(Book book:MainActivity.addbook){
                    DatabaseOP.getInstance().insertBook(book);
                }

                 Intent intent=new Intent(ScannerActivity.this,MainActivity.class);

                 startActivity(intent);
                break;
            case R.id.handbook:




        }
        return super.onOptionsItemSelected(item);
    }


public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            fragmentchange=true;
            scanfragment scan=new scanfragment();

            return scan;
        }
        else {

            listfragment list=new listfragment();
            fragmentchange=false;
            return list;
        }

    }
    @Override
    public int getCount() {
        return 2;
    }
}}
