package Lab1;

public class Dessert extends Food {
    String component1;
    String component2;
    public static String[] components1 = {"cake","cupcake","pie"};
    public static int[] components1_calories = {320, 180, 220};
    public static String[] components2 = {"caramel","chocolate","almonds"};
    public static int[] components2_calories = {67, 102, 132};

    public Dessert(){
        super(MainApplication.dessert);
        this.component1 = component(components1);
        this.component2 = component(components2);
        this.calories = calories();
    }

    public String component(String[] components){
        System.out.println("\nChoose a component.");
        return Food.choiceComponent(components);

    }

    public int calories(){
        calories += component1_calories();
        calories += component2_calories();
        return calories;
    }

    public int component1_calories(){
        for(int i = 0; i < components1.length; i++){
            if(this.component1.equals(components1[i])){
                return components1_calories[i];
            }
        }
        return 0;
    }

    public int component2_calories(){
        for(int i = 0; i < components2.length; i++){
            if(this.component2.equals(components2[i])){
                return components2_calories[i];
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
        if (!(arg0 instanceof Dessert)) return false;
        return component1.equals(((Dessert)arg0).component1) && component2.equals(((Dessert)arg0).component2);
        } else
        return false;
    }
    
}
