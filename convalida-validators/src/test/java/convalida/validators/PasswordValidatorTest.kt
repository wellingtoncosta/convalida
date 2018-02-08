package convalida.validators

import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 01/11/2017.
 */
class PasswordValidatorTest : BaseTest() {

    @Test
    fun emptyPassword() {
        val validator = PasswordValidator(mockEditText, 0, "", errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun passwordLessThan5() {
        val validator = PasswordValidator(mockEditText, 5, "", errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("test")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun passwordGreaterThan5() {
        val validator = PasswordValidator(mockEditText, 5, "", errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("test123")
        assertEquals(validator.validate(), true)
    }

    @Test
    fun passwordNotContainsLettersAndNumbersCaseInsensitive() {
        val validator = PasswordValidator(mockEditText, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("qweQWE")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun passwordContainsLettersAndNumbersCaseInsensitive() {
        val validator = PasswordValidator(mockEditText, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("qweQWE123")
        assertEquals(validator.validate(), true)
    }

    @Test
    fun passwordContainsLettersAndNumbersCaseInsensitiveAndLengthLessThan5() {
        val validator = PasswordValidator(mockEditText, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("qW3")
        assertEquals(validator.validate(), false)
    }

    @Test
    fun passwordContainsLettersAndNumbersCaseInsensitiveAndLengthGreaterThan5() {
        val validator = PasswordValidator(mockEditText, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true)
        `when`(mockEditText.text.toString()).thenReturn("qweQWE123")
        assertEquals(validator.validate(), true)
    }

    companion object {
        private const val LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$"
    }

}