package introduction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Проверка орфографии
 * <p/>
 * Некоторые люди не обращают внимание на орфографию. Например, не пишут новое
 * предложение с заглавной буквы. Или не ставят пробел после знаков препинания.
 * <p/>
 * Ваша задача: исправить их ошибки.
 * <p/>
 * Что нужно сделать:
 * <p/>
 * 1. Каждое новое предложение должно начинаться с заглавной буквы.
 * 2. После знаков препинания (точка и запятая) должны быть пробелы.
 */
public class TextUtils {

    public static final String SENTENCE_REGEX_PATTERN = "(\\p{L}+[\\p{L},;'\"\\s\\-]+[.?!]+)+";

    public String correctText(String text) {
        StringBuilder builder = new StringBuilder(text);
        correctSpaceAfterPunctuationSymbolRule(builder);
        correctTextEndingRule(builder);
        correctSentencesStartWithCapitalLetterRule(builder);
        return builder.toString();
    }

    private StringBuilder correctSentencesStartWithCapitalLetterRule(StringBuilder builder) {
        Pattern pattern = Pattern.compile(SENTENCE_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(builder.toString());
        while (matcher.find()) {
            char sentenceStartSymbol = builder.charAt(matcher.start());
            if (Character.isLowerCase(sentenceStartSymbol)) {
                builder.setCharAt(matcher.start(), Character.toUpperCase(sentenceStartSymbol));
            }
        }
        return builder;
    }

    private StringBuilder correctTextEndingRule(StringBuilder builder) {
        if (builder.charAt(builder.length() - 1) != '.') {
            builder.append('.');
        }
        return builder;
    }

    private StringBuilder correctSpaceAfterPunctuationSymbolRule(StringBuilder builder) {
        for (int i = 0; i < builder.length(); i++) {
            if ((builder.charAt(i) == ',' || builder.charAt(i) == '.')
                    && i + 1 < builder.length() && builder.charAt(i + 1) != ' ') {
                builder.insert(i + 1, ' ');
            }
        }
        return builder;
    }
}