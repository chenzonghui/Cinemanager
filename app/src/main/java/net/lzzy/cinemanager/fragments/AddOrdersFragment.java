package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CancellationException;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    private AddCinemasFragment.OnFragmentInteractionListener listener;
    private AddCinemasFragment orderListener;
    private EditText edtName;
    private TextView tvDate;
    private Spinner spOrder;
    private EditText edtPrice;
    private ImageView imgQRCode;
    List<Cinema> cinemas;
    private CustomDatePicker datePicker;

    public AddOrdersFragment(){}

    @Override
    protected void populate() {
        listener.hideSearch();
        edtName = find(R.id.add_order_edt_movie_name);
        tvDate = find(R.id.add_order_select_time);
        spOrder = find(R.id.add_order_sp);
        edtPrice = find(R.id.add_order_edt_movie_price);
        imgQRCode = find(R.id.add_order_rq_code);
        addListener();
        initDatePicker();
        find(R.id.add_order_layout_time).setOnClickListener(v -> datePicker.show(tvDate.getText().toString()));


    }
    /**添加数据*/
    private void addListener() {
        cinemas= CinemaFactory.getInstance().get();
        //获取cinema中的地址
        spOrder.setAdapter(new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_spinner_dropdown_item, cinemas));

        find(R.id.add_order_btn_cancel)
                .setOnClickListener(v -> {orderListener.cancelAddOrder(); });
        //点击保存，保存数据
        //region
        find(R.id.activity_cinema_dialog_yes_btn).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            String strPrice=edtPrice.getText().toString();
            if (TextUtils.isEmpty(name)||TextUtils.isEmpty(strPrice)){
                Toast.makeText(getActivity(),"信息不完整，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }
            float price;
            try{
                price=Float.parseFloat(strPrice);
            }catch (NumberFormatException e){
                Toast.makeText(getActivity(),"票价格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }

            Order order=new Order();
            Cinema cinema=cinemas.get(spOrder.getSelectedItemPosition());
            order.setCinemaId(cinema.getId());
            order.setMovie(name);
            order.setPrice(price);
            order.setMovieTime(tvDate.getText().toString());
            orderListener.aveOrder(order);
            edtName.setText("");
            edtPrice.setText("");

        });
        //endregion
        //生成二维码
        //region
        find(R.id.dialog_qrcode_img).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            String price=edtPrice.getText().toString();
            String location=spOrder.getSelectedItem().toString();
            String time=tvDate.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price)) {
                Toast.makeText(getActivity(),"信息不完整",Toast.LENGTH_SHORT).show();
                return;
            }
            String content="["+name+"]"+time+"\n"+location+"票价"+price+"元";
            imgQRCode.setImageBitmap(AppUtils.createQRCodeBitmap(content,200,200));
        });
        imgQRCode.setOnLongClickListener(v -> {
            Bitmap bitmap=((BitmapDrawable)imgQRCode.getDrawable()).getBitmap();
            Toast.makeText(getActivity(),AppUtils.readQRCode(bitmap),Toast.LENGTH_SHORT).show();
            return true;
        });
        //endregion

    }


    /**日期*/
    public void initDatePicker() {
        //region
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now=sdf.format(new Date());
        tvDate.setText(now);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,1);
        String end=sdf.format(calendar.getTime());
        datePicker=new CustomDatePicker(getActivity(), s -> tvDate.setText(s),now,end);
        datePicker.showSpecificTime(true);
        datePicker.setIsLoop(true);
        //endregion
    }



    @Override
    public int getLayoutRes() {
        return R.id.bar_title_tv_add_order;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onResume() {
        super.onResume();
        listener.hideSearch();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddCinemasFragment.OnFragmentInteractionListener) context;
        }catch (CancellationException e){
            throw  new ClassCastException(context.toString()+"必须实现OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    public interface OnOrderCreatedListener{
        /**取消保存数据*/
        void cancelAddOrder();

        /**保存数据
         * @param order rule order
         */
        void saveOrder(Order order);
    }
}


