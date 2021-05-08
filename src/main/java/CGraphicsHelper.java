import CoordinatedTypes.СCoordinatedBaseType;
import CoordinatedTypes.CRectangle;

import java.awt.*;
import java.awt.font.FontRenderContext;

// Класс, реализующий отрисовку элементов
public class CGraphicsHelper {

    // Данные для отрисовки
    public static final int A4_WIDTH = 830; // Ширина листа в пикселях
    public static final int A4_HEIGHT = 1170; // Высота
    public static final int LEFT_PADDING = 60;
    public static final int TOP_PADDING = 50;
    public static final int RIGHT_PADDING = 60;
    public static final int ELEMENTS_SPACING = 5;
    public static final int BLOCKS_SPACING = 25;

    // Получилось ли сгенерировать число, не большее данной вероятности
    public static boolean IsRandomTrue( double probability ) {
        assert( probability >= 0. && probability <= 1. );
        return Math.random() <= probability;
    }

    // рисуем строку, ее координаты нас не интересуют, возвращаем координаты занятого места
    public static CRectangle DrawString( Graphics2D g2d, String str, int x, int y ) {
        g2d.drawString( str, x, y );

        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle bounds = g2d.getFont().getStringBounds( str, frc ).getBounds();
        FontMetrics fm = g2d.getFontMetrics();
        return new CRectangle( x, y + fm.getDescent(), bounds.width, bounds.height );
    }

    // рисуем строку и сохраняем ее координаты
    public static CRectangle DrawString( Graphics2D g2d, СCoordinatedBaseType str, int x, int y ) {
        CRectangle rect = DrawString( g2d, str.GetValue(), x, y );
        str.SetRect( rect );
        return rect;
    }

    // рисуем строку с начальным дополнительным значением (координаты которых нас не интересуют) и сохраняем ее координаты
    public static CRectangle DrawString( Graphics2D g2d, String defStr, СCoordinatedBaseType str, int x, int y ) {
        // Рисуем строку
        g2d.drawString( defStr.concat( str.GetValue() ), x, y );

        int left = x + ( !defStr.isEmpty() ? CStringsHelper.GetStringWidth( g2d, defStr ) : 0 ) - 1;

        // Сохраняем координаты
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle bounds = g2d.getFont().getStringBounds( str.GetValue(), frc ).getBounds();
        FontMetrics fm = g2d.getFontMetrics();
        CRectangle rectangle = new CRectangle( left, y + fm.getDescent(), bounds.width, bounds.height );
        str.SetRect( rectangle );
        return new CRectangle( x, rectangle.GetBottom(), left - x + rectangle.GetWidth(), rectangle.GetHeight() );
    }

    // отрисовка двух значений, координаты которых хотимм сохранить, со стандартным разделителем между ними
    public static CRectangle DrawStrings( Graphics2D g2d, СCoordinatedBaseType str1, СCoordinatedBaseType str2, int x, int y  ) {
        CRectangle rectangle1 = DrawString( g2d, str1, x, y );
        x += rectangle1.GetWidth();
        CRectangle rectangle2 = DrawString( g2d, CStringsHelper.COMMA_DELIMITER, str2, x, y );
        return new CRectangle( rectangle1.GetLeft(), rectangle1.GetBottom(), rectangle1.GetWidth() + rectangle2.GetWidth(),
                rectangle1.GetHeight() );
    }

    // Возвращается нижнюю координату текста, помещенного в рамки left/top
    public static CRectangle DrawMultilineText( Graphics2D g2d, СCoordinatedBaseType str, int left, int right, int top ) {
        int y = top;
        FontMetrics fm = g2d.getFontMetrics();

        int w = 0;
        int t = y - fm.getHeight() + fm.getDescent();

        java.util.List<String> strs = CStringsHelper.SplitText( g2d, str.GetValue(), right - left );
        for( int i = 0; i < strs.size(); i++ ) {
            CRectangle rect = DrawString( g2d, strs.get( i ), left, y );
            w = Math.max( w, rect.GetWidth() );
            if( i < strs.size() - 1 ) {
                y += DefaultYShift( g2d );
            }
        }

        int b = y + fm.getDescent();
        CRectangle rectangle = new CRectangle( left - 1, b, w, b - t );
        str.SetRect( rectangle );

        return rectangle;
    }

    public static int DefaultYShift( Graphics2D g2d ) {
        return g2d.getFont().getSize() + ELEMENTS_SPACING;
    }
}
