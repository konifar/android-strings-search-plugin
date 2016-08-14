package main.java.com.konifar.stringssearch;

import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import main.java.com.konifar.stringssearch.models.StringElement;
import main.java.com.konifar.stringssearch.search.SearchStringFilterFactory;
import main.java.com.konifar.stringssearch.search.SearchStringItemProvider;
import main.java.com.konifar.stringssearch.search.SearchStringModel;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

public class SearchStringsAction extends GotoActionBase implements DumbAware {

    @Override
    protected void gotoActionPerformed(AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
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
                if (element != null && element instanceof StringElement) {
                    insertToEditor(project, (StringElement) element);
                }
            }
        };

        SearchStringItemProvider provider = new SearchStringItemProvider(getPsiContext(e));
        showNavigationPopup(e, searchStringModel, callback, "strings matching pattern", true, true, provider);
    }

    private void insertToEditor(final Project project, final StringElement stringElement) {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                        if (editor != null) {
                            int offset = editor.getCaretModel().getOffset();
                            Document document = editor.getDocument();
                            String key = stringElement.getName();
                            if (key != null) {
                                document.insertString(offset, key);
                                editor.getCaretModel().moveToOffset(offset + key.length());
                            }
                        }
                    }
                });
            }
        }, "WriteStringKeyCommand", "", UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
    }
}
