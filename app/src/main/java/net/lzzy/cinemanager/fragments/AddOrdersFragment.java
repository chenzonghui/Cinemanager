package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;

import java.util.concurrent.CancellationException;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    private AddCinemasFragment.OnFragmentInteractionListener listener;

    public AddOrdersFragment(){}

    @Override
    protected void populate() {

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

}
