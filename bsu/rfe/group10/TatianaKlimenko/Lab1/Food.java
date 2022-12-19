package Lab1;
import java.util.Scanner;

public abstract class Food implements Consumable, Nutritious {

    public String name = null;
    public int calories;

    public Food(String name){
        this.name = name;
        this.calories = 0;
    }

    public String toString(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setCalories(int calories){
        this.calories = calories;
    }

    public int getCalories(){
        return calories;
    }

    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Food)) return false; // Шаг 1
        if (name==null || ((Food)arg0).name==null) return false; // Шаг 2
        return name.equals(((Food)arg0).name); // Шаг 3
    }

    public static String choiceComponent(String[] component){
        Scanner in = new Scanner(System.in);
        System.out.println("We have:");
        for(int i = 0; i < component.length; i++){
            System.out.println("- " + component[i]);
        }
        System.out.println("\nEnter the name of what you want.");

        String choice;
        while(true){
            choice = in.nextLine().toLowerCase();
            for(int i=0; i < component.length; i++){
                if(choice.equals(component[i])){
                    return component[i];
                }
            }
            System.out.println("Wrong input, try again");
        }
    }

    public void consume(){
    }
}