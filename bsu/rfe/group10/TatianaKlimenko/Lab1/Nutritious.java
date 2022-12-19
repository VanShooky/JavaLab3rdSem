package Lab1;

public interface Nutritious {

    public static int calculateCalories(Food[] breakfast){
        int sum = 0;
        for(int i = 0; breakfast[i] != null; i++){
            sum+=breakfast[i].calories;
        }
        System.out.println("Calories: " + sum);

        return sum;
    }
}
