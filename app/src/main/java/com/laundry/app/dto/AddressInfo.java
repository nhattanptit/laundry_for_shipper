package com.laundry.app.dto;

import android.content.Context;

import com.laundry.app.dto.address.CityResponseDto;
import com.laundry.app.dto.address.DistrictResponseDto;
import com.laundry.app.dto.address.WardResponseDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddressInfo {

    private static final AddressInfo INSTANCE = new AddressInfo();
    private static final String JSON_FILE_NAME = "address_hn_hcm.json";
    private ArrayList<CityResponseDto> mCityList;

    private AddressInfo() {
    }

    public static AddressInfo getInstance() {
        return INSTANCE;
    }

    public void setCityList(ArrayList<CityResponseDto> list) {
        this.mCityList = list;
    }

    public ArrayList<CityResponseDto> getCityList() {
        return mCityList;
    }

    /**
     * Init address
     *
     * @param context Context
     */
    public void init(Context context) {
        try {
            JSONObject jsonObject = new JSONObject(getJsonFile(context, JSON_FILE_NAME));
            JSONArray data = jsonObject.getJSONArray("data");
            ArrayList<CityResponseDto> cityList = new ArrayList<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject city = data.getJSONObject(i);
                String cityId = city.getString("level1_id");
                String cityName = city.getString("name");
                String cityType = city.getString("type");
                JSONArray districtJson = city.getJSONArray("level2s");
                ArrayList<DistrictResponseDto> districtList = new ArrayList<>();
                for (int j = 0; j < districtJson.length(); j++) {
                    JSONObject district = districtJson.getJSONObject(j);
                    String districtId = district.getString("level2_id");
                    String districtName = district.getString("name");
                    String districtType = district.getString("type");
                    JSONArray wardJson = district.getJSONArray("level3s");
                    ArrayList<WardResponseDto> wardList = new ArrayList<>();
                    for (int m = 0; m < wardJson.length(); m++) {
                        JSONObject ward = wardJson.getJSONObject(m);
                        String wardId = ward.getString("level3_id");
                        String wardName = ward.getString("name");
                        String wardType = ward.getString("type");
                        wardList.add(new WardResponseDto(wardId, wardName, wardType));
                    }
                    districtList.add(new DistrictResponseDto(districtId, districtName, districtType, wardList));
                }
                cityList.add(new CityResponseDto(cityId, cityName, cityType, districtList));
            }
            setCityList(cityList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert json file to string
     *
     * @param context  Context
     * @param fileName File name
     * @return string address
     */
    private String getJsonFile(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
