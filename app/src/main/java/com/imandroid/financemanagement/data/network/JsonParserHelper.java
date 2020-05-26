package com.imandroid.financemanagement.data.network;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParserHelper {

    public static VersionDTO parseGenresJson(String json){
            VersionDTO versionDTO = null;
        try {
            JSONObject response = new JSONObject(json);
            String version_name = response.getString("last_version_name");
            String version_number = response.getString("last_version_number");

            versionDTO = new VersionDTO(version_number,version_name);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return versionDTO;
    }





}
