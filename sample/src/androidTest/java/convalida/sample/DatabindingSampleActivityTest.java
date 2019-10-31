package convalida.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author Wellington Costa on 14/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DatabindingSampleActivityTest extends ConvalidaBaseActivityTest {

    @Rule public ActivityTestRule<DatabindingSampleActivity> activityTestRule =
            new ActivityTestRule<>(DatabindingSampleActivity.class);

}
