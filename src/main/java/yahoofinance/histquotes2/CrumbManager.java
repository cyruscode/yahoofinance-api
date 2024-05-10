package yahoofinance.histquotes2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yahoofinance.YahooFinance;
import yahoofinance.util.RedirectableRequest;

/**
 * Created by Stijn on 23/05/2017.
 */
public class CrumbManager {

  private static final Logger log = LoggerFactory.getLogger(CrumbManager.class);

  private static String crumb = "";
  private static String cookie = "";

  private static void setCookie() throws IOException {
    if (CookieHandler.getDefault() == null) {
      CookieManager cm = new CookieManager();
      CookieHandler.setDefault(cm);
    }

    if (YahooFinance.HISTQUOTES2_COOKIE != null && !YahooFinance.HISTQUOTES2_COOKIE.isEmpty()) {
      cookie = YahooFinance.HISTQUOTES2_COOKIE;
      log.debug("Set cookie from system property: {}", cookie);
      return;
    }

    URL request = new URL(YahooFinance.HISTQUOTES2_SCRAPE_URL);
    RedirectableRequest redirectableRequest = new RedirectableRequest(request, 5);
    redirectableRequest.setConnectTimeout(YahooFinance.CONNECTION_TIMEOUT);
    redirectableRequest.setReadTimeout(YahooFinance.CONNECTION_TIMEOUT);
    redirectableRequest.openConnection();

    CookieStore cookieJar = ((CookieManager) CookieHandler.getDefault()).getCookieStore();
    List<HttpCookie> cookies = cookieJar.getCookies();

    cookie = cookies.get(0).toString();
  }


  public static void setCrumb() throws IOException {
    if (YahooFinance.HISTQUOTES2_CRUMB != null && !YahooFinance.HISTQUOTES2_CRUMB.isEmpty()) {
      crumb = YahooFinance.HISTQUOTES2_CRUMB;
      log.debug("Set crumb from system property: {}", crumb);
      return;
    }

    URL crumbRequest = new URL(YahooFinance.HISTQUOTES2_CRUMB_URL);
    RedirectableRequest redirectableCrumbRequest = new RedirectableRequest(crumbRequest, 5);
    redirectableCrumbRequest.setConnectTimeout(YahooFinance.CONNECTION_TIMEOUT);
    redirectableCrumbRequest.setReadTimeout(YahooFinance.CONNECTION_TIMEOUT);

    Map<String, String> requestProperties = new HashMap<>();
    requestProperties.put("Cookie", cookie);
    requestProperties.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

    URLConnection crumbConnection = redirectableCrumbRequest.openConnection(requestProperties);
    InputStreamReader is = new InputStreamReader(crumbConnection.getInputStream());
    BufferedReader br = new BufferedReader(is);
    String crumbResult = br.readLine();

    if (crumbResult != null && !crumbResult.isEmpty()) {
      crumb = crumbResult.trim();
      log.debug("Set crumb from http request: {}", crumb);
    } else {
      log.debug("Failed to set crumb from http request. Historical quote requests will most likely fail.");
    }

  }

  public static void refresh() throws IOException {
    setCookie();
    setCrumb();
  }

  public static synchronized String getCrumb() throws IOException {
    if (crumb == null || crumb.isEmpty()) {
      refresh();
    }
    return crumb;
  }

  public static String getCookie() throws IOException {
    if (cookie == null || cookie.isEmpty()) {
      refresh();
    }
    return cookie;
  }

}
