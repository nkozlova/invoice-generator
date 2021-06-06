package CoordinatedTypes;

import com.mifmif.common.regex.Generex;
import java.util.Locale;
import java.util.Random;

public class CUsedLocale {

    private static TUsedLocale type; // Тип используемой локали, которая будет одна на документ

    private static final String DOLLAR_DEF = isRandomTrue( 0.5 ) ? "$" : " USD";
    private static final String CAN_DOLLAR_DEF = isRandomTrue( 1/3 ) ? "C$" : isRandomTrue( 0.5 ) ? "$" : " CAD";
    private static final String FUNT_DEF = isRandomTrue( 0.5 ) ? "£" : " GBP";
    private static final String EURO_DEF = isRandomTrue( 0.5 ) ? "€" : " EUR";

    public CUsedLocale() {
        Random random = new Random();
        switch( random.nextInt( TUsedLocale.values().length ) ) {
            case 0:
                type = TUsedLocale.UL_US;
                break;
            case 1:
                type = TUsedLocale.UL_UK;
                break;
            default:
                type = TUsedLocale.UL_Canada;
        }
    }

    public static TUsedLocale GetLocaleType() { return type; }

    public static Locale GetLocale() {
        switch( type ) {
            case UL_US:
                return Locale.US;
            case UL_UK:
                return Locale.UK;
            case UL_Canada:
                return Locale.CANADA;
        }
        assert( false );
        return Locale.US;
    }

    public static String GetCurrency() {
        switch( type ) {
            case UL_US:
                return DOLLAR_DEF;
            case UL_UK:
                return FUNT_DEF;
            case UL_Canada:
                return CAN_DOLLAR_DEF;
        }
        assert( false );
        return EURO_DEF;
    }

    public static String GetDateFormat() {
        Generex delimiterGenerex = new Generex( "[\\-\\/a]" ); // a заменим на точку, если выпадет такой разделитель
        String delimiter = delimiterGenerex.random();
        if( delimiter.equals( "a" ) ) {
            delimiter = ".";
        }

        String year = isRandomTrue( 0.5 ) ? "yyyy" : "yy";
        switch( type ) {
            case UL_US:
                return "MM".concat( delimiter ).concat( "dd" ).concat( delimiter ).concat( year );
            case UL_UK:
                return "dd".concat( delimiter ).concat( "MM" ).concat( delimiter ).concat( year );
            case UL_Canada:
                return year.concat( delimiter ).concat( "MM" ).concat( delimiter ).concat( "dd" );
        }
        assert( false );
        return "dd-MM-yyyy";
    }

    // Получилось ли сгенерировать число, не большее данной вероятности
    private static boolean isRandomTrue( double probability ) {
        assert( probability >= 0. && probability <= 1. );
        return Math.random() <= probability;
    }
}
