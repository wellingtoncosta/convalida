package convalida.validators

import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 01/11/2017.
 */
class PatternValidatorTest : BaseTest() {

    @Test
    fun valueNotContainsLettersAndNumbers() {
        val validatorWithEditText = PatternValidator(mockEditText, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, true)
        `when`(mockEditText.text.toString()).thenReturn("qweQWE")
        assertEquals(validatorWithEditText.validate(), false)
    }

    @Test
    fun valueContainsLettersAndNumbers() {
        val validatorWithEditText = PatternValidator(mockEditText, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, true)
        `when`(mockEditText.text.toString()).thenReturn("qweQWE123")
        assertEquals(validatorWithEditText.validate(), true)
    }

    companion object {
        private const val LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$"
    }

}