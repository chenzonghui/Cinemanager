package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;

import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String provinece = "广西壮族自治区";
    private String city = "柳州市";
    private String area = "鱼峰区";
    private TextView tvArea;
    private  EditText etnName;
    private OnFragmentInteractionListener listener;
    private OnCityItemClickListener cinemalistenner;


    @Override
    protected void populate() {
        listener.hideSearch();
        etnName= find(R.id.activity_cinema_dialog_edt);
        tvArea = find(R.id.activity_cinema_dialog_area);


        find(R.id.activity_cinema_dialog_yes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etnName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cinema cinema = new Cinema();
                cinema.setArea(area);
                cinema.setCity(city);
                cinema.setName(name);
                cinema.setProvince(provinece);
                cinema.setLocation(tvArea.getText().toString());
                //adapter.add(cinema);
                etnName.setText("");



                find(R.id.activity_cinema_dialog_layout_area).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JDCityPicker cityPicker = new JDCityPicker();
                        cityPicker.init(getActivity());
                        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                            @Override
                            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                                AddCinemasFragment.this.provinece = province.getName();
                                AddCinemasFragment.this.city = city.getName();
                                AddCinemasFragment.this.area = district.getName();
                                String loc = province.getName() + city.getName() + district.getName();
                                tvArea.setText(loc);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                        cityPicker.showCityPicker();
                    }


                });
            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_cinemas;
    }

    @Override
    public void search(String kw) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        }catch (CancellationException e){
            throw  new ClassCastException(context.toString()+"必须实现OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener{
        void cancelAddCinema();

        void  hideSearch();

        void saveCinema(Cinema cinema);
    }


}