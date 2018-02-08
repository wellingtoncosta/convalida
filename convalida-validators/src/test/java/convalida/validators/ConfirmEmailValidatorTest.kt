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
class ConfirmEmailValidatorTest : BaseTest() {

    private lateinit var emailEditText: EditText

    @Before
    fun setUpValidators() {
        emailEditText = mock(EditText::class.java)
        `when`(emailEditText.text).thenReturn(mock(Editable::class.java))
    }

    @Test
    fun emailsDoesNotMatch() {
        val validator = ConfirmEmailValidator(emailEditText, mockEditText, errorMessage, true)
        `when`(emailEditText.text.toString()).thenReturn("wellington@email.com")
        `when`(mockEditText.text.toString()).thenReturn("wellington@email.co")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun emailsMatch() {
        val validator = ConfirmPasswordValidator(emailEditText, mockEditText, errorMessage, true)
        `when`(emailEditText.text.toString()).thenReturn("wellington@email.com")
        `when`(mockEditText.text.toString()).thenReturn("wellington@email.com")
        assertEquals(validator.validate(), true)

    }

}