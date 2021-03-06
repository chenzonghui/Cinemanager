package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;
import net.lzzy.sqllib.GenericAdapter;

import java.util.List;

import javax.xml.transform.Transformer;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddCinemasFragment.OnFragmentInteractionListener {
    public static final String EXTRA_NEW_CINEMA = "new_cinema";
    private FragmentManager manager = getSupportFragmentManager();

    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SparseArray<String> titleArray = new SparseArray<>();
    private SparseArray<Fragment> fragments = new SparseArray<>();
    private boolean isDelete = false;

    private SearchView search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                if (fragment != null) {
                    if (fragment instanceof BaseFragment) {
                      ((BaseFragment) fragment).search(kw);
                    }
                }
                return false;
            }
        });
    }



    private void setTitleMenu() {
        titleArray.put(R.id.bar_title_tv_add_order, "添加影院");
        titleArray.put(R.id.bar_title_tv_view_cinema, "影院列表");
        titleArray.put(R.id.bar_title_tv_add_order, "添加订单");
        titleArray.put(R.id.bar_title_tv_add_order, "我的订单");
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);

        findViewById(R.id.bar_title_layout_menu).setOnClickListener(this);
        tvTitle = findViewById(R.id.bar_title_tv);
        tvTitle.setText("我的订单");
        search = findViewById(R.id.bar_title_sv);
        findViewById(R.id.bar_title_iv_menu).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_my_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = fragments.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragments.put(v.getId(), fragment);
            transaction.add(R.id.fragment_container,fragment);
        }
        for (Fragment f : manager.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();
    }
//        switch (v.getId()) {
//            case R.id.bar_title_iv_menu:
//                int visible = layoutMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
//
//                layoutMenu.setVisibility(visible);
//
//                break;
//            case R.id.bar_title_tv_my_order:
//                tvTitle.setText("我的订单");
//                manager.beginTransaction()
//                        .replace(R.id.fragment_container, new CinemasFragment())
//                        .commit();
//
//                break;
//            case R.id.bar_title_tv_add_order:
//
//                break;
//            case R.id.bar_title_tv_add_cinema:
//
//                break;
//            case R.id.bar_title_tv_view_cinema:
//
////                    manager.beginTransaction().add();
//                break;
//            case R.id.bar_title_tv_exit:
//                break;
//            default:
//                break;
//        }

    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_title_tv_add_cinema:
                break;
            case R.id.bar_title_tv_view_cinema:
                return new CinemasFragment();
            case R.id.bar_title_tv_add_order:
                break;
            case R.id.bar_title_tv_my_order:
                return new OrdersFragment();
            default:
                break;
        }
        return null;

    }

    @Override
    public void hideSearch() {
        search.setVisibility(View.INVISIBLE);
    }
    @Override
    public void cancelAddCinema() {
        Fragment addCinemaFragment=fragments.get(R.id.bar_title_tv_add_cinema);
        if (addCinemaFragment==null){
            return;
        }
        Fragment cinemasFragment=fragments.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){
            cinemasFragment=new CinemasFragment();
            fragments.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);

        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);
    }
    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemasFragment=fragments.get(R.id.bar_title_tv_add_cinema);
        if (addCinemasFragment==null){
            return;
        }
        Fragment cinemasFragment=fragments.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){

            cinemasFragment=new CinemasFragment(cinema);
            fragments.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);
        }else {
            ((CinemasFragment)cinemasFragment).save(cinema);

        }
        transaction.hide(addCinemasFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);


    }

}

