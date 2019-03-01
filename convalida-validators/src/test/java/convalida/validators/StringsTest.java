package convalida.validators;

import org.junit.Test;

import static convalida.validators.util.Strings.repeat;
import static org.junit.Assert.assertEquals;

/**
 * @author WellingtonCosta on 26/04/18.
 */
public class StringsTest {

    @Test public void repeatOneNegativeTimes() {
        String string = "a";
        String repeated = repeat(string, -1);
        assertEquals(repeated, "");
    }

    @Test public void repeatZeroTimes() {
        String string = "a";
        String repeated = repeat(string, 0);
        assertEquals(repeated, "");
    }

    @Test public void repeatOneTime() {
        String string = "a";
        String repeated = repeat(string, 1);
        assertEquals(repeated, string);
    }

    @Test public void repeatTwoTimes() {
        String string = "a";
        String repeated = repeat(string, 2);
        assertEquals(repeated, string + string);
    }

    @Test public void repeatThreeTimes() {
        String string = "a";
        String repeated = repeat(string, 3);
        assertEquals(repeated, string + string + string);
    }

    @Test public void repeatSixTimes() {
        String string = "a";
        String repeated = repeat(string, 6);
        assertEquals(repeated, string + string + string + string + string + string);
    }

}
