package convalida.validators

import java.util.ArrayList
import java.util.HashSet

/**
 * @author Wellington Costa on 21/06/2017.
 */
class ValidatorSet {

    private val validators: MutableSet<Validator>
    private var isValid: Boolean = false

    internal val validatorsSize: Int
        get() = validators.size

    init {
        this.validators = HashSet()
        this.isValid = true
    }

    fun addValidator(validator: Validator) {
        this.validators.add(validator)
    }

    fun isValid(): Boolean {
        executeValidators()
        return isValid
    }

    private fun executeValidators() {
        val validationResults = ArrayList<Boolean>()

        for (validator in validators) {
            validationResults.add(validator.validate())
        }

        for (validationResult in validationResults) {
            isValid = validationResult
            if (!validationResult) {
                break
            }
        }
    }

    fun clearValidators() {
        for (validator in validators) {
            validator.clear()
        }
    }

}