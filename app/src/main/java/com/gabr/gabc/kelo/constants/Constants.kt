package com.gabr.gabc.kelo.constants

import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.utils.common.CurrencyModel

/** Class that defines constant values for their usage in all of the code */
object Constants {
        // true -> user has completed the welcome form
        // false -> user must log in
        const val FIRST_LAUNCHED = "isFirstLaunched"
        const val SHOW_COMPLETED_CHORES = "showCompletedChores"
        const val GROUP_ID = "GroupID"
        const val USER_ID = "UserID"

        const val CREATE_GROUP = "CREATE_GROUP"
        const val JOIN_GROUP = "JOIN_GROUP"

        const val NO_FREQUENCY = 0
        const val WEEKLY = 1
        const val EVERY_TWO_WEEKS = 2
        const val MONTHLY = 3
        const val EVERY_TWO_MONTHS = 4
        const val ANNUALLY = 5

        const val NAME_VALIDATOR = "^[A-Za-zñÁÉÍÓÚÜáéíóúüç ]{3,32}\$"
        const val GROUP_NAME_VALIDATOR = "^[A-Za-z0-9ñÁÉÍÓÚÜáéíóúüç ]{5,32}\$"
        const val CHORE_NAME_VALIDATOR = "^[A-Za-z0-9ñÁÉÍÓÚÜáéíóúüç ]{5,32}\$"
        const val REWARD_NAME_VALIDATOR = "^[A-Za-z0-9ñÁÉÍÓÚÜáéíóúüç ]{5,48}\$"

        const val fbUsersCollection = "users"
        const val fbGroupsCollection = "groups"
        const val fbRewardsSubCollection = "rewards"
        const val fbChoresSubCollection = "chores"

        val MONTHS : Map<String, Map<Int, String>> = hashMapOf(
                "en" to hashMapOf(
                        0 to "January", 1 to "February", 2 to "March",
                        3 to "April", 4 to "May", 5 to "June",
                        6 to "July", 7 to "August", 8 to "September",
                        9 to "October", 10 to "November", 11 to "December"
                ),
                "es" to hashMapOf(
                        0 to "Enero", 1 to "Febrero", 2 to "Marzo",
                        3 to "Abril", 4 to "Mayo", 5 to "Junio",
                        6 to "Julio", 7 to "Agosto", 8 to "Septiembre",
                        9 to "Octubre", 10 to "Noviembre", 11 to "Diciembre"
                )
        )

        val DAY_OF_WEEK : Map<String, Map<Int, String>> = hashMapOf(
                "en" to hashMapOf(
                        1 to "Sunday", 2 to "Monday",
                        3 to "Tuesday", 4 to "Wednesday", 5 to "Thursday",
                        6 to "Friday", 7 to "Saturday"
                ),
                "es" to hashMapOf(
                        1 to "Domingo", 2 to "Lunes",
                        3 to "Martes", 4 to "Miércoles", 5 to "Jueves",
                        6 to "Viernes", 7 to "Sábado"
                )
        )

        val CURRENCIES: Array<CurrencyModel> = arrayOf(
                CurrencyModel("EUR", "Euro", "€", R.drawable.flag_eur),
                CurrencyModel("USD", "United States Dollar", "$", R.drawable.flag_usd),
                CurrencyModel("GBP", "British Pound", "£", R.drawable.flag_gbp),
                CurrencyModel("CZK", "Czech Koruna", "Kč", R.drawable.flag_czk),
                CurrencyModel("TRY", "Turkish Lira", "₺", R.drawable.flag_try),
                CurrencyModel("AED", "Emirati Dirham", "د.إ", R.drawable.flag_aed),
                CurrencyModel("AFN", "Afghanistan Afghani", "؋", R.drawable.flag_afn),
                CurrencyModel("ARS", "Argentine Peso", "$", R.drawable.flag_ars),
                CurrencyModel("AUD", "Australian Dollar", "$", R.drawable.flag_aud),
                CurrencyModel("BBD", "Barbados Dollar", "$", R.drawable.flag_bbd),
                CurrencyModel("BDT", "Bangladeshi Taka", " Tk", R.drawable.flag_bdt),
                CurrencyModel("BGN", "Bulgarian Lev", "лв", R.drawable.flag_bgn),
                CurrencyModel("BHD", "Bahraini Dinar", "BD", R.drawable.flag_bhd),
                CurrencyModel("BND", "Brunei Darussalam Dollar", "$", R.drawable.flag_bnd),
                CurrencyModel("BOB", "Bolivia Bolíviano", "\$b", R.drawable.flag_bob),
                CurrencyModel("BRL", "Brazil Real", "R$", R.drawable.flag_brl),
                CurrencyModel("BTN", "Bhutanese Ngultrum", "Nu.", R.drawable.flag_btn),
                CurrencyModel("BZD", "Belize Dollar", "BZ$", R.drawable.flag_bzd),
                CurrencyModel("CAD", "Canada Dollar", "$", R.drawable.flag_cad),
                CurrencyModel("CHF", "Switzerland Franc", "CHF", R.drawable.flag_chf),
                CurrencyModel("CLP", "Chile Peso", "$", R.drawable.flag_clp),
                CurrencyModel("CNY", "China Yuan Renminbi", "¥", R.drawable.flag_cny),
                CurrencyModel("COP", "Colombia Peso", "$", R.drawable.flag_cop),
                CurrencyModel("CRC", "Costa Rica Colon", "₡", R.drawable.flag_crc),
                CurrencyModel("DKK", "Denmark Krone", "kr", R.drawable.flag_dkk),
                CurrencyModel("DOP", "Dominican Republic Peso", "RD$", R.drawable.flag_dop),
                CurrencyModel("EGP", "Egypt Pound", "£", R.drawable.flag_egp),
                CurrencyModel("ETB", "Ethiopian Birr", "Br", R.drawable.flag_etb),
                CurrencyModel("GEL", "Georgian Lari", "₾", R.drawable.flag_gel),
                CurrencyModel("GHS", "Ghana Cedi", "¢", R.drawable.flag_ghs),
                CurrencyModel("GMD", "Gambian dalasi", "D", R.drawable.flag_gmd),
                CurrencyModel("GYD", "Guyana Dollar", "$", R.drawable.flag_gyd),
                CurrencyModel("HRK", "Croatia Kuna", "kn", R.drawable.flag_hrk),
                CurrencyModel("HUF", "Hungary Forint", "Ft", R.drawable.flag_huf),
                CurrencyModel("IDR", "Indonesia Rupiah", "Rp", R.drawable.flag_idr),
                CurrencyModel("ILS", "Israel Shekel", "₪", R.drawable.flag_ils),
                CurrencyModel("INR", "Indian Rupee", "₹", R.drawable.flag_inr),
                CurrencyModel("ISK", "Iceland Krona", "kr", R.drawable.flag_isk),
                CurrencyModel("JMD", "Jamaica Dollar", "J$", R.drawable.flag_jmd),
                CurrencyModel("JPY", "Japanese Yen", "¥", R.drawable.flag_jpy),
                CurrencyModel("KES", "Kenyan Shilling", "KSh", R.drawable.flag_kes),
                CurrencyModel("KRW", "Korea (South) Won", "₩", R.drawable.flag_krw),
                CurrencyModel("KWD", "Kuwaiti Dinar", "د.ك", R.drawable.flag_kwd),
                CurrencyModel("KZT", "Kazakhstan Tenge", "лв", R.drawable.flag_kzt),
                CurrencyModel("LAK", "Laos Kip", "₭", R.drawable.flag_lak),
                CurrencyModel("LKR", "Sri Lanka Rupee", "₨", R.drawable.flag_lkr),
                CurrencyModel("LRD", "Liberia Dollar", "$", R.drawable.flag_lrd),
                CurrencyModel("LTL", "Lithuanian Litas", "Lt", R.drawable.flag_ltl),
                CurrencyModel("MAD", "Moroccan Dirham", "MAD", R.drawable.flag_mad),
                CurrencyModel("MDL", "Moldovan Leu", "MDL", R.drawable.flag_mdl),
                CurrencyModel("MKD", "Macedonia Denar", "ден", R.drawable.flag_mkd),
                CurrencyModel("MNT", "Mongolia Tughrik", "₮", R.drawable.flag_mnt),
                CurrencyModel("MUR", "Mauritius Rupee", "₨", R.drawable.flag_mur),
                CurrencyModel("MWK", "Malawian Kwacha", "MK", R.drawable.flag_mwk),
                CurrencyModel("MXN", "Mexico Peso", "$", R.drawable.flag_mxn),
                CurrencyModel("MYR", "Malaysia Ringgit", "RM", R.drawable.flag_myr),
                CurrencyModel("MZN", "Mozambique Metical", "MT", R.drawable.flag_mzn),
                CurrencyModel("NAD", "Namibia Dollar", "$", R.drawable.flag_nad),
                CurrencyModel("NGN", "Nigeria Naira", "₦", R.drawable.flag_ngn),
                CurrencyModel("NIO", "Nicaragua Cordoba", "C$", R.drawable.flag_nio),
                CurrencyModel("NOK", "Norway Krone", "kr", R.drawable.flag_nok),
                CurrencyModel("NPR", "Nepal Rupee", "₨", R.drawable.flag_npr),
                CurrencyModel("NZD", "New Zealand Dollar", "$", R.drawable.flag_nzd),
                CurrencyModel("OMR", "Oman Rial", "﷼", R.drawable.flag_omr),
                CurrencyModel("PEN", "Peru Sol", "S/.", R.drawable.flag_pen),
                CurrencyModel("PGK", "Papua New Guinean Kina", "K", R.drawable.flag_pgk),
                CurrencyModel("PHP", "Philippines Peso", "₱", R.drawable.flag_php),
                CurrencyModel("PKR", "Pakistan Rupee", "₨", R.drawable.flag_pkr),
                CurrencyModel("PLN", "Poland Zloty", "zł", R.drawable.flag_pln),
                CurrencyModel("PYG", "Paraguay Guarani", "Gs", R.drawable.flag_pyg),
                CurrencyModel("QAR", "Qatar Riyal", "﷼", R.drawable.flag_qar),
                CurrencyModel("RON", "Romania Leu", "lei", R.drawable.flag_ron),
                CurrencyModel("RSD", "Serbia Dinar", "Дин.", R.drawable.flag_rsd),
                CurrencyModel("RUB", "Russia Ruble", "₽", R.drawable.flag_rub),
                CurrencyModel("SAR", "Saudi Arabia Riyal", "﷼", R.drawable.flag_sar),
                CurrencyModel("SEK", "Sweden Krona", "kr", R.drawable.flag_sek),
                CurrencyModel("SGD", "Singapore Dollar", "$", R.drawable.flag_sgd),
                CurrencyModel("SOS", "Somalia Shilling", "S", R.drawable.flag_sos),
                CurrencyModel("SRD", "Suriname Dollar", "$", R.drawable.flag_srd),
                CurrencyModel("THB", "Thailand Baht", "฿", R.drawable.flag_thb),
                CurrencyModel("TTD", "Trinidad and Tobago Dollar", "TT$", R.drawable.flag_ttd),
                CurrencyModel("TWD", "Taiwan New Dollar", "NT$", R.drawable.flag_twd),
                CurrencyModel("TZS", "Tanzanian Shilling", "TSh", R.drawable.flag_tzs),
                CurrencyModel("UAH", "Ukraine Hryvnia", "₴", R.drawable.flag_uah),
                CurrencyModel("UGX", "Ugandan Shilling", "USh", R.drawable.flag_ugx),
                CurrencyModel("UYU", "Uruguay Peso", "\$U", R.drawable.flag_uyu),
                CurrencyModel("VEF", "Venezuela Bolívar", "Bs", R.drawable.flag_vef),
                CurrencyModel("VND", "Viet Nam Dong", "₫", R.drawable.flag_vnd),
                CurrencyModel("YER", "Yemen Rial", "﷼", R.drawable.flag_yer),
                CurrencyModel("ZAR", "South Africa Rand", "R", R.drawable.flag_zar)
        )
}