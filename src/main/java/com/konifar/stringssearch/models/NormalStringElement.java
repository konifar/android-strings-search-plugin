package main.java.com.konifar.stringssearch.models;

public final class NormalStringElement extends AbstractStringElement {

    private final String name;

    private final String value;

    public NormalStringElement(String name, String value) {
        this.value = value;
        this.name = name;
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

}
