package com.example.yuli.electriccard.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yuli.electriccard.Beans.User;
import com.example.yuli.electriccard.DataSource.LittleQDBOpenHelper;
import com.example.yuli.electriccard.R;

import java.util.ArrayList;

/**
 * Created by YULI on 2018/1/21.
 */

public class TelRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private final int userLayout = R.layout.item_tel_recycleview;
    private LayoutInflater inflater = null;
    private int currentIndex;

    public TelRecycleAdapter(Context context,ArrayList<User> userList) {
        super();
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(userLayout,null);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        String tel = ((User)userList.get(i)).getTel();
        String headPic = ((User)userList.get(i)).getHeadPic();
        if (headPic==null){
            ((UserViewHolder)viewHolder).ib_headPic.setBackgroundResource(R.mipmap.ic_launcher);
        }else {
            Bitmap bitmap = BitmapFactory.decodeFile(headPic);
            ((UserViewHolder)viewHolder).ib_headPic.setImageBitmap(bitmap);
        }
        currentIndex = i;

        ((UserViewHolder)viewHolder).tv_tel.setText(tel);
        ((UserViewHolder)viewHolder).ib_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LittleQDBOpenHelper littleQDBOpenHelper = new LittleQDBOpenHelper(context);
                littleQDBOpenHelper.delete("User",userList.get(currentIndex).getTel());
                TelRecycleAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_tel;
        public ImageButton ib_headPic;
        public ImageButton ib_del;

        public UserViewHolder(View itemView) {
            super(itemView);
            tv_tel = (TextView) itemView.findViewById(R.id.tv_item_tel);
            ib_headPic = (ImageButton)itemView.findViewById(R.id.imageBtn_item_headpic);
            ib_del = (ImageButton)itemView.findViewById(R.id.imageBtn_item_tel_del);
        }
    }
}
