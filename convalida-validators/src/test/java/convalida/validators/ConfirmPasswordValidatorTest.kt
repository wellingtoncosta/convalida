package convalida.validators

import android.text.Editable
import android.widget.EditText
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * @author Wellington Costa on 02/11/2017.
 */
class ConfirmPasswordValidatorTest : BaseTest() {

    private lateinit var passwordEditText: EditText

    @Before
    fun setUpValidators() {
        passwordEditText = mock(EditText::class.java)
        `when`(passwordEditText.text).thenReturn(mock(Editable::class.java))
    }

    @Test
    fun passwordsDoesNotMatch() {
        val validator = ConfirmPasswordValidator(passwordEditText, mockEditText, errorMessage, true)
        `when`(passwordEditText.text.toString()).thenReturn("test@123")
        `when`(mockEditText.text.toString()).thenReturn("test@12")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun passwordsMatch() {
        val validatorWithEditText = ConfirmPasswordValidator(passwordEditText, mockEditText, errorMessage, true)
        `when`(passwordEditText.text.toString()).thenReturn("test@123")
        `when`(mockEditText.getText().toString()).thenReturn("test@123")
        assertEquals(validatorWithEditText.validate(), true)

    }

}