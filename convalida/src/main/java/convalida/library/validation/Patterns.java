package convalida.library.validation;

import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class Patterns {

    public static final Pattern NUMERIC_ONLY = Pattern.compile("^\\d+$");

    public static final Pattern LOWER_CASE_ONLY = Pattern.compile("^[a-z]+$");

    public static final Pattern UPPER_CASE_ONLY = Pattern.compile("^[A-Z]+$");

    public static final Pattern LOWER_UPPER_CASE = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{1,}+$");

    public static final Pattern LOWER_UPPER_CASE_NUMERIC = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$");

    public static final String LOWER_UPPER_CASE_NUMERIC_SPECIAL = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{1,}$";

}
