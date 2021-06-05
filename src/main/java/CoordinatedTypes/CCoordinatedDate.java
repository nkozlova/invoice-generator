package CoordinatedTypes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Тип для хранения даты
public class CCoordinatedDate extends СCoordinatedBaseType {

    private static final SimpleDateFormat MASK = new SimpleDateFormat( LOCALE.GetDateFormat() );

    private Date value;

    @Override
    public String GetValue() { return MASK.format( value ); }

    public Date Get() { return value; }
    public void Set( Date v ) { value = v; }
}
