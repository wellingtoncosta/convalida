package convalida.validators;

import android.widget.EditText;

import static convalida.validators.util.Strings.repeat;

/**
 * @author WellingtonCosta on 26/04/18.
 */
public class CnpjValidator extends AbstractValidator {

    private boolean required;

    private static final int CNPJ_LENGHT = 14;

    public CnpjValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss,
            boolean required
    ) {
        super(editText, errorMessage, autoDismiss);
        this.required = required;
    }

    @Override public boolean isValid(String value) {
        value = value
                .replace(".", "")
                .replace("/", "")
                .replace("-", "")
                .replace(" ", "");

        if(required && value.isEmpty()) return false;

        boolean isValid = validLength(value) && !isRepeated(value) && calculateCnpj(value);

        return (!required || !value.isEmpty()) && (value.isEmpty() || isValid);
    }

    private static boolean validLength(String cnpj) {
        return cnpj.length() == CNPJ_LENGHT;
    }

    private static boolean isRepeated(String cnpj) {
        boolean isRepeated = false;

        for(int i = 0; i < 9 && !isRepeated; i ++) {
            String repeated = repeat(String.valueOf(i), CNPJ_LENGHT);
            if(repeated.equals(cnpj)) {
                isRepeated = true;
            }
        }

        return isRepeated;
    }

    private static boolean calculateCnpj(String cnpj) {
        Integer sum = 0;

        char[] characteres = cnpj.toCharArray();

        String calculatedCnpj = cnpj.substring(0, 12);

        calculatedCnpj = calculatePart(sum, characteres, calculatedCnpj, 4, 6);

        sum = 0;

        calculatedCnpj = calculatePart(sum, characteres, calculatedCnpj, 5, 7);

        return cnpj.equals(calculatedCnpj);
    }

    private static String calculatePart(
            Integer sum,
            char[] characteres,
            String calculatedCnpj,
            int charAt,
            int sumFactor
    ) {
        int digit;

        for (int i = 0; i < charAt; i++) {
            if (characteres[i] - 48 >= 0 && characteres[i] - 48 <= 9) {
                sum += (characteres[i] - 48) * (sumFactor - (i + 1));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (characteres[i + charAt] - 48 >= 0 && characteres[i + charAt] - 48 <= 9) {
                sum += (characteres[i + charAt] - 48) * (10 - (i + 1));
            }
        }

        digit = 11 - (sum % 11);

        calculatedCnpj += (digit == 10 || digit == 11) ? "0" : Integer.toString(digit);

        return calculatedCnpj;
    }

}
