package main.java.com.konifar.stringssearch.models;

public final class QuantityStringElement extends AbstractStringElement {

    private final String quantity;

    private final String value;

    private final String parentDirName;

    public QuantityStringElement(String quantity, String value, String parentDirName) {
        this.quantity = quantity;
        this.value = value;
        this.parentDirName = parentDirName;
    }

    @Override
    public String getName() {
        return quantity;
    }

    @Override
    public String getTag() {
        return "quantity";
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
