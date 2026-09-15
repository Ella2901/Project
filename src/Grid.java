import java.util.ArrayList;

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

    public void displayEntities() {
        for (T entity : entities) {
            entity.display();
        }
    }
}
