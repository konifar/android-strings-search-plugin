package main.java.com.konifar.stringssearch.search;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.util.containers.FList;
import com.intellij.util.text.Matcher;
import org.jetbrains.annotations.NotNull;

final class StringEllipsisPolicy {

    private static final int MAX_LENGTH = 100;

    private static final int ELLIPSIS_EXTRA_LENGTH = 20;

    private static final String DOTS = "...";

    String ellipsizeText(@NotNull String text, Matcher matcher) {
        final int textLength = text.length();

        if (textLength > MAX_LENGTH) {
            if (matcher instanceof MinusculeMatcher) {
                FList iterable = ((MinusculeMatcher) matcher).matchingFragments(text);
                if (iterable != null) {
                    TextRange textRange = (TextRange) iterable.getHead();
                    int startOffset = textRange.getStartOffset();

                    int startIndex = 0;
                    int endIndex = textLength;

                    if (startOffset > ELLIPSIS_EXTRA_LENGTH) {
                        startIndex = startOffset - ELLIPSIS_EXTRA_LENGTH;
                    }
                    if (textLength > startIndex + MAX_LENGTH) {
                        endIndex = startIndex + MAX_LENGTH;
                    }

                    String ellipsizedText = text.substring(startIndex, endIndex);

                    if (startIndex > 0) {
                        ellipsizedText = DOTS + ellipsizedText;
                    }
                    if (endIndex < textLength) {
                        ellipsizedText = ellipsizedText + DOTS;
                    }

                    return ellipsizedText;
                }
            }
        }

        return text;
    }

}
