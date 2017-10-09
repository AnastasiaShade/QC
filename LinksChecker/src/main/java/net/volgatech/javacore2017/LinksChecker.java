package net.volgatech.javacore2017;

import java.io.*;
import java.util.*;
import java.net.*;
import java.time.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinksChecker {
    private static BufferedWriter allLinksFile = null;
    private static BufferedWriter brokenLinksFile = null;

    private static HashSet<String> urls = new HashSet<String>();
    private static int linksCounter = 0;
    private static int brokenLinksCounter = 0;
    private static String host = "";
    private static String linksFileName = "AllLinks.txt";
    private static String brokenLinksFileName = "BrokenLinks.txt";

    private static final int BAD_REQUEST_CODE = 400;

    private static void openFiles() {
        try {
            allLinksFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linksFileName)));
            brokenLinksFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(brokenLinksFileName)));
        } catch (FileNotFoundException e) {
            printError("Ошибка открытия файла");
            System.exit(1);
        }
    }

    private static void closeFiles() {
        try {
            allLinksFile.close();
            brokenLinksFile.close();
        } catch (IOException e) {
            printError("Ошибка закрытия файла");
            System.exit(1);
        }
    }

    private static String setProtocolIfExists(String url) {
        if (!url.startsWith("http") && !url.startsWith("https")){
            url = "http://" + url;
        }
        return url;
    }

    private static int getStatusCode(String url) throws IOException {
        Response response = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).followRedirects(false).execute();
        return response.statusCode();
    }

    private static void addToFile(String url, int statusCode) throws IOException {
        if (statusCode >= BAD_REQUEST_CODE) {
            try {
                brokenLinksFile.write("[" + statusCode + "] " + url);
                brokenLinksFile.newLine();
                ++brokenLinksCounter;
            }
            catch (IOException err) {
                printError("Ошибка записи в файл");
                System.exit(1);
            }
        }
        try {
            allLinksFile.write("[" + statusCode + "] " + url);
            allLinksFile.newLine();
        }
        catch (IOException err) {
            printError("Ошибка записи в файл");
            System.exit(1);
        }
    }

    private static void getAllLinks(Document currPage) {
        Elements links = currPage.getElementsByTag("a");
        for (Element link : links) {
            String url = setProtocolIfExists(link.attr("abs:href"));
            if (url.contains(host)) {
                if (!urls.contains(url)) {
                    urls.add(url);
                    int statusCode = 0;
                    try {
                        statusCode = getStatusCode(url);
                        addToFile(url, statusCode);
                    }
                    catch (IOException err) {
                        printError("Соединение не установлено");
                    }

                    ++linksCounter;
                    System.out.println("Ссылка №" + linksCounter);
                    if (statusCode >= BAD_REQUEST_CODE) {
                        continue;
                    }

                    try {
                        Document currDoc = Jsoup.connect(url).get();
                        getAllLinks(currDoc);
                    }
                    catch (IOException err) {
                        printError("Соединение не установлено");
                    }
                }
            }
        }
    }

    private static Boolean isUrlValid(String arg) {
        return (arg.startsWith("http") && !arg.isEmpty() && !arg.contains(" "));
    }

    private static void printError(String msg) {
        System.err.println(msg);
        System.err.println("Используйте: LinksChecker <URL>");
        System.err.println("Пример: LinksChecker http://test.com");
    }

    private static void printInfoInFile() {
        final String time = "Время завершения: " + LocalDate.now().toString() + " " + LocalTime.now().toString();
        try {
            allLinksFile.write("Проверено ссылок: " + linksCounter);
            allLinksFile.newLine();
            allLinksFile.write(time);
            allLinksFile.newLine();

            brokenLinksFile.write("Битых ссылок: " + brokenLinksCounter);
            brokenLinksFile.newLine();
            brokenLinksFile.write(time);
            brokenLinksFile.newLine();
        } catch (IOException e) {
            printError("Ошибка записи в файл");
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            printError("Неверное количество аргументов");
        }
        if (args.length == 2 && !args[1].isEmpty()) {
            linksFileName = args[1];
        }
        if (args.length >= 3 && !args[2].isEmpty()) {
            linksFileName = args[1];
            brokenLinksFileName = args[2];
        }

        String url = setProtocolIfExists(args[0]);
        if (!isUrlValid(url)) {
            printError("Введен неверный URL-адрес");
        }

        try {
            URL domain = new URL(url);
            host = domain.getHost();
        }
        catch (MalformedURLException err) {
            printError("Введен неверный URL-адрес");
        }

        openFiles();

        try {
            Document page = Jsoup.connect(url).get();
            getAllLinks(page);
        }
        catch (IOException err) {
            printError(err.toString());
        }

        printInfoInFile();

        System.out.println("Проверка закончена");
        closeFiles();
    }
}
