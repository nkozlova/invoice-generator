import CoordinatedTypes.CRectangle;
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

    public void SetCity( String c ) { city.Set( c ); }
    public void SetState( String s ) { state.Set( s ); }
    public void SetCountry( String c ) {
        country.Set( c );
    }

    public СCoordinatedString GetLines() { return lines; }
    public СCoordinatedString GetZip() { return zip; }
    public СCoordinatedString GetCity() { return city; }
    public СCoordinatedString GetState() { return state; }
    public СCoordinatedString GetCountry() { return country; }

    @Override
    public void Generate() {
        Random random = new Random();
        Integer no = random.nextInt( 126 ) + 1;

        Generex linesGenerex = new Generex( "[A-Z][a-z]+" );
        lines.Set( no.toString().concat( ", St. " ).concat( linesGenerex.random( 6, 12 ) ) );

        Generex zipGenerex = new Generex( "[0-9]{5}(-[0-9]{4})?" );
        zip.Set( zipGenerex.random() );

        if( city.Get() == null ) {
            city.Set( linesGenerex.random( 5, 10 ) );
        }
        if( state.Get() == null ) {
            state.Set( linesGenerex.random( 5, 10 ) );
        }
        if( country.Get() == null ) {
            country.Set( linesGenerex.random( 5, 10 ) );
        }
    }

    @Override
    public String GetData() {
        return lines.Show( LINES_DEF ).
                concat( zip.Show( ZIP_DEF ) ).
                concat( city.Show( CITY_DEF ) ).
                concat( state.Show( STATE_DEF ) ).
                concat( country.Show( COUNTRY_DEF ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        lines.DrawRects( g2d );
        zip.DrawRects( g2d );
        city.DrawRects( g2d );
        state.DrawRects( g2d );
        country.DrawRects( g2d );
    }
}
