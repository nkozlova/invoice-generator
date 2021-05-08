import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;

public class CInvoiceDates implements IModel {

    private static final String INVOICE_DATA_DEF = "  InvoiceDate: ";
    private static final String COMMAND_DATA_DEF = "  CommandDate: ";
    private static final String EXPEDITION_DATA_DEF = "  ExpeditionDate: ";
    private static final String PAYMENT_DATA_DEF = "  PaymentDate: ";

    // TODO - посл-ть и проверка на корректность
    private СCoordinatedString invoiceDate = new СCoordinatedString();
    private СCoordinatedString commandDate = new СCoordinatedString(); // Order Date, дата заказа в таблице LineItems
    private СCoordinatedString expeditionDate = new СCoordinatedString();  // Delivery Date, дата доставки
    private СCoordinatedString paymentDate = new СCoordinatedString();  // Due Date, "Оплатить до..."

    public СCoordinatedString GetInvoiceDate() { return invoiceDate; }
    public СCoordinatedString GetCommandDate() { return commandDate; }
    public СCoordinatedString GetExpeditionDate() { return expeditionDate; }
    public СCoordinatedString GetPaymentDate() { return paymentDate; }

    @Override
    public void Generate() {
        Generex delimiterGenerex = new Generex( "[ \\-\\/a]" ); // a заменим на точку, если выпадет такой разделитель
        String delimiter = delimiterGenerex.random();
        if( delimiter.equals( "a" ) ) {
            delimiter = "\\.";
        }

        Generex dateGenerex = new Generex( "(0[1-9]|[12][0-9]|3[01])".concat( delimiter ).concat( "(0[1-9]|1[012])" ).
                concat( delimiter ).concat( "20(1|2)\\d" ) );
        invoiceDate.Set( dateGenerex.random() );
        commandDate.Set( dateGenerex.random() );
        expeditionDate.Set( dateGenerex.random() );
        paymentDate.Set( dateGenerex.random() );
    }

    @Override
    public String GetData() {
        return invoiceDate.Show( INVOICE_DATA_DEF ).
                concat( commandDate.Show( COMMAND_DATA_DEF ) ).
                concat( expeditionDate.Show( EXPEDITION_DATA_DEF ) ).
                concat( paymentDate.Show( PAYMENT_DATA_DEF ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        invoiceDate.DrawRects( g2d );
        commandDate.DrawRects( g2d );
        expeditionDate.DrawRects( g2d );
        paymentDate.DrawRects( g2d );
    }
}
