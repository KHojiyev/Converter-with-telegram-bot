package module3.lesson10_TelegramBot.task2_Converter.version2;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<Integer,String> test = new HashMap<>();

        test.put(12,"salom");
        test.put(13,"salom");
        test.put(14,"salom");
        test.put(15,"salom");
        System.out.println(test);
        System.out.println(test.get(13));

    }
}
