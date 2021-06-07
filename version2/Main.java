package module3.lesson10_TelegramBot.task2_Converter.version2;


import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new CurrencyConverterBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }



    }
}
