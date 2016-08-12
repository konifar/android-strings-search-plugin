package main.java.com.konifar.stringssearch;

import com.intellij.codeInsight.actions.SimpleCodeInsightAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import main.java.com.konifar.stringssearch.utils.PluginUtil;
import org.jetbrains.annotations.NotNull;

public class SearchStringsAction extends SimpleCodeInsightAction {

    private Project project;

    @Override
    public void actionPerformed(AnActionEvent event) {
        project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        invoke(project, editor, file);
    }

    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
        // TODO
        PluginUtil.showInfoNotification(project, "Test");
    }
}
