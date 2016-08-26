package org.deidentifier.arx.risk;


import java.util.Arrays;

class RiskModelAlphaDistinctionSeparation extends RiskModelSample {

    /**
     * Creates a new instance
     *
     * @param histogram
     */
    RiskModelAlphaDistinctionSeparation(RiskModelHistogram histogram) {
        super(histogram);
    }

    double getAlphaDistinction() {
        // alpha-distinct = countUniqueTuples / countAllTuples
        return getHistogram().getNumClasses() / getHistogram().getNumRecords();

    }
    double getAlphaSeparation() {
        RiskModelHistogram histogram = getHistogram();
        int[] classes = histogram.getHistogram();
        int comparesTotal = sum((int)histogram.getNumRecords()-1);

        int collisions = 0;
        for (int i = 0; i < classes.length; i+=2) {
            int classSize = classes[i];
            int count = classes[i+1];
            int cc = 0;
            int[] classesWithoutCurrent = Arrays.copyOfRange(classes, i+2, classes.length);
            int restRecords = getNumRecords(classesWithoutCurrent);
            for (int ii = 1; ii < count; ii++) {
                cc += classSize * (classSize+restRecords);
            }
            cc += classSize * restRecords;
            collisions += cc;
        }

        return (double)collisions/(double)comparesTotal;
    }

    private int getNumRecords(int[] classes) {
        int num = 0;
        for (int i = 0; i<classes.length;i+=2) {
            num += classes[i+1] * classes[i];
        }
        return num;
    }

    private int sum(int n) {
        // 4 3 2 1
        if (n % 2 == 0) {
            return (n/2)*(n+1);
        } else {
            // 5 4 3 2 1
            return n + ((n-1)/2)*(n);
        }
        //return sum_r(n, 0);
    }
    private int sum_r(int n, int sum) {
        // 1. Summe rekursiv sum(n)+sum(n-1)
        // 2. Summe mit Gauß 4+3+2+1 = 4+1 + 3+2 = 5 + 5 = 10 => 1.elem/2 * (1.elem +1)
        // paralell for von Java?
        return sum_r(n-1, sum+n);
    }

}