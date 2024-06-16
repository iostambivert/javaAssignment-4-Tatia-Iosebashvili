import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExchangeRateService {
    private static final String API_KEY = "bac5b93ef4b57c6326ec45ce"; // Replace with your API key
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public String getExchangeRateData(String currency) throws Exception {
        String urlString = String.format(BASE_URL + "%s/latest/USD", API_KEY);
        String jsonResponse = fetchDataFromAPI(urlString);
        return parseExchangeRate(jsonResponse, currency);
    }

    private String fetchDataFromAPI(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

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

    private String parseExchangeRate(String jsonResponse, String currency) {
        String conversionRates = extractValue(jsonResponse, "\"conversion_rates\":{", "}");
        String rateString = extractValue(conversionRates, "\"" + currency + "\":", ",");
        double rate = Double.parseDouble(rateString);
        return String.format("Exchange rate from USD to %s: %.2f", currency, rate);
    }

    private String extractValue(String json, String key, String delimiter) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(delimiter, startIndex);
        if (endIndex == -1) { // Handle case when the key is the last element in the JSON string
            endIndex = json.length();
        }
        return json.substring(startIndex, endIndex).trim();
    }

    public static void main(String[] args) {
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        try {
            String exchangeRate = exchangeRateService.getExchangeRateData("EUR");
            System.out.println(exchangeRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
