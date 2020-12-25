package CoordinatedTypes;

// Тип для хранения строкового значения
public class СCoordinatedString extends СCoordinatedBaseType {

    private String value;

    @Override
    public String GetValue() {
        return value;
    }

    public String Get() { return value; }
    public void Set( String v ) {
        value = v;
    }
}
