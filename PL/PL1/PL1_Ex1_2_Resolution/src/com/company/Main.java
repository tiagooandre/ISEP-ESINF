package com.company;

import java.time.chrono.MinguoDate;

public class Main {

    public static String sameOrder(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return str.charAt(0) + sameOrder(str.substring(1));
    }

    public static String reverseOrder(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return reverseOrder(str.substring(1)) + str.charAt(0);
        //SUBSTRING(x) - x = int startIndex - Neste caso substring(1) na palavra "hello" começa "ello".
        //CHARAT(y) - y = int index - Neste caso na palavra "hello" daria "h"
    }

    public static int product(int m, int n) {
        if (m < n) {
            product(n, m);
        }
        else if (n != 0) {
            return (m + product(m, n-1));
        }
        return 0;
    }

    public static int max_num(int[] a, int num) {
        if (num == 1) {
            return a[0];
        }

        return Math.max(a[num-1], max_num(a, num-1));
    }

    public static int gcd(int x, int y) {
        if (y != 0) {
            return gcd(y, x % y);
        }
        return x;
    }

    public static int str_to_int(String str) {
        if (str.length() == 1) {
            return str.charAt(0) - '0'; // - '0' converte o char para int
        }
        double y = str_to_int(str.substring(1)); //começa a partir do 2º
        double x = str.charAt(0) - '0'; //Vai buscar 1º dígito

        x = x * Math.pow(10, str.length() - 1) + y;
        return (int) (x);
    }

    public static int palindrome(int n, int temp) {
        if (n == 0) {
            return temp;
        }
        temp = (temp * 10) + (n % 10);

        return palindrome(n / 10, temp);
    }

    public static void main(String[] args) {
        String str = "Hello World!";
        System.out.println("1.");
        System.out.println("a) Retornar string com a mesma ordem." + "\n" + sameOrder(str));
        System.out.println("b) Retornar string com a ordem inversa." + "\n" + reverseOrder(str));
        System.out.println("\n");
        System.out.println("2.");
        System.out.println("a) Produto de dois inteiros positivos." + "\n" + product(3, 2));
        int a[] = {1, 4, 45, 6, -50, 10, 2};
        int num = a.length;
        System.out.println("b) Valor máximo num array." + "\n" + max_num(a, num-1));
        System.out.println("c) Máximo Divisor Comum de dois números positivos." + "\n" + gcd(48, 30));
        System.out.println("d) Converter String de dígitos para um inteiro." + "\n" + str_to_int("13531"));
        System.out.println("e) Verificar se um número é palíndrome.");
        int n = 1221;
        int temp = palindrome(n, 0);
        if (temp == n) {
            System.out.println("YES");
        }
        else {
            System.out.println("NO");
        }
    }
}
