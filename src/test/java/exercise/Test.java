package exercise;

public class Test {

    public static void main(String[] args) {
        int count = 0;
        int sum = 0;

        for (int i = 1; i <= 10; i++) {
            sum += i;
            count++;
            System.out.println("total count: " + count + "numbers");
            System.out.println("next number: " + i + "of count");
            System.out.println("Sum: " + sum);
        }
        System.out.println("final total count: " + count);
    }
}