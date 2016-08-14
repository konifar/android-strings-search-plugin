package main.java.com.konifar.stringssearch.models;

public final class NormalStringElement implements StringElement {

    private final String name;

    private final String value;

    private final String parentDirName;

    public NormalStringElement(String name, String value, String parentDirName) {
        this.value = value;
        this.name = name;
        this.parentDirName = parentDirName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return "string";
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getParentDirName() {
        return parentDirName;
    }

}
