package convalida.validators

import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 01/11/2017.
 */
class NotEmptyValidatorTest : BaseTest() {

    @Test
    fun emptyValue() {
        val validatorWithEditText = NotEmptyValidator(mockEditText, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("")
        assertEquals(validatorWithEditText.validate(), false)
    }

    @Test
    fun validValue() {
        val validatorWithEditText = NotEmptyValidator(mockEditText, errorMessage)
        `when`(mockEditText.text.toString()).thenReturn("test")
        assertEquals(validatorWithEditText.validate(), true)
    }

}