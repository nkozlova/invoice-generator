import CoordinatedTypes.CCoordinatedProcent;
import CoordinatedTypes.CCoordinatedPrice;
import CoordinatedTypes.CRectangle;
import CoordinatedTypes.TUsedCurrency;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CProductContainer implements IModel {

    private static final String HEADERS_DEF = "\tHeaders: ";
    private static final String TOTAL_SUM_DEF = "\tTotalSum: ";
    private static final String TAX_RATE_DEF = "\tTaxRate: ";
    private static final String TAX_DEF = "\tTax: ";
    private static final String TOTAL_DEF = "\tTotal: ";
    private static final String PRODUCT_DEF = "\tProduct ";
    private static final String LEFT_BRACKET = " {\n";
    private static final String RIGHT_BRACKET = "\t}\n";
    private static final String NEXT_STR = "\n";

    private List<String> headers;
    private CCoordinatedPrice tax = new CCoordinatedPrice(); // Сумма налога
    private CCoordinatedProcent taxRate = new CCoordinatedProcent(); // Ставка налога, в процентах (0-15)
    //private Double discount;  // скидка
    private CCoordinatedPrice totalSum = new CCoordinatedPrice();    // total без учета налога
    private CCoordinatedPrice total = new CCoordinatedPrice();
    private List<CProduct> products;

    public List<String> GetHeaders() { return headers; }
    public CCoordinatedPrice GetTax() { return tax; }
    public CCoordinatedProcent GetTaxRate() { return taxRate; }
    public CCoordinatedPrice GetTotalSum() { return totalSum; }
    public CCoordinatedPrice GetTotal() { return total; }
    public List<CProduct> GetProducts() { return products; }

    @Override
    public void Generate() {
        Random random = new Random();
        // Устанавливаем валюту
        switch( random.nextInt( TUsedCurrency.values().length ) ) {
            case 0:
                CCoordinatedPrice.SetCurrency( TUsedCurrency.$ );
                break;
            default:
                CCoordinatedPrice.SetCurrency( TUsedCurrency.€ );
        }

        headers = new ArrayList<>();
        headers.add( "Product" );
        headers.add( "Price" );
        headers.add( "Quantity" );
        headers.add( "Total Price" );
        totalSum.Set( 0.0 );

        int count = random.nextInt( 6 ) + 1;
        products = new ArrayList<>( count );

        try {
            // записи Amazon Global Store US From Saudi Souq с kaggle
            RandomAccessFile raf = new RandomAccessFile( "Souq_Saudi_Amazon_Global_Store_US.csv", "r" );

            for( int i = 0; i < count; i++ ) {
                CProduct product = new CProduct();

                String productInfo = "";
                while( productInfo.isEmpty() ) {
                    try {
                        long nextBytePosition = ThreadLocalRandom.current().nextLong( raf.length() );
                        raf.seek(nextBytePosition);
                        raf.readLine();
                        productInfo = raf.readLine().trim();
                    } catch( IOException e ) {
                        System.out.println( "Failed, CProductContainer" );
                    }
                }

                if( productInfo.charAt( 0 ) == '\"' ) {
                    String[] strs = productInfo.split( "\"" );
                    product.SetDescription( strs[1].trim() );
                    if( strs.length > 3 ) {
                        product.SetPrice( Double.valueOf( strs[3].trim().replace( ",", "" ) ) );
                    } else {
                        strs = strs[2].trim().split( "," );
                        product.SetPrice( Double.valueOf( strs[1].trim() ) );
                    }
                } else {
                    String[] strs = productInfo.split( ",", 2 );
                    product.SetDescription( strs[0].trim() );
                    if( strs[1].trim().charAt( 0 ) == '\"' ) {
                        strs = strs[1].trim().split( "\"" );
                        product.SetPrice( Double.valueOf( strs[1].trim().replace( ",", "" ) ) );
                    } else {
                        strs = strs[1].trim().split( "," );
                        product.SetPrice( Double.valueOf( strs[0].trim() ) );
                    }
                }

                product.Generate();
                products.add( product );
                totalSum.Set( totalSum.Get() + product.GetTotalPrice().Get() );
            }

            raf.close();
        } catch( FileNotFoundException e ) {
            System.out.println( "Not fined csv file!" );
        } catch( IOException e ) {
            System.out.println( "Failed, CProductContainer" );
        }

        taxRate.Set( (double)random.nextInt( 15 ) );
        tax.Set( totalSum.Get() / 100 * taxRate.Get() );
        total.Set( totalSum.Get() + tax.Get() );
    }

    @Override
    public void Show() {
        System.out.print( HEADERS_DEF.concat( headers.toString() ).concat( NEXT_STR ) );
        System.out.print( TOTAL_SUM_DEF.concat( totalSum.Show() ) );
        System.out.print( TAX_RATE_DEF.concat( taxRate.Show() ) );
        System.out.print( TAX_DEF.concat( tax.Show() ) );
        System.out.print( TOTAL_DEF.concat( total.Show() ) );

        for( Integer i = 0; i < products.size(); i++ ) {
            System.out.print( PRODUCT_DEF.concat( i.toString() ).concat( LEFT_BRACKET ) );
            products.get( i ).Show();
            System.out.print( RIGHT_BRACKET );
        }
    }

    @Override
    public String GetData() {
        String data = HEADERS_DEF.concat( headers.toString() ).
                concat( TOTAL_SUM_DEF ).concat( totalSum.Show() ).
                concat( TAX_RATE_DEF ).concat( taxRate.Show() ).
                concat( TAX_DEF ).concat( tax.Show() ).
                concat( TOTAL_DEF ).concat( total.Show() );

        for( Integer i = 0; i < products.size(); i++ ) {
            data = data.concat( PRODUCT_DEF ).concat( i.toString() ).concat( LEFT_BRACKET ).
                    concat( products.get( i ).GetData() ).
                    concat( RIGHT_BRACKET );
        }
        return data;
    }


    @Override
    public void DrawRects( Graphics2D g2d ) {
        totalSum.DrawRects( g2d );
        taxRate.DrawRects( g2d );
        tax.DrawRects( g2d );
        total.DrawRects( g2d );
        for( Integer i = 0; i < products.size(); i++ ) {
            products.get( i ).DrawRects( g2d );
        }
    }
}
