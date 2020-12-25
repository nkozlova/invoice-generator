package CoordinatedTypes;

// Тип для хранения целых чисел
public class CCoordinatedInteger extends СCoordinatedBaseType {

    private Integer value;

    @Override
    public String GetValue() {
        return value.toString();
    }

    public Integer Get() { return value; }
    public void Set( Integer v ) {
        value = v;
    }
}
