package com.laundry.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.util.ArrayList;

public class MapUtils {
    /**
     * Get longitude & latitude by address name
     *
     * @param addressStr Address name
     */
    public static Address getLatLongAddress(Context context, String addressStr) {
        Geocoder coder = new Geocoder(context);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(addressStr, 1);
            for (Address add : adresses) {
                return add;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert feature collection to draw polyline
     * @param geometryJson
     * @return
     */
    public static FeatureCollection convertFeatureCollection(String geometryJson) {
        return FeatureCollection.fromJson("{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"name\": \"Crema to Council Crest\"\n" +
                "      },\n" +
                "     \"geometry\":" +
                geometryJson +
                "    }\n" +
                "  ]\n" +
                "}");
    }

    /**
     * Draw polyline
     *
     * @param featureCollection
     */
    public static void drawPolylines(@NonNull FeatureCollection featureCollection, MapboxMap mapboxMap) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                if (featureCollection.features() != null) {
                    if (featureCollection.features().size() > 0) {
                        style.addSource(new GeoJsonSource("line-source", featureCollection));

                        // The layer properties for our line. This is where we make the line dotted, set the
                        // color, etc.
                        style.addLayer(new LineLayer("linelayer", "line-source")
                                .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                        PropertyFactory.lineOpacity(.7f),
                                        PropertyFactory.lineWidth(7f),
                                        PropertyFactory.lineColor(Color.parseColor("#3bb2d0"))));
                    }
                }
            });
        }
    }

    /**
     * Get cordinate of 2 point string
     * @return Coordinate string url
     */
    public static String getCoordinate(double longStart, double latStart, double longEnd, double latEnd ) {
        return longStart + "," + latStart + ";" + longEnd + "," + latEnd;
    }
}
