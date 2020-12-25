package CoordinatedTypes;

// Тип для хранения процентов
public class CCoordinatedProcent extends СCoordinatedBaseType {

    private static final String MASK = "%.2f%%";

    private Double value;

    @Override
    public String GetValue() {
        // Пишем число с двумя знаками после запятой и процент
        return String.format( MASK, value );
    }

    public Double Get() { return value; }
    public void Set( Double v ) {
        value = v;
    }
}
