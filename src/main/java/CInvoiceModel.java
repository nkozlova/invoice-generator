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

    private static final String DATES_DEF = "Dates:\n";
    private static final String NUMBERS_DEF = "Numbers:\n";
    private static final String PAYMENTS_INFO_DEF = "PaymentsInfo:\n";
    private static final String COMPANIES_DEF = "Companies:\n";
    private static final String PRODUCT_CONTAINER_DEF = "ProductContainer:\n";
    private static final Boolean USE_BU = CGraphicsHelper.IsRandomTrue( 0.4661319073083779 ); // Будет ли информация о bu
    private static final Boolean USE_SHIP_TO = CGraphicsHelper.IsRandomTrue( 0.6283422459893048 ); // Будет ли информация о ship-to
    private static final Boolean USE_BILL_TO = CGraphicsHelper.IsRandomTrue( 0.535650623885918 ); // Будет ли информация о bill-to
    private static final Boolean USE_SHIPPER = CGraphicsHelper.IsRandomTrue( 0.19073083778966132 ); // Будет ли информация о shipper
    private static final Boolean USE_REMIT_TO = CGraphicsHelper.IsRandomTrue( 0.375222816399287 ); // Будет ли информация о remit-to
    private static final int VALUE_SHIFT = 5;

    private CInvoiceDates invoiceDates = new CInvoiceDates();
    private CInvoiceNumbers invoiceNumbers = new CInvoiceNumbers();
    private CPaymentInfos paymentInfos = new CPaymentInfos();
    private CCompany bu = new CCompany( TCompanyRole.CR_BU );  // bu, покупатель, получатель счета
    private CCompany vendor = new CCompany( TCompanyRole.CR_Vendor );  // vendor, продавец
    private CCompany shipTo = new CCompany( TCompanyRole.CR_ShipTo );  // ship to, получатель товаров/услуг
    private CCompany billTo = new CCompany( TCompanyRole.CR_BillTo );  // bill to, плательщик
    private CCompany shipper = new CCompany( TCompanyRole.CR_Shipper );  // shipper, грузоотправитель
    private CCompany remitTo = new CCompany( TCompanyRole.CR_RemitTo );  // remit to, получатель платежа
    private CProductContainer productContainer = new CProductContainer();   // блоки LineItems, Amounts

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

    public void Show() { System.out.print( GetData() ); }

    @Override
    public String GetData() {
        String res = DATES_DEF.concat( invoiceDates.GetData() ).
                concat( NUMBERS_DEF ).concat( invoiceNumbers.GetData() ).
                concat( PAYMENTS_INFO_DEF ).concat( paymentInfos.GetData() ).
                concat( COMPANIES_DEF );
        if( USE_BU ) {
            res = res.concat( bu.GetData() );
        }
        res = res.concat( vendor.GetData() );
        if( USE_SHIP_TO ) {
            res = res.concat( shipTo.GetData() );
        }
        if( USE_BILL_TO ) {
            res = res.concat( billTo.GetData() );
        }
        if( USE_SHIPPER ) {
            res = res.concat( shipper.GetData() );
        }
        if( USE_REMIT_TO ) {
            res = res.concat( remitTo.GetData() );
        }
        return res.concat( PRODUCT_CONTAINER_DEF ).concat( productContainer.GetData() );
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

        int rightElementsStart = CGraphicsHelper.A4_WIDTH - CGraphicsHelper.RIGHT_PADDING - CGraphicsHelper.BLOCK_WIDTH;

        Boolean isVendorOnLeft = CGraphicsHelper.IsRandomTrue( 0.8459687123946589 ); // Покажем ли информацию о vendor и логотип слева/справа
        int x = !isVendorOnLeft ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        int y = CGraphicsHelper.TOP_PADDING;
        CRectangle logoCoord = drawVendorLogo( g2d, x, y );
        y = logoCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        CRectangle vendorCoord = vendor.DrawInfo( g2d, x, CGraphicsHelper.BLOCK_WIDTH, y );

        g2d.setFont( CGraphicsHelper.INVOICE_CAPTION_FONT );
        g2d.setPaint( Color.BLACK );
        Boolean isCaptionOnCenter = CGraphicsHelper.IsRandomTrue( 0.7 ); // Покажем ли 'INVOICE' по центру или над информацией об инвойсе
        y = CGraphicsHelper.TOP_PADDING + g2d.getFont().getSize();
        x = isCaptionOnCenter ? 350 : isVendorOnLeft ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        CRectangle captionCoord = CGraphicsHelper.DrawString( g2d, "INVOICE", x, y );

        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );

        if( isCaptionOnCenter ) {
            x = isVendorOnLeft ? rightElementsStart : CGraphicsHelper.LEFT_PADDING;
        } else {
            y = captionCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING;
        }
        CRectangle invoiceInfoCoord = drawInvoiceInfo( g2d, x, y );

        y = Math.max( invoiceInfoCoord.GetBottom(), vendorCoord.GetBottom() ) + CGraphicsHelper.BLOCKS_SPACING * 2;
        CRectangle companiesCoord = drawCompaniesInfo( g2d, y );

        x = CGraphicsHelper.LEFT_PADDING;
        y = companiesCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 3;
        CRectangle tableCoord = productContainer.DrawTableInfo( g2d, y );

        if( CGraphicsHelper.IsRandomTrue( 0.4 ) ) {
            Boolean isBankInfoOnLeft = CGraphicsHelper.IsRandomTrue( 0.6 ); // Покажем банковскую инфомацию слева/справа
            x = isBankInfoOnLeft ? CGraphicsHelper.LEFT_PADDING : rightElementsStart;
            y = tableCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 3;
            vendor.GetBank().DrawInfo( g2d, x, y );
        }
    }

    // Блок с логотипом компании
    private CRectangle drawVendorLogo( Graphics2D g2d, int x, int y ) {
        int left = x;
        int width = 0;
        int top = y;

        // Логотип
        g2d.setFont( new Font( CGraphicsHelper.FONT_NAME, Font.BOLD | Font.ITALIC, 30 ) );
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
            width = ( (BufferedImage) image ).getWidth();

            switch( i ) {
                case 0: {
                    g2d.setPaint( Color.WHITE );
                    CGraphicsHelper.DrawString( g2d, logoName, x + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                    break;
                }
                case 1: {
                    g2d.setPaint( new Color( 1, 82, 145 ) );
                    CGraphicsHelper.DrawString( g2d, logoName, x + width + CGraphicsHelper.ELEMENTS_SPACING,
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
                    CGraphicsHelper.DrawString( g2d, logoName, x + width + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                    break;
                }
                default:
                {
                    g2d.setPaint( Color.BLACK );
                    CGraphicsHelper.DrawString( g2d, logoName, x + width + CGraphicsHelper.ELEMENTS_SPACING,
                            y + ((BufferedImage) image).getHeight() / 2 + CGraphicsHelper.ELEMENTS_SPACING * 2 );
                }
            }
            y += ( (BufferedImage) image ).getHeight() + CGraphicsHelper.BLOCKS_SPACING;
        } catch( IOException e ) {
            System.out.println( "Not fined logo file!" );
        }

        g2d.setPaint( Color.BLACK );
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );

        return new CRectangle( left, y, width, y - top );
    }

    // Блок информации об инвойсе
    private CRectangle drawInvoiceInfo( Graphics2D g2d, int x, int y ) {
        int left = x;
        int width = 0;
        int top = y;

        CRectangle rect;
        if( CGraphicsHelper.IsRandomTrue( 0.8672014260249554 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Invoice Number: ", invoiceNumbers.GetInvoiceNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.9010695187165776 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Invoice Date: ", invoiceDates.GetInvoiceDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "PO Number: ", invoiceNumbers.GetPurchaseOrderNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.3 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Client Number: ", invoiceNumbers.GetClientNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.3342245989304813 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Due Date: ", invoiceDates.GetPaymentDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.33511586452762926 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Delivery Date: ", invoiceDates.GetExpeditionDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.3 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Used payment: ", paymentInfos.GetUsedPyment(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }
        if( CGraphicsHelper.IsRandomTrue( 0.25363692896900713 ) ) {
            rect = CGraphicsHelper.DrawString( g2d, "Total: ", productContainer.GetTotal(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( left, y + fm.getDescent(), width, y - top );
    }

    // Блок с информацией о других компаниях
    private CRectangle drawCompaniesInfo( Graphics2D g2d, int y ) {
        int left = CGraphicsHelper.LEFT_PADDING;
        int right = CGraphicsHelper.A4_WIDTH - CGraphicsHelper.RIGHT_PADDING;
        int width = right - left;
        int top = y;

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
            return new CRectangle( left, top, 0, 0 );
        }
        g2d.setFont( CGraphicsHelper.BOLD_DEFAULT_FONT );
        int columnWidth = width / companies.size(); // Ширина одного столбца
        int yStart = y - CGraphicsHelper.DefaultYShift( g2d );
        for( int i = 0; i < companies.size(); i++ ) {
            g2d.drawString( companies.get( i ).GetRole(), left + i * columnWidth + VALUE_SHIFT, y );
        }

        // Отчеркивание заголовка
        y += CGraphicsHelper.ELEMENTS_SPACING;
        if( withHorizontalSeparator ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( left, y, right, y );
            g2d.setColor( Color.BLACK );
        }

        y += CGraphicsHelper.DefaultYShift( g2d );
        int b = y;

        // Данные о компаниях
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );
        for( int i = 0; i < companies.size(); i++ ) {
            CRectangle rect = companies.get( i ).DrawInfo( g2d, left + i * columnWidth + VALUE_SHIFT,
                    columnWidth - VALUE_SHIFT * 2, y );
            b = Math.max( b, rect.GetBottom() );
        }

        // Вертикальное отделение
        if( withVerticalSeparators ) {
            g2d.setColor( Color.GRAY );
            for( int i = 1; i < companies.size(); i++ ) {
                int xCoord = left + i * columnWidth;
                g2d.drawLine( xCoord, yStart, xCoord, b );
            }
        }

        // Границы таблицы
        if( withBorder ) {
            g2d.setColor( Color.GRAY );
            g2d.drawLine( left, yStart, right, yStart );
            g2d.drawLine( left, yStart, left, b );
            g2d.drawLine( right, yStart, right, b );
            g2d.drawLine( left, b, right, b );
        }

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( left, b, width, b - top );
    }
}
