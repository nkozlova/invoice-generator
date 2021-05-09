package CoordinatedTypes;

// Тип для хранения типа оплаты
public class CCoordinatedUsedPayment extends СCoordinatedBaseType {

    private static final String EMPTY_STR = "";

    private TUsedPayment value;

    @Override
    public String GetValue() {
        switch( value ) {
            case UP_Cash:
                return "Cash";
            case UP_Ewallet:
                return "Ewallet";
            case UP_CreditCart:
                return "Credit Cart";
        }
        assert( false );
        return EMPTY_STR;
    }

    public TUsedPayment Get() { return value; }
    public void Set( TUsedPayment v ) { value = v; }
}