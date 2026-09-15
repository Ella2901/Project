public class Warehouse {
    private Grid<Entity> grid;

    public Warehouse() {
        grid = new Grid<>(800,600);
    }

    public void addEntity(Entity entity) {
        grid.addEntity(entity);
    }

    public void displayEntities() {
        grid.displayEntities();
    }

    public Grid<Entity> getGrid() {
        return grid;
    }
}
