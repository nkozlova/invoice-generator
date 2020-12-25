import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;

// Класс для хранения работы со строками в одном месте
public class CStringsHelper {

    public static final String EMPTY_STR = "";
    public static final String SPACE_STR = " ";
    public static final String COMMA_DELIMITER = ", ";

    // Ширина строки выбранным шрифтом
    public static int GetStringWidth( Graphics2D g2d, String str ) {
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle bounds = g2d.getFont().getStringBounds( str, frc ).getBounds();
        return bounds.width;
    }

    // Разобивка строки по пробелам, чтобы длина текста заданным шрифтом не превышала заданной длины
    public static java.util.List<String> SplitText( Graphics2D g2d, String text, int capacityWidth ) {
        List<String> res = new ArrayList<>();
        if( GetStringWidth( g2d, text ) <= capacityWidth ) {
            res.add( text ); // Длины хватает, просто выводим в одну строку
            return res;
        }

        String[] strs = text.split( SPACE_STR ); // Разделили текст по пробелам
        String currentStr = EMPTY_STR;
        for( String str : strs ) {
            if( GetStringWidth( g2d, currentStr.concat( SPACE_STR ).concat( str ) ) <= capacityWidth ){
                currentStr = currentStr.isEmpty() ? str : currentStr.concat( SPACE_STR ).concat( str );
            } else {
                res.add( currentStr );
                currentStr = str;
            }
        }
        if( !currentStr.isEmpty() ) {
            res.add( currentStr );
        }

        return res;
    }

    // Приводим первые буквы названий к большому размеру
    public static String ToUpperCaseNames( String str ) {
        String[] splits = str.split( SPACE_STR );
        String res = EMPTY_STR;
        for( String part: splits ) {
            if( part.isEmpty() ) {
                continue;
            } else if( !Character.isLetter( part.charAt( 0 ) ) ) {
                res = res.concat( SPACE_STR ).concat( part );
            } else if( part.length() == 1 ) {
                res = res.concat( SPACE_STR ).concat( part.toUpperCase() );
            } else {
                res = res.concat( SPACE_STR ).concat( part.substring( 0, 1 ).toUpperCase() ).concat( part.substring( 1 ) );
            }
        }
        return res.trim();
    }

    // Оставляем только первые буквы в словах строки
    public static String GetStartedOfStr( String str, Boolean asUpperCase ) {
        String[] splits = str.split(SPACE_STR);
        String res = EMPTY_STR;
        for( String part : splits ) {
            if( part.isEmpty() ) {
                continue;
            } else if( !Character.isLetter( part.charAt(0) ) ) {
                res = res.concat(part);
            } else {
                Character addedChar = asUpperCase ? Character.toUpperCase( part.charAt( 0 ) ) : part.charAt( 0 );
                res = res.concat( Character.toString( addedChar ) );
            }
        }
        return res;
    }
}
