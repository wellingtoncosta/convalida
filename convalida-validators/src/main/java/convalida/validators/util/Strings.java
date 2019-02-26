package convalida.validators.util;

/**
 * @author WellingtonCosta on 26/04/18.
 */
public class Strings {

    public static String repeat(String string, int times) {
        if(times <= 0) return "";
        else if(times == 1) return string;
        else if (times % 2 == 0) return repeat(string + string, times / 2);
        else return string + repeat(string + string, times / 2);
    }

}
