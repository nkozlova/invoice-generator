package CoordinatedTypes;

// Тип для хранения цены
public class CCoordinatedPrice extends СCoordinatedBaseType {

    private static final String MASK = "%.2f";

    private static TUsedCurrency currency; // Валюта, общая на весь документ
    private static СCoordinatedString currencyName;
    private Double value;

    @Override
    public String GetValue() {
        // Пишем число с двумя знаками после запятой и валюту
        return String.format( MASK, value ).concat( currency.toString() );
    }

    public Double Get() { return value; }
    public void Set( Double v ) {
        value = v;
    }

    public static TUsedCurrency GetCurrency() { return currency; }
    public static void SetCurrency( TUsedCurrency c ) { currency = c; }
}
