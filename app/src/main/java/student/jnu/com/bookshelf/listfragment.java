package student.jnu.com.bookshelf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 91214 on 2019/6/5.
 */

public class listfragment extends Fragment {
    private List<Book> bookList = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multilistlayout, container, false);
        setHasOptionsMenu(true);
        RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recycler_view1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView3.setLayoutManager(layoutManager);

        recyclerView3.setAdapter(MainActivity.bookAdapter);
        MainActivity.bookAdapter.notifyDataSetChanged();
        return  view;
    }



}