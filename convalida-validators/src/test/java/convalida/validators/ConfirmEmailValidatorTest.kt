package convalida.validators

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.widget.EditText

import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

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
        val validator = ConfirmEmailValidator(emailEditText, mockEditText, errorMessage)
        `when`(emailEditText.text.toString()).thenReturn("wellington@email.com")
        `when`(mockEditText.text.toString()).thenReturn("wellington@email.co")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun emailsMatch() {
        val validator = ConfirmPasswordValidator(emailEditText, mockEditText, errorMessage)
        `when`(emailEditText.text.toString()).thenReturn("wellington@email.com")
        `when`(mockEditText.text.toString()).thenReturn("wellington@email.com")
        assertEquals(validator.validate(), true)

    }

}