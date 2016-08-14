package main.java.com.konifar.stringssearch.search;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.text.Matcher;
import com.intellij.util.text.MatcherHolder;
import com.intellij.util.ui.UIUtil;
import icons.AndroidIcons;
import main.java.com.konifar.stringssearch.models.StringElement;

import javax.swing.*;
import java.awt.*;

/**
 * @see com.intellij.ide.util.PsiElementListCellRenderer
 */
final class StringElementListCellRenderer extends JPanel implements ListCellRenderer, MatcherHolder {

    private Matcher matcher;

    StringElementListCellRenderer() {
        super(new BorderLayout());
    }

    @Override
    public void setPatternMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        removeAll();

        // Right
        DefaultListCellRenderer rightRenderer = new RightCellRenderer();
        Component rightComponent = rightRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        add(rightComponent, BorderLayout.EAST);

        // Space
        JPanel spacer = new JPanel();
        spacer.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        add(spacer, BorderLayout.CENTER);

        // Left
        ListCellRenderer leftRenderer = new LeftCellRenderer(matcher);
        Component leftComponent = leftRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        add(leftComponent, BorderLayout.WEST);

        // Background color
        Color bg = isSelected ? UIUtil.getListSelectionBackground() : leftComponent.getBackground();
        setBackground(bg);
        rightComponent.setBackground(bg);
        spacer.setBackground(bg);

        return this;
    }

    private class LeftCellRenderer extends ColoredListCellRenderer {
        private final Matcher matcher;

        private LeftCellRenderer(Matcher matcher) {
            this.matcher = matcher;
        }

        @Override
        protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
            Color bgColor = UIUtil.getListBackground();
            setPaintFocusBorder(hasFocus && UIUtil.isToUseDottedCellBorder());

            if (value instanceof StringElement) {
                StringElement element = (StringElement) value;
                String stringKeyText = "(" + element.getName() + ")";
                String text = new StringEllipsisPolicy().ellipsizeText(element.getValue(), matcher);

                SimpleTextAttributes nameAttributes = new SimpleTextAttributes(Font.PLAIN, list.getForeground());
                SpeedSearchUtil.appendColoredFragmentForMatcher(text, this, nameAttributes, matcher, bgColor, selected);
                // TODO Change icon
                setIcon(AndroidIcons.EmptyFlag);

                append(" " + stringKeyText, new SimpleTextAttributes(Font.PLAIN, JBColor.GRAY));
            }

            setBackground(selected ? UIUtil.getListSelectionBackground() : bgColor);
        }
    }

    private class RightCellRenderer extends DefaultListCellRenderer {

        RightCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            customize(value, isSelected);
            return component;
        }

        private void customize(Object value, boolean selected) {
            if (value instanceof StringElement) {
                StringElement element = (StringElement) value;

                // TODO Change icon to each countries flags.
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
