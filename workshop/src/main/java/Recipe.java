class Recipe {

    private final String name;
    private final int preparationTime;

    Recipe(String name, int preparationTime) {
        this.name = name;
        this.preparationTime = preparationTime;
    }

    String getName() {
        return name;
    }

    int getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        return String.format("%s (%d seconds)", name, preparationTime);
    }
}
