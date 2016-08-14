package main.java.com.konifar.stringssearch.search;

import com.intellij.ide.util.gotoByName.ChooseByNameBase;
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import main.java.com.konifar.stringssearch.models.StringElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class SearchStringItemProvider extends DefaultChooseByNameItemProvider {

    public SearchStringItemProvider(@Nullable PsiElement context) {
        super(context);
    }

    @Override
    public boolean filterElements(@NotNull ChooseByNameBase base,
                                  @NotNull String pattern,
                                  boolean everywhere,
                                  @NotNull ProgressIndicator indicator,
                                  @NotNull Processor<Object> consumer) {
        Collection<StringElement> elements = ((SearchStringModel) base.getModel()).getFilterItems();

        if (elements != null) {
            for (StringElement element : elements) {
                String value = element.getValue();

                if (value == null) {
                    return false;
                }

                if (value.toLowerCase().contains(pattern.toLowerCase()) && !consumer.process(element)) {
                    return false;
                }
            }
        }

        return false;
    }
}
