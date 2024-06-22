package com.jewelbackend.backend.common.constants;

import java.util.regex.Pattern;

public class Constants {
    private Constants() {
    }

    public static final String SETUPSCHEMA = "jewellers";
    public static final Pattern EMAILREGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    public static final String SALE_CASH = "CREDIT";
    public static final String PURCHASE_CASH = "DEBIT";

    public static Boolean matchEmail(String email) {
        return EMAILREGEX.matcher(email).matches();
    }

}
