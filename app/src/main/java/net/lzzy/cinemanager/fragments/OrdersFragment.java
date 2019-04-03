package net.lzzy.cinemanager.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {
    public static final int MIN_DISTANCE = 100;
    private ListView lv;
    private View empty;
    private OrderFactory factory = OrderFactory.getInstance();
    private List<Order> orders;
    private Order order;
    private GenericAdapter<Order> adapter;
    private Button but;
    private float touchX1;
    private boolean isDelete = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, null);

        return view;
    }

    @Override
    protected void populate() {
        lv = find(R.id.activity_cinema_lv);
        empty = find(R.id.activity_cinema_tv_none);
        lv.setEmptyView(empty);
        orders = factory.get();
        adapter = new GenericAdapter<Order>(getActivity(), R.layout.order_item, orders) {
            @Override
            public void populate(ViewHolder viewHolder, Order order) {
                String location = String.valueOf(CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()));
                viewHolder.setTextView(R.id.add_order_edt_movie_name, order.getMovie())
                        .setTextView(R.id.order_item_tv_location,location);
                but = viewHolder.getView(R.id.cinema_item_btn);
                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("删除确认")
                                .setMessage("要删除订单吗？")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    isDelete = false;
                                    adapter.remove(order);
                                }).show();
                    }
                });
                int visibility = isDelete ? View.VISIBLE : View.GONE;
                but.setVisibility(visibility);

                viewHolder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                    @Override
                    public boolean handleTouch(MotionEvent event) {
                        slideToDelete(event, order, but);
                        return true;
                    }
                });


            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }
        };
            lv.setAdapter(adapter);
            if (order!=null){
                svae(order);
            }
    }

    private void slideToDelete(MotionEvent event, Order order, Button but) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float touchX2 = event.getX();
                if (touchX1 - touchX2 > MIN_DISTANCE) {
                    if (!isDelete) {
                        but.setVisibility(View.VISIBLE);
                        isDelete = true;
                    }

                } else {
                    if (but.isShown()) {
                        but.setVisibility(View.GONE);
                        isDelete = false;
                    } else {
                        clickOrder(order);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void clickOrder(Order order) {
        Cinema cinema = CinemaFactory.getInstance()
                .getById(order.getCinemaId().toString().toString());
        String content = "[" + order.getMovie() + "]" + order.getMovieTime() + "\n" + cinema + "票价" + order.getPrice() + "元";
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_qrcode, null);
        ImageView img = view.findViewById(R.id.dialog_qrcode_img);
        img.setImageBitmap(AppUtils.createQRCodeBitmap(content, 300, 300));
        new AlertDialog.Builder(getContext())
                .setView(view).show();
    }
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_order;
    }

    public void svae(Order order){
        adapter.add(order);
    }



    @Override
    public void search(String kw) {
        orders.clear();
        if (TextUtils.isEmpty(kw)) {
            orders.addAll(factory.get());
        }else {
            orders.addAll(factory.searchOrders(kw));
        }
        adapter.notifyDataSetChanged();

    }

}
