import CoordinatedTypes.CCoordinatedDate;

import com.mifmif.common.regex.Generex;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class CInvoiceDates implements IModel {

    private static final String INVOICE_DATA_DEF = "  InvoiceDate: ";
    private static final String EXPEDITION_DATA_DEF = "  ExpeditionDate: ";
    private static final String PAYMENT_DATA_DEF = "  PaymentDate: ";

    // TODO - посл-ть и проверка на корректность
    private CCoordinatedDate invoiceDate = new CCoordinatedDate();
    private CCoordinatedDate expeditionDate = new CCoordinatedDate();  // Delivery Date, дата доставки
    private CCoordinatedDate paymentDate = new CCoordinatedDate();  // Due Date, "Оплатить до..."

    public CCoordinatedDate GetInvoiceDate() { return invoiceDate; }
    public CCoordinatedDate GetExpeditionDate() { return expeditionDate; }
    public CCoordinatedDate GetPaymentDate() { return paymentDate; }

    @Override
    public void Generate() {
        Random random = new Random();
        Calendar date = new GregorianCalendar( 2020 - random.nextInt( 10 ) + random.nextInt( 5 ),
                random.nextInt( 12 ) + 1, random.nextInt( 28 ) + 1 );
        invoiceDate.Set( date.getTime() );
        date.add( Calendar.DAY_OF_YEAR, random.nextInt( 30 ) );
        expeditionDate.Set( date.getTime() );
        date.add( Calendar.DAY_OF_YEAR, random.nextInt( 10 ) );
        paymentDate.Set( date.getTime() );
    }

    @Override
    public String GetData( Boolean withCoords ) {
        return invoiceDate.Show( INVOICE_DATA_DEF, withCoords ).
                concat( expeditionDate.Show( EXPEDITION_DATA_DEF, withCoords ) ).
                concat( paymentDate.Show( PAYMENT_DATA_DEF, withCoords ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        invoiceDate.DrawRects( g2d );
        expeditionDate.DrawRects( g2d );
        paymentDate.DrawRects( g2d );
    }
}
