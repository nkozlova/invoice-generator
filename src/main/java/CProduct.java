import CoordinatedTypes.CRectangle;
import CoordinatedTypes.СCoordinatedString;
import CoordinatedTypes.CCoordinatedInteger;
import CoordinatedTypes.CCoordinatedPrice;

import com.mifmif.common.regex.Generex;
import java.awt.*;
import java.util.Random;

public class CProduct implements IModel {

    private static final String DELIMITER_STR = ", ";
    private static final String DESCRIPTION_DEF = "\t\tDescription: ";
    private static final String PRICE_DEF = "\t\tPrice: ";
    private static final String QUANTITY_DEF = "\t\tQuantity: ";
    private static final String TOTAL_DEF = "\t\tTotal: ";

    private СCoordinatedString description = new СCoordinatedString();
    private CCoordinatedPrice price = new CCoordinatedPrice();   // price for unit
    private CCoordinatedInteger quantity = new CCoordinatedInteger();
    private CCoordinatedPrice totalPrice = new CCoordinatedPrice();

    public СCoordinatedString GetDescription() { return description; }
    public CCoordinatedPrice GetPrice() { return price; }
    public CCoordinatedInteger GetQuantity() { return quantity; }
    public CCoordinatedPrice GetTotalPrice() { return totalPrice; }

    public void SetDescription( String d ) {
        if( description.Get() == null ) {
            description.Set( d );
        } else {
            description.Set( DELIMITER_STR.concat( d ) );
        }
    }
    public void SetPrice( double p ) { price.Set( p ); }

    @Override
    public void Generate() {
        Random random = new Random();
        Generex generex = new Generex( "[A-Z][a-z]+" );

        quantity.Set( random.nextInt( 20 ) + 1 );

        if( price.Get() == null){
            price.Set( (double)( 1 + random.nextInt( 1000 ) ) / 100 );
        }
        if( description.Get() == null ) {
            description.Set( generex.random( 6, 12 ) );
        }

        totalPrice.Set( price.Get() * quantity.Get() );
    }

    @Override
    public void Show() {
        System.out.print( DESCRIPTION_DEF.concat( description.Show() ) );
        System.out.print( PRICE_DEF.concat( price.Show() ) );
        System.out.print( QUANTITY_DEF.concat( quantity.Show() ) );
        System.out.print( TOTAL_DEF.concat( totalPrice.Show() ) );
    }

    @Override
    public String GetData() {
        return DESCRIPTION_DEF.concat( description.Show() ).
                concat( PRICE_DEF ).concat( price.Show() ).
                concat( QUANTITY_DEF ).concat( quantity.Show() ).
                concat( TOTAL_DEF ).concat( totalPrice.Show() );
    }


    @Override
    public void DrawRects( Graphics2D g2d ) {
        description.DrawRects( g2d );
        price.DrawRects( g2d );
        quantity.DrawRects( g2d );
        totalPrice.DrawRects( g2d );
    }
}
