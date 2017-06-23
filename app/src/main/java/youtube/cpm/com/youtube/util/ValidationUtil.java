package youtube.cpm.com.youtube.util;

import java.util.regex.Pattern;

/**
 * Created by desktop-23 on 28/9/16.
 */
public class ValidationUtil {
    private static final String USERNAME_PATTERN = "[A-Z][a-zA-Z]*";
    private static String EMAILID="^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    private static String MOBILENO="^\\+[1-9]{1}[0-9]{3,14}$";
    public static boolean validateName(String name) {
        boolean result = true;
        if(Pattern.compile(USERNAME_PATTERN).matcher(name).matches())
        {
            result=true;
        }
        else
        {
            result=false;
        }
        return result;
    }

    public static boolean validateEmail(String email) {
        boolean result = true;
        // Do your validation and return the result
        if(Pattern.compile(EMAILID).matcher(email).matches())
        {
            result=true;
        }
        else
        {
            result=false;
        }
        return result;
    }

    public static boolean validatePassword(String password) {
        boolean result = true;
        // Do your validation and return the result
        if(password.length()<5)
        {
            result=false;
        }
        return result;
    }
    public static boolean validateMobileNo(String mobileNo) {
        boolean result = true;
        // Do your validation and return the result
        if(Pattern.compile(MOBILENO).matcher(mobileNo).matches())
        {
            result=true;
        }
        else
        {
            result=false;
        }
        return result;

    }
}
