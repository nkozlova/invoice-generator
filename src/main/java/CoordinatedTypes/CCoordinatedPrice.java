package CoordinatedTypes;

import java.text.DecimalFormat;

// Тип для хранения цены
public class CCoordinatedPrice extends СCoordinatedBaseType {

    private static final DecimalFormat MASK = ((DecimalFormat) DecimalFormat.getInstance( LOCALE.GetLocale() ) );

    private Double value;

    public CCoordinatedPrice() {
        // Устанавливаем паттерн
        MASK.applyPattern( "###,###,###.##" );
    }

    @Override
    public String GetValue() {
        // Пишем число с двумя знаками после запятой и валюту
        String res = MASK.format( value );
        return res.concat( LOCALE.GetCurrency() );
    }

    public Double Get() { return value; }
    public void Set( Double v ) {
        value = v;
    }
}
