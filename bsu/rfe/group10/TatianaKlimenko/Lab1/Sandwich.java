package Lab1;

public class Sandwich extends Food{
    static final String[] fillings1 = {"fish","ham","chicken"};
    static final String[] fillings2 = {"cheese","cucumber","sauce"};
    static final int[] fillings1_calories = {38,64,68};
    static final int[] fillings2_calories = {42,28,72};
    public String filling1;
    public String filling2;
    
    static final int breadCalories = 104;
    public Sandwich(){
       super (MainApplication.sandwich);
       this.filling1 = filling(fillings1);
       this.filling2 = filling(fillings2);
       this.calories = calories();
    }
    
    public static String filling(String[] fillings){
        System.out.println("\nChoose a filling.");
        return Food.choiceComponent(fillings);
    }

    public int calories(){
        calories += breadCalories;
        calories += filling1_calorie();
        calories += filling2_calorie();
        return calories;
    }

    public int filling1_calorie(){
        for(int i = 0; i < fillings1.length; i++){
            if(this.filling1.equals(fillings1[i])){
                return fillings1_calories[i];
            }
        }
        return 0;
    }

    public int filling2_calorie(){
        for(int i = 0; i < fillings2.length; i++){
            if(this.filling2.equals(fillings2[i])){
                return fillings2_calories[i];
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
        if (!(arg0 instanceof Sandwich)) return false;
        return filling1.equals(((Sandwich)arg0).filling1) && filling2.equals(((Sandwich)arg0).filling2);
        } else
        return false;
    }
        
}
