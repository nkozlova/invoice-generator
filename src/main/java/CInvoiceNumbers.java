import CoordinatedTypes.CRectangle;
import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;

public class CInvoiceNumbers implements IModel {

    private static final String INVOICE_NUMBER_DEF = "  InvoiceNumber: ";
    private static final String COMMAND_NUMBER_DEF = "  CommandNumber: ";
    private static final String CLIENT_NUMBER_DEF = "  ClientNumber: ";

    private СCoordinatedString invoiceNumber = new СCoordinatedString();
    private СCoordinatedString commandNumber = new СCoordinatedString();   // Order number, номер заказа
    private СCoordinatedString clientNumber = new СCoordinatedString();

    public СCoordinatedString GetInvoiceNumber() { return invoiceNumber; }
    public СCoordinatedString GetCommandNumber() { return commandNumber; }
    public СCoordinatedString GetClientNumber() { return clientNumber; }

    @Override
    public void Generate() {
        Generex numberGenerex = new Generex( "\\d+" ); // TODO бывают буквы а-ля '2017-9-WAL-008' - проверить форматы

        invoiceNumber.Set( numberGenerex.random( 6, 9 ) );
        commandNumber.Set( numberGenerex.random( 6, 9 ) );
        clientNumber.Set( numberGenerex.random( 6, 9 ) );
    }

    @Override
    public String GetData() {
        return invoiceNumber.Show( INVOICE_NUMBER_DEF ).
                concat( commandNumber.Show( COMMAND_NUMBER_DEF ) ).
                concat( clientNumber.Show( CLIENT_NUMBER_DEF ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        invoiceNumber.DrawRects( g2d );
        commandNumber.DrawRects( g2d );
        clientNumber.DrawRects( g2d );
    }
}
