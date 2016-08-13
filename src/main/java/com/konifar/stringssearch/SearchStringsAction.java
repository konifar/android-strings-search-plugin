package main.java.com.konifar.stringssearch;

import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import main.java.com.konifar.stringssearch.models.StringElement;
import main.java.com.konifar.stringssearch.search.SearchStringFilterFactory;
import main.java.com.konifar.stringssearch.search.SearchStringItemProvider;
import main.java.com.konifar.stringssearch.search.SearchStringModel;
import org.jetbrains.annotations.NotNull;

public class SearchStringsAction extends GotoActionBase implements DumbAware {

    private Project project;

    @Override
    protected void gotoActionPerformed(AnActionEvent e) {
        project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) return;

        FeatureUsageTracker.getInstance().triggerFeatureUsed("navigation.popup.file");

        final SearchStringModel searchStringModel = new SearchStringModel(project);
        GotoActionCallback<StringElement> callback = new GotoActionCallback<StringElement>() {
            @Override
            protected ChooseByNameFilter<StringElement> createFilter(@NotNull ChooseByNamePopup popup) {
                return SearchStringFilterFactory.get(project).create(popup, searchStringModel);
            }

            @Override
            public void elementChosen(ChooseByNamePopup popup, Object element) {
                if (element == null) return;
                // TODO
            }
        };

        SearchStringItemProvider provider = new SearchStringItemProvider(project, getPsiContext(e));
        showNavigationPopup(e, searchStringModel, callback, "strings matching pattern", true, true, provider);
    }

}
