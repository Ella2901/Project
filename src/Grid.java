import java.util.ArrayList;

public class Grid <T extends Entity> {
    private ArrayList<T> entities;

    public Grid() {
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

    public void displayEntities() {
        for (T entity : entities) {
            entity.display();
        }
    }
}
