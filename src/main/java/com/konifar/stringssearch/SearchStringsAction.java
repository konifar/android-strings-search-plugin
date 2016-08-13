package main.java.com.konifar.stringssearch;

import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import main.java.com.konifar.stringssearch.models.NormalStringElement;
import main.java.com.konifar.stringssearch.models.PluralStringElement;
import main.java.com.konifar.stringssearch.models.QuantityStringElement;
import main.java.com.konifar.stringssearch.models.StringElement;
import main.java.com.konifar.stringssearch.search.SearchStringConfiguration;
import main.java.com.konifar.stringssearch.search.SearchStringItemProvider;
import main.java.com.konifar.stringssearch.search.SearchStringModel;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class SearchStringsAction extends GotoActionBase implements DumbAware {

    private static final String STRINGS_XML = "strings.xml";

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
                return new ChooseStringFilter(popup, searchStringModel, project);
            }

            @Override
            public void elementChosen(ChooseByNamePopup popup, Object element) {
                if (element == null) return;
                // TODO
            }
        };

        SearchStringItemProvider provider = new SearchStringItemProvider(project, getPsiContext(e));
        showNavigationPopup(e, searchStringModel, callback, "strings matching pattern", true, false, provider);
    }

    private class ChooseStringFilter extends ChooseByNameFilter<StringElement> {

        ChooseStringFilter(final ChooseByNamePopup popup, SearchStringModel model, final Project project) {
            super(popup, model, SearchStringConfiguration.getInstance(project), project);
        }

        @Override
        @NotNull
        protected List<StringElement> getAllFilterValues() {
            return loadStringElements(project);
        }

        @Override
        protected String textForFilterValue(@NotNull StringElement element) {
            return element.getValue();
        }

        @Override
        protected Icon iconForFilterValue(@NotNull StringElement value) {
            return null;
        }

    }

    private List<StringElement> loadStringElements(Project project) {
        List<StringElement> result = new LinkedList<StringElement>();

        PsiFile[] files = FilenameIndex.getFilesByName(project, STRINGS_XML, GlobalSearchScope.projectScope(project));

        try {
            PsiFile file = files[0];
            if (file != null) {
                String text = file.getText();
                InputStream is = new ByteArrayInputStream(text.getBytes());

                Document doc = JDOMUtil.loadDocument(is);
                Element root = doc.getRootElement();
                Element resources = root.getChild("resources");
                if (resources != null) root = resources;

                List<Element> elements = root.getChildren();
                for (Element element : elements) {
                    if ("plurals".equals(element.getName())) {
                        List<QuantityStringElement> quantities = new LinkedList<QuantityStringElement>();
                        List<Element> items = element.getChildren("item");
                        for (Element item : items) {
                            String key = item.getAttributeValue("quantity");
                            String value = item.getText();
                            quantities.add(new QuantityStringElement(key, value));
                        }
                        result.add(new PluralStringElement(element.getAttributeValue("name"), quantities));
                    } else {
                        String key = element.getAttributeValue("name");
                        String value = element.getText();
                        result.add(new NormalStringElement(key, value));
                    }
                }
            }

            for (StringElement element : result) {
                System.out.println(element.getValue());
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
