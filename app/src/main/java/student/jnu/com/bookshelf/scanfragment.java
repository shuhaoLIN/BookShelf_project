package student.jnu.com.bookshelf;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by 91214 on 2019/6/5.
 */

public class scanfragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final String TAG = "BatchScanFragment";
    private static final String FLASH_STATE = "FLASH_STATE";
    public  static  String bookinfo;
    public static ZXingScannerView mScannerView;
    public static boolean mFlash = false;
    public  static boolean isExist=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multifragmentlayout, container, false);
        setHasOptionsMenu(true);

        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.scan);
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(mFlash);
        mScannerView.setResultHandler(this);
        viewGroup.addView(mScannerView);
        return view;
    }




    @Override
    public void handleResult(final Result rawResult) {
        Log.i(TAG, "ScanResult Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());
        bookinfo=rawResult.getText();
        mScannerView.stopCamera();
        new Thread(){
            @Override
            public  void run(){
                super.run();
                BookJsonOP bookJsonOP=new BookJsonOP(multiHandler,bookinfo);
                bookJsonOP.getBookInfo();}

        }.start();


    }

    private Handler multiHandler = new Handler(){
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
                Book book1=(Book)bundle.getSerializable("book");
                //Toast.makeText(getApplicationContext(),book.getName(),Toast.LENGTH_SHORT).show();
                Log.i("newbookname",book1.getName());
                isExist=false;
                for( Book book :MainActivity.addbook){
                  if( book.getISBN()==book1.getISBN()) {
                      isExist=true;
                  }
                }
                for(Book book: DatabaseOP.getInstance().selectALLBook()) {
                    if(book.getISBN()==book1.getISBN()){
                        isExist=true;
                    }

                }
                if(!isExist){
                MainActivity.addbook.add(book1);
                MainActivity.bookAdapter.notifyDataSetChanged();
                MainActivity.bookList.add(book1);
                MainActivity.adapter.notifyDataSetChanged();
                ScannerActivity.titles[1]="已添加"+"("+MainActivity.addbook.size()+")";}
               mScannerView.resumeCameraPreview(scanfragment.this);
               //getActivity().finish();
                mScannerView.startCamera();

            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void onResume() {
        super.onResume();

        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(mFlash);
        mScannerView.resumeCameraPreview(scanfragment.this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();

        mScannerView.stopCamera();
    }
}
