package student.jnu.com.bookshelf;

import Decoder.BASE64Decoder;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.*;
import java.util.Date;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


/**
 * Created by lenovo on 2019/5/30.
 */
public class BookJsonOP {


    Handler handler;
    StringBuilder text;
    Book book;
    BASE64Decoder decoder = new BASE64Decoder();
    String url_s;
    final String SAVE_PATH = "/storage/emulated/0/bookshelf/";

    public BookJsonOP(Handler handler, String ISBN) {
        this.handler = handler;
        book = new Book("aa",1,"123");
        this.url_s = "http://119.29.3.47:9001/book/worm/isbn?isbn="+ISBN;
    }

    public void getBookInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(url_s);
                    System.out.println("开始执行啦");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置连接属性，不喜欢的话可以直接默认
                    conn.setConnectTimeout(5000); //设置连接最长时间
                    conn.setUseCaches(false); //数据不多时可以不使用缓存

                    //这里连接了
                    conn.connect();
                    //这里才真正获取到数据
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader input = new InputStreamReader(inputStream);
                    BufferedReader buffer = new BufferedReader(input);
                    if (conn.getResponseCode() == 200) {
                        //返回两百意味着ok
                        String inputLine;
                        StringBuffer resultData = new StringBuffer();
                        while ((inputLine = buffer.readLine()) != null) {
                            resultData.append(inputLine);
                        }
                        System.out.println(resultData);
                        JSONObject jsonObject = new JSONObject(resultData.toString());

                        JSONObject bookJson = jsonObject.getJSONObject("data");
                        book.setISBN(bookJson.getString("isbn"));
                        book.setPublishing(bookJson.getString("publisher"));
                        book.setAddTime(Calendar.getInstance());
                        book.setDate(bookJson.getString("publishingTime"));

//                        String[] str = bookJson.getString("author").split("；");
//
//                        book.setEditor(str[0]);
//                        book.setTranslator(str[1]);
                        book.setEditor(bookJson.getString("author"));
                        String image  = bookJson.getString("image");
                        base64StringToImage(image, book.getISBN());

                        book.setImageId("src/main/res/drawable/"+book.getISBN()+".bmp");
                        book.setName(bookJson.getString("name"));
                        book.setNotes(bookJson.getString("introduction"));
                        book.setOuputURL(url_s);
                        //这个不知道填什么book.setOutput();
                        book.setTitle(bookJson.getString("title"));


                        Message msg = handler.obtainMessage();
                        msg.what=1;
                        msg.arg1=1;
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("book",book);
                        msg.setData(bundle);
                        msg.sendToTarget();
                        System.out.println(book.getISBN());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void base64StringToImage(String base64String, String ISBN) {
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            //ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            //File file = new File(SAVE_PATH,ISBN+".bmp");
//            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/bookshelf/",ISBN+".bmp");
//            file.createNewFile();
//            file.setWritable(true);
//            file.setReadable(true);

            //创建文件夹，再创建文件
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))//判断是否有读写权限
            {
                File root = new File(Environment.getExternalStorageDirectory().getPath() + "/bookshelf/")
                ;//构造函数参数为路径
                if (!root.exists()) {
                    root.mkdirs();
                }
            }
            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/bookshelf/",ISBN+".bmp");
            if(!file.exists()){
                file.createNewFile();
            }
            file.setWritable(true);
            file.setReadable(true);
            //ImageIO.write(bi1, "jpg", f1);
            PrintStream ps = new PrintStream(new FileOutputStream(file, true));
            ps.write(bytes1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//                        text = resultData.toString();
//                        parseJson(text);//获取
//                        handler.sendEmptyMessage(1);
//                        Message msg = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("name","你好啊！");
//
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
//sendMessage（）允许处理Message对象（可以包含数据）
//sendEmptyMessage（int what）只能放数据，类似于区分0-1

//                        Log.v("out--------->",text);