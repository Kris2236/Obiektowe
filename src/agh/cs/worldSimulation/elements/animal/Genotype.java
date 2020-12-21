package agh.cs.worldSimulation.elements.animal;

import java.security.SecureRandom;
import java.util.*;

public class Genotype {
    public static int genomeSize = 32;
    public static int numberOfGeneTypes = 8;
    protected List<Integer> genotype = new ArrayList<>(genomeSize);
    protected int[] numberOfGeneType = new int[numberOfGeneTypes];


    public Genotype() {
        for(int i=0; i<numberOfGeneTypes; i++) {
            genotype.add(i);                    // every animal must be able to move in every direction in its genes
        }

        for (int i=0; i<genomeSize-numberOfGeneTypes; i++) {
            genotype.add(randomNumberBetween(0,numberOfGeneTypes - 1));     // rest gene are random
        }

        Collections.sort(genotype);             // more random values will be if genotype unsorted

        for (int i = 0; i < genomeSize; i++) {
            numberOfGeneType[genotype.get(i)]++;
        }
    }

    public Genotype(List<Integer> genotype){
        if (genotype.size() == 32) {
            this.genotype = genotype;
        }
    }

    public Genotype(Genotype genotype1, Genotype genotype2) {
        int[] childNumberOfGeneType = new int[numberOfGeneTypes];
        List<Integer> childGenotype = connectGenotypes(genotype1, genotype2);
        Collections.sort(childGenotype);

        if (childGenotype.size() != genomeSize)
            throw new IllegalArgumentException(childGenotype + " length: " + childGenotype.size() + " genotype length is not legal. Length have to be " + genomeSize);

        // Parsing genotype
        boolean parsedGenotype = false;

        while(!parsedGenotype){
            parsedGenotype = true;

            for (int i = 0; i < genomeSize; i++)
                childNumberOfGeneType[childGenotype.get(i)]++;

            for (int i = 0; i < numberOfGeneTypes; i++) {
                if (childNumberOfGeneType[i] == 0) {        // Replacement the random gene with the i-th value
                    parsedGenotype = false;
                    childGenotype.remove(randomNumberBetween(0,genomeSize - 1));
                    childGenotype.add(i);
                    Collections.sort(childGenotype);
                }
            }
        }

        genotype.addAll(childGenotype);
        if (numberOfGeneTypes >= 0) System.arraycopy(childNumberOfGeneType, 0, numberOfGeneType, 0, numberOfGeneTypes);
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");

        for(Integer i : genotype) {
            result.append(" " + i);
        }
        result.append(" ]");

        return result.toString();
    }

    public List<Integer> getGenotype() { return this.genotype; }

    private List<Integer> connectGenotypes(Genotype genotype1, Genotype genotype2) {
        List<Integer> childGenotype = new ArrayList<>();

        int splitPoint1 = randomNumberBetween(1, genomeSize-2);                 // First parents genotypes split point
        int splitPoint2 = randomNumberBetween(splitPoint1 + 1, genomeSize-1);   // Second parents genotypes split point

        int genotypePart1 = randomNumberBetween(1,3);       // Choose random first genotype part

        int genotypePart2 = randomNumberBetween(1,3);       // Choose random second genotype part
        while(genotypePart2 == genotypePart1)               // have to be different than first choice
            genotypePart2 = randomNumberBetween(1,3);

        childGenotype.addAll(getPartGenotype(genotypePart1, splitPoint1, splitPoint2, genotype1));    // random part from first genotype
        childGenotype.addAll(getPartGenotype(genotypePart2, splitPoint1, splitPoint2, genotype2));    // random part from second genotype

        int genotypePart3 = randomNumberBetween(1,3);
        while(genotypePart3 == genotypePart1 || genotypePart3 == genotypePart2)     // have to be different than first choice
            genotypePart3 = randomNumberBetween(1,3);

        int chosenLastGenotype = randomNumberBetween(1,2);     // have to be different than previous choices
        if(chosenLastGenotype == 1)
            childGenotype.addAll(getPartGenotype(genotypePart3, splitPoint1, splitPoint2, genotype1));
        else
            childGenotype.addAll(getPartGenotype(genotypePart3, splitPoint1, splitPoint2, genotype2));

        return childGenotype;
    }

    private List<Integer> getPartGenotype(int part, int div1, int div2, Genotype genotype) {
        return switch (part){
            case 1 -> genotype.genotype.subList(0, div1 + 1);
            case 2 -> genotype.genotype.subList(div1 + 1, div2 + 1);
            case 3 -> genotype.genotype.subList(div2 + 1, genomeSize);
            default -> throw new IllegalStateException("Unexpected value: " + part + ". Partition point have to belong to set {1,2,3}.");
        };
    }

    public int getGeneAccordingGenotype() {
        return genotype.get(randomNumberBetween(0, genomeSize-1));
    }

    private int randomNumberBetween(int min, int max) {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(max - min + 1) + min;
    }
}
