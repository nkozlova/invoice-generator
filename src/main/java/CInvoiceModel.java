import CoordinatedTypes.CRectangle;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CInvoiceModel implements IModel {

    private static final String DATES_DEF = "Dates {\n";
    private static final String NUMBERS_DEF = "Numbers {\n";
    private static final String PAYMENTS_INFO_DEF = "PaymentsInfo {\n";
    private static final String COMPANY_DEF = "Company {\n";
    private static final String VENDOR_DEF = "Vendor {\n";
    private static final String PRODUCT_CONTAINER_DEF = "ProductContainer {\n";
    private static final String RIGHT_BRACKET = "}\n";
    private static final Boolean USE_ARIAL = CGraphicsHelper.IsRandomTrue(); // Arial или TNR
    private static final String FONT_NAME = USE_ARIAL ? "Arial" : "Times New Roman";
    private static final Font DEFAULT_FONT = new Font( FONT_NAME, 0, USE_ARIAL ? 13 : 14 );
    private static final Font BOLD_DEFAULT_FONT = new Font( FONT_NAME, Font.BOLD, USE_ARIAL ? 13 : 14 );

    //private static int languageId;
    private CInvoiceDates invoiceDates = new CInvoiceDates();
    private CInvoiceNumbers invoiceNumbers = new CInvoiceNumbers();
    private CPaymentInfos paymentInfos = new CPaymentInfos();
    private CCompany company = new CCompany();  // bu
    private CCompany vendor = new CCompany();  // vendor3
    private CProductContainer productContainer = new CProductContainer();   // LineItems, Amounts

    public CInvoiceDates GetInvoiceDates() { return invoiceDates; }
    public CInvoiceNumbers GetInvoiceNumbers() { return invoiceNumbers; }
    public CPaymentInfos GetPaymentInfos() { return paymentInfos; }
    public CCompany GetCompany() { return company; }
    public CCompany GetVendor() { return vendor; }
    public CProductContainer GetProductContainer() { return productContainer; }

    @Override
    public void Generate() {
        invoiceDates.Generate();
        invoiceNumbers.Generate();
        paymentInfos.Generate();
        company.Generate();
        vendor.Generate();
        productContainer.Generate();
    }

    @Override
    public void Show() {
        System.out.print( DATES_DEF );
        invoiceDates.Show();
        System.out.print( RIGHT_BRACKET );

        System.out.print( NUMBERS_DEF );
        invoiceNumbers.Show();
        System.out.print( RIGHT_BRACKET );

        System.out.print( PAYMENTS_INFO_DEF );
        paymentInfos.Show();
        System.out.print( RIGHT_BRACKET );

        System.out.print( COMPANY_DEF );
        company.Show();
        System.out.print( RIGHT_BRACKET );

        System.out.print( VENDOR_DEF );
        vendor.Show();
        System.out.print( RIGHT_BRACKET );

        System.out.print( PRODUCT_CONTAINER_DEF );
        productContainer.Show();
        System.out.print( RIGHT_BRACKET );
    }

    @Override
    public String GetData() {
        return DATES_DEF.concat( invoiceDates.GetData() ).concat( RIGHT_BRACKET ).
                concat( NUMBERS_DEF ).concat( invoiceNumbers.GetData() ).concat( RIGHT_BRACKET ).
                concat( PAYMENTS_INFO_DEF ).concat( paymentInfos.GetData() ).concat( RIGHT_BRACKET ).
                concat( COMPANY_DEF ).concat( company.GetData() ).concat( RIGHT_BRACKET ).
                concat( VENDOR_DEF ).concat( vendor.GetData() ).concat( RIGHT_BRACKET ).
                concat( PRODUCT_CONTAINER_DEF ).concat( productContainer.GetData() ).concat( RIGHT_BRACKET );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        g2d.setColor( Color.RED );
        invoiceDates.DrawRects( g2d );
        invoiceNumbers.DrawRects( g2d );
        paymentInfos.DrawRects( g2d );
        company.DrawRects( g2d );
        vendor.DrawRects( g2d );
        productContainer.DrawRects( g2d );
    }

    public void CreateAndSaveImage() {
        BufferedImage bImg = new BufferedImage( CGraphicsHelper.A4_WIDTH, CGraphicsHelper.A4_HEIGHT, BufferedImage.TYPE_INT_RGB );
        Graphics2D g2d = bImg.createGraphics();

        draw( g2d ); // отрисовываем

        // Будем сохранять результат по текущей дате
        Long currentData = System.currentTimeMillis();

        try {
            // Картинка инвойса
            ImageIO.write( bImg, "PNG", new File( "C:\\invoice-images-generate\\".
                    concat( currentData.toString() ).concat( ".PNG" ) ) );
        } catch( IOException e ) {
            e.printStackTrace();
        }

        try {
            // Сохранение данных
            File file = new File( "C:\\invoice-images-generate\\".concat( currentData.toString() ).concat( ".txt" ) );
            file.createNewFile();
            FileWriter writer = new FileWriter( file );
            writer.write( GetData() );
            writer.close();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        // Проверка, что координаты находятся правильно
        DrawRects( g2d );
        try {
            // Картинка инвойса
            ImageIO.write( bImg, "PNG", new File( "C:\\invoice-images-generate\\".
                    concat( currentData.toString() ).concat( "_check.PNG" ) ) );
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        CInvoiceModel invoice = new CInvoiceModel();
        invoice.Generate(); // Генерируем данные
        invoice.CreateAndSaveImage(); // Рисуем и созраняем изображение
        invoice.Show(); // Вывод данных
    }

    // Нарисуем инвойс
    private void draw( Graphics2D g2d ) {
        g2d.setBackground( Color.WHITE );
        g2d.clearRect( 0, 0, CGraphicsHelper.A4_WIDTH, CGraphicsHelper.A4_HEIGHT );

        int rightBorder = CGraphicsHelper.A4_WIDTH - CGraphicsHelper.RIGHT_PADDING;
        int rightElementsStart = rightBorder - 220;

        Boolean isLeftCompanyShowed = CGraphicsHelper.IsRandomTrue(); // Покажем ли информацию о комании и логотип слева/справа
        int x = isLeftCompanyShowed ? CGraphicsHelper.LEFT_PADDING : rightElementsStart;
        int y = CGraphicsHelper.TOP_PADDING;
        CRectangle logoCoord = drawCompanyLogo( g2d, x, y );
        y = logoCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        CRectangle companyCoord = drawCompanyInfo( g2d, x, y );

        g2d.setFont( new Font( FONT_NAME, Font.BOLD, 20 ) );
        g2d.setPaint( Color.BLACK );
        Boolean isCenterInvoiceShowed = CGraphicsHelper.IsRandomTrue(); // Покажем ли 'INVOICE' по центру или над информацией об инвойсе
        y = CGraphicsHelper.TOP_PADDING + g2d.getFont().getSize();
        x = isCenterInvoiceShowed ? 350 : isLeftCompanyShowed ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        CRectangle captionCoord = CGraphicsHelper.DrawString( g2d, "INVOICE", x, y );

        g2d.setFont( DEFAULT_FONT );

        if( isCenterInvoiceShowed ) {
            x = isLeftCompanyShowed ? x = rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        } else {
            y = captionCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        }
        CRectangle invoiceInfoCoord = drawInvoiceInfo( g2d, x, rightBorder, y );

        Boolean isVendorAfterCompany = false; //!isLeftCompanyShowed && CGraphicsHelper.IsRandomTrue(); // Покажем vendor-информацию справа
                                                            // под информацией о компании или под инормацией об инвойсе слева/справа
        if( isVendorAfterCompany ) {
            x = rightElementsStart;
            y = companyCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        } else {
            y = invoiceInfoCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        }
        CRectangle vendorCoord = drawVendorInfo( g2d, x, rightBorder, y );

        x = CGraphicsHelper.LEFT_PADDING;
        y = Math.max( companyCoord.GetBottom(), Math.max( invoiceInfoCoord.GetBottom(), vendorCoord.GetBottom() ) ) +
                CGraphicsHelper.BLOCKS_SPACING * 4;
        CRectangle tableCoord = drawTableInfo( g2d, x, y );

        Boolean isLeftBankInfo = CGraphicsHelper.IsRandomTrue(); // Покажем банковскую инфомацию слева/справа
        x = isLeftBankInfo ? CGraphicsHelper.LEFT_PADDING : rightElementsStart;
        y = tableCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 3;
        drawBankInfo( g2d, x, y );
    }

    // Блок с логотипом компании
    private CRectangle drawCompanyLogo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        // Логотип
        g2d.setFont( new Font( FONT_NAME, Font.BOLD | Font.ITALIC, 30 ) );
        String logoName = GetCompany().GetName().GetValue();
        if( logoName.contains( " " ) && logoName.length() > 7 ) {
            // До 7 символов в названии -- оставляем их
            // Иначе - по первым буквам
            logoName = CStringsHelper.GetStartedOfStr( logoName, true );
        }
        try {
            File directory = new File( "logos/" );
            File logos[] = directory.listFiles();
            int i = (int)( Math.random() * logos.length );

            Image image = ImageIO.read( logos[i] );
            g2d.drawImage( image, x, y, ( (BufferedImage) image ).getWidth(), ( ( BufferedImage ) image ).getHeight(), null );
            w = ( (BufferedImage) image ).getWidth();

            switch( i ) {
                case 0: {
                    g2d.setPaint( Color.WHITE );
                    CGraphicsHelper.DrawString( g2d, logoName, x + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                    break;
                }
                case 1: {
                    g2d.setPaint( new Color( 1, 82, 145 ) );
                    CGraphicsHelper.DrawString( g2d, logoName, x + w + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                    break;
                }
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                {
                    g2d.setPaint( new Color( 251, 0, 131 ) );
                    CGraphicsHelper.DrawString( g2d, logoName, x + w + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                    break;
                }
                default:
                {
                    g2d.setPaint( Color.BLACK );
                    CGraphicsHelper.DrawString( g2d, logoName, x + w + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                }
            }
            y += ( (BufferedImage) image ).getHeight() + CGraphicsHelper.BLOCKS_SPACING;
        } catch( IOException e ) {
            System.out.println( "Not fined logo file!" );
        }

        g2d.setPaint( Color.BLACK );
        g2d.setFont( DEFAULT_FONT );

        return new CRectangle( l, y, w, y - t );
    }

    // Блок информации о компании
    private CRectangle drawCompanyInfo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        // Обязательные элементы
        CRectangle rect = CGraphicsHelper.DrawString( g2d, GetCompany().GetName(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, GetCompany().GetAddress().GetLines(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawStrings( g2d, GetCompany().GetAddress().GetZip(), GetCompany().GetAddress().GetCity(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        // Необязательные элементы - выводим с 50% вероятностью
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawStrings( g2d, GetCompany().GetAddress().GetState(), GetCompany().GetAddress().GetCountry(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawString( g2d, "Phone: ", GetCompany().GetPhone(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawString( g2d, "Website: ", GetCompany().GetWebSite(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawString( g2d, "Email: ", GetCompany().GetEmail(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent(), w, y - t );
    }

    // Блок информации об инвойсе
    private CRectangle drawInvoiceInfo( Graphics2D g2d, int x, int rightBorder, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        CRectangle rect = CGraphicsHelper.DrawString( g2d, "Invoice Number: ", GetInvoiceNumbers().GetInvoiceNumber(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Date: ", GetInvoiceDates().GetInvoiceDate(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        // Необязательные элементы - выводим с 50% вероятностью
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawString( g2d, "Client Number: ", GetInvoiceNumbers().GetClientNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue() ) {
            CRectangle rect1 = CGraphicsHelper.DrawString( g2d, "Company Name: ", x, y );
            rect = CGraphicsHelper.DrawMultilineText( g2d, GetCompany().GetName(), x + rect1.GetWidth(), rightBorder, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() + rect1.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawString( g2d, "Due Date: ", GetInvoiceDates().GetPaymentDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawString( g2d, "Used payment: ", GetPaymentInfos().GetUsedPyment(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent(), w, y - t );
    }

    // Блок информации о вендоре
    private CRectangle drawVendorInfo( Graphics2D g2d, int x, int rightBorder, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        CRectangle rect = CGraphicsHelper.DrawMultilineText( g2d, GetVendor().GetName(), x, rightBorder, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawMultilineText( g2d, GetVendor().GetAddress().GetLines(), x, rightBorder, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawStrings( g2d, GetVendor().GetAddress().GetZip(), GetVendor().GetAddress().GetCity(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        // Необязательные элементы - выводим с 50% вероятностью
        if( CGraphicsHelper.IsRandomTrue() ) {
            rect = CGraphicsHelper.DrawStrings( g2d, GetVendor().GetAddress().GetState(), GetVendor().GetAddress().GetCountry(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent(), w, y - t );
    }

    // Блок банковской информации
    private CRectangle drawBankInfo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        CRectangle rect = CGraphicsHelper.DrawString( g2d, "Bank: ", GetVendor().GetBank().GetName(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Account No: ", GetVendor().GetBank().GetBankAccount(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Sort Code: ", GetVendor().GetBank().GetSortCode(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "SWIFT: ", GetVendor().GetBank().GetSwift(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "IBAN No: ", GetVendor().GetBank().GetIban(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent(), w, y - t );
    }

    // Блок с таблицей
    private CRectangle drawTableInfo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        Boolean withEnumeration = CGraphicsHelper.IsRandomTrue(); // Показывать ли с нумерацией продуктов
        Boolean withHorizontalSeparators = false;// CGraphicsHelper.IsRandomTrue(); // Рисовать ли горизонтальные разделители между элементами
        Boolean withVerticalSeparators = false;//CGraphicsHelper.IsRandomTrue(); // Рисовать ли вертикальные разделители между столбцами
        Boolean withBorder = CGraphicsHelper.IsRandomTrue(); // Границы таблицы

        // Заголовок
        g2d.setFont( BOLD_DEFAULT_FONT );
        int startedIndex = withEnumeration ? 1 : 0;
        int xStart = CGraphicsHelper.LEFT_PADDING;
        int xEnd = CGraphicsHelper.LEFT_PADDING + 700;
        int[] x_starts;
        if( withEnumeration ) {
            x_starts = new int[] { xStart + 5, xStart + 25, xStart + 435, xStart + 525, xStart + 615 };
        } else {
            x_starts = new int[] { xStart + 5, xStart + 405, xStart + 505, xStart + 605 };
        }
        int yStart = y - CGraphicsHelper.DefaultYShift( g2d );
        for( int i = 0; i < GetProductContainer().GetHeaders().size(); i++ ) {
            g2d.drawString( GetProductContainer().GetHeaders().get( i ), x_starts[i + startedIndex], y );
        }

        // Отчеркивание заголовка
        g2d.setColor( Color.GRAY );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        g2d.drawLine( xStart, y, xEnd, y );
        g2d.setColor( Color.BLACK );

        // Данные продуктов
        g2d.setFont( DEFAULT_FONT );
        for( int i = 0; i < GetProductContainer().GetProducts().size(); i++ ) {
            CProduct product = GetProductContainer().GetProducts().get( i );
            y += CGraphicsHelper.DefaultYShift( g2d );
            if( withEnumeration ) {
                Integer enumerate = i + 1;
                CGraphicsHelper.DrawString( g2d, enumerate.toString(), x_starts[0], y );
            }
            CGraphicsHelper.DrawString( g2d, product.GetPrice(), x_starts[startedIndex + 1], y );
            CGraphicsHelper.DrawString( g2d, product.GetQuantity(), x_starts[startedIndex + 2], y );
            CGraphicsHelper.DrawString( g2d, product.GetTotalPrice(), x_starts[startedIndex + 3], y );

            y = CGraphicsHelper.DrawMultilineText( g2d, product.GetDescription(), x_starts[startedIndex], x_starts[startedIndex + 1] - 10, y ).GetBottom() +
                    CGraphicsHelper.ELEMENTS_SPACING;

            // Горизонтальное отделение
            if( withHorizontalSeparators || i == GetProductContainer().GetProducts().size() - 1 ) {
                g2d.setColor( Color.GRAY );
                g2d.drawLine( xStart, y, xEnd, y );
                g2d.setColor( Color.BLACK );
            }
        }

        int xTotalStart = x_starts[x_starts.length - 2] - 5;
        int yEnd = y + 4 * ( CGraphicsHelper.DefaultYShift( g2d ) + CGraphicsHelper.ELEMENTS_SPACING );

        // Вертикальное отделение
        if( withVerticalSeparators ) {
            g2d.setColor( Color.GRAY );
            for( int i = 0; i < x_starts.length; i++ ) {
                int yCurrentEnd = i < x_starts.length - 1 ? y : yEnd;
                if( i != 0 ) {
                    g2d.drawLine( x_starts[i] - 5, yStart, x_starts[i] - 5, yCurrentEnd );
                }
            }
        }

        // Границы таблицы
        if( withBorder ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xStart, yStart, xEnd, yStart );
            g2d.drawLine( xStart, yStart, xStart, y );
            g2d.drawLine( xEnd, yStart, xEnd, yEnd );
            g2d.drawLine( xTotalStart, y, xTotalStart, yEnd );
            g2d.drawLine( xTotalStart, yEnd, xEnd, yEnd );
        }
        if( !withBorder && !( withHorizontalSeparators && withVerticalSeparators ) ) {
            // Обведем строку total
            g2d.setColor( Color.GRAY );
            int yCurrentStart = yEnd - CGraphicsHelper.DefaultYShift( g2d ) - CGraphicsHelper.ELEMENTS_SPACING;
            g2d.drawLine( xTotalStart, yCurrentStart, xEnd, yCurrentStart );
            g2d.drawLine( xTotalStart, yCurrentStart, xTotalStart, yEnd );
            g2d.drawLine( xTotalStart, yEnd, xEnd, yEnd );
            g2d.drawLine( xEnd, yCurrentStart, xEnd, yEnd );
        }

        // Итоги таблицы
        g2d.setColor( Color.BLACK );
        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Total Sum: ", x_starts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, GetProductContainer().GetTotalSum(), x_starts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Tax rate: ", x_starts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, GetProductContainer().GetTaxRate(), x_starts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Tax: ", x_starts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, GetProductContainer().GetTax(), x_starts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Total: ", x_starts[startedIndex + 2], y );
        g2d.setFont( BOLD_DEFAULT_FONT );
        CGraphicsHelper.DrawString( g2d, GetProductContainer().GetTotal(), x_starts[startedIndex + 3], y );
        g2d.setFont( DEFAULT_FONT );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent(), w, y - t );
    }
}
