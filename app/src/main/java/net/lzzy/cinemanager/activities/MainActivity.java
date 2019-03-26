package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.simpledatepicker.CustomDatePicker;
import net.lzzy.sqllib.GenericAdapter;

import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_NEW_CINEMA = "new_cinema";
    private FragmentManager manager = getSupportFragmentManager();
    public  static  final  float MIN_DISTANCE=100;
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SparseArray<String> titleArray = new SparseArray<>();
    private SearchView searchView;
    private ListView listView;
    private LinearLayout addOrderView;
    private Spinner spinner;
    private ImageView imgQRCode;
    private TextView tvDate;
    private EditText edtMovieName;
    private EditText edtPrice;
    private CustomDatePicker picker;
    private float touchX1;
    private boolean isDelete=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitleMenu();
        }

    private void setTitleMenu(){
        titleArray.put(R.id.bar_title_tv_add_order,"添加影院");
        titleArray.put(R.id.bar_title_tv_view_cinema,"影院列表");
        titleArray.put(R.id.bar_title_tv_add_order,"添加订单");
        titleArray.put(R.id.bar_title_tv_add_order,"我的订单");
        layoutMenu=findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_layout_menu).setOnClickListener(this);
        tvTitle=findViewById(R.id.bar_title_tv);
        tvTitle.setText("我的订单");
        searchView=findViewById(R.id.bar_title_sv);
        findViewById(R.id.bar_title_iv_menu).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_my_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bar_title_iv_menu:
                int visible=layoutMenu.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE;

                layoutMenu.setVisibility(visible);

                break;
            case R.id.bar_title_tv_my_order:
                tvTitle.setText("我的订单");
                manager.beginTransaction()
                        .replace(R.id.fragment_container,new CinemasFragment())
                        .commit();

                break;
            case R.id.bar_title_tv_add_order:

                break;
            case R.id.bar_title_tv_add_cinema:

                break;
            case R.id.bar_title_tv_view_cinema:
                manager.beginTransaction()
                        .replace(R.id.fragment_orders,new CinemasFragment())
                        .commit();
                break;
            case R.id.bar_title_tv_exit:
                break;
            default:
                break;
        }
    }
}
