package kg.sunrise.infoapteka.enums

enum class SortType(val ordering: String?) {
    PRICE_ASC("price"),
    PRICE_DESC("-price"),
    ALPHABET_ASC("name"),
    ALPHABET_DESC("-name"),
    NEW_ASC("-created_at"),
    NEW_DESC("created_at"),
    BESTSELLER("is_hit"),
    DEFAULT(null)
}