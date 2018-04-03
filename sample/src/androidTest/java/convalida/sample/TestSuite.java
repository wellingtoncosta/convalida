package convalida.sample;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author wellingtoncosta on 03/04/18
 */
@RunWith(Suite.class)
@SuiteClasses({
        AnnotataionSampleActivityTest.class,
        DatabindingSampleActivityTest.class
})
public class TestSuite { }