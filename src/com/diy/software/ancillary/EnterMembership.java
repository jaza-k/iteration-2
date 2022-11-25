
package com.diy.software.ancillary;

import java.util.Scanner;

public class EnterMembership {


    public static void chill(long zzz) {
        if (zzz > 2500) zzz = 2500;
        long mil = System.currentTimeMillis();
        while (System.currentTimeMillis() - mil < zzz) ;
    }

    public static void filler() {
        chill(2500);
        System.out.print("\n\n... hmmm, well I never...\n");
        chill(2500);
        System.out.print("\n... I didn't know you felt that way about eggnog...\n");
        chill(2500);
        System.out.print("\n... guess I will see you around...\n");
        chill(2000);
        System.out.print("\n........ \n\n\n");
        chill(1500);
    }


    public static void main(String[] args) {

        //String num = "12345";
        Member mem = new Member();
        mem.setNumb("12345");
        mem.setName("Dr. Walker");

        final String numb = mem.getNumb();
        final String name = mem.getName();


        String ent, attempt;
        boolean closed = false;

        // TODO Auto-generated method stub
        System.out.print("\nHello there - welcome to your Do-It-Yourself checkout station!\n\n");

        System.out.print("######################################\n");
        System.out.print("## You are at the membership screen ##\n");
        System.out.print("######################################\n");

        System.out.print("\nOnce your membership number has been inputted, please use \'enter\' to submit.");
        System.out.print("\n  -----  You may skip this step at any point by using \'skip\'  -----\n\n");
        System.out.print("\nPlease provide your membership number:\n\n");

        // INIT SCANNER
        Scanner in = new Scanner(System.in);
        attempt = "";

        while (!closed) {
            ent = in.nextLine();

            if (ent.equals("")) {
                String secret = "";
                for (int i = 0; i < attempt.length(); i++) {
                    secret = secret + "*";
                }
                System.out.print(secret);
                continue;
            } else if (ent.equals("skip")) closed = true;
            else if (ent.equals("enter")) {
                if (attempt.equals(numb)) closed = true;
                else {
                    System.out.print("\n\nYou have entered an incorrect membership number.\n\n");
                    chill(1500);

                    System.out.print("Please provide your number and then using \'enter\' to submit...\n");
                    System.out.print(" ---  You may skip this step at any point by using \'skip\'  ---\n\n");

                    // RESET SUBMISSION
                    attempt = "";
                }
            } else {

                attempt += ent.charAt(0);

                String secret = "";
                for (int i = 0; i < attempt.length(); i++) {
                    secret = secret + "*";
                }
                System.out.print(secret);
                //System.out.print(attempt);

            }

        }

        in.close();

        if (attempt.equals(numb)) {
            System.out.print("\n\nThank you, " + name + "!");     //It is great to see you again :)\n\n");
            chill(1111);
            System.out.print(" It is great to see you again :)\n\n");
            chill(10000);
        }

        // BE HELPFUL?
        System.out.print("\nHow may we help you today?\n\n");
        chill(2000);
        filler();

        // TERMINATE
        System.out.print("\nPlease come again!\n\n");
        chill(1000);
        System.out.print("\n------- SESSION TERMINATED -------\n");
        System.exit(0);
    }

}
