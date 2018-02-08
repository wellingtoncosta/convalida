package convalida.validators

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 01/11/2017.
 */
class LengthValidatorTest : BaseTest() {

    @Test
    fun textLengthLessThan5() {
        val validatorWithEditText = LengthValidator(mockEditText, 5, 0, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test")
        assertEquals(validatorWithEditText.validate(), false)
    }

    @Test
    fun textLengthGreaterThan5() {
        val validatorWithEditText = LengthValidator(mockEditText, 5, 0, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test@test")
        assertEquals(validatorWithEditText.validate(), true)
    }

    @Test
    fun textLengthLessThan8() {
        val validatorWithEditText = LengthValidator(mockEditText, 0, 8, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test@test")
        assertEquals(validatorWithEditText.validate(), false)
    }

    @Test
    fun textLengthGreaterThan8() {
        val validatorWithEditText = LengthValidator(mockEditText, 0, 9, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test@test")
        assertEquals(validatorWithEditText.validate(), true)
    }

}