package CoordinatedTypes;

// Тип для хранения цены
public class CCoordinatedPrice extends СCoordinatedBaseType {

    private static final String MASK = "%.2f";
    private static final String DOLLAR_DEF = "$";
    private static final String EURO_DEF = "€";

    private static TUsedCurrency currency; // Валюта, общая на весь документ
    private static СCoordinatedString currencyName;
    private Double value;

    @Override
    public String GetValue() {
        // Пишем число с двумя знаками после запятой и валюту
        String res = String.format( MASK, value );
        switch( currency ) {
            case UC_Dollar:
                res = res.concat( DOLLAR_DEF );
                break;
            case UC_Euro:
                res = res.concat( EURO_DEF );
                break;
        }
        return res;
    }

    public Double Get() { return value; }
    public void Set( Double v ) {
        value = v;
    }

    public static TUsedCurrency GetCurrency() { return currency; }
    public static void SetCurrency( TUsedCurrency c ) { currency = c; }
}
