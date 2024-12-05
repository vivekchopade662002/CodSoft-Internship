package TASK_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Currency_Convertor {

    public static double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        String urlStr = String.format("https://api.exchangerate-api.com/v4/latest/%s", baseCurrency);
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        String json = content.toString();
        String targetRateString = "\"" + targetCurrency + "\":";
        int index = json.indexOf(targetRateString) + targetRateString.length();
        int endIndex = json.indexOf(",", index);

        if (index < targetRateString.length() || endIndex == -1) {
            throw new Exception("Currency not found");
        }

        return Double.parseDouble(json.substring(index, endIndex));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = amount * exchangeRate;
            System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
        } catch (Exception e) {
            System.out.println("Error fetching the exchange rate: " + e.getMessage());
        }

        scanner.close();
    }
}
