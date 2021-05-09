import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;

public class CInvoiceNumbers implements IModel {

    private static final String INVOICE_NUMBER_DEF = "  Invoice Number: ";
    private static final String PURCHASE_ORDER_NUMBER_DEF = "  Pupchase Order Number: ";
    private static final String CLIENT_NUMBER_DEF = "  Client Number: ";

    private СCoordinatedString invoiceNumber = new СCoordinatedString();
    private СCoordinatedString purchaseOrderNumber = new СCoordinatedString(); //  номер заказа
    private СCoordinatedString clientNumber = new СCoordinatedString();

    public СCoordinatedString GetInvoiceNumber() { return invoiceNumber; }
    public СCoordinatedString GetPurchaseOrderNumber() { return purchaseOrderNumber; }
    public СCoordinatedString GetClientNumber() { return clientNumber; }

    @Override
    public void Generate() {
        Generex numberGenerex = new Generex( "\\d+" ); // TODO бывают буквы а-ля '2017-9-WAL-008' - проверить форматы

        invoiceNumber.Set( numberGenerex.random( 6, 9 ) );
        purchaseOrderNumber.Set( numberGenerex.random( 6, 9 ) );
        clientNumber.Set( numberGenerex.random( 6, 9 ) );
    }

    @Override
    public String GetData() {
        return invoiceNumber.Show( INVOICE_NUMBER_DEF ).
                concat( purchaseOrderNumber.Show( PURCHASE_ORDER_NUMBER_DEF ) ).
                concat( clientNumber.Show( CLIENT_NUMBER_DEF ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        invoiceNumber.DrawRects( g2d );
        purchaseOrderNumber.DrawRects( g2d );
        clientNumber.DrawRects( g2d );
    }
}
