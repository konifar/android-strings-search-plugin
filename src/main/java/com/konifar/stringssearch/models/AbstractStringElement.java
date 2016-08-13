package main.java.com.konifar.stringssearch.models;

import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStringElement implements StringElement {

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        return null;
    }

    @Override
    public void navigate(boolean canNavigate) {
        // Do nothing
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }

    @Override
    public abstract String getName();

    @Override
    public abstract String getTag();

    @Override
    public abstract String getValue();

}
