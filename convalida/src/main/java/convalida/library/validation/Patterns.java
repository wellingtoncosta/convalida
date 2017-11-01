package convalida.library.validation;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class Patterns {

    public static final String NUMERIC_ONLY = "^\\d+$";

    public static final String LOWER_CASE_ONLY = "^[a-z]+$";

    public static final String UPPER_CASE_ONLY = "^[A-Z]+$";

    public static final String LOWER_UPPER_CASE = "^(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    public static final String LOWER_UPPER_CASE_NUMERIC = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    public static final String LOWER_UPPER_CASE_NUMERIC_SPECIAL = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{1,}$";

}
