package com.phasmidsoftware.dsaipg.adt.threesum;

import com.phasmidsoftware.dsaipg.adt.threesum.*;
//import util.StopWatch;  // 假设存储库中已有 StopWatch 类
import com.phasmidsoftware.dsaipg.util.Stopwatch;
import java.util.Random;

public class ThreeSumCubicTimingTest {
    // 生成随机整数数组，整数范围可以适当调整
    public static int[] randomArray(int N) {
        Random rand = new Random();
        int[] a = new int[N];
        for (int i = 0; i < N; i++) {
            // 生成 -N 到 N 范围内的随机整数
            a[i] = rand.nextInt(2 * N + 1) - N;
        }
        return a;
    }

    public static void main(String[] args) {
        int[] sizes = {250, 500, 1000, 2000, 4000};
        for (int N : sizes) {
            int[] array = randomArray(N);
         //   ThreeSumCubic ts = new ThreeSumCubic(array);
          //  ThreeSumQuadratic ts1=new ThreeSumQuadratic(array);
          ThreeSumQuadrithmic ts2=new ThreeSumQuadrithmic(array);
            // 利用 Stopwatch 对 getTriples() 方法进行计时
        //    try (Stopwatch sw = new Stopwatch()) {
        //        Triple[] triples = ts.getTriples();
                // 获取从开始到调用 lap() 的时间差（单位：毫秒）
       //         long elapsedMillis = sw.lap();
        //        System.out.println("For array size: " + N);
        //        System.out.println("Number of triples found: " + triples.length);
        //        System.out.println("Time elapsed: " + elapsedMillis + " msec");
       //     }
          //  try (Stopwatch sw = new Stopwatch()) {
         //       Triple[] triples = ts1.getTriples();
                // 获取从开始到调用 lap() 的时间差（单位：毫秒）
         //       long elapsedMillis = sw.lap();
          //      System.out.println("For array size: " + N);
          //      System.out.println("Number of triples found: " + triples.length);
          //      System.out.println("Time elapsed: " + elapsedMillis + " msec");
        //    }

          try (Stopwatch sw = new Stopwatch()) {
              Triple[] triples = ts2.getTriples();
                // 获取从开始到调用 lap() 的时间差（单位：毫秒）
               long elapsedMillis = sw.lap();
                System.out.println("For array size: " + N);
               System.out.println("Number of triples found: " + triples.length);
               System.out.println("Time elapsed: " + elapsedMillis + " msec");
          }
        }
    }
}