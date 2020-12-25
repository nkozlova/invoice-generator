import CoordinatedTypes.CRectangle;
import java.awt.*;

public interface IModel {
    // Генерируем данные элементов
    public void Generate();

    // Выводим готовые данные и их координаты
    public void Show();

    // озвращаем готовые данные и их координаты в текстовом виде
    public String GetData();

    // Показываем на изображении рамки элементов
    public void DrawRects( Graphics2D g2d );
}
