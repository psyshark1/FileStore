package utils;

import entity.UserData;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class appUtils {


    private static int REDIRECT_ID = 0;

    private static final Map<Integer, String> id_uri_map = new HashMap<>();
    private static final Map<String, Integer> uri_id_map = new HashMap<>();

    // Сохранить информацию пользователя в Session.
    public static void storeLoginedUser(HttpSession session, UserData loginedUser) {
        // На JSP можно получить доступ через ${loginedUser}
        session.setAttribute("loginedUser", loginedUser);
    }

    // Получить информацию пользователя, сохраненную в Session.
    public static UserData getLoginedUser(HttpSession session) {
        return (UserData) session.getAttribute("loginedUser");
    }

    public static void delLoginedUser(HttpSession session) {
        session.removeAttribute("loginedUser");
    }

    public static int storeRedirectAfterLoginUrl(HttpSession session, String requestUri) {
        Integer id = uri_id_map.get(requestUri);

        if (id == null) {
            id = REDIRECT_ID++;

            uri_id_map.put(requestUri, id);
            id_uri_map.put(id, requestUri);
            return id;
        }

        return id;
    }

    public static String getRedirectAfterLoginUrl(HttpSession session, int redirectId) {
        String url = id_uri_map.get(redirectId);
        if (url != null) {
            return url;
        }
        return null;
    }


}