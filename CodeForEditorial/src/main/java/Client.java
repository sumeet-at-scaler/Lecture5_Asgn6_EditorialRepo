import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int n = scn.nextInt();

        for(int i = 1; i <= n; i++){
            TableCreator tc = new TableCreator(i);
            ScalerThread t = new ScalerThread(tc);
            t.start();
        }

    }
}