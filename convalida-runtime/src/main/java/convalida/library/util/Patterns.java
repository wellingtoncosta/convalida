package convalida.library.util;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class Patterns {

    public static final String NUMERIC_ONLY = "^\\d+$";

    public static final String ALPHA_ONLY = "\\w+";

    public static final String LOWER_CASE_ONLY = "^[a-z]+$";

    public static final String UPPER_CASE_ONLY = "^[A-Z]+$";

    public static final String ALPHA_MIXED_CASE = "^(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    public static final String ALPHA_NUMERIC =  "(?=.*[a-zA-Z])(?=.*[\\d]).+";

    public static final String MIXED_CASE_NUMERIC = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    public static final String ALPHA_NUMERIC_MIXED_CASE = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).+";

    public static final String ALPHA_NUMERIC_MIXED_CASE_SYMBOLS = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*([^\\w])).+";
}
