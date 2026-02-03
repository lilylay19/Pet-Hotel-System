package co.istad.jdbc.project;

import java.net.HttpURLConnection;
import java.net.URL;

public class TelegramBot {

    static String BOT_TOKEN = "8500148336:AAGWDMedKNxzJCihjjqEkbPw3Kov7DR63H0";
    static String CHAT_ID = "1190975832";

    public static void sendMessage(String message) {
        try {
            // Build the URL for Telegram API
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN +
                    "/sendMessage?chat_id=" + CHAT_ID +
                    "&text=" + java.net.URLEncoder.encode(message, "UTF-8");

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println(">> Telegram notification sent successfully!");
            } else {
                System.out.println(">> Failed to send Telegram notification. Check your bot token and chat ID.");
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println(">> Telegram Error: " + e.getMessage());
            System.out.println(">> (This is normal if you haven't set up Telegram bot)");
        }
    }
}