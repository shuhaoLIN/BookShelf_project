package student.jnu.com.bookshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SingleAddActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private static final String TAG = "BatchScanFragment";
    private static final String FLASH_STATE = "FLASH_STATE";
     public  static String data;
     public static String isbn;
    private ZXingScannerView mScannerView;
    public static boolean mFlash = false;
    LinearLayout isbn1;
    public static boolean isExist=false;
   public  EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       editText=(EditText)findViewById(R.id.edit_01);
        setSupportActionBar(toolbar);

        toolbar.setTitle("toolbar");
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }



        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.singlescanframe);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(mFlash);
        mScannerView.setResultHandler(this);
        viewGroup.addView(mScannerView);
        if (savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
        } else {
            mFlash = false;
        }

    }

    private Handler mHandler = new Handler(){
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
                Toast.makeText(getApplicationContext(),book.getName(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SingleAddActivity.this,BookEditActivity.class);
                intent.putExtra("book_object",book);
                startActivity(intent);
            }
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.singleadd_menu, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
            case R.id.flash:
                mFlash = !mFlash;
                if (mFlash) {

                    item.setIcon(R.drawable.ic_flash_on);
                } else {

                }
                mScannerView.setFlash(mFlash);
                break;
            case R.id.bookisbn:
                //mScannerView.stopCamera();
                AlertDialog.Builder dialog=new AlertDialog.Builder(SingleAddActivity.this);
                isbn1=(LinearLayout)getLayoutInflater().inflate(R.layout.dialoglayout,null);
                dialog.setTitle("手动添加").setMessage("请输入书籍的ISBN码（10或13位数字)").setView(isbn1);
                dialog.setPositiveButton("取消",new okclick() );
                dialog.setNegativeButton("添加",new cancleclick());
            dialog.create();

            dialog.show();

            break;

            case R.id.handbook:
                mScannerView.stopCamera();
                /*
                Book book=new Book("1",2,"4564654");
                Intent intent1 = new Intent(SingleAddActivity.this, BookEditActivity.class);
                intent1.putExtra("book_object",book);

                startActivity(intent1);
                finish();*//*
                new Thread(){
                    @Override
                    public  void run(){
                        super.run();
                        BookJsonOP bookJsonOP=new BookJsonOP(mHandler,"9787111547426");
                        bookJsonOP.getBookInfo();}

                }.start();*/
  Intent intent=new Intent(SingleAddActivity.this,BookEditActivity.class);
  Book book=new Book("",1,"");
  book.setDate("");
  book.setNotes("");
  book.setEditor("");
  book.setTranslator("");
  book.setDate("");
  book.setPublishing("");
  book.setName("");

                intent.putExtra("book_object",book);
  startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.i(TAG, "ScanResult Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());
        Toast.makeText(getApplicationContext(),rawResult.getText(),Toast.LENGTH_SHORT).show();
        data=rawResult.getText();
         new Thread(){
             @Override
             public  void run(){
                 super.run();
                 BookJsonOP bookJsonOP=new BookJsonOP(mHandler,data);
                 bookJsonOP.getBookInfo();}

         }.start();


        }





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.stopCamera();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(mFlash);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public class okclick implements DialogInterface.OnClickListener{
        @Override
        public  void onClick(DialogInterface dialog,int which){
            mScannerView.stopCamera();

            dialog.cancel();
            mScannerView.startCamera();
        }
    }
    public  class  cancleclick implements  DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog,int which){
            mScannerView.stopCamera();
            //isbn= editText.getText().toString();
            editText=(EditText)isbn1.findViewById(R.id.edit_01);

             final String s=editText.getText().toString();
  Log.i("ss",s);
            new Thread(){
                @Override
                public  void run(){
                    super.run();

                    BookJsonOP bookJsonOP=new BookJsonOP(mHandler,s);
                    bookJsonOP.getBookInfo();}

            }.start();
            dialog.cancel();
            mScannerView.startCamera();
        }
    }
    private  void addbook(String isbn){


    }
}
