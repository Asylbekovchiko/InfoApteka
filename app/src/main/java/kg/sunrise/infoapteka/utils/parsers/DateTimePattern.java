package kg.sunrise.infoapteka.utils.parsers;

public enum DateTimePattern {
    /** yyyy-MM-dd'T'HH:mm:ss */
    DATE_TIME_ISO("yyyy-MM-dd'T'HH:mm:ss"),
    /** yyyy-MM-dd */
    yyyy_MM_dd_with_dash("yyyy-MM-dd"),
    /** dd.MM.yyyy */
    dd_MM_yyy_with_dots("dd.MM.yyyy"),
    /** dd MM yyyy */
    dd_MM_yyyy_with_spacing("dd MM yyyy"),
    /** HH:mm:ss */
    HH_mm_ss("HH:mm:ss"),
    /** HH:mm */
    HH_mm("HH:mm"),
    /** MM.dd.yyyy HH:mm */
    MM_dd_yyyy_hh_mm("MM.dd.yyyy HH:mm"),
    dd_MMMM_yyyy("dd MMMM yyyy");

    private final String pattern;

    DateTimePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
