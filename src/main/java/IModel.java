import java.awt.*;

public interface IModel {
    // Генерируем данные элементов
    void Generate();

    // Возвращаем готовые данные и их координаты (опционально) в текстовом виде
    String GetData( Boolean withCoords );

    // Показываем на изображении рамки элементов
    void DrawRects( Graphics2D g2d );
}
