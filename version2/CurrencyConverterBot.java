package module3.lesson10_TelegramBot.task2_Converter.version2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyConverterBot extends TelegramLongPollingBot {
    public static module3.lesson10_TelegramBot.task2_Converter.version2.Currency firstCurrency;
    public static module3.lesson10_TelegramBot.task2_Converter.version2.Currency secondCurrency;
    public static List<Currency> currency = new ArrayList<>();
    public static Long userChatId;
    public static ConcurrentHashMap<Long, Boolean> enterDate = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Boolean> enterCurrencyFrom = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, Boolean> enterCurrencyTo = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long, String> activeCurrencyFrom = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, String> activeCurrencyTo = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long ,Double> amount = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long ,List<Currency>> currencyBase = new ConcurrentHashMap<>();
    public static List<Currency> currencyList = new ArrayList<>();

    public static void setCurrencyForUser(){
        activeCurrencyFrom.put(userChatId,textWords.UZB);
        activeCurrencyTo.put(userChatId,textWords.USD);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage()) {
            userChatId = update.getMessage().getChatId();
            String amount = update.getMessage().getText();
            switch (update.getMessage().getText()) {
                case "/start":
                    enterDate.put(userChatId, false);
                    defaultRefresh();
                    sendMessage.setText("Please refresh currency data for working true");
                    replyKeyBoardMarkup(sendMessage);
                    sendMessage.setChatId(userChatId);
                    tryCatch(sendMessage);
                    break;
                case textWords.REFRESH:
                    setCurrencyForUser();
                    enterDate.put(userChatId, false);
                    if (refresh("")) {
                        sendMessage.setText(textWords.REFRESHED).setChatId(userChatId);
                        tryCatch(sendMessage);
                    }
                    break;
                case textWords.CURRENCY_HISTORY:
                    enterDate.put(userChatId, true);
                    sendMessage.setText(textWords.ENTER_CURRENCY_HISTORY_DATE).setChatId(userChatId);
                    tryCatch(sendMessage);
                    break;
                case textWords.CURRENCY_FROM:
                    enterCurrencyFrom.put(userChatId,true);
                    try {
                        execute(setCurrencyChoice("From(Currency)"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case textWords.CURRENCY_TO:
                    enterCurrencyTo.put(userChatId,true);
                    try {
                        execute(setCurrencyChoice("To(Currency)"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    if (enterDate.get(userChatId)) {
                        enterDate.put(userChatId, false);

                        if (refresh(update.getMessage().getText())) {
                            sendMessage.setText(textWords.REFRESHED).setChatId(userChatId);
                            tryCatch(sendMessage);
                        } else {
                            sendMessage.setText(textWords.ERRORDATE).setChatId(userChatId);
                            tryCatch(sendMessage);
                        }
                    }else

                    if(isDouble(amount)){
                        System.out.println("enter1");

                        convertMachine(Double.parseDouble(amount),activeCurrencyFrom.get(userChatId),
                                                                    activeCurrencyTo.get(userChatId));
                    }



                    break;
            }
        }

        else if (update.hasCallbackQuery()) {
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            EditMessageText editMessageText = new EditMessageText();
            userChatId = update.getCallbackQuery().getFrom().getId().longValue();
            String text = update.getCallbackQuery().getData();
            if(enterCurrencyFrom.get(userChatId)){
                enterCurrencyFrom.put(userChatId,false);
                activeCurrencyFrom.put(userChatId,text);
                System.out.println(text);
                editMessageText.setText(textWords.REFRESHED).setChatId(userChatId).setMessageId(messageId);
                tryCatchForInline(editMessageText);
            }else if(enterCurrencyTo.get(userChatId)){
               enterCurrencyTo.put(userChatId,false);
                activeCurrencyTo.put(userChatId,text);
                System.out.println(text);
                editMessageText.setText(textWords.REFRESHED).setChatId(userChatId).setMessageId(messageId);
                tryCatchForInline(editMessageText);
            }

        }
    }

    private void convertMachine(double amount,String from,String to) {
        SendMessage sendMessage = new SendMessage();
        System.out.println("enter2");
        Gson gson = new Gson();
        File file = new File("src/main/java/module3/lesson10_TelegramBot/task2_Converter/version2/currency(" + userChatId + ").json");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            currencyBase.put(userChatId,Arrays.asList(gson.fromJson(bufferedReader, Currency[].class)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(from.equals(textWords.UZB)){
            System.out.println("enter3");
            for (Currency currency1 : currencyBase.get(userChatId)) {
                if(currency1.getCcy().equals(to)){
                    System.out.println(currency1.getRate()+" "+currency1.getCcyNm_UZ());
                    sendMessage.setText(amount+" uzb = "+ amount/ Double.parseDouble(currency1.getRate())
                            +" "+currency1.getCcyNm_UZ());
                    sendMessage.setChatId(userChatId);
                    tryCatch(sendMessage);
                }
            }
        }else if (to.equals(textWords.UZB)){

        }else {

        }


    }

    public static boolean isDouble(String amount){
        try{
            System.out.println("testing");
            Double d = Double.parseDouble(amount);
            return true;
        }catch(NumberFormatException e){
            System.out.println("failed");
            return false;
        }
    }

    private void tryCatchForInline(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void tryCatch(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static SendMessage setCurrencyChoice(String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        InlineKeyboardButton button6 = new InlineKeyboardButton();

        button1.setText(textWords.UZB);
        button1.setCallbackData(textWords.UZB);
        button2.setText(textWords.RUB);
        button2.setCallbackData(textWords.RUB);
        button3.setText(textWords.USD);
        button3.setCallbackData(textWords.USD);
        button4.setText(textWords.EUR);
        button4.setCallbackData(textWords.EUR);
        button5.setText(textWords.KRW);
        button5.setCallbackData(textWords.KRW);
        button6.setText(textWords.CNY);
        button6.setCallbackData(textWords.CNY);

        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonList3 = new ArrayList<>();
        List<InlineKeyboardButton> buttonList4 = new ArrayList<>();
        List<InlineKeyboardButton> buttonList5 = new ArrayList<>();
        List<InlineKeyboardButton> buttonList6 = new ArrayList<>();

        buttonList1.add(button1);
        buttonList1.add(button2);
        buttonList2.add(button3);
        buttonList2.add(button4);
        buttonList3.add(button5);
        buttonList3.add(button6);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList1);
        rowList.add(buttonList2);
        rowList.add(buttonList3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setText(text).setChatId(userChatId).setReplyMarkup(inlineKeyboardMarkup);
    }

    private boolean refresh(String date) {
        LocalDate localDate = LocalDate.now();
        String activeDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(activeDate);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("src/main/java/module3/lesson10_TelegramBot/task2_Converter/version2/currency(" + userChatId + ").json");
        if (date.length() > 0) {
            date = date.trim();
            String[] dateNumber = date.split("-");
            for (String s : dateNumber) {
                if (!isInteger(s)) {
                    return false;
                }
            }
            try {
                System.out.println("passive");
                URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/" + date + "/");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line);
                }
                bufferedWriter.close();
                bufferedReader.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                System.out.println("active");
                URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line);
                }
                bufferedWriter.close();
                bufferedReader.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void defaultRefresh() {
        File file = new File("src/main/java/module3/lesson10_TelegramBot/task2_Converter/version2/currency(" + userChatId + ").json");
        try {
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
            }
            bufferedWriter.close();
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replyKeyBoardMarkup(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton(textWords.REFRESH));
        row2.add(new KeyboardButton(textWords.CURRENCY_FROM));
        row2.add(new KeyboardButton(textWords.CURRENCY_TO));
        row3.add(new KeyboardButton(textWords.CURRENCY_HISTORY));
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            System.out.println(Integer.parseInt(str));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    @Override
    public String getBotUsername() {
        return "http://t.me/Converter_Currencies_Bot";
    }

    @Override
    public String getBotToken() {
        return "1674097258:AAHvEZHvdv3Kxl3_UD1X2G5noOdnFK0kXz8";
    }
}
