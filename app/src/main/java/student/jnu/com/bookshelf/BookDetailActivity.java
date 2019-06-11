package student.jnu.com.bookshelf;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.sliding.SlidingActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class BookDetailActivity extends SlidingActivity{
    private Book mBook;
    @Override
    public void init(Bundle savedInstanceState) {
        //<init> function instead of onCreate
        Intent intent = getIntent();
        mBook = (Book) intent.getSerializableExtra("book_object");
        setTitle(mBook.getName());

        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        setContent(R.layout.activity_book_detail);

        setHeaderContent(R.layout.activity_book_detail_header);
        setFab(
                getResources().getColor(R.color.colorAccent),
                R.drawable.ic_edit,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(BookDetailActivity.this,BookEditActivity.class);
                        intent1.putExtra("book_object",mBook);
                        startActivity(intent1);
                        finish();
                    }
                }
        );

        setHeader();
        setBookDetail();
    }

    private void setBookDetail() {
        TextView book_author = (TextView)findViewById(R.id.book_info_author_content);
        book_author.setText(mBook.getEditor());

        TextView book_translator = (TextView)findViewById(R.id.book_info_translator_content);
        book_translator.setText(mBook.getTranslator());

        TextView book_publisher = (TextView)findViewById(R.id.book_info_publisher_content);
        book_publisher.setText(mBook.getPublishing());

        TextView book_pubtime = (TextView)findViewById(R.id.book_info_pubtime_content);
        book_pubtime.setText(mBook.getDate());

        TextView book_isbn = (TextView)findViewById(R.id.book_info_isbn_content);
        book_isbn.setText(mBook.getISBN());

        TextView book_reading_status = (TextView)findViewById(R.id.book_detail_reading_status_content);
        CSVOP op = new CSVOP();
        Settings settings = op.importSettings();
        String[] status = settings.getReadState();
        mBook.setReadState(0);
        book_reading_status.setText(status[mBook.getReadState()] );

        TextView book_bookshelf = (TextView)findViewById(R.id.book_detail_bookshelf_content);
        book_bookshelf.setText(mBook.getShelf());

        TextView book_notes = (TextView)findViewById(R.id.book_detail_notes_content);
        book_notes.setText(mBook.getNotes());

        TextView book_labels = (TextView)findViewById(R.id.book_detail_labels_content);
        book_labels.setText(mBook.getLabels());

        TextView book_website = (TextView)findViewById(R.id.book_detail_website_content);
        book_website.setText(mBook.getOuputURL());
    }

    private void setHeader() {
        @SuppressLint("ResourceType")
        CSVOP op = new CSVOP();
        Settings settings = op.importSettings();
        String picPath = settings.getPictrueDir()+"/"+mBook.getISBN()+".bmp";
        Bitmap bitmap = null;
        try
        {
            File file = new File(picPath);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(picPath);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            // TODO: handle exception
        }
        ImageView bookCover = (ImageView) findViewById(R.id.book_detail_cover);
        bookCover.setImageBitmap(bitmap);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookdetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.book_detail_menu_delete:
                DatabaseOP.getInstance().deleteBook(mBook);
//                Toast.makeText(BookDetailActivity.this,
//                        "删除这本书", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
