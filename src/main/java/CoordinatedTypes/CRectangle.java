package CoordinatedTypes;

import java.awt.*;

// Координаты расположения объекта на странице
public class CRectangle {

    private static final String LEFT_BRACKET = "[";
    private static final String DELIMITER = ", ";
    private static final String RIGHT_BRACKET = "]";

    private Integer left;
    private Integer top;
    private Integer right;
    private Integer bottom;

    public Integer GetLeft() { return left; }
    public Integer GetTop() { return top; }
    public Integer GetRight() { return right; }
    public Integer GetBottom() { return bottom; }
    public Integer GetWidth() { return right - left; }
    public Integer GetHeight() { return bottom - top; }

    public CRectangle(int l, int b, int w, int h ) {
        assert( l >= 0 && b >= h && w >= 0 && h >= 0 );
        left = l;
        bottom = b;
        right = l + w;
        top = b - h;
    }

    public void DrawRect( Graphics2D g2d ) {
        g2d.drawRect( left, top, GetWidth(), GetHeight() );
    }

    public String ShowRect() {
        // Вывод в формате [<l>, <t>, <r>, <b>]
        return LEFT_BRACKET.concat( left.toString() ).concat( DELIMITER ).concat( top.toString() ).concat( DELIMITER ).
                concat( right.toString() ).concat( DELIMITER ).concat( bottom.toString() ).concat( RIGHT_BRACKET );
    }
}
