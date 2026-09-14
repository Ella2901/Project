import java.util.ArrayList;

public class Warehouse {
    private ArrayList<Entity> entities;

    public Warehouse() {
        entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
}
