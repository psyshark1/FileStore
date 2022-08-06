package model;

import java.util.*;

public class SecurityConf {
    /*
    * Класс с ролями приложения
    * */
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_USER = "user";

    private static final Map<String, List<String>> mapConfig = new HashMap<>();

    static {init();}

    private static void init() {

        // Конфигурация для роли "admin"
        List<String> urlPatterns = new ArrayList<>();

        urlPatterns.add("/userInfo");
        urlPatterns.add("/forAdmin");
        urlPatterns.add("/forManager");
        urlPatterns.add("/getActs");

        mapConfig.put(ROLE_ADMIN, urlPatterns);

        // Конфигурация для роли "manager"

        List<String> urlPatterns1 = new ArrayList<>();

        urlPatterns1.add("/userInfo");
        urlPatterns1.add("/forManager");
        urlPatterns1.add("/getActs");

        mapConfig.put(ROLE_MANAGER, urlPatterns1);
        // Конфигурация для роли "user"
        List<String> urlPatterns2 = new ArrayList<>();

        urlPatterns2.add("/userInfo");
        urlPatterns2.add("/getActs");

        mapConfig.put(ROLE_USER, urlPatterns2);
    }

    public static Set<String> getAllAppRoles() {
        return mapConfig.keySet();
    }

    public static List<String> getUrlPatternsForRole(String role) {
        return mapConfig.get(role);
    }



}
