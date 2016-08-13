package main.java.com.konifar.stringssearch.models;

import java.util.LinkedList;
import java.util.List;

public final class PluralStringElement extends AbstractStringElement {

    private String name;

    private final LinkedList<QuantityStringElement> quantities = new LinkedList<QuantityStringElement>();

    public PluralStringElement(String name, List<QuantityStringElement> quantities) {
        this.name = name;
        this.quantities.addAll(quantities);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return "plurals";
    }

    @Override
    public String getValue() {
        if (quantities.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < quantities.size(); i++) {
            QuantityStringElement quantity = quantities.get(i);
            builder.append(quantity.getValue());
            if (i < quantities.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

}
