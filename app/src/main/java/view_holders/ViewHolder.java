package view_holders;


import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sala7khaled.todo_auth.R;


public class ViewHolder extends RecyclerView.ViewHolder
{
    public TextView title,desc;
    public CheckBox checkBox;


    public ViewHolder(@NonNull View view) {
        super(view);
        // ViewHolder for the layout which gonna use into RecyclerView.
        title = view.findViewById(R.id.title_TV);
        desc = view.findViewById(R.id.desc_TV);
        checkBox = view.findViewById(R.id.checkBox);

    }
}
