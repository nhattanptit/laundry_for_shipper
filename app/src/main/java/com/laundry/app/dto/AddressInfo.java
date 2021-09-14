package com.laundry.app.dto;

import android.content.Context;
import android.text.TextUtils;

import com.laundry.app.R;
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

    private ArrayList<DistrictResponseDto> getDistrictByCityId(String cityId) {
        ArrayList<DistrictResponseDto> districtList = new ArrayList<>();
        for (CityResponseDto dto : getCityList()) {
            if (TextUtils.equals(cityId, dto.level1Id)) {
                districtList.addAll(dto.level2s);
                break;
            }
        }
        return districtList;
    }

    private ArrayList<WardResponseDto> getWarByDistrictId(String cityId, String districtId) {
        ArrayList<DistrictResponseDto> districtList = getDistrictByCityId(cityId);
        ArrayList<WardResponseDto> list = new ArrayList<>();
        for (DistrictResponseDto dto : districtList) {
            if (TextUtils.equals(districtId, dto.level2Id)) {
                list.addAll(dto.level3s);
                break;
            }
        }
        return list;
    }

    public String getCityNameById(String cityId) {
        for (CityResponseDto dto : getCityList()) {
            if (TextUtils.equals(cityId, dto.level1Id)) {
                return dto.name;
            }
        }
        return "";
    }

    public String getDistrictNameById(String cityId, String districtId) {
        ArrayList<DistrictResponseDto> list = getDistrictByCityId(cityId);

        for (DistrictResponseDto dto : list) {
            if (TextUtils.equals(districtId, dto.level2Id)) {
                return dto.name;
            }
        }
        return "";
    }

    public String getWardNameById(String cityId, String districtId, String wardId) {
        ArrayList<WardResponseDto> list = getWarByDistrictId(cityId, districtId);

        for (WardResponseDto dto : list) {
            if (TextUtils.equals(wardId, dto.level3Id)) {
                return dto.name;
            }
        }
        return "";
    }

    /**
     * Get address string
     * @param context
     * @param address
     * @param city
     * @param district
     * @param ward
     * @return
     */
    public String getAddressStr(Context context, String address, String city, String district, String ward) {

        return String.format(context.getString(R.string.address_format),
                address,
                getWardNameById(city, district, ward),
                getDistrictNameById(city, district),
                getCityNameById(city));

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
