package utils;

import model.SecurityConf;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SecurityUtils {

    static final int OUTPUT_STRING_LENGTH = 8;
    static final String RANDOM_PASS_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_";

    // Проверить требует ли данный 'request' входа в систему или нет.
    public static boolean isSecurityPage(HttpServletRequest request) {
        String urlPattern = URLpatternUtils.getUrlPattern(request);

        Set<String> roles = SecurityConf.getAllAppRoles();

        for (String role : roles) {
            List<String> urlPatterns = SecurityConf.getUrlPatternsForRole(role);
            if (urlPatterns != null && urlPatterns.contains(urlPattern)) {
                return true;
            }
        }
        return false;
    }

    // Проверить имеет ли данный 'request' подходящую роль?
    public static boolean hasPermission(HttpServletRequest request) {
        String urlPattern = URLpatternUtils.getUrlPattern(request);

        Set<String> allRoles = SecurityConf.getAllAppRoles();

        for (String role : allRoles) {
            if (!request.isUserInRole(role)) {
                continue;
            }
            List<String> urlPatterns = SecurityConf.getUrlPatternsForRole(role);
            if (urlPatterns != null && urlPatterns.contains(urlPattern)) {
                return true;
            }
        }
        return false;
    }

    public static String encPass(String pass){
        return Base64.getEncoder().encodeToString(RotateChar(convertStringToHex(pass)).getBytes());
    }

    public static String decPass(String pass){
        return convertHexToString(new String(Base64.getDecoder().decode(pass)));
    }

    private static String convertStringToHex(String str) {

        StringBuffer hex = new StringBuffer();

        // loop chars one by one
        for (char temp : str.toCharArray()) {

            // convert char to int, for char `a` decimal 97
            int decimal = (int) temp;

            // convert int to hex, for decimal 97 hex 61
            hex.append(Integer.toHexString(decimal));
        }
        hex.reverse();
        return hex.toString();

    }

    // Hex -> Decimal -> Char
    private static String convertHexToString(String hex) {

        StringBuilder result = new StringBuilder();

        // split into two chars per loop, hex, 0A, 0B, 0C...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            String tempInHex = hex.substring(i, (i + 2));

            //convert hex to decimal
            int decimal = Integer.parseInt(tempInHex, 16);

            // convert the decimal to char
            result.append((char) decimal);

        }
        result.reverse();
        return result.toString();

    }

    private static String RotateChar(String arg) {
        StringBuilder result = new StringBuilder();
        StringBuilder tmpsB = new StringBuilder();
        for (int i = 0; i < arg.length() - 1; i += 2) {
            tmpsB.append(arg.substring(i, (i + 2))).reverse();
            result.append(tmpsB.toString());
            tmpsB.setLength(0);
        }
        return result.toString();
    }

   public static String setRandomPass() {

        Random random = new Random();
        StringBuilder sbRandomString = new StringBuilder(OUTPUT_STRING_LENGTH);

        for(int i = 0 ; i < OUTPUT_STRING_LENGTH; i++){

            //get random integer between 0 and string length
            int randomInt = random.nextInt(RANDOM_PASS_CHARACTERS.length());

            //get char from randomInt index from string and append in StringBuilder
            sbRandomString.append(RANDOM_PASS_CHARACTERS.charAt(randomInt));
        }

        return sbRandomString.toString();

    }


}
