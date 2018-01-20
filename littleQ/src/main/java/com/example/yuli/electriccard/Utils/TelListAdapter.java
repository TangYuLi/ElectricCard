package com.example.yuli.electriccard.Utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yuli.electriccard.R;

import java.util.List;

/**
 * Created by YULI on 2017/9/4.
 */

public class TelListAdapter extends RecyclerView.Adapter {

    //    事件监听接口是为了降低耦合度，如果行为实现方式改变了，无需改动该类的内容
    //    而且这些事件监听是设置在item里面的元素控件
    //    static对于修饰内部类是多余的
    public interface OnRecycleViewListener {
        //public对于修饰接口方法也是多余的
        void onItemDelClick(int pos);

        //Recycle没有为我们设置onItemClick事件，ListView有
        //所以我们设置在Adapter适配器上
        void onItemClick(int pos);
    }

    public OnRecycleViewListener onRecycleViewListener;

    public void setOnRecycleViewListener(OnRecycleViewListener onRecycleViewListener) {
        this.onRecycleViewListener = onRecycleViewListener;
    }

    //    RecyclerView.ViewHolder实现类
    //一般提醒该类没有defalut constructor时，
    // 表明自己要创建一个构造方法并在里面调用超类的构造方法
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_tel;
        public ImageButton ib_tel_del;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_tel = (TextView) itemView.findViewById(R.id.tv_tel);
            ib_tel_del = (ImageButton) itemView.findViewById(R.id.ib_tel_del);
            ib_tel_del.setOnClickListener(this);
            itemView.setOnClickListener(this);
            ib_tel_del.setTag("onClickIB_Tel_Del");
            itemView.setTag("onClickItemView");
        }

        @Override
        public void onClick(View view) {
            switch (""+view.getTag()){
                case "onClickIB_Tel_Del":
                    onRecycleViewListener.onItemDelClick(getPosition());
                    break;
                case "onClickItemView":
                    onRecycleViewListener.onItemClick(getPosition());
                    break;
                default:
                    break;
            }
        }
    }

    private static final int layout = R.layout.rv_tellist_item;
    public List<Person> personList;
    public static final String TAG = TelListAdapter.class.getSimpleName();

    public TelListAdapter(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.e(TAG, "viewGroup context:" + viewGroup.getContext().hashCode());
        Log.e(TAG, "onCreateViewHolder:i=" + i);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder)viewHolder;
        Person person = null;
        person = personList.get(i);
        holder.tv_tel.setText(person.getTel());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (personList!=null) count = personList.size();
        return count;
    }
}
