import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;
import java.awt.*;


public class CIdNumbers implements IModel {

    private static final String CID_DEF = "    CID: ";
    private static final String VAT_DEF = "    VAT: ";

    private СCoordinatedString cid = new СCoordinatedString(); // company id, use as Customer Number in InvoiceData
    private СCoordinatedString vat = new СCoordinatedString(); // vat id, номер НДС (фикс. налоговой) https://en.wikipedia.org/wiki/VAT_identification_number

    public СCoordinatedString GetCID() { return cid; }
    public СCoordinatedString GetVAT() { return vat; }

    public void SetCID( String c ) { cid.Set( c ); }

    @Override
    public void Generate() {
        Generex generex = new Generex( "\\d+" );
        if( cid.Get() == null ) {
            cid.Set( generex.random( 6, 8 ) );
        }

        vat.Set( "CA".concat( generex.random( 9, 9 ) ) );  // VATID for Canada
    }

    @Override
    public String GetData( Boolean withCoords ) {
        return cid.Show( CID_DEF, withCoords ).
                concat( vat.Show( VAT_DEF, withCoords ) );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        cid.DrawRects( g2d );
        vat.DrawRects( g2d );
    }
}
