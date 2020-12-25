package CoordinatedTypes;

// Тип для хранения типа оплаты
public class CCoordinatedUsedPayment extends СCoordinatedBaseType {

    private TUsedPayment value;

    @Override
    public String GetValue() {
        return value.toString();
    }

    public TUsedPayment Get() { return value; }
    public void Set( TUsedPayment v ) {
        value = v;
    }
}