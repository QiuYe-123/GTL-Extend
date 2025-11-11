package cn.qiuye.gtl_extend.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class FormattingUtil {

    public static final DecimalFormat DECIMAL_FORMAT_1F = new DecimalFormat("#,##0.#");
    public static final DecimalFormat DECIMAL_FORMAT_2F = new DecimalFormat("#,##0.##");
    public static final DecimalFormat DECIMAL_FORMAT_SIC_2F = new DecimalFormat("0.00E00");

    private static final String[] UNITS = { "", "K", "M", "G", "T", "P", "E", "Z", "Y", "B", "N", "D", "C", "S", "O", "Q", "X", "W", "V", "U", "Tt", "Gt", "Mt", "St", "Ot", "Nt", "Dt", "Ct", "Lt", "Kt", "Jt", "It", "Ht", "Gtt", "Ett", "Dtt", "Ctt", "Btt", "Att" };

    private static final BigDecimal ONE_THOUSAND = new BigDecimal(1000);

    public static String formatNumberReadable(BigDecimal number) {
        return formatNumberReadable(number, false);
    }

    public static String formatNumberReadable(BigDecimal number, boolean milli) {
        return formatNumberReadable(number, milli, DECIMAL_FORMAT_1F, null);
    }

    public static String formatNumberReadable(BigDecimal number, boolean milli, NumberFormat fmt, @Nullable String unit) {
        StringBuilder sb = new StringBuilder();
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal oneThousand = new BigDecimal(1000);
        BigDecimal number1 = number;
        if (number.compareTo(zero) < 0) {
            number = number.abs();
            sb.append('-');
        }

        if (milli && number.compareTo(oneThousand) >= 0) {
            milli = false;
            number = number.divide(oneThousand, MathContext.DECIMAL128);
        }

        int exp = 0;
        if (number.compareTo(ONE_THOUSAND) >= 0) {
            exp = calculateExponent(number);

            // 当指数超过单位数组范围时使用科学计数法
            if (exp > UNITS.length - 1) {
                return DECIMAL_FORMAT_SIC_2F.format(number);
            }

            // 使用BigDecimal进行幂运算
            if (exp > 0) {
                BigDecimal divisor = power(ONE_THOUSAND, exp);
                number1 = number.divide(divisor, MathContext.DECIMAL128);
            }
        }

        sb.append(fmt.format(number1));
        if (exp > 0) {
            sb.append(UNITS[exp]);
        } else if (milli && number1.compareTo(zero) != 0) {
            sb.append('m');
        }

        if (unit != null) sb.append(unit);
        return sb.toString();
    }

    /**
     * 使用BigDecimal计算指数，避免double精度限制
     */
    private static int calculateExponent(BigDecimal number) {
        int exponent = 0;
        BigDecimal temp = number;

        // 循环除以1000，直到数值小于1000
        while (temp.compareTo(ONE_THOUSAND) >= 0) {
            temp = temp.divide(ONE_THOUSAND, MathContext.DECIMAL128);
            exponent++;
        }

        return exponent;
    }

    /**
     * 使用BigDecimal计算幂运算
     */
    private static BigDecimal power(BigDecimal base, int exponent) {
        if (exponent == 0) return BigDecimal.ONE;

        BigDecimal result = BigDecimal.ONE;
        for (int i = 0; i < exponent; i++) {
            result = result.multiply(base);
        }
        return result;
    }
}
