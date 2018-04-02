package convalida.databinding;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import convalida.validators.AbstractValidator;
import convalida.validators.ValidatorSet;

/**
 * @author WellingtonCosta on 29/03/18.
 */
public class ValidationUtils {

    private static ValidatorSet validatorSet;

    static {
        validatorSet = new ValidatorSet();
    }

    public static boolean validateFields(View root) {
        if(validatorSet.getValidatorsCount() == 0) {
            List<View> views = ViewTagUtils.getViewsByTag(
                    (ViewGroup) root,
                    R.id.validation_type
            );
            for (View view : views) {
                validatorSet.addValidator(
                        (AbstractValidator) view.getTag(R.id.validation_type)
                );
            }
        }
        return validatorSet.isValid();
    }

    public static void clearValidations(View root) {
        if(validatorSet.getValidatorsCount() == 0) {
            List<View> views = ViewTagUtils.getViewsByTag(
                    (ViewGroup) root,
                    R.id.validation_type
            );
            for(View view : views) {
                validatorSet.addValidator(
                        (AbstractValidator) view.getTag(R.id.validation_type)
                );
            }
        }
        validatorSet.clearValidators();
    }

}