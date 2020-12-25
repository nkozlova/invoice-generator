package CoordinatedTypes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Базовый тип для хранения значения и его координат в готовом инвойсе
public abstract class СCoordinatedBaseType {

    private static final String DELIMITER = ", ";
    private static final String COORDINATE_DELIMITER = " -- ";
    private static final String NEXT_STR = "\n";
    private static final String EMPTY_STR = "";

    private List<CRectangle> coordinates = new ArrayList<>(); // Может быть несколько появлений на странице, вынесли в отдельный класс

    // Установить координаты
    public void SetRect( CRectangle rectangle ) {
        coordinates.add( rectangle );
    }

    // Получить текстовое представление хранимого значения, переопределяется в наследнике
    public abstract String GetValue();

    // Вывод значения и его координат на изображении инвойса
    public String Show() {
        if( coordinates.size() == 0 ){
            return EMPTY_STR;
        }
        // Вывод в формате: <text> -- [<l>, <t>, <r>, <b>], [<l>, <t>, <r>, <b>]
        return GetValue().concat( COORDINATE_DELIMITER ).concat( showRects() ).concat( NEXT_STR );
    }

    // Отобразить прямоугольник координат значения
    public void DrawRects( Graphics2D g2d ) {
        for( int i = 0; i < coordinates.size(); i++ ){
            coordinates.get( i ).DrawRect( g2d );
        }
    }

    // Вывести координаты
    private String showRects() {
        String str = EMPTY_STR;
        for( int i = 0; i < coordinates.size(); i++ ){
            if( i > 0 ) {
                str = str.concat( DELIMITER );
            }
            str = str.concat( coordinates.get( i ).ShowRect() );
        }
        return str;
    }
}
