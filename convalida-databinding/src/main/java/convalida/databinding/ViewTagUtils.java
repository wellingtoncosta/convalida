package convalida.databinding;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import convalida.validators.AbstractValidator;

/**
 * @author WellingtonCosta on 29/03/18.
 */
public class ViewTagUtils {

    @SuppressWarnings(value = "unchecked")
    static void addValidatorToField(View field, AbstractValidator validator) {
        int tagId = R.id.validation_type;

        Object object = field.getTag(tagId);

        if(object instanceof List<?>) {
            ((List<AbstractValidator>)object).add(validator);
        } else {
            List<AbstractValidator> validators = new ArrayList<>();
            validators.add(validator);
            field.setTag(tagId, validators);
        }
    }

    public static List<View> getViewsByTag(ViewGroup root, int tagId) {
        List<View> views = new ArrayList<>();
        int childCount = root.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View child = root.getChildAt(i);
            if(child != null) {
                if (child instanceof ViewGroup) {
                    views.addAll(getViewsByTag((ViewGroup) child, tagId));
                }
                addViewWhenContainsTag(tagId, views, child);
            }
        }
        return views;
    }

    private static void addViewWhenContainsTag(int tagId, List<View> views, View view) {
        Object tagValue = view.getTag(tagId);
        if (tagValue != null) {
            views.add(view);
        }
    }

}
