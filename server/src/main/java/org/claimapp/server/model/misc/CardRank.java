package org.claimapp.server.model.misc;

import java.util.Arrays;
import java.util.List;

public class CardRank {
    public static final String R_A = "A";
    public static final String R_2 = "2";
    public static final String R_3 = "3";
    public static final String R_4 = "4";
    public static final String R_5 = "5";
    public static final String R_6 = "6";
    public static final String R_7 = "7";
    public static final String R_8 = "8";
    public static final String R_9 = "9";
    public static final String R_10 = "10";
    public static final String R_J = "J";
    public static final String R_Q = "Q";
    public static final String R_K = "K";

    public static final List<String> RanksAsList = Arrays.asList(R_A, R_2, R_3, R_4, R_5, R_6, R_7, R_8, R_9, R_10, R_J, R_Q, R_K);

    public static int getScore(String rank) {
        switch (rank) {
            case R_A: return 1;
            case R_2: return 2;
            case R_3: return 3;
            case R_4: return 4;
            case R_5: return 5;
            case R_6: return 6;
            case R_7: return 7;
            case R_8: return 8;
            case R_9: return 9;
            case R_10: return 10;
            case R_J: return 12;
            case R_Q: return 13;
            case R_K: return 14;
        }

        return 100;
    }
}
