import CoordinatedTypes.CRectangle;
import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;

public class CBank implements IModel {

    private static final String NAME_DEF = "\t\tName: ";
    private static final String IBAN_DEF = "\t\tIban: ";
    private static final String SWIFT_DEF = "\t\tSwift: ";
    private static final String SORT_CODE_DEF = "\t\tSortCode: ";
    private static final String BANK_ACCOUNT_DEF = "\t\tBankAccount: ";
    private static final String INT_PATTERN = "\\d+";
    private static final String STR_PATTERN = "[A-Z]+";

    private СCoordinatedString name = new СCoordinatedString();
    private СCoordinatedString iban = new СCoordinatedString();    // международный номер банковского счета
    private СCoordinatedString swift = new СCoordinatedString();
    private СCoordinatedString sortCode = new СCoordinatedString();
    private СCoordinatedString bankAccount = new СCoordinatedString();

    public СCoordinatedString GetName() { return name; }
    public СCoordinatedString GetIban() { return iban; }
    public СCoordinatedString GetSwift() { return swift; }
    public СCoordinatedString GetSortCode() { return sortCode; }
    public СCoordinatedString GetBankAccount() { return bankAccount; }

    @Override
    public void Generate() {
        name.Set( "National Bank" ); // TODO

        Generex generex = new Generex( INT_PATTERN );
        Generex lineGenerex = new Generex( STR_PATTERN );
        // Великобританский
        bankAccount.Set( generex.random( 9, 9 ) );
        sortCode.Set( generex.random( 6, 6 ) );
        swift.Set( lineGenerex.random( 7, 7 ).concat( generex.random( 1, 1 ) ) );
        iban.Set( "GB".concat( generex.random( 2, 2 ).
                concat( lineGenerex.random( 4, 4 ) ).
                concat( sortCode.Get() ).
                concat( bankAccount.Get() ) ) );
    }

    @Override
    public void Show() {
        System.out.print( NAME_DEF.concat( name.Show() ) );
        System.out.print( IBAN_DEF.concat( iban.Show() ) );
        System.out.print( SWIFT_DEF.concat( swift.Show() ) );
        System.out.print( SORT_CODE_DEF.concat( sortCode.Show() ) );
        System.out.print( BANK_ACCOUNT_DEF.concat( bankAccount.Show() ) );
    }

    @Override
    public String GetData() {
        return NAME_DEF.concat( name.Show() ).
                concat( IBAN_DEF ).concat( iban.Show() ).
                concat( SWIFT_DEF ).concat( swift.Show() ).
                concat( SORT_CODE_DEF ).concat( sortCode.Show() ).
                concat( BANK_ACCOUNT_DEF ).concat( bankAccount.Show() );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        name.DrawRects( g2d );
        iban.DrawRects( g2d );
        swift.DrawRects( g2d );
        sortCode.DrawRects( g2d );
        bankAccount.DrawRects( g2d );
    }
}
