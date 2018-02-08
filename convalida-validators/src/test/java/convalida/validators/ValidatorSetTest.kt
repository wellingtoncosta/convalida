package convalida.validators

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.`when`

/**
 * @author Wellington Costa on 22/01/2018.
 */
class ValidatorSetTest : BaseTest() {

    private lateinit var validatorSet: ValidatorSet

    @Before
    fun setupValidator() {
        validatorSet = ValidatorSet()
    }

    @Test
    fun addOneValidator() {
        validatorSet.addValidator(NotEmptyValidator(mockEditText, errorMessage))
        assertEquals(validatorSet.validatorsSize, 1)
    }

    @Test
    fun addTwoValidators() {
        validatorSet.addValidator(NotEmptyValidator(mockEditText, errorMessage))
        validatorSet.addValidator(EmailValidator(mockEditText, errorMessage))
        assertEquals(validatorSet.validatorsSize, 2)
    }

    @Test
    fun addThreeValidators() {
        validatorSet.addValidator(NotEmptyValidator(mockEditText, errorMessage))
        validatorSet.addValidator(EmailValidator(mockEditText, errorMessage))
        validatorSet.addValidator(LengthValidator(mockEditText,0, 5, errorMessage))
        assertEquals(validatorSet.validatorsSize, 3)
    }

    @Test
    fun executeValidationsWithSuccess() {
        validatorSet.addValidator(NotEmptyValidator(mockEditText, errorMessage))
        `when`(mockEditText.text.toString()).thenReturn("test")
        assertEquals(validatorSet.isValid(), true)
    }

    @Test
    fun executeValidationsWithError() {
        validatorSet.addValidator(NotEmptyValidator(mockEditText, errorMessage))
        `when`(mockEditText.text.toString()).thenReturn("")
        assertEquals(validatorSet.isValid(), false)
    }

    @Test
    fun clearValidations() {
        validatorSet.addValidator(NotEmptyValidator(mockEditText, errorMessage))
        validatorSet.clearValidators()
        assertEquals(mockEditText.error, null)
    }

}