import java.awt.*;

public interface IModel {
    // Генерируем данные элементов
    void Generate();

    // Возвращаем готовые данные и их координаты в текстовом виде
    String GetData();

    // Показываем на изображении рамки элементов
    void DrawRects( Graphics2D g2d );
}
