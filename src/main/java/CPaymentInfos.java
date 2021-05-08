import CoordinatedTypes.CCoordinatedUsedPayment;
import CoordinatedTypes.TUsedPayment;

import java.awt.*;
import java.util.Random;

public class CPaymentInfos implements IModel {

    private static final String USED_PAYMENT_DEF = "  UsedPayment: ";

    private CCoordinatedUsedPayment usedPayment = new CCoordinatedUsedPayment();

    public CCoordinatedUsedPayment GetUsedPyment() { return usedPayment; }

    @Override
    public void Generate() {
        Random random = new Random();
        switch( random.nextInt( TUsedPayment.values().length ) ) {
            case 0:
                usedPayment.Set( TUsedPayment.Ewallet );
                break;
            case 1:
                usedPayment.Set( TUsedPayment.Cash );
                break;
            default:
                usedPayment.Set( TUsedPayment.Credit_cart );
                break;
        }
    }

    @Override
    public String GetData() { return usedPayment.Show( USED_PAYMENT_DEF ); }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        usedPayment.DrawRects( g2d );
    }
}
