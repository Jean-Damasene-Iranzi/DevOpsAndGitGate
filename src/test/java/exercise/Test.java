package exercise;

public class Test {

    public static void main(String[] args) {
        int count = 0;
        int sum = 0;

        for (int i = 1; i <= 10; i++) {
            sum += i;
            count++;
            System.out.println("Total count: " + count + "numbers");
            System.out.println("Next number: " + i + "of count");
            System.out.println("Sum: " + sum);
        }
        System.out.println("Final Total count: " + count);
    }
}