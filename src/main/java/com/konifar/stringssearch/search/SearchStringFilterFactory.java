package main.java.com.konifar.stringssearch.search;

import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import icons.AndroidIcons;
import main.java.com.konifar.stringssearch.models.NormalStringElement;
import main.java.com.konifar.stringssearch.models.PluralStringElement;
import main.java.com.konifar.stringssearch.models.QuantityStringElement;
import main.java.com.konifar.stringssearch.models.StringElement;
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

public final class SearchStringFilterFactory {

    private static SearchStringFilterFactory INSTANCE;

    private Project project;

    public static SearchStringFilterFactory get(Project project) {
        if (INSTANCE == null) {
            INSTANCE = new SearchStringFilterFactory(project);
        }
        return INSTANCE;
    }

    private SearchStringFilterFactory(Project project) {
        this.project = project;
    }

    public SearchStringFilter create(ChooseByNamePopup popup, SearchStringModel model) {
        return new SearchStringFilter(popup, model, project);
    }

    private class SearchStringFilter extends ChooseByNameFilter<StringElement> {

        private static final String STRINGS_XML = "strings.xml";

        private SearchStringFilter(ChooseByNamePopup popup, SearchStringModel model, Project project) {
            super(popup, model, SearchStringConfiguration.getInstance(project), project);
        }

        @Override
        @NotNull
        protected List<StringElement> getAllFilterValues() {
            return loadStringElements();
        }

        @Override
        protected String textForFilterValue(@NotNull StringElement element) {
            return element.getValue();
        }

        @Override
        protected Icon iconForFilterValue(@NotNull StringElement element) {
            return AndroidIcons.AndroidFile;
        }

        private List<StringElement> loadStringElements() {
            List<StringElement> result = new LinkedList<StringElement>();

            PsiFile[] files = FilenameIndex.getFilesByName(project, STRINGS_XML, GlobalSearchScope.projectScope(project));

            try {
                PsiFile file = files[0];
                if (file != null) {
                    InputStream is = new ByteArrayInputStream(file.getText().getBytes());

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
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

    }

}
