import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherService {
    private static final String API_KEY = "30b53ba6f38a382402a6f01fc7ff3263"; // Replace with your API key
    private String lat = "41.710847549123216";
    private String lng = "44.76425271781566";


    // Method to fetch JSON data from the API
    // Method to fetch weather data using latitude and longitude
    public String getWeatherData(double latitude, double longitude) throws Exception {
        // Construct the API request URL with latitude, longitude, and API key
        String urlString = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                lat, lng, API_KEY);
        // Fetch the JSON data from the API
        String jsonResponse = fetchDataFromAPI(urlString);

        // Manually parse the JSON data
        String cityName = extractValue(jsonResponse, "\"name\":\"", "\"");
        String temperature = extractValue(jsonResponse, "\"temp\":", ",");
        String weatherDescription = extractValue(jsonResponse, "\"description\":\"", "\"");
        String humidity = extractValue(jsonResponse, "\"humidity\":", "},");
        String windSpeed = extractValue(jsonResponse, "\"speed\":", ",");

        // Format and return the weather data
        return String.format("Destination: %s\nTemperature: %.2f°C\nDescription: %s\nHumidity: %s%%\nWind Speed: %s m/s",
                cityName, (Double.parseDouble(temperature) - 273.15), weatherDescription, humidity, windSpeed);
    }

    // Helper method to fetch data from the API
    private String fetchDataFromAPI(String urlString) throws Exception {
        // Create a URL object from the URL string
        URL url = new URL(urlString);
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Get the response code from the server
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        } else {
            throw new Exception("Failed to fetch data from API. HTTP response code: " + responseCode);
        }
    }

    // Utility method to extract values between specific markers
    private String extractValue(String json, String key, String delimiter) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(delimiter, startIndex);
        return json.substring(startIndex, endIndex);
    }

    // Main method to test the WeatherService class
    public static void main(String[] args) {
        try {
            // Create an instance of WeatherService
            WeatherService weatherService = new WeatherService();
            // Fetch and display the weather data for specific coordinates (e.g., tbilisi, Georgia)
            String weatherData = weatherService.getWeatherData(41.5428, 45.0025); // Example coordinates for Rustavi, Georgia
            System.out.println(weatherData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}