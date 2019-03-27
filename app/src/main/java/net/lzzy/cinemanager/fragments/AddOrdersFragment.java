package net.lzzy.cinemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    public AddOrdersFragment(){}

    @Override
    protected void populate() {
        TextView tv = find(R.id.bar_title_tv_my_order);
    }

    @Override
    public int getLayoutRes() {
        return R.id.bar_title_tv_add_order;
    }


}
