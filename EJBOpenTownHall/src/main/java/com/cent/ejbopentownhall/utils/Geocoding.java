/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cent.ejbopentownhall.utils;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author cent
 */
public class Geocoding {
    
    private static final String GoogleGeocodingBaseUrl = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GoogleKey = "<mykey>";
    
    public String findCoordinates(String address){
        
        Client client = ClientBuilder.newClient();
        //https://maps.googleapis.com/maps/api/geocode/json
        String serviceResult = ClientBuilder.newClient().target(getBaseURI()).queryParam("address", address).queryParam("key", GoogleKey).request().get(String.class);
                
        JSONObject JSONResponse = new JSONObject(serviceResult); 
        System.out.println("STATUS"+JSONResponse.getString("status"));
        //CONTROLLO status -> ok | ZERO_RESULTS
        JSONArray results = JSONResponse.getJSONArray("results");
        
        if(JSONResponse.getString("status").equals("OK")){
            double lat = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double lng = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            return lat + ", " + lng;
        }else{
            return null;
        }
    }
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri(GoogleGeocodingBaseUrl).build();
    }
        
}
