import CoordinatedTypes.CRectangle;
import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;

public class CBank implements IModel {

    private static final String NAME_DEF = "    Name: ";
    private static final String IBAN_DEF = "    Iban: ";
    private static final String SWIFT_DEF = "    Swift: ";
    private static final String SORT_CODE_DEF = "    Sort Code: ";
    private static final String BANK_ACCOUNT_DEF = "    Bank Account: ";
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
    public String GetData() {
        return name.Show( NAME_DEF ).
                concat( iban.Show( IBAN_DEF ) ).
                concat( swift.Show( SWIFT_DEF ) ).
                concat( sortCode.Show( SORT_CODE_DEF ) ).
                concat( bankAccount.Show( BANK_ACCOUNT_DEF ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        name.DrawRects( g2d );
        iban.DrawRects( g2d );
        swift.DrawRects( g2d );
        sortCode.DrawRects( g2d );
        bankAccount.DrawRects( g2d );
    }

    // Отрисовка банковской информации
    public CRectangle DrawInfo(Graphics2D g2d, int x, int y ) {
        int left = x;
        int width = 0;
        int top = y;

        CRectangle rect = CGraphicsHelper.DrawString( g2d, "Bank: ", GetName(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        width = Math.max( width, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Account No: ", GetBankAccount(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        width = Math.max( width, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "Sort Code: ", GetSortCode(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        width = Math.max( width, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "SWIFT: ", GetSwift(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        width = Math.max( width, rect.GetWidth() );

        rect = CGraphicsHelper.DrawString( g2d, "IBAN No: ", GetIban(), x, y );
        y = rect.GetBottom() + CGraphicsHelper.DefaultYShift( g2d );
        width = Math.max( width, rect.GetWidth() );

        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( left, y + fm.getDescent(), width, y - top );
    }
}
