package convalida.compiler.model;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

/**
 * @author Wellington Costa on 31/07/2019.
 */
public class CommonValidationParameters {

    public final Class<? extends Annotation> annotation;
    public final Element element;
    public final int errorMessageResId;
    public final boolean autoDismiss;

    public CommonValidationParameters(
            Class<? extends Annotation> annotation,
            Element element,
            int errorMessageResId,
            boolean autoDismiss
    ) {
        this.annotation = annotation;
        this.element = element;
        this.errorMessageResId = errorMessageResId;
        this.autoDismiss = autoDismiss;
    }

}
