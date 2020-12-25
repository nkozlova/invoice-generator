import CoordinatedTypes.CRectangle;
import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;

public class CInvoiceNumbers implements IModel {

    private static final String INVOICE_NUMBER_DEF = "\tInvoiceNumber: ";
    private static final String COMMAND_NUMBER_DEF = "\tCommandNumber: ";
    private static final String CLIENT_NUMBER_DEF = "\tClientNumber: ";

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
    public void Show() {
        System.out.print( INVOICE_NUMBER_DEF.concat( invoiceNumber.Show() ) );
        System.out.print( COMMAND_NUMBER_DEF.concat( commandNumber.Show() ) );
        System.out.print( CLIENT_NUMBER_DEF.concat( clientNumber.Show() ) );
    }

    @Override
    public String GetData() {
        return INVOICE_NUMBER_DEF.concat( invoiceNumber.Show() ).
                concat( COMMAND_NUMBER_DEF ).concat( commandNumber.Show() ).
                concat( CLIENT_NUMBER_DEF ).concat( clientNumber.Show() );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        invoiceNumber.DrawRects( g2d );
        commandNumber.DrawRects( g2d );
        clientNumber.DrawRects( g2d );
    }
}
