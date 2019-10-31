package convalida.compiler;

import java.util.Objects;

import javax.annotation.processing.ProcessingEnvironment;

public class ProcessingOptions {

    // Can not be instantiated
    private ProcessingOptions() { }

    private static ProcessingEnvironment processingEnvironment;

    private static boolean initialized = false;

    private static final String DATABINDING_ENABLED_OPTION = "android.databinding.enableV2";

    static void init(ProcessingEnvironment processingEnvironment) {
        if (initialized) {
            throw new IllegalStateException("Cannot call init more than once.");
        } else {
            Objects.requireNonNull(
                    processingEnvironment,
                    "processingEnvironment parameter cannot be null."
            );

            initialized = true;

            ProcessingOptions.processingEnvironment = processingEnvironment;
        }
    }

    public static boolean isDatabindingEnabled() {
        return processingEnvironment.getOptions().get(DATABINDING_ENABLED_OPTION).equals("1");
    }

}
