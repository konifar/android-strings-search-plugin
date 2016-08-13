package main.java.com.konifar.stringssearch.search;

import com.intellij.ide.util.gotoByName.ChooseByNameFilterConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import main.java.com.konifar.stringssearch.models.StringElement;

/**
 * Configuration for strings filtering popup in "Search string" search.
 *
 * @author konifar
 */
@State(
        name = "SearchStringConfiguration",
        storages = {@Storage(file = StoragePathMacros.WORKSPACE_FILE)}
)
public final class SearchStringConfiguration extends ChooseByNameFilterConfiguration<StringElement> {

    /**
     * Get configuration instance
     *
     * @param project a project instance
     * @return a configuration instance
     */
    public static SearchStringConfiguration getInstance(Project project) {
        return ServiceManager.getService(project, SearchStringConfiguration.class);
    }

    @Override
    protected String nameForElement(StringElement element) {
        return element.getName();
    }

}
