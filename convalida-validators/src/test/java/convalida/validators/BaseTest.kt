package convalida.validators

import android.text.Editable
import android.widget.EditText
import org.junit.Before
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * @author Wellington Costa on 16/11/2017.
 */
open class BaseTest {

    internal lateinit var mockEditText: EditText
    internal val errorMessage = "Field invalid"

    @Before
    fun setUp() {
        mockEditText = mock(EditText::class.java)
        `when`(mockEditText.text).thenReturn(mock(Editable::class.java))
    }

}
