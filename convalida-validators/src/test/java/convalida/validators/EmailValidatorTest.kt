package convalida.validators

import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 01/11/2017.
 */
class EmailValidatorTest : BaseTest() {

    @Test
    fun invalidEmail() {
        val validatorWithEditText = EmailValidator(mockEditText, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test@email")
        assertEquals(validatorWithEditText.validate(), false)
    }

    @Test
    fun validEmail() {
        val validatorWithEditText = EmailValidator(mockEditText, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test@email.com")
        assertEquals(validatorWithEditText.validate(), true)
    }

}
