import CoordinatedTypes.CRectangle;
import CoordinatedTypes.TUsedLocale;
import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;
import java.util.Random;

public class CAddress implements IModel {

    private static final String LINES_DEF = "    Lines: ";
    private static final String ZIP_DEF = "    Zip: ";
    private static final String CITY_DEF = "    City: ";
    private static final String STATE_DEF = "    State: ";
    private static final String COUNTRY_DEF = "    Country: ";

    private СCoordinatedString lines = new СCoordinatedString();   // улица, дом
    private СCoordinatedString zip = new СCoordinatedString(); // zip code, индекс
    private СCoordinatedString city = new СCoordinatedString();
    private СCoordinatedString state = new СCoordinatedString();
    private СCoordinatedString country = new СCoordinatedString();
    private TUsedLocale localeType;

    CAddress( TUsedLocale l ) {
        localeType = l;
    }

    public void SetCity( String c ) { city.Set( c ); }

    @Override
    public void Generate() {
        Random random = new Random();
        Integer no = random.nextInt( 126 ) + 1;

        Generex linesGenerex = new Generex( "[A-Z][a-z]+" );
        Generex streetGenerex = new Generex( " (Road|a|Street|Drive|Avenue)");
        String street = streetGenerex.random();
        if( street.equals( " a" ) ) {
            street = " St.";
        }
        lines.Set( no.toString().concat( " " ).concat( linesGenerex.random( 6, 12 ) ).concat( street ) );

        Generex zipGenerex = new Generex( getZipCodeFormat() );
        zip.Set( zipGenerex.random() );

        if( city.Get() == null ) {
            city.Set( linesGenerex.random( 5, 10 ) );
        }

        Generex stateGenerex = new Generex( getStateFormat() );
        state.Set( stateGenerex.random() );

        country.Set( getCountry() );
    }

    @Override
    public String GetData( Boolean withCoords ) {
        return lines.Show( LINES_DEF, withCoords ).
                concat( zip.Show( ZIP_DEF, withCoords ) ).
                concat( city.Show( CITY_DEF, withCoords ) ).
                concat( state.Show( STATE_DEF, withCoords ) ).
                concat( country.Show( COUNTRY_DEF, withCoords ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        lines.DrawRects( g2d );
        zip.DrawRects( g2d );
        city.DrawRects( g2d );
        state.DrawRects( g2d );
        country.DrawRects( g2d );
    }

    public CRectangle DrawInfo( Graphics2D g2d, int x, int y ) {
        int left = x;
        int width = 0;
        int top = y;

        CRectangle rect = CGraphicsHelper.DrawString( g2d, lines, x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        width = Math.max( width, rect.GetWidth() );

        switch( localeType ) {
            case UL_US:
                rect = CGraphicsHelper.DrawStrings( g2d, city, state, x, y );
                CRectangle rect1 = CGraphicsHelper.DrawString( g2d, " ", zip, x + rect.GetWidth(), y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() + rect1.GetWidth() );

                rect = CGraphicsHelper.DrawString( g2d, country, x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
                break;
            case UL_UK:
                rect = CGraphicsHelper.DrawStrings( g2d, city, zip, x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );

                rect = CGraphicsHelper.DrawString( g2d, country, x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
                break;
            case UL_Canada:
                rect = CGraphicsHelper.DrawStrings( g2d, city, state, x, y );
                CRectangle rect2 = CGraphicsHelper.DrawString( g2d, ", ", x + rect.GetWidth(), y );
                CRectangle rect3 = CGraphicsHelper.DrawStrings( g2d, zip, country, x + rect.GetWidth() + rect2.GetWidth(), y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() + rect2.GetWidth() + rect3.GetWidth() );
                break;
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( left, y + fm.getDescent() - CGraphicsHelper.DefaultYShift( g2d ), width, y - top );
    }

    private String getZipCodeFormat() {
        switch( localeType ) {
            case UL_US:
                return "[0-9]{5}";
            case UL_UK:
                return "([A-Z0-9]){3,4} ([A-Z0-9]){3}";
            case UL_Canada:
                return "([A-Z0-9]){3} ([A-Z0-9]){3}";
        }
        assert( false );
        return "[0-9]{5}(-[0-9]{4})?";
    }

    private String getStateFormat() {
        switch( localeType ) {
            case UL_US:
                return "(ID|IA|AL|AK|AZ|AR|WY|WA|VT|VA|WI|HI|DE|GA|WV|IL|IN|CA|KS|KY|CO|CT|LA|MA|MN|MS|MO|MI|MT|ME|MD|NE|NV|NH|NJ|NY|NM|OH|OK|OR|PA|RI|ND|NC|TN|TX|FL|SD|SC|UT)";
            case UL_UK:
                return "";
            case UL_Canada:
                return "(ON|QC|NS|NB|MB|BC|PE|SK|AB|NL|NT|YT|NU)";
        }
        assert( false );
        return "Smth else";
    }

    private String getCountry() {
        switch( localeType ) {
            case UL_US:
                return "United Stated";
            case UL_UK:
                return "United Kingdom";
            case UL_Canada:
                return "Canada";
        }
        assert( false );
        return "Smth else";
    }
}
