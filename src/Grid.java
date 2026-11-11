import java.util.ArrayList;
import java.util.stream.Stream;

public class Grid <T extends Entity> {
    private ArrayList<T> entities;
    private int width;
    private int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        entities = new ArrayList<>();
    }

    public void addEntity(T entity) {
        entities.add(entity);
    }

    public T getEntity(int index) {
        return entities.get(index);
    }

    public int size() {
        return entities.size();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void checkPosition(int x, int y) {
        if (!isValidPosition(x, y)) {
            throw new InvalidPositionException(
                "Position: (" + x + ", " + y + ") is outside the grid");
        }
    }

    public void displayEntities() {
        entities.stream().forEach(e -> e.display());
    }

    public Stream<T> stream() {
        return entities.stream();
    }
}
