package module3.lesson10_TelegramBot.task2_Converter.version1;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class ConverterRoot extends TelegramLongPollingBot {
    public static Currency activeCurrency1;
    public static Currency activeCurrency2;
    public static List<Currency> currencyList = new ArrayList<>();
    public static ConcurrentHashMap<String,Double> amountMoneyMap = new ConcurrentHashMap<>();

    private static void currencyFinder(String currencyName1, String currencyName2) {
        activeCurrency1 = null;
        activeCurrency2 = null;
        for (Currency currency : currencyList) {
            if (currency.getCcy().equals(currencyName1)) {
                activeCurrency1 = currency;
            }
        }
        for (Currency currency : currencyList) {
            if (currency.getCcy().equals(currencyName2)) {
                activeCurrency2 = currency;
            }
        }
    }

    private static String fromUZSSUM(double amount) {
        double rate = (Double.parseDouble(activeCurrency2.getRate()));
        return "Amount: " + amount + " Uzbekistan sum" + " <=> " + "result: " + (amount / rate) + " " + activeCurrency2.getCcyNm_EN();
    }

    private static String defaultResult(double amount) {
        double rate = (Double.parseDouble(activeCurrency1.getRate()));
        return "Amount: " + amount + " " + activeCurrency1.getCcyNm_EN() + " <=> " + "result: " + (rate * amount) + " " + " Uzbekistan sum";
    }

    private static String result(double amount) {
        double rate = (Double.parseDouble(activeCurrency1.getRate()) / Double.parseDouble(activeCurrency2.getRate()));
        return "Amount: " + amount + " " + activeCurrency1.getCcyNm_EN() + " <=> " + "result: " + (rate * amount) + " " + activeCurrency2.getCcyNm_EN();
    }

    private void currencyButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add(new KeyboardButton("US Dollar-РФ Рубл"));
        row1.add(new KeyboardButton("US Dollar-UZB Sum"));
        row1.add(new KeyboardButton("US Dollar-CHINE Yuan"));

        row2.add(new KeyboardButton("РФ Рубл-US Dollar"));
        row2.add(new KeyboardButton("РФ Рубл-USB Sum"));
        row2.add(new KeyboardButton("РФ Рубл-CHINE Yuan"));

        row3.add(new KeyboardButton("CHINE Yuan-USD Dollar"));
        row3.add(new KeyboardButton("CHINE Yuan-USB Sum"));
        row3.add(new KeyboardButton("CHINE Yuan-РФ Рубл"));

        row4.add(new KeyboardButton("USB Sum-USD Dollar"));
        row4.add(new KeyboardButton("USB Sum-РФ Рубл"));
        row4.add(new KeyboardButton("USB Sum-CHINE Yuan"));

        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardRows.add(row4);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();

        if (update.hasMessage()) {
            switch (message.getText()) {
                case "/start":
                    sendMessage.setText(" \uD83D\uDC4B\uD83D\uDC4BHello welcome to Currency Converter bot  " + update.getMessage().getChat().getFirstName() + "\n" +
                            "Admin: @KhojiyevML \n\n" +
                            "Select currency ");
                    currencyButtons(sendMessage);
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "US Dollar-РФ Рубл":
                    currencyFinder("USD", "RUB");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "US Dollar-UZB Sum":
                    currencyFinder("USD", "");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "US Dollar-CHINE Yuan":
                    currencyFinder("USD", "CNY");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "РФ Рубл-US Dollar":
                    currencyFinder("RUB", "USD");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "РФ Рубл-USB Sum":
                    currencyFinder("RUB", "");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "РФ Рубл-CHINE Yuan":
                    currencyFinder("RUB", "CNY");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "CHINE Yuan-USD Dollar":
                    currencyFinder("CNY", "USD");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "CHINE Yuan-РФ Рубл":
                    currencyFinder("CNY", "RUB");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "CHINE Yuan-USB Sum":
                    currencyFinder("CNY", "");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "USB Sum-USD Dollar":
                    currencyFinder("", "USD");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "USB Sum-РФ Рубл":
                    currencyFinder("", "RUB");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "USB Sum-CHINE Yuan":
                    currencyFinder("", "CNY");
                    sendMessage.setText("Amount money:");
                    try {
                        sendMessage.setChatId(update.getMessage().getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    amountMoneyMap.put(update.getMessage().getChat().getUserName(),Double.parseDouble(update.getMessage().getText()));
                    break;

            }
            if (activeCurrency1 != null || activeCurrency2 != null) {
                if (amountMoneyMap.size()>0) {
                    if (activeCurrency2 != null && activeCurrency1!= null) {
                        sendMessage.setText(result(amountMoneyMap.get(update.getMessage().getChat().getUserName())));
                        amountMoneyMap.remove(update.getMessage().getChat().getUserName());
                        try {
                            sendMessage.setChatId(update.getMessage().getChatId());
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if (activeCurrency1 != null && activeCurrency2 == null) {
                        sendMessage.setText(defaultResult(amountMoneyMap.get(update.getMessage().getChatId())));
                        amountMoneyMap.remove(update.getMessage().getChatId());

                        try {
                            sendMessage.setChatId(update.getMessage().getChatId());
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if (activeCurrency1 == null && activeCurrency2 != null) {
                        sendMessage.setText(fromUZSSUM(amountMoneyMap.get(update.getMessage().getChatId())));
                        amountMoneyMap.remove(update.getMessage().getChatId());
                        try {
                            sendMessage.setChatId(update.getMessage().getChatId());
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (amountMoneyMap.size()>0) {
                sendMessage.setText("Select currency");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "http://t.me/usd_rubl_sum_Convert_bot";
    }

    @Override
    public String getBotToken() {
        return "1682497681:AAECA3grICyrk0Oj6NLy6F_cSHbEwJyK1t0";
    }
}
