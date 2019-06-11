package student.jnu.com.bookshelf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private List<Book> mBookList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View bookView;//子项的外部holder，用于实现点击
        ImageView bookImage;
        TextView bookName;
        TextView bookEditor;
        TextView bookPublishing;
        TextView bookDate;

        public ViewHolder(View view){
            super(view);
            bookView = view;
            bookImage = (ImageView) view.findViewById(R.id.book_image);
            bookName = (TextView) view.findViewById(R.id.book_name);
            bookEditor = (TextView) view.findViewById(R.id.book_editor);
            bookPublishing = (TextView) view.findViewById(R.id.book_publishing);
            bookDate = (TextView) view.findViewById(R.id.book_date);
        }
    }

    public BookAdapter(Context context,List<Book> bookList){
        this.context = context;
        this.mBookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item,parent,false);
        final ViewHolder holder = new ViewHolder(view); //Must 为final，否则无法设置点击事件
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Book book = mBookList.get(position);
                Intent intent = new Intent(parent.getContext(),BookDetailActivity.class);
                intent.putExtra("book_object",book);
                intent.putExtra("activity","1");
                context.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mBookList.get(position);

        CSVOP op = new CSVOP();
        Settings settings = op.importSettings();
        String picPath = settings.getPictrueDir()+"/"+book.getISBN()+".bmp";
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
        holder.bookImage.setImageBitmap(bitmap);
        holder.bookName.setText(book.getName());

        String editor = book.getEditor();
        holder.bookEditor.setText(editor);
//        if (editor.length() <=8){
//            holder.bookEditor.setText(editor);
//        }else {
//            editor = editor.substring(8) + "..";
//            holder.bookEditor.setText(editor);
//        }
        holder.bookPublishing.setText(book.getPublishing());
        holder.bookDate.setText(book.getDate());
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
        //How many items in recycler_view
    }

    public void setmBookList(List<Book> bookList){
        this.mBookList = bookList;
    }
}
