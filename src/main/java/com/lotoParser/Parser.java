package com.lotoParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.io.*;
import java.util.*;


/**
 * Created by AtagaN on 16.01.2019.
 */
public class Parser {

    private static WebDriver driver;
    private static Map<String, Integer> map = new HashMap<>();
    private static Map<String, Integer> result = new LinkedHashMap<>();

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        driver = new ChromeDriver();

        try {
            start();
        } catch (Exception e) {

        } finally {
            try {
                writeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            driver.close();
        }

    }


    public static void start() {
        //Map<String, Integer> map = new HashMap<>();

        while (true) {
            driver.get("https://www.stoloto.ru/ruslotto/game?int=right");
            String html = driver.findElement(By.cssSelector("html")).getAttribute("outerHTML");

            Document document = Jsoup.parse(html);
            List<Element> elements = document.select("div.bingo_ticket.ruslotto > table > tbody > tr.numbers  > td");

            elements.forEach(element -> {
                String text = element.ownText();
                if (!text.equals("")) {
                    System.out.print(text + " ");
                    if (map.containsKey(text)) {
                        int num = map.get(text);
                        num++;
                        map.put(text, num);
                    } else {
                        map.put(text, 1);
                    }

                }
            });
            System.out.println();
        }
    }


    public static void writeFile() throws IOException {
        sort(map);
        FileWriter fileWriter = new FileWriter("." + File.separator + "statistic.txt");

        result.forEach((value, number) -> {
            System.out.println(value + " count: " + number );
            try {
                fileWriter.write(value + " count: " + number +"\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fileWriter.flush();
        fileWriter.close();
    }


    public static void sort(Map<String, Integer> map){
        map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach( entry -> {result.put(entry.getKey(), entry.getValue());}); // или любой другой конечный метод

    }

}
