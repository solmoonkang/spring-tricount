package kr.co.springtricount.infra.response;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalFormatter {

    public static String formatWithComma(BigDecimal value) {

        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.KOREAN);

        return numberInstance.format(value);
    }
}
