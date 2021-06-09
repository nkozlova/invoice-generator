import CoordinatedTypes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CInvoiceModel implements IModel {

    private static final String DATES_DEF = "Dates:\n";
    private static final String NUMBERS_DEF = "Numbers:\n";
    private static final String PAYMENTS_INFO_DEF = "PaymentsInfo:\n";
    private static final String COMPANIES_DEF = "Companies:\n";
    private static final String PRODUCT_CONTAINER_DEF = "ProductContainer:\n";
    private static Boolean USE_BU = CGraphicsHelper.IsRandomTrue( 0.4661319073083779 ); // Будет ли информация о bu
    private static Boolean USE_BU_ON_TOP = CGraphicsHelper.IsRandomTrue( 0.84637964774951079 ); // Будет ли информация о bu наверху или внизу
    private static Boolean USE_SHIP_TO = CGraphicsHelper.IsRandomTrue( 0.6283422459893048 ); // Будет ли информация о ship-to
    private static Boolean USE_SHIP_TO_ON_TOP = CGraphicsHelper.IsRandomTrue( 0.9603841536614646 ); // Будет ли информация о ship-to наверху
    private static Boolean USE_BILL_TO = CGraphicsHelper.IsRandomTrue( 0.535650623885918 ); // Будет ли информация о bill-to
    private static Boolean USE_BILL_TO_ON_TOP = CGraphicsHelper.IsRandomTrue( 0.82416011436628242 ); // Будет ли информация о bill-to наверху
    private static Boolean USE_SHIPPER = CGraphicsHelper.IsRandomTrue( 0.19073083778966132 ); // Будет ли информация о shipper
    private static Boolean USE_SHIPPER_ON_TOP = CGraphicsHelper.IsRandomTrue( 0.9116279069767441 ); // Будет ли информация о shipper наверху
    private static Boolean USE_REMIT_TO = CGraphicsHelper.IsRandomTrue( 0.375222816399287 ); // Будет ли информация о remit-to
    private static Boolean USE_REMIT_TO_ON_TOP = CGraphicsHelper.IsRandomTrue( 0.4067333939945401 ); // Будет ли информация о remit-to наверху
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

    public void Show() { System.out.print( GetData( false ) ); }

    @Override
    public String GetData(  Boolean withCoords ) {
        String res = DATES_DEF.concat( invoiceDates.GetData( withCoords ) ).
                concat( NUMBERS_DEF ).concat( invoiceNumbers.GetData( withCoords ) ).
                concat( PAYMENTS_INFO_DEF ).concat( paymentInfos.GetData( withCoords ) ).
                concat( COMPANIES_DEF );
        if( USE_BU ) {
            res = res.concat( bu.GetData( withCoords ) );
        }
        res = res.concat( vendor.GetData( withCoords ) );
        if( USE_SHIP_TO ) {
            res = res.concat( shipTo.GetData( withCoords ) );
        }
        if( USE_BILL_TO ) {
            res = res.concat( billTo.GetData( withCoords ) );
        }
        if( USE_SHIPPER ) {
            res = res.concat( shipper.GetData( withCoords ) );
        }
        if( USE_REMIT_TO ) {
            res = res.concat( remitTo.GetData( withCoords ) );
        }
        return res.concat( PRODUCT_CONTAINER_DEF ).concat( productContainer.GetData( withCoords ) );
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

    public void CreateAndSaveImage( String filePathStart, Boolean withChecking ) {
        BufferedImage bImg = new BufferedImage( CGraphicsHelper.A4_WIDTH, CGraphicsHelper.A4_HEIGHT, BufferedImage.TYPE_INT_RGB );
        Graphics2D g2d = bImg.createGraphics();

        draw( g2d ); // отрисовываем

        // Будем сохранять результат по текущей дате
        Long currentData = System.currentTimeMillis();

        try {
            // Картинка инвойса
            ImageIO.write( bImg, "PNG", new File( filePathStart.
                    concat( currentData.toString() ).concat( ".PNG" ) ) );
        } catch( IOException e ) {
            e.printStackTrace();
        }

        try {
            // Сохранение данных
            File file = new File( filePathStart.concat( currentData.toString() ).concat( ".txt" ) );
            file.createNewFile();
            FileWriter writer = new FileWriter( file );
            writer.write( GetData( true ) );
            writer.close();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        // Проверка, что координаты находятся правильно
        if( withChecking ) {
            DrawRects( g2d );
            try {
                // Картинка инвойса
                ImageIO.write( bImg, "PNG", new File( filePathStart.
                        concat( currentData.toString() ).concat( "_check.PNG" ) ) );
            } catch( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public static void main( String[] args ) {
        CInvoiceModel invoice = new CInvoiceModel();
        try {
            invoice.Generate(); // Генерируем данные
        } catch ( Exception e ) {
            e.printStackTrace();
            return;
        }
        invoice.Show(); // Вывод сгенерированных данных
        invoice.CreateAndSaveImage( args.length > 0 ? args[0] : "C:\\invoice-images-generate\\", false ); // Рисуем и созраняем изображение
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
        CRectangle vendorCoord = vendor.DrawInfo( g2d, x, y );

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

        if( CGraphicsHelper.IsRandomTrue( 0.35 ) ) {
            y = invoiceInfoCoord.GetBottom() + CGraphicsHelper.ELEMENTS_SPACING * 3;
            CRectangle barcodeCoord = drawBarcode( g2d, invoiceInfoCoord.GetLeft(), y );
            y = Math.max( barcodeCoord.GetBottom(), vendorCoord.GetBottom() ) +
                    CGraphicsHelper.BLOCKS_SPACING;
        } else {
            y = Math.max( invoiceInfoCoord.GetBottom(), vendorCoord.GetBottom() ) +
                    CGraphicsHelper.BLOCKS_SPACING * 2;
        }

        CRectangle companiesCoord = drawCompaniesInfo( g2d, y, true );

        x = CGraphicsHelper.LEFT_PADDING;
        if( companiesCoord.GetHeight() > 0 ) {
            y = companiesCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 3;
        }
        CRectangle tableCoord = productContainer.DrawTableInfo( g2d, y );

        y = tableCoord.GetBottom() + CGraphicsHelper.BLOCKS_SPACING * 2;
        companiesCoord = drawCompaniesInfo( g2d, y, false );

        Boolean isBankInfoOnLeft = CGraphicsHelper.IsRandomTrue( 0.6 ); // Покажем банковскую инфомацию слева/справа
        x = companiesCoord.GetWidth() == 0 && isBankInfoOnLeft ? CGraphicsHelper.LEFT_PADDING
                : Math.max( companiesCoord.GetRight() + 20, rightElementsStart );
        vendor.GetBank().DrawInfo( g2d, x, y );
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
            y += ( (BufferedImage) image ).getHeight();
        } catch( IOException e ) {
            System.out.println( "Not fined logo file!" );
        }

        g2d.setPaint( Color.BLACK );
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );

        return new CRectangle( left, y, width, y - top );
    }

    // Блок со штрих-кодом
    private CRectangle drawBarcode( Graphics2D g2d, int x, int y ) {
        int left = x;
        int width = 0;
        int top = y;

        try {
            File directory = new File( "barcodes/" );
            File barcodes[] = directory.listFiles();
            int i = (int)( Math.random() * barcodes.length );

            Image image = ImageIO.read( barcodes[i] );
            g2d.drawImage( image, x, y, ( (BufferedImage) image ).getWidth(), ( ( BufferedImage ) image ).getHeight(), null );
            width = ( (BufferedImage) image ).getWidth();
            y += ( (BufferedImage) image ).getHeight();
        } catch( IOException e ) {
            System.out.println( "Not fined barcode file!" );
        }

        return new CRectangle( left, y, width, y - top );
    }

    // Блок информации об инвойсе
    private CRectangle drawInvoiceInfo( Graphics2D g2d, int x, int y ) {
        int left = x;
        int width = 0;
        int top = y;
        FontMetrics fm = g2d.getFontMetrics();

        Boolean withPO = CGraphicsHelper.IsRandomTrue( 0.5 ); // Показывать ли PO number
        Boolean withClientNumber = CGraphicsHelper.IsRandomTrue( 0.3 ); // Рисовать ли Client number
        Boolean withDueDate = CGraphicsHelper.IsRandomTrue( 0.3342245989304813 );
        Boolean withDeliveryDate = CGraphicsHelper.IsRandomTrue( 0.33511586452762926 );
        Boolean withUsedPayment = CGraphicsHelper.IsRandomTrue( 0.3 );
        Boolean withTotal = CGraphicsHelper.IsRandomTrue( 0.25363692896900713 );
        Boolean withCurrency = CGraphicsHelper.IsRandomTrue(0.5);

        if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
            // Все записываем просто строчками, одно под другим
            CRectangle rect = CGraphicsHelper.DrawString( g2d, "Invoice Number: ", invoiceNumbers.GetInvoiceNumber(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );

            rect = CGraphicsHelper.DrawString( g2d, "Invoice Date: ", invoiceDates.GetInvoiceDate(), x, y );
            y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
            width = Math.max( width, rect.GetWidth() );

            if( withPO ) {
                rect = CGraphicsHelper.DrawString( g2d, "PO Number: ", invoiceNumbers.GetPurchaseOrderNumber(), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }
            if( withClientNumber ) {
                rect = CGraphicsHelper.DrawString( g2d, "Client Number: ", invoiceNumbers.GetClientNumber(), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }
            if( withDueDate ) {
                rect = CGraphicsHelper.DrawString( g2d, "Due Date: ", invoiceDates.GetPaymentDate(), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }
            if( withDeliveryDate ) {
                rect = CGraphicsHelper.DrawString( g2d, "Delivery Date: ", invoiceDates.GetExpeditionDate(), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }
            if( withUsedPayment ) {
                rect = CGraphicsHelper.DrawString( g2d, "Used payment: ", paymentInfos.GetUsedPyment(), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }
            if( withTotal ) {
                rect = CGraphicsHelper.DrawString( g2d, "Total: ", productContainer.GetTotal(), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }
            if( withCurrency ) {
                rect = CGraphicsHelper.DrawString( g2d, "Currency: ".concat( СCoordinatedBaseType.LOCALE.GetCurrency() ), x, y );
                y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
                width = Math.max( width, rect.GetWidth() );
            }

            if( CGraphicsHelper.IsRandomTrue( 0.5 ) ) {
                // Очертим границу вокруг блока
                g2d.setColor( Color.GRAY );
                int lBorder = left - VALUE_SHIFT;
                int rBorder = left + width + VALUE_SHIFT;
                int tBorder = top - fm.getHeight() - VALUE_SHIFT;
                int bBorder = rect.GetBottom() + VALUE_SHIFT;

                g2d.drawLine( lBorder, tBorder, rBorder, tBorder );
                g2d.drawLine( lBorder, tBorder, lBorder, bBorder );
                g2d.drawLine( rBorder, tBorder, rBorder, bBorder );
                g2d.drawLine( lBorder, bBorder, rBorder, bBorder );

                g2d.setColor( Color.BLACK );
            }
        } else {
            // Записываем в виде таблицы
            int[] xStarts = new int[] { left + VALUE_SHIFT,
                    left + 100 + VALUE_SHIFT * 2 };

            drawAsTableValue( g2d, "INVOICE", invoiceNumbers.GetInvoiceNumber(), xStarts[0], y );
            y = drawAsTableValue( g2d, "DATE", invoiceDates.GetInvoiceDate(), xStarts[1], y );

            Boolean hasFirst = false;
            int yEnd = y;
            if( withPO ) {
                yEnd = drawAsTableValue( g2d, "PO №", invoiceNumbers.GetPurchaseOrderNumber(), xStarts[0], y );
                hasFirst = true;
            }
            if( withClientNumber ) {
                yEnd = drawAsTableValue( g2d, "Client №", invoiceNumbers.GetClientNumber(), xStarts[hasFirst ? 1 : 0], y );
                if( hasFirst ) {
                    hasFirst = false;
                    y = yEnd;
                } else {
                    hasFirst = true;
                }
            }
            if( withDueDate ) {
                yEnd = drawAsTableValue( g2d, "Due Date", invoiceDates.GetPaymentDate(), xStarts[hasFirst ? 1 : 0], y );
                if( hasFirst ) {
                    hasFirst = false;
                    y = yEnd;
                } else {
                    hasFirst = true;
                }
            }
            if( withDeliveryDate ) {
                yEnd = drawAsTableValue( g2d, "Delivery Date", invoiceDates.GetExpeditionDate(), xStarts[hasFirst ? 1 : 0], y );
                if( hasFirst ) {
                    hasFirst = false;
                    y = yEnd;
                } else {
                    hasFirst = true;
                }
            }
            if( withUsedPayment ) {
                yEnd = drawAsTableValue( g2d, "Used payment", paymentInfos.GetUsedPyment(), xStarts[hasFirst ? 1 : 0], y );
                if( hasFirst ) {
                    hasFirst = false;
                    y = yEnd;
                } else {
                    hasFirst = true;
                }
            }
            if( withTotal ) {
                yEnd = drawAsTableValue( g2d, "Total", productContainer.GetTotal(), xStarts[hasFirst ? 1 : 0], y );
                if( hasFirst ) {
                    hasFirst = false;
                    y = yEnd;
                } else {
                    hasFirst = true;
                }
            }
            if( withCurrency ) {
                СCoordinatedString currency = new СCoordinatedString();
                currency.Set( СCoordinatedBaseType.LOCALE.GetCurrency() );
                yEnd = drawAsTableValue( g2d, "Currency: ", currency, xStarts[hasFirst ? 1 : 0], y );
            }

            y = yEnd;
        }

        return new CRectangle( left, y + fm.getDescent(), width, y - top );
    }

    // Отрисовать в рамке значение типа с заголовком header
    private int drawAsTableValue( Graphics2D g2d, String header, СCoordinatedBaseType value, int x, int y ) {
        int top = y - CGraphicsHelper.DefaultYShift( g2d );
        int left = x - VALUE_SHIFT;
        int right = x + 100;

        g2d.setFont( CGraphicsHelper.BOLD_DEFAULT_FONT );
        g2d.drawString( header, x, y );
        y += CGraphicsHelper.ELEMENTS_SPACING;
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );

        // Отчеркивание заголовка
        g2d.setColor( Color.GRAY );
        g2d.drawLine( left, y, right, y );
        y += CGraphicsHelper.DefaultYShift( g2d );
        g2d.setColor( Color.BLACK );

        // Данные
        CGraphicsHelper.DrawString( g2d, value, x, y );
        y += CGraphicsHelper.ELEMENTS_SPACING;

        // Границы таблицы
        g2d.setColor( Color.GRAY );
        g2d.drawLine( left, top, right, top );
        g2d.drawLine( left, top, left, y );
        g2d.drawLine( right, top, right, y );
        g2d.drawLine( left, y, right, y );
        g2d.setColor( Color.BLACK );

        FontMetrics fm = g2d.getFontMetrics();
        return y + CGraphicsHelper.ELEMENTS_SPACING * 3 + fm.getDescent();
    }

    // Блок с информацией о других компаниях
    private CRectangle drawCompaniesInfo( Graphics2D g2d, int y, Boolean onTop ) {
        int left = CGraphicsHelper.LEFT_PADDING;
        int right = CGraphicsHelper.A4_WIDTH - CGraphicsHelper.RIGHT_PADDING;
        int width = right - left;
        int top = y;

        Boolean withHorizontalSeparator = CGraphicsHelper.IsRandomTrue( 0.5 ); // Рисовать ли горизонтальный разделитель заголовка
        Boolean withBorder = CGraphicsHelper.IsRandomTrue( 0.5 ); // Границы таблицы

        // Заголовок
        List<CCompany> companies = new ArrayList<>();
        if( USE_BU && onTop == USE_BU_ON_TOP ) {
            companies.add( bu );
        }
        if( USE_SHIP_TO && onTop == USE_SHIP_TO_ON_TOP ) {
            companies.add( shipTo );
        }
        if( USE_BILL_TO && onTop == USE_BILL_TO_ON_TOP ) {
            companies.add( billTo );
        }
        if( USE_SHIPPER && onTop == USE_SHIPPER_ON_TOP ) {
            if( companies.size() == 3 && onTop ) {
                USE_SHIP_TO_ON_TOP = false;
            } else {
                companies.add( shipper );
            }
        }
        if( USE_REMIT_TO && onTop == USE_REMIT_TO_ON_TOP ) {
            if( companies.size() == 3 && onTop ) {
                USE_REMIT_TO_ON_TOP = false;
            } else {
                companies.add( remitTo );
            }
        }
        if( companies.size() == 0 ) {
            return new CRectangle( left, top, 0, 0 );
        }
        if( companies.size() > 1 ) {
            Collections.shuffle( companies );
        }

        int l = left;
        int b = y;

        for( int i = 0; i < companies.size(); i++ ) {
            CRectangle rect = drawCompanyInfo( g2d, companies.get( i ), l, y, withHorizontalSeparator, withBorder );
            width = rect.GetRight() - left;
            l = companies.size() == 2 && onTop && CGraphicsHelper.A4_WIDTH / 2 > rect.GetRight() + VALUE_SHIFT
                    ? CGraphicsHelper.A4_WIDTH / 2
                    : rect.GetRight() + CGraphicsHelper.BLOCKS_SPACING * 2;
            b = Math.max( b, rect.GetBottom() );
        }

        return new CRectangle( left, b, width, b - top );
    }

    private CRectangle drawCompanyInfo( Graphics2D g2d, CCompany company, int x, int y,
                                        Boolean withHorizontalSeparator, Boolean withBorder ) {
        int left = x;
        int top = y - CGraphicsHelper.DefaultYShift( g2d );

        g2d.setFont( CGraphicsHelper.BOLD_DEFAULT_FONT );
        g2d.drawString( company.GetRole(), x + VALUE_SHIFT, y );
        int captionEnd = y + CGraphicsHelper.ELEMENTS_SPACING;
        y = captionEnd + CGraphicsHelper.DefaultYShift( g2d );
        g2d.setFont( CGraphicsHelper.DEFAULT_FONT );

        CRectangle rect = company.DrawInfo( g2d, x + VALUE_SHIFT, y );
        int right = rect.GetRight() + VALUE_SHIFT;
        int bottom = rect.GetBottom() + VALUE_SHIFT;

        g2d.setColor( Color.GRAY );
        // Отчеркивание заголовка
        if( withHorizontalSeparator ) {
            g2d.drawLine( left, captionEnd, right, captionEnd );
        }

        // Границы таблицы
        if( withBorder ) {
            g2d.drawLine( left, top, right, top );
            g2d.drawLine( left, top, left, bottom );
            g2d.drawLine( right, top, right, bottom );
            g2d.drawLine( left, bottom, right, bottom );
        }
        g2d.setColor( Color.BLACK );

        return new CRectangle( left, bottom, right - left, bottom - top );
    }
}
