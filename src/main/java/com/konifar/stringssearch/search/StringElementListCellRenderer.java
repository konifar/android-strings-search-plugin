package main.java.com.konifar.stringssearch.search;

import com.intellij.icons.AllIcons;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.Function;
import com.intellij.util.IconUtil;
import com.intellij.util.text.Matcher;
import com.intellij.util.text.MatcherHolder;
import com.intellij.util.ui.UIUtil;
import main.java.com.konifar.stringssearch.models.StringElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

/**
 * @see com.intellij.ide.util.PsiElementListCellRenderer
 */
final class StringElementListCellRenderer extends JPanel implements ListCellRenderer, MatcherHolder {

    private Project project;

    private int maxWidth;

    private Matcher matcher;

    private int rightComponentWidth;

    private boolean focusBorderEnabled = true;

    StringElementListCellRenderer(Project project, int maxSize) {
        super(new BorderLayout());
        this.project = project;
        this.maxWidth = maxSize;
    }

    private static Color getBackgroundColor(@Nullable Object value) {
        return UIUtil.getListBackground();
    }

    @Override
    public void setPatternMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        removeAll();
        rightComponentWidth = 0;
        DefaultListCellRenderer rightRenderer = getRightCellRenderer(value);
        Component rightCellRendererComponent = null;
        JPanel spacer = null;
        if (rightRenderer != null) {
            rightCellRendererComponent = rightRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            add(rightCellRendererComponent, BorderLayout.EAST);
            spacer = new JPanel();
            spacer.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            add(spacer, BorderLayout.CENTER);
            rightComponentWidth = rightCellRendererComponent.getPreferredSize().width;
            rightComponentWidth += spacer.getPreferredSize().width;
        }

        ListCellRenderer leftRenderer = new LeftRenderer(null, matcher);
        final Component leftCellRendererComponent = leftRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        add(leftCellRendererComponent, BorderLayout.WEST);
        final Color bg = isSelected ? UIUtil.getListSelectionBackground() : leftCellRendererComponent.getBackground();
        setBackground(bg);
        if (rightCellRendererComponent != null) {
            rightCellRendererComponent.setBackground(bg);
        }
        if (spacer != null) {
            spacer.setBackground(bg);
        }
        return this;
    }

    @Nullable
    private DefaultListCellRenderer getRightCellRenderer(final Object value) {
        return new RightCellRenderer();
    }

    private class LeftRenderer extends ColoredListCellRenderer {
        private final String moduleName;
        private final Matcher matcher;

        public LeftRenderer(final String moduleName, Matcher matcher) {
            this.moduleName = moduleName;
            this.matcher = matcher;
        }

        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            Color bgColor = UIUtil.getListBackground();
            Color color = list.getForeground();
            setPaintFocusBorder(hasFocus && UIUtil.isToUseDottedCellBorder() && focusBorderEnabled);
            if (value instanceof StringElement) {
                StringElement element = (StringElement) value;
                String name = element.getValue();

                TextAttributes attributes = getNavigationItemAttributes(value);

                SimpleTextAttributes nameAttributes = attributes != null
                        ? SimpleTextAttributes.fromTextAttributes(attributes)
                        : null;

                if (nameAttributes == null) {
                    nameAttributes = new SimpleTextAttributes(Font.PLAIN, color);
                }

                assert name != null : "Null name for PSI element " + element + " (by " + StringElementListCellRenderer.this + ")";
                SpeedSearchUtil.appendColoredFragmentForMatcher(name, this, nameAttributes, matcher, bgColor, selected);
//                setIcon(StringElementListCellRenderer.this.getIcon(element));

                String containerText = getContainerTextForLeftComponent(element, name + (moduleName != null ? moduleName + "        " : ""));
                if (containerText != null) {
                    append(" " + containerText, new SimpleTextAttributes(Font.PLAIN, JBColor.GRAY));
                }
            } else if (!customizeNonPsiElementLeftRenderer(this, list, value, index, selected, hasFocus)) {
                setIcon(IconUtil.getEmptyIcon(false));
                append(value == null ? "" : value.toString(), new SimpleTextAttributes(Font.PLAIN, list.getForeground()));
            }
            setBackground(selected ? UIUtil.getListSelectionBackground() : bgColor);
        }

    }

    @Nullable
    private TextAttributes getNavigationItemAttributes(Object value) {
        TextAttributes attributes = null;

        if (value instanceof NavigationItem) {
            TextAttributesKey attributesKey = null;
            final ItemPresentation presentation = ((NavigationItem) value).getPresentation();
            if (presentation instanceof ColoredItemPresentation) attributesKey =
                    ((ColoredItemPresentation) presentation).getTextAttributesKey();

            if (attributesKey != null) {
                attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(attributesKey);
            }
        }
        return attributes;
    }

    @Nullable
    protected String getContainerTextForLeftComponent(StringElement element, final String name) {
        return "(" + element.getName() + ")";
    }

    protected boolean customizeNonPsiElementLeftRenderer(ColoredListCellRenderer renderer,
                                                         JList list,
                                                         Object value,
                                                         int index,
                                                         boolean selected,
                                                         boolean hasFocus) {
        if (!(value instanceof NavigationItem)) return false;

        NavigationItem item = (NavigationItem) value;

        TextAttributes attributes = getNavigationItemAttributes(item);

        SimpleTextAttributes nameAttributes = attributes != null ? SimpleTextAttributes.fromTextAttributes(attributes) : null;

        Color color = list.getForeground();
        if (nameAttributes == null) nameAttributes = new SimpleTextAttributes(Font.PLAIN, color);

        renderer.append(item + " ", nameAttributes);
        ItemPresentation itemPresentation = item.getPresentation();
        assert itemPresentation != null;
        renderer.setIcon(itemPresentation.getIcon(true));

        String locationString = itemPresentation.getLocationString();
        if (!StringUtil.isEmpty(locationString)) {
            renderer.append(locationString, new SimpleTextAttributes(Font.PLAIN, JBColor.GRAY));
        }
        return true;
    }

    protected void setFocusBorderEnabled(boolean enabled) {
        focusBorderEnabled = enabled;
    }

    public Comparator<StringElement> getComparator() {
        return new Comparator<StringElement>() {
            @Override
            public int compare(StringElement o1, StringElement o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
    }

    public void installSpeedSearch(PopupChooserBuilder builder) {
        installSpeedSearch(builder, false);
    }

    public void installSpeedSearch(PopupChooserBuilder builder, final boolean includeContainerText) {
        builder.setFilteringEnabled(new Function<Object, String>() {
            @Override
            public String fun(Object o) {
                if (o instanceof StringElement) {
                    if (includeContainerText) {
                        return ((StringElement) o).getValue();
                    }
                    return ((StringElement) o).getValue();
                } else {
                    return o.toString();
                }
            }
        });
    }

    protected int getIconFlags() {
        return Iconable.ICON_FLAG_READ_STATUS;
    }

    private class RightCellRenderer extends DefaultListCellRenderer {

        RightCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component listCellRendererComponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            customizeCellRenderer(value, isSelected);
            return listCellRendererComponent;
        }

        private void customizeCellRenderer(Object value, boolean selected) {
            if (value instanceof StringElement) {
                StringElement element = (StringElement) value;

                setIcon(AllIcons.Modules.SourceFolder);
                setText(element.getParentDirName());

                setBorder(BorderFactory.createEmptyBorder(0, 0, 0, UIUtil.getListCellHPadding()));
                setHorizontalTextPosition(2);
                setBackground(selected ? UIUtil.getListSelectionBackground() : UIUtil.getListBackground());
                setForeground(selected ? UIUtil.getListSelectionForeground() : UIUtil.getInactiveTextColor());

                if (UIUtil.isUnderNimbusLookAndFeel()) {
                    setOpaque(false);
                }
            }
        }
    }
}
