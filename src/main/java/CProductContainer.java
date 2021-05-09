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

    private static final String TOTAL_SUM_DEF = "  Total Sum: ";
    private static final String TAX_RATE_DEF = "  Tax Rate: ";
    private static final String TAX_DEF = "  Tax: ";
    private static final String TOTAL_DEF = "  Total: ";
    private static final String PRODUCTS_DEF = "  Products:\n";
    private static final int ENUMERATION_COLUMN_WIDTH = 25;
    private static final int DEFINITION_COLUMN_WIDTH = 400;
    private static final String ENUMERATION_HEADER = "№";
    private static final int VALUE_SHIFT = 5;

    private List<String> headers;
    private CCoordinatedPrice tax = new CCoordinatedPrice(); // Сумма налога
    private CCoordinatedProcent taxRate = new CCoordinatedProcent(); // Ставка налога, в процентах (0-15)
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
                CCoordinatedPrice.SetCurrency( TUsedCurrency.UC_Dollar );
                break;
            default:
                CCoordinatedPrice.SetCurrency( TUsedCurrency.UC_Euro );
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
    public String GetData() {
        String data = totalSum.Show( TOTAL_SUM_DEF ).
                concat( taxRate.Show( TAX_RATE_DEF ) ).
                concat( tax.Show( TAX_DEF ) ).
                concat( total.Show( TOTAL_DEF ) ).
                concat( PRODUCTS_DEF );
        for( Integer i = 0; i < products.size(); i++ ) {
            data = data.concat( products.get( i ).GetData() );
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

    // Отрисовка таблицы
    public CRectangle DrawTableInfo( Graphics2D g2d, int y ) {
        int left = CGraphicsHelper.LEFT_PADDING;
        int right = CGraphicsHelper.A4_WIDTH - CGraphicsHelper.RIGHT_PADDING;
        int width = right - left;
        int top = y;

        Boolean withEnumeration = CGraphicsHelper.IsRandomTrue( 0.5 ); // Показывать ли с нумерацией продуктов
        Boolean withHorizontalSeparators = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли горизонтальные разделители между элементами
        Boolean withVerticalSeparators = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли вертикальные разделители между столбцами
        Boolean withBorder = CGraphicsHelper.IsRandomTrue( 0.5 ); // Границы таблицы

        // Заголовок таблицы
        g2d.setFont( CGraphicsHelper.BOLD_DEFAULT_FONT );
        int startedIndex = withEnumeration ? 1 : 0;
        int[] xStarts; // Стартовые х-координаты колонок с отступами
        if( withEnumeration ) {
            int colWidths = ( width - ENUMERATION_COLUMN_WIDTH - DEFINITION_COLUMN_WIDTH ) / 3;
            xStarts = new int[] { left + VALUE_SHIFT,
                    left + ENUMERATION_COLUMN_WIDTH + VALUE_SHIFT,
                    left + ENUMERATION_COLUMN_WIDTH + DEFINITION_COLUMN_WIDTH + VALUE_SHIFT,
                    left + ENUMERATION_COLUMN_WIDTH + DEFINITION_COLUMN_WIDTH + colWidths + VALUE_SHIFT,
                    left + ENUMERATION_COLUMN_WIDTH + DEFINITION_COLUMN_WIDTH + colWidths * 2 + VALUE_SHIFT };
        } else {
            int colWidths = ( width - DEFINITION_COLUMN_WIDTH ) / 3;
            xStarts = new int[] { left + VALUE_SHIFT,
                    left + DEFINITION_COLUMN_WIDTH + VALUE_SHIFT,
                    left + DEFINITION_COLUMN_WIDTH + colWidths + VALUE_SHIFT,
                    left + DEFINITION_COLUMN_WIDTH + colWidths * 2 + VALUE_SHIFT };
        }
        int yStart = y - CGraphicsHelper.DefaultYShift( g2d );
        for( int i = 0; i < headers.size(); i++ ) {
            if( i == 0 && withEnumeration && CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
               g2d.drawString( ENUMERATION_HEADER, xStarts[0], y );
            }
            g2d.drawString( headers.get( i ), xStarts[i + startedIndex], y );
        }

        // Отчеркивание заголовка
        g2d.setColor( Color.GRAY );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        g2d.drawLine( left, y, right, y );
        g2d.setColor( Color.BLACK );

        // Данные продуктов
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );
        for( int i = 0; i < products.size(); i++ ) {
            CProduct product = products.get( i );
            y += CGraphicsHelper.DefaultYShift( g2d );
            if( withEnumeration ) {
                Integer enumerate = i + 1;
                CGraphicsHelper.DrawString( g2d, enumerate.toString(), xStarts[0], y );
            }
            CGraphicsHelper.DrawString( g2d, product.GetPrice(), xStarts[startedIndex + 1], y );
            CGraphicsHelper.DrawString( g2d, product.GetQuantity(), xStarts[startedIndex + 2], y );
            CGraphicsHelper.DrawString( g2d, product.GetTotalPrice(), xStarts[startedIndex + 3], y );

            y = CGraphicsHelper.DrawMultilineText( g2d, product.GetDescription(), xStarts[startedIndex],
                    xStarts[startedIndex + 1] - VALUE_SHIFT * 2, y ).GetBottom() + CGraphicsHelper.ELEMENTS_SPACING;

            // Горизонтальное отделение
            if( withHorizontalSeparators || i == products.size() - 1 ) {
                g2d.setColor( Color.GRAY );
                g2d.drawLine( left, y, right, y );
                g2d.setColor( Color.BLACK );
            }
        }

        int xTotalStart = xStarts[xStarts.length - 2] - VALUE_SHIFT;
        int yEnd = y + 4 * ( CGraphicsHelper.DefaultYShift( g2d ) + CGraphicsHelper.ELEMENTS_SPACING );

        // Вертикальное отделение
        if( withVerticalSeparators ) {
            g2d.setColor( Color.GRAY );
            for( int i = 0; i < xStarts.length; i++ ) {
                int yCurrentEnd = i < xStarts.length - 1 ? y : yEnd;
                if( i != 0 ) {
                    g2d.drawLine( xStarts[i] - VALUE_SHIFT, yStart, xStarts[i] - VALUE_SHIFT, yCurrentEnd );
                }
            }
        }

        // Границы таблицы
        if( withBorder ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( left, yStart, right, yStart );
            g2d.drawLine( left, yStart, left, y );
            g2d.drawLine( right, yStart, right, yEnd );
            g2d.drawLine( xTotalStart, y, xTotalStart, yEnd );
            g2d.drawLine( xTotalStart, yEnd, right, yEnd );
        }
        if( !withBorder && !( withHorizontalSeparators && withVerticalSeparators ) ) {
            // Обведем строку total
            g2d.setColor( Color.GRAY );
            int yCurrentStart = yEnd - CGraphicsHelper.DefaultYShift( g2d ) - CGraphicsHelper.ELEMENTS_SPACING;
            g2d.drawLine( xTotalStart, yCurrentStart, right, yCurrentStart );
            g2d.drawLine( xTotalStart, yCurrentStart, xTotalStart, yEnd );
            g2d.drawLine( xTotalStart, yEnd, right, yEnd );
            g2d.drawLine( right, yCurrentStart, right, yEnd );
        }

        // Итоги таблицы
        g2d.setColor( Color.BLACK );
        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Sub Total: ", xStarts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, totalSum, xStarts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, right, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Tax rate: ", xStarts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, taxRate, xStarts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, right, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Tax: ", xStarts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, tax, xStarts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, right, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Total: ", xStarts[startedIndex + 2], y );
        g2d.setFont( CGraphicsHelper.BOLD_DEFAULT_FONT );
        CGraphicsHelper.DrawString( g2d, total, xStarts[startedIndex + 3], y );
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, right, y );
            g2d.setColor( Color.BLACK );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( left, y + fm.getDescent(), width, y - top );
    }
}
