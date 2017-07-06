package convalida.library.validation;

import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class Patterns {

    public static final String NUMERIC_ONLY = Pattern.compile("^\\d+$").pattern();

    public static final String LOWER_CASE_ONLY = Pattern.compile("^[a-z]+$").pattern();

    public static final String UPPER_CASE_ONLY = Pattern.compile("^[A-Z]+$").pattern();

    public static final String LOWER_UPPER_CASE = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{1,}+$").pattern();

    public static final String LOWER_UPPER_CASE_NUMERIC = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$").pattern();

    public static final String LOWER_UPPER_CASE_NUMERIC_SPECIAL = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{1,}$").pattern();

}
