package convalida.validators

import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 01/11/2017.
 */
class OnlyNumberValidatorTest : BaseTest() {

    @Test
    fun valueIsNotNumber() {
        val validatorWithEditText = OnlyNumberValidator(mockEditText, errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("test")
        assertEquals(validatorWithEditText.validate(), false)
    }

    @Test
    fun valueIsNumber() {
        val validatorWithEditText = OnlyNumberValidator(mockEditText, errorMessage, true)
        `when`(mockEditText.getText().toString()).thenReturn("1234")
        assertEquals(validatorWithEditText.validate(), true)
    }

}