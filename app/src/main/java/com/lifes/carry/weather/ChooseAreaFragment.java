package com.lifes.carry.weather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lifes.carry.weather.db.City;
import com.lifes.carry.weather.db.County;
import com.lifes.carry.weather.db.Province;
import com.lifes.carry.weather.util.HttpUtil;
import com.lifes.carry.weather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Administrator
 * @time 2018-02-26 22:25
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ChooseAreaFragment extends android.support.v4.app.Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVIE_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> dataList = new ArrayList<>();
    //    省列表
    private List<Province> mProvinceList;
    //    市列表
    private  List<City> mCityList;
    //    县列表
    private List<County> mCountyList;
    //    选中的省
    private  Province selectedProvince;
    //    选中的市
    private City selectedCity;
    //    当前选中的级别
    private int currentLevel;

    private static final String TAG = "ChooseAreaFragment";


    @Override
//    控件的初始化
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,true);
        titleText= (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.title_bt);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        list列表的 选择及处理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = mProvinceList.get(position);
                    queryCitys();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = mCityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVIE_COUNTY){
                    String weatherId = mCountyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    Log.e(TAG, "onItemClick"+weatherId);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
//        返回按钮的判断（当前是县返回市  市返回省）
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVIE_COUNTY){
                    queryCitys();
                }else if (currentLevel == LEVEL_CITY)
                    queryProvinces();
            }
        });
        queryProvinces();
    }
//查询所有省，优先查询数据库数据 没有就去调用服务去联网查询  省级列表是没有返回按钮的
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
//
//       litepal.xml
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size()>0){
            dataList.clear();
            for (Province province :mProvinceList ) {
            	dataList.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;

        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCitys() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        mCityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (mCityList.size()>0){
            dataList.clear();
            for (City city  :mCityList ) {
            	dataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            //              修改选择状态
            currentLevel = LEVEL_CITY;
        }else{
            int proviceCode = selectedProvince.getprovinceCode();
            String address = "http://guolin.tech/api/china/"+proviceCode;
            queryFromServer(address,"city");

        }

    }

    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        mCountyList=DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if (mCountyList.size()>0){
            dataList.clear();
            for (County county :mCountyList ) {
                dataList.add(county.getCountyName());
                Log.i(TAG, "queryCounties++++++++++++++++++++"+county.getWeatherId());
                Log.i(TAG, "queryCounties"+county.getId());
                Log.i(TAG, "queryCounties"+county.getCityId());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVIE_COUNTY;
        }else{
            int provicecode=selectedProvince.getprovinceCode();
            int citycode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provicecode+"/"+citycode;
            Log.i(TAG, "queryCounties "+address);
            queryFromServer(address,"county");
        }


    }

//联网去查询对应的区域的 数据
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText =response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                    Log.i(TAG, "输出结果"+result);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCitys();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
    /*
        进度对话框
     */
    private void showProgressDialog(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /*
    /   关闭进度对话框
     */
    private void closeProgressDialog(){
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
}
