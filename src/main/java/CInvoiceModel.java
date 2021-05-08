import CoordinatedTypes.CRectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CInvoiceModel implements IModel {

    private static final String DATES_DEF = "Dates {\n";
    private static final String NUMBERS_DEF = "Numbers {\n";
    private static final String PAYMENTS_INFO_DEF = "PaymentsInfo {\n";
    private static final String PRODUCT_CONTAINER_DEF = "ProductContainer {\n";
    private static final String LEFT_BRACKET = " {\n";
    private static final String RIGHT_BRACKET = "}\n";
    private static final Boolean USE_ARIAL = CGraphicsHelper.IsRandomTrue( 0.5 ); // Arial или TNR
    private static final String FONT_NAME = USE_ARIAL ? "Arial" : "Times New Roman";
    private static final Font DEFAULT_FONT = new Font( FONT_NAME, 0, USE_ARIAL ? 13 : 14 );
    private static final Font BOLD_DEFAULT_FONT = new Font( FONT_NAME, Font.BOLD, USE_ARIAL ? 13 : 14 );
    private static final Boolean USE_ADDRESS_COUNTRY = CGraphicsHelper.IsRandomTrue( 0.21657754010695 ); // Показывать ли в адресе страну
    private static final Boolean USE_ADDRESS_STATE = USE_ADDRESS_COUNTRY || CGraphicsHelper.IsRandomTrue( 0.9429590017825 ); // Показывать ли в адресе штат
    private static final Boolean USE_BU = CGraphicsHelper.IsRandomTrue( 0.4661319073083779 ); // Будет ли информация о bu
    private static final Boolean USE_SHIP_TO = CGraphicsHelper.IsRandomTrue( 0.6283422459893048 ); // Будет ли информация о ship-to
    private static final Boolean USE_BILL_TO = CGraphicsHelper.IsRandomTrue( 0.535650623885918 ); // Будет ли информация о bill-to
    private static final Boolean USE_SHIPPER = CGraphicsHelper.IsRandomTrue( 0.19073083778966132 ); // Будет ли информация о shipper
    private static final Boolean USE_REMIT_TO = CGraphicsHelper.IsRandomTrue( 0.375222816399287 ); // Будет ли информация о remit-to

    //private static int languageId;
    private CInvoiceDates invoiceDates = new CInvoiceDates();
    private CInvoiceNumbers invoiceNumbers = new CInvoiceNumbers();
    private CPaymentInfos paymentInfos = new CPaymentInfos();
    private CCompany bu = new CCompany( TCompanyRole.CR_BU );  // bu, покупатель, получатель счета
    private CCompany vendor = new CCompany( TCompanyRole.CR_Vendor );  // vendor, продавец
    private CCompany shipTo = new CCompany( TCompanyRole.CR_ShipTo );  // ship to, получатель товаров/услуг
    private CCompany billTo = new CCompany( TCompanyRole.CR_BillTo );  // bill to, плательщик
    private CCompany shipper = new CCompany( TCompanyRole.CR_Shipper );  // shipper, грузоотправитель
    private CCompany remitTo = new CCompany( TCompanyRole.CR_RemitTo );  // remit to, получатель платежа
    private CProductContainer productContainer = new CProductContainer();   // LineItems, Amounts

    @Override
    public void Generate() {
        invoiceDates.Generate();
        invoiceNumbers.Generate();
        paymentInfos.Generate();
        if( USE_BU ) {
            bu.Generate();
        }
        vendor.Generate();
        if( USE_SHIP_TO ) {
            shipTo.Generate();
        }
        if( USE_BILL_TO ) {
            billTo.Generate();
        }
        if( USE_SHIPPER ) {
            shipper.Generate();
        }
        if( USE_REMIT_TO ) {
            remitTo.Generate();
        }
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

        if( USE_BU ) {
            System.out.print( bu.GetRole().concat( LEFT_BRACKET ) );
            bu.Show();
            System.out.print( RIGHT_BRACKET );
        }

        System.out.print( vendor.GetRole().concat( LEFT_BRACKET ) );
        vendor.Show();
        System.out.print( RIGHT_BRACKET );

        if( USE_SHIP_TO ) {
            System.out.print( shipTo.GetRole().concat( LEFT_BRACKET ) );
            shipTo.Show();
            System.out.print( RIGHT_BRACKET );
        }

        if( USE_BILL_TO ) {
            System.out.print( billTo.GetRole().concat( LEFT_BRACKET ) );
            billTo.Show();
            System.out.print( RIGHT_BRACKET );
        }

        if( USE_SHIPPER ) {
            System.out.print( shipper.GetRole().concat( LEFT_BRACKET ) );
            shipper.Show();
            System.out.print( RIGHT_BRACKET );
        }

        if( USE_REMIT_TO ) {
            System.out.print( remitTo.GetRole().concat( LEFT_BRACKET ) );
            remitTo.Show();
            System.out.print( RIGHT_BRACKET );
        }

        System.out.print( PRODUCT_CONTAINER_DEF );
        productContainer.Show();
        System.out.print( RIGHT_BRACKET );
    }

    @Override
    public String GetData() {
        String res = DATES_DEF.concat( invoiceDates.GetData() ).concat( RIGHT_BRACKET ).
                concat( NUMBERS_DEF ).concat( invoiceNumbers.GetData() ).concat( RIGHT_BRACKET ).
                concat( PAYMENTS_INFO_DEF ).concat( paymentInfos.GetData() ).concat( RIGHT_BRACKET );
        if( USE_BU ) {
            res = res.concat( bu.GetRole() ).concat( LEFT_BRACKET ).concat( bu.GetData() ).concat( RIGHT_BRACKET );
        }
        res = res.concat( vendor.GetRole() ).concat( LEFT_BRACKET ).concat( vendor.GetData() ).concat( RIGHT_BRACKET );
        if( USE_SHIP_TO ) {
            res = res.concat( shipTo.GetRole() ).concat( LEFT_BRACKET ).concat( shipTo.GetData() ).concat( RIGHT_BRACKET );
        }
        if( USE_BILL_TO ) {
            res = res.concat( billTo.GetRole() ).concat( LEFT_BRACKET ).concat( billTo.GetData() ).concat( RIGHT_BRACKET );
        }
        if( USE_SHIPPER ) {
            res = res.concat( shipper.GetRole() ).concat( LEFT_BRACKET ).concat( shipper.GetData() ).concat( RIGHT_BRACKET );
        }
        if( USE_REMIT_TO ) {
            res = res.concat( remitTo.GetRole() ).concat( LEFT_BRACKET ).concat( remitTo.GetData() ).concat( RIGHT_BRACKET );
        }
        return res.concat( PRODUCT_CONTAINER_DEF ).concat( productContainer.GetData() ).concat( RIGHT_BRACKET );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        g2d.setColor( Color.RED );
        invoiceDates.DrawRects( g2d );
        invoiceNumbers.DrawRects( g2d );
        paymentInfos.DrawRects( g2d );
        if( USE_BU ) {
            bu.DrawRects( g2d );
        }
        vendor.DrawRects( g2d );
        if( USE_SHIP_TO ) {
            shipTo.DrawRects( g2d );
        }
        if( USE_BILL_TO ) {
            billTo.DrawRects( g2d );
        }
        if( USE_SHIPPER ) {
            shipper.DrawRects( g2d );
        }
        if( USE_REMIT_TO ) {
            remitTo.DrawRects( g2d );
        }
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
        int rightElementsStart = rightBorder - 240;

        Boolean isVendorOnLeft = CGraphicsHelper.IsRandomTrue( 0.8459687123946589 ); // Покажем ли информацию о vendor и логотип слева/справа
        int x = !isVendorOnLeft ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        int y = CGraphicsHelper.TOP_PADDING;
        CRectangle logoCoord = drawVendorLogo( g2d, x, y );
        y = logoCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        CRectangle vendorCoord = drawCompanyInfo( g2d, x, 240, y, vendor );

        g2d.setFont( new Font( FONT_NAME, Font.BOLD, 20 ) );
        g2d.setPaint( Color.BLACK );
        Boolean isCaptionOnCenter = CGraphicsHelper.IsRandomTrue( 0.5 ); // Покажем ли 'INVOICE' по центру или над информацией об инвойсе
        y = CGraphicsHelper.TOP_PADDING + g2d.getFont().getSize();
        x = isCaptionOnCenter ? 350 : isVendorOnLeft ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        CRectangle captionCoord = CGraphicsHelper.DrawString( g2d, "INVOICE", x, y );

        g2d.setFont( DEFAULT_FONT );

        if( isCaptionOnCenter ) {
            x = isVendorOnLeft ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        } else {
            y = captionCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        }
        CRectangle invoiceInfoCoord = drawInvoiceInfo( g2d, x, y );

        y = Math.max( invoiceInfoCoord.GetBottom(), vendorCoord.GetBottom() ) +
                CGraphicsHelper.BLOCKS_SPACING;
        CRectangle companiesCoord = drawCompaniesInfo( g2d, y );

        x = CGraphicsHelper.LEFT_PADDING;
        y = companiesCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 2;
        CRectangle tableCoord = drawTableInfo( g2d, x, y );

        Boolean isLeftBankInfo = CGraphicsHelper.IsRandomTrue( 0.5 ); // Покажем банковскую инфомацию слева/справа
        x = isLeftBankInfo ? CGraphicsHelper.LEFT_PADDING : rightElementsStart;
        y = tableCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 3;
        drawBankInfo( g2d, x, y );
    }

    // Блок с логотипом компании
    private CRectangle drawVendorLogo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        // Логотип
        g2d.setFont( new Font( FONT_NAME, Font.BOLD | Font.ITALIC, 30 ) );
        String logoName = vendor.GetName().GetValue();
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
    private CRectangle drawCompanyInfo( Graphics2D g2d, int x, int maxWidth, int y, CCompany company ) {
        int l = x;
        int w = 0;
        int t = y;

        // Обязательные элементы
        CRectangle rect = CGraphicsHelper.DrawMultilineText( g2d, company.GetName(), x, x + maxWidth, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawMultilineText( g2d, company.GetAddress().GetLines(), x, x + maxWidth, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawStrings( g2d, company.GetAddress().GetZip(), company.GetAddress().GetCity(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        // Необязательные элементы
        if( USE_ADDRESS_STATE ) {
            rect = USE_ADDRESS_COUNTRY ? CGraphicsHelper.DrawStrings( g2d, company.GetAddress().GetState(),
                    company.GetAddress().GetCountry(), x, y )
                    : CGraphicsHelper.DrawString( g2d, company.GetAddress().GetState(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Phone: ", company.GetPhone(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Website: ", company.GetWebSite(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Email: ", company.GetEmail(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent() - CGraphicsHelper.DefaultYShift( g2d ), w, y - t );
    }

    // Блок информации об инвойсе
    private CRectangle drawInvoiceInfo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        CRectangle rect;
        if( CGraphicsHelper.IsRandomTrue( 0.8672014260249554 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Invoice Number: ", invoiceNumbers.GetInvoiceNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.9010695187165776 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Invoice Date: ", invoiceDates.GetInvoiceDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Client Number: ", invoiceNumbers.GetClientNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.3342245989304813 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Due Date: ", invoiceDates.GetPaymentDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.33511586452762926 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Delivery Date: ", invoiceDates.GetExpeditionDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Used payment: ", paymentInfos.GetUsedPyment(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.25363692896900713 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Total: ", productContainer.GetTotal(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            w = Math.max( w, rect.GetWidth() );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, y + fm.getDescent(), w, y - t );
    }

    // Блок с информацией о других компаниях
    private CRectangle drawCompaniesInfo( Graphics2D g2d, int y ) {
        int l = CGraphicsHelper.LEFT_PADDING;
        int r = CGraphicsHelper.LEFT_PADDING + 700;
        int t = y;

        Boolean withHorizontalSeparator = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли горизонтальный разделитель заголовка
        Boolean withVerticalSeparators = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли вертикальные разделители между столбцами
        Boolean withBorder = CGraphicsHelper.IsRandomTrue( 0.5 ); // Границы таблицы

        // Заголовок
        List<CCompany> companies = new ArrayList<>();
        if( USE_BU ) {
            companies.add( bu );
        }
        if( USE_SHIP_TO ) {
            companies.add( shipTo );
        }
        if( USE_BILL_TO ) {
            companies.add( billTo );
        }
        if( USE_SHIPPER ) {
            companies.add( shipper );
        }
        if( USE_REMIT_TO ) {
            companies.add( remitTo );
        }
        if( companies.size() == 0 ) {
            return new CRectangle( l, t, 0, 0 );
        }
        g2d.setFont( BOLD_DEFAULT_FONT );
        int columnWidth = 700 / companies.size(); // Ширина одного столбца
        int yStart = y - CGraphicsHelper.DefaultYShift( g2d );
        for( int i = 0; i < companies.size(); i++ ) {
            g2d.drawString( companies.get( i ).GetRole(), l + 5 + i * columnWidth, y );
        }

        // Отчеркивание заголовка
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparator ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( l, y, r, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        int b = y;

        // Данные о компаниях
        g2d.setFont( DEFAULT_FONT );
        for( int i = 0; i < companies.size(); i++ ) {
            CRectangle rect = drawCompanyInfo( g2d, l + 5 + i * columnWidth, columnWidth - 10, y, companies.get( i ) );
            b = Math.max( b, rect.GetBottom() );
        }

        // Вертикальное отделение
        if( withVerticalSeparators ) {
            g2d.setColor( Color.GRAY );
            for( int i = 1; i < companies.size(); i++ ) {
                int xCoord = l + i * columnWidth;
                g2d.drawLine( xCoord, yStart, xCoord, b );
            }
        }

        // Границы таблицы
        if( withBorder ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( l, yStart, r, yStart );
            g2d.drawLine( l, yStart, l, b );
            g2d.drawLine( r, yStart, r, b );
            g2d.drawLine( l, b, r, b );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( l, b, r - l, b - t );
    }

    // Блок банковской информации
    private CRectangle drawBankInfo( Graphics2D g2d, int x, int y ) {
        int l = x;
        int w = 0;
        int t = y;

        CRectangle rect = CGraphicsHelper.DrawString( g2d, "Bank: ", vendor.GetBank().GetName(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Account No: ", vendor.GetBank().GetBankAccount(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Sort Code: ", vendor.GetBank().GetSortCode(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "SWIFT: ", vendor.GetBank().GetSwift(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        w = Math.max( w, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "IBAN No: ", vendor.GetBank().GetIban(), x, y );
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

        Boolean withEnumeration = CGraphicsHelper.IsRandomTrue( 0.5 ); // Показывать ли с нумерацией продуктов
        Boolean withHorizontalSeparators = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли горизонтальные разделители между элементами
        Boolean withVerticalSeparators = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли вертикальные разделители между столбцами
        Boolean withBorder = CGraphicsHelper.IsRandomTrue( 0.5 ); // Границы таблицы

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
        for( int i = 0; i < productContainer.GetHeaders().size(); i++ ) {
            g2d.drawString( productContainer.GetHeaders().get( i ), x_starts[i + startedIndex], y );
        }

        // Отчеркивание заголовка
        g2d.setColor( Color.GRAY );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        g2d.drawLine( xStart, y, xEnd, y );
        g2d.setColor( Color.BLACK );

        // Данные продуктов
        g2d.setFont( DEFAULT_FONT );
        for( int i = 0; i < productContainer.GetProducts().size(); i++ ) {
            CProduct product = productContainer.GetProducts().get( i );
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
            if( withHorizontalSeparators || i == productContainer.GetProducts().size() - 1 ) {
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
        CGraphicsHelper.DrawString( g2d, "Sub Total: ", x_starts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, productContainer.GetTotalSum(), x_starts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Tax rate: ", x_starts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, productContainer.GetTaxRate(), x_starts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Tax: ", x_starts[startedIndex + 2], y );
        CGraphicsHelper.DrawString( g2d, productContainer.GetTax(), x_starts[startedIndex + 3], y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparators ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( xTotalStart, y, xEnd, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        CGraphicsHelper.DrawString( g2d, "Total: ", x_starts[startedIndex + 2], y );
        g2d.setFont( BOLD_DEFAULT_FONT );
        CGraphicsHelper.DrawString( g2d, productContainer.GetTotal(), x_starts[startedIndex + 3], y );
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
