package com.palsulich.nyubustracker.models;

import com.google.android.gms.maps.model.LatLng;
import com.palsulich.nyubustracker.helpers.BusManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bus {
    private String vehicleID = "";
    private LatLng loc;
    private String heading = "";
    private String route;

    public Bus(String mVehicleID) {
        vehicleID = mVehicleID;
    }

    Bus setLocation(String lat, String lng) {
        loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        return this;
    }

    public LatLng getLocation() {
        return loc;
    }

    Bus setHeading(String mHeading) {
        heading = mHeading;
        return this;
    }

    Bus setRoute(String mRoute) {
        route = mRoute;
        return this;
    }

    public String getRoute() {
        return route;
    }

    public Float getHeading() {
        try {
            return Float.parseFloat(heading);
        } catch (Exception e) {
            return 0f;
        }
    }

    public String getID() {
        return vehicleID;
    }

    public static void parseJSON(JSONObject vehiclesJson) throws JSONException {
        BusManager sharedManager = BusManager.getBusManager();
        JSONObject jVehiclesData = null;
        if (vehiclesJson != null) jVehiclesData = vehiclesJson.getJSONObject(BusManager.TAG_DATA);
        JSONArray jVehicles = new JSONArray();
        if (jVehiclesData != null) jVehicles = jVehiclesData.getJSONArray("72");
        for (int j = 0; j < jVehicles.length(); j++) {
            JSONObject busObject = jVehicles.getJSONObject(j);
            JSONObject busLocation = busObject.getJSONObject(BusManager.TAG_LOCATION);
            String busLat = busLocation.getString(BusManager.TAG_LAT);
            String busLng = busLocation.getString(BusManager.TAG_LNG);
            String busRoute = busObject.getString(BusManager.TAG_ROUTE_ID);
            String vehicleID = busObject.getString(BusManager.TAG_VEHICLE_ID);
            String busHeading = busObject.getString(BusManager.TAG_HEADING);
            // getBus will either return an existing bus, or create a new one for us. We'll have to parse the bus JSON often.
            Bus b = sharedManager.getBus(vehicleID);
            b.setHeading(busHeading).setLocation(busLat, busLng).setRoute(busRoute);
            //Log.v("BusLocations", "Parsing buses: bus id: " + vehicleID + " | bus' route: " + busRoute);
            //Log.v("JSONDebug", "Bus ID: " + vehicleID + " | Heading: " + busHeading + " | (" + busLat + ", " + busLng + ")");
        }
    }
}
