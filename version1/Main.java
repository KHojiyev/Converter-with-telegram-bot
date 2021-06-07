package module3.lesson10_TelegramBot.task2_Converter.version1;

import com.google.gson.Gson;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new ConverterRoot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
            Gson gson = new Gson();
            try {
                URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
                ConverterRoot.currencyList = Arrays.asList(gson.fromJson(bufferedReader,Currency[].class));

            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
