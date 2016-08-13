package main.java.com.konifar.stringssearch.models;

public final class QuantityStringElement extends AbstractStringElement {

    private final String quantity;

    private final String value;

    public QuantityStringElement(String quantity, String value) {
        this.quantity = quantity;
        this.value = value;
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

}
