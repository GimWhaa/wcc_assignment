package wcc.assignment.arthur_leong.postcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostcodeImplementation {

    private static final double EARTH_RADIUS = 6371; // Radius in kilometers
    private Map<String, PostcodeModel> postcodeMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(PostcodeImplementation.class);
    private static final Marker CODE_OUTPUT_MARKER = MarkerFactory.getMarker("APILOG");

    public PostcodeImplementation() {
        loadPostcodes("C:\\Users\\Another-PC\\Downloads\\ukpostcodes.csv");
    }

    public Postcode callCalculateDistance(String postcode1 , String postcode2) {
        Postcode response = new Postcode();
        Double calculatedDistance;

        List<Double> locationResult = locationLookup(postcode1,postcode2);
        calculatedDistance = calculateDistance(locationResult.get(0),locationResult.get(1),locationResult.get(2),locationResult.get(3));
        response = new Postcode(postcode1,postcode2,locationResult.get(0),locationResult.get(1),locationResult.get(2),locationResult.get(3),calculatedDistance,"km");
        logger.info(CODE_OUTPUT_MARKER,"Request - Postcode1: "+postcode1+", Postcode2: "+postcode2);
        logger.info(CODE_OUTPUT_MARKER,"Response - " + response);
        return response;
    }

    private double calculateDistance(double latitude, double longitude, double latitude2, double
            longitude2) {
        // Using Haversine formula! See Wikipedia;
        double lon1Radians = Math.toRadians(longitude);
        double lon2Radians = Math.toRadians(longitude2);
        double lat1Radians = Math.toRadians(latitude);
        double lat2Radians = Math.toRadians(latitude2);
        double a = haversine(lat1Radians, lat2Radians)
                + Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (EARTH_RADIUS * c);
    }

    private double haversine(double deg1, double deg2) {
        return square(Math.sin((deg1 - deg2) / 2.0));
    }

    private double square(double x) {
        return x * x;
    }

    private List<Double> locationLookup(String postcode1,String postcode2){
        List<Double> latitudeLongitude = new ArrayList<Double>();
        if(postcodeMap.containsKey(postcode1) && postcodeMap.containsKey(postcode2)) {
            latitudeLongitude.add(postcodeMap.get(postcode1).getLatitude());
            latitudeLongitude.add(postcodeMap.get(postcode1).getLongitude());
            latitudeLongitude.add(postcodeMap.get(postcode2).getLatitude());
            latitudeLongitude.add(postcodeMap.get(postcode2).getLongitude());
        }
        return latitudeLongitude;
    }

    private void loadPostcodes(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                String postcode = values[1].trim();
                double latitude;
                double longitude;

                try {
                    latitude = Double.parseDouble(values[2].trim());
                } catch (ArrayIndexOutOfBoundsException e) {
                    latitude = 0.0;
                }

                try {
                    longitude = Double.parseDouble(values[3].trim());
                } catch (ArrayIndexOutOfBoundsException e) {
                    longitude = 0.0;
                }

                postcodeMap.put(postcode, new PostcodeModel(postcode, latitude, longitude));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error [locationLookup]: " + e.getMessage());
        }
    }

}
