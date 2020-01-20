package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sala7khaled.todo_auth.R;

import java.util.ArrayList;

import models.ListItem;
import view_holders.ViewHolder;

public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private ArrayList<ListItem> listItemsArrayList;
    private CallBack callBack;

    public MyAdapter(Context context, ArrayList<ListItem> listItemsArrayList, CallBack callBack) {
        this.context = context;
        this.listItemsArrayList = listItemsArrayList;
        this.callBack = callBack;
    }

    public void addItem(ListItem item) {
        listItemsArrayList.add(item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int view_type) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final ListItem listItem = listItemsArrayList.get(position);
        final String todoText = listItem.getTodo();
        final String descText = listItem.getDesc();
        final boolean checkbox_ = listItem.isCheckbox();

        viewHolder.title.setText(todoText);

        if (listItem.isCheckbox()) viewHolder.checkBox.setChecked(true);
        else viewHolder.checkBox.setChecked(false);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callBack.onItemClick(position);
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                boolean checked = viewHolder.checkBox.isChecked();
                callBack.onChecked(position, checked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItemsArrayList.size();
    }

    public interface CallBack {
        void onItemClick(int position);
        void onChecked(int position, boolean checked);
    }
}