package restaurant;

/**
 * Simple recipe with name and preparation time only.
 * We just boil some water.
 * @author hom
 * @author ode
 */
public class Recipe {
    /** Name */
    private final String name;
    /** Time to "cook" it */
    private final int preparationTime;

    /**
     * A recipe with name and cook time.
     * @param name
     * @param preparationTime
     */
    public Recipe(String name, int preparationTime) {
        super();
        this.name = name;
        this.preparationTime = preparationTime;
    }
    /**
     * Get name.
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Get time it takes to prepare this.
     * @return
     */
    public int getPreparationTime() {
        return preparationTime;
    }
}
