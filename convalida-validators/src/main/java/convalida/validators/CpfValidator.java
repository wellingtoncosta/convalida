package convalida.validators;

import android.widget.EditText;

/**
 * @author WellingtonCosta on 25/04/18.
 */
public class CpfValidator extends AbstractValidator {

    public CpfValidator(EditText editText, String errorMessage, boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
    }

    @Override
    public boolean isNotValid(String value) {
        if(value.isEmpty() || value.length() < 11) return true;
        boolean hasOnlyDigits = value.matches("\\d{11}");
        boolean isNotInBlackList = !inBlackList(value);
        int charAt9Position = Character.getNumericValue(value.charAt(9));
        int charAt10Position = Character.getNumericValue(value.charAt(10));
        boolean digit9IsValid = cpfDv(value, 1) == charAt9Position;
        boolean digit10IsValid = cpfDv(value, 2) == charAt10Position;
        return !(hasOnlyDigits && isNotInBlackList && digit9IsValid && digit10IsValid);
    }

    private static int cpfDv(final String cpf, final int step) {
        final int dv = 11 - cpfSum(cpf, step) % 11;
        return (dv == 10 || dv == 11) ? 0 : dv;
    }

    private static int cpfSum(final String rawCPF, final int step) {
        int sum = 0;
        final int count = 8 + step;
        final int baseMultiplier = 9 + step;
        for (int i = 0; i < count; ++i) {
            sum += ((baseMultiplier - i) * Character.getNumericValue(rawCPF.charAt(i)));
        }
        return sum;
    }

    private static boolean inBlackList(String cpf) {
        boolean equal = true;
        for (int i = 1; i < 11 && equal; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                equal = false;
            }
        }
        return equal || cpf.equals("12345678909");
    }

}
