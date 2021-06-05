package CoordinatedTypes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Базовый тип для хранения значения и его координат в готовом инвойсе
public abstract class СCoordinatedBaseType {

    public static final CUsedLocale LOCALE = new CUsedLocale(); // Локаль, общая на весь документ
    private static final String VALUE_KEY = "{value: '";
    private static final String COORDINATE_KEY = "', coords: [";
    private static final String END_LINE = "]}\n";
    private static final String COORDINATE_DELIMITER = ", ";
    private static final String EMPTY_STR = "";

    private List<CRectangle> coordinates = new ArrayList<>(); // Может быть несколько появлений на странице, вынесли в отдельный класс

    // Установить координаты
    public void SetRect( CRectangle rectangle ) {
        coordinates.add( rectangle );
    }

    // Получить текстовое представление хранимого значения, переопределяется в наследнике
    public abstract String GetValue();

    // Вывод значения и его координат на изображении инвойса
    public String Show( String key ) {
        if( coordinates.size() == 0 ){
            return EMPTY_STR;
        }
        // Вывод в формате: <key>: {value: '<value>', coords: [(<l>, <t>, <r>, <b>), (<l>, <t>, <r>, <b>)]}
        return key.concat( VALUE_KEY ).concat( GetValue() ).concat( COORDINATE_KEY ).concat( showRects() ).concat( END_LINE );
    }

    // Отображение прямоугольников координат
    public void DrawRects( Graphics2D g2d ) {
        for( int i = 0; i < coordinates.size(); i++ ){
            coordinates.get( i ).DrawRect( g2d );
        }
    }

    // Вывод координат
    private String showRects() {
        String str = EMPTY_STR;
        for( int i = 0; i < coordinates.size(); i++ ){
            if( i > 0 ) {
                str = str.concat( COORDINATE_DELIMITER );
            }
            str = str.concat( coordinates.get( i ).ShowRect() );
        }
        return str;
    }
}
