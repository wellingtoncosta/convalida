package convalida.compiler.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

/**
 * @author Wellington Costa on 10/07/2017.
 */
public final class Id {
    private static final ClassName ANDROID_R = ClassName.get("android", "R");

    private final int value;
    public final CodeBlock code;

    public Id(int value) {
        this.value = value;
        this.code = CodeBlock.of("$L", value);
    }

    public Id(int value, ClassName className, String resourceName) {
        this.value = value;
        this.code = className.topLevelClassName().equals(ANDROID_R)
                ? CodeBlock.of("$L.$N", className, resourceName)
                : CodeBlock.of("$T.$N", className, resourceName);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Id && value == ((Id) o).value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Please use value or code explicitly");
    }
}