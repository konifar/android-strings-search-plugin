package main.java.com.konifar.stringssearch.models;

import com.intellij.navigation.NavigationItem;

public interface StringElement extends NavigationItem {

    String getName();

    String getTag();

    String getValue();

    String getParentDirName();

}
