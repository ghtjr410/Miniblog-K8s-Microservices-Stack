package com.miniblog.query.util;

import org.jsoup.Jsoup;

public class HtmlUtils {
    private HtmlUtils() {}

    public static String toPlainText(String html) {
        return Jsoup.parse(html).text();
    }
}
