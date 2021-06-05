package CoordinatedTypes;

import java.text.DecimalFormat;

// Тип для хранения процентов
public class CCoordinatedPercent extends СCoordinatedBaseType {

    private static final DecimalFormat MASK = ((DecimalFormat) DecimalFormat.getInstance( LOCALE.GetLocale() ));
    private static final String PERCENT = "%";

    private Double value;

    public CCoordinatedPercent() {
        // Устанавливаем паттерн
        MASK.applyPattern( "##.##" );
    }

    @Override
    public String GetValue() {
        // Пишем число с двумя знаками после запятой и процент
        return MASK.format( value ).concat( PERCENT );
    }

    public Double Get() { return value; }
    public void Set( Double v ) {
        value = v;
    }
}
