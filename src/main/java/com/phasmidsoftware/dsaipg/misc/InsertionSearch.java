package com.phasmidsoftware.dsaipg.misc;

import java.util.Arrays;

public class InsertionSearch {
    public static void main(String[] args) {
        int[] ar = {10, 2, 3, 4, 5, 6, 7, 8, 9};
        int key = 0;
        int i;
        int j;

       for(i=1;i<ar.length;i++) {
           key = ar[i];

           while (i>0&&key < ar[i-1]) {
               ar[i] = ar[i-1];
               i--;
           }
           ar[i]=key;
       }


      for(int s=0;s<ar.length;s++)
        System.out.println(ar[s]);
    }

    /**
     * Method to do binary search.
     *
     * @param a    the ordered array.
     * @param from the first index on interest.
     * @param to   the first subsequent index that is NOT of interest.
     * @param key  the value we are searching for.
     * @return the index of the element whose value is <code>key</code>, or null if there is no such element.
     */
    static int binarySearch(int[] a, int from, int to, int key) {
        int lo = from;
        int hi = to;
        while (hi > lo) {
            // TO BE IMPLEMENTED  : implement binary search
            return -1;
            // END SOLUTION
        }
        return -1;
    }

}
