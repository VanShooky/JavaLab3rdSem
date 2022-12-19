package Lab1;

public class Coctail extends Food {
    public String drink;
    public static String[] drinks = {"lemonade", "juice", "milk"};
    public static int[] drinks_calories = {140, 180, 60};
    public String fruit;
    public static String[] fruits = {"orange", "apple", "grape"};
    public static int[] fruits_calories = {47, 52, 67};

    public Coctail(){
        super(MainApplication.coctail);
        this.drink = drink();
        this.fruit = fruit();
        this.calories = calories();
    }

    public String drink(){
        System.out.println("\nChoose a drink.");
        return Food.choiceComponent(drinks);
    }

    public String fruit(){
        System.out.println("\nChoose a fruit.");
        return Food.choiceComponent(fruits);
    }

    public int calories(){
        calories += drink_calories();
        calories += fruit_calories();
        return calories;
    }

    public int drink_calories(){
        for(int i = 0; i < drinks.length; i++){
            if(this.drink.equals(drinks[i])){
                return drinks_calories[i];
            }
        }
        return 0;
    }

    public int fruit_calories(){
        for(int i = 0; i < fruits.length; i++){
            if(this.fruit.equals(fruits[i])){
                return fruits_calories[i];
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
        if (!(arg0 instanceof Coctail)) return false;
        return drink.equals(((Coctail)arg0).drink) && fruit.equals(((Coctail)arg0).fruit);
        } else
        return false;
    }
}
