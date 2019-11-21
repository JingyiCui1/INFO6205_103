package model;/*
EvolutionaryAgent class applies an evolutionary algorithm to Conway's Game of
Life to evolve interesting patterns. This class manages the population,
selection, and mutation.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EvolutionaryAgent {
    public CellGrid myGrid;
    private Configuration[] population;
    private Random randGen = new Random();

    private int gridHeight = 16;
    private int gridWidth = 16;

    private int popSize = 200;
    private int numGens = 100;
    private int numGameGens = 50;
    private int numElites = 20;
    private int tournamentSize = 40;

    private int mutationChance = 5;
    private int crossoverChance = 5;
    private double hyperMutationPercentThresh = 0.9;

    public EvolutionaryAgent() {
        myGrid = new CellGrid(gridHeight, gridWidth);
        generateStartingPopulation();
    }

    /**
     * Initialize myGrid(cellMatrix, liveDieTable, scoreMap),
     * population(certain num of population(each contains configMatrix))
     * @param numGens
     */
    public EvolutionaryAgent(int numGens) {
        this.numGens = numGens;
        myGrid = new CellGrid(gridHeight, gridWidth);
        generateStartingPopulation();
    }

    public EvolutionaryAgent(int gridHeight, int gridWidth) {
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        myGrid = new CellGrid(gridHeight, gridWidth);
        generateStartingPopulation();
    }

    public EvolutionaryAgent(int gridHeight, int gridWidth, int popSize) {
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.popSize = popSize;
        myGrid = new CellGrid(gridHeight, gridWidth);
        generateStartingPopulation();
    }

    public static void main(String args[]) {
        EvolutionaryAgent myAgent = new EvolutionaryAgent(5);
        Configuration bestPattern = myAgent.evolvePattern(true);
        myAgent.displayPattern(bestPattern);
        System.out.println(bestPattern.getScore());
        myAgent.myGrid.viewSimulation(true, 5, bestPattern);

        int numRuns = 15;
        //baseCaseTesting();
        //knownPatternTesting();
        //runTest(numRuns);
        //testHypermutation(numRuns);
    }

    public static void runTest(int numRuns) {
        int[] genNumList = {10, 50, 100, 200};
        double[] averageFitnesses = new double[4];
        EvolutionaryAgent testAgent;

        System.out.println("========================\nTest 1: Vanilla GA");
        for (int genNumIndex = 0; genNumIndex < 4; genNumIndex++) {
            double bestFitnessTotal = 0;
            testAgent = new EvolutionaryAgent(genNumList[genNumIndex]);
            for (int run = 0; run < numRuns; run++) {
                System.out.println("Run " + String.valueOf(run + 1) + " for gen. size of " +
                        String.valueOf(genNumList[genNumIndex]));
                Configuration bestConfig = testAgent.evolvePattern(true);
                bestFitnessTotal += bestConfig.getScore();
            }
            averageFitnesses[genNumIndex] = bestFitnessTotal/20;
            System.out.println(averageFitnesses[genNumIndex]);
        }
        System.out.println("========================");

        for (int i = 0; i < 4; i++) {
            System.out.println("When gen. size = " + String.valueOf(genNumList[i]) +
                    " the avg. best fitness was: " + String.valueOf(averageFitnesses[i]));
        }
    }

    public static void testHypermutation(int numRuns) {
        double[] bestFitnesses = new double[15];
        EvolutionaryAgent testAgent = new EvolutionaryAgent(200);
        Configuration bestOverallConfig = new Configuration(testAgent.gridHeight, testAgent.gridWidth);

        System.out.println("========================\nTest 3: Hypermutation");
        for (int run = 0; run < numRuns; run++) {
            System.out.println("Run " + String.valueOf(run + 1));
            Configuration bestConfig = testAgent.evolvePattern(true);
            bestFitnesses[run] = bestConfig.getScore();
            System.out.println("The best fitness of Run " + String.valueOf(run + 1) +
                    " is: " + String.valueOf(bestFitnesses[run]));
            if (bestOverallConfig.getScore() < bestConfig.getScore()) {
                bestOverallConfig.deepCopy(bestConfig);
            }
        }
        System.out.println("========================");

        double totalFitness = 0;
        for (int i = 0; i < 15; i++) {
            totalFitness += bestFitnesses[i];
            System.out.println(bestFitnesses[i]);
        }
        System.out.println("The average of the best fitnesses is " + String.valueOf(totalFitness/15));
        testAgent.displayPattern(bestOverallConfig);
        System.out.println(bestOverallConfig.getScore());
        testAgent.myGrid.viewSimulation(true, 50, bestOverallConfig);
    }

    public void displayPattern(Configuration config) {
        myGrid.setStartingConfiguration(config);
        myGrid.printGrid();
    }

    private void generateStartingPopulation() {

        population = new Configuration[popSize];
        for (int i = 0; i < popSize; i++) {
            population[i] = new Configuration(gridHeight, gridWidth);
        }
    }

    private void initializePopulation() {
        for (int i = 0; i < popSize; i++) {
            population[i].setRandomConfiguration(10);
        }
    }

    private void initializeNewPopulation(Configuration[] newPopulation) {
        for (int i = 0; i < popSize; i++) {
            newPopulation[i] = new Configuration(gridHeight, gridWidth);
        }
    }

    private double calcAvgFitness() {
        double avgFitness = 0;
        for (int i = 0; i < popSize; i++) {
            avgFitness += population[i].getScore();
        }
        return avgFitness/popSize;
    }

    private double calculateStandardDev(double avgFitness) {
        double sumSquares = 0;
        for (int i = 0; i < popSize; i++) {
            sumSquares = Math.pow((population[i].getScore() - avgFitness), 2);
        }
        return Math.sqrt(sumSquares/popSize);
    }

    private void triggerHyperMutation() {
        mutationChance = 20;
        crossoverChance = 20;
    }

    private void resetVarianceOperators() {
        mutationChance = 5;
        crossoverChance = 5;
    }

    public Configuration evolvePattern(boolean withHyperMutation) {
        //Initialize individuals in the population with gridHeight and gridWidth
        generateStartingPopulation();
        //Set initial status randomly of cells in each individual
        initializePopulation();
        double oldAvgFitness = 1;
        double newAvgFitness = 1;
        boolean hyperMutationTriggered = false;
        int hyperMutationTimer = 0;

        for (int gen = 0; gen < numGens; gen++) {
            // Evaluation
            for (Configuration config: population) {
                //Initialize cellMatrix in CellGrid using Configuration
                myGrid.setStartingConfiguration(config);
                //Get total score for each individual after numGameGens generations
                config.setScore(myGrid.runGame(numGameGens, false));
            }
            //Sort individuals by total score descendently
            sortPopulation();

            // Optional triggered hypermutation
            if (withHyperMutation) {
                //Get average score of the population
                newAvgFitness = calcAvgFitness();
                if (hyperMutationTimer > 10) {
                    if (newAvgFitness < hyperMutationPercentThresh * oldAvgFitness) {
                        //Set both mutationChance and crossoverChance to 20
                        triggerHyperMutation();
                        hyperMutationTriggered = true;
                        hyperMutationTimer = -1;
                    }
                }
                hyperMutationTimer++;
                oldAvgFitness = newAvgFitness;
            }


            // Selection (w/ elitism)
            //Create a new population
            Configuration[] newPopulation = new Configuration[popSize];
            //Initialize new population with gridHeight and gridWidth
            initializeNewPopulation(newPopulation);
            //Clone first numElites individuals from population to new population
            cloneElites(newPopulation);
            //Shuffle remaining individuals in the new population and find the highest score, then
            //make remaining equal to it
            selectRemainingIndividuals(numElites, newPopulation);
            //Save the new population into population
            saveNewPopulation(newPopulation);

            // Apply chance for mutation
            //Implement mutation and crossover based on mutationChance and crossoverChance
            applyVariationOperators(numElites/2, mutationChance, crossoverChance);

            // Reset mutation operators (if using triggered hypermutation)
            if (hyperMutationTriggered) {
                //Make both mutationChance and crossoverChance equal to 5
                resetVarianceOperators();
                hyperMutationTriggered = false;
            }
        }

        for (Configuration config: population) {
            //Initialize cellMatrix
            myGrid.setStartingConfiguration(config);
            //Get total score for each individual after numGameGens generations
            config.setScore(myGrid.runGame(numGameGens, false));
        }

        //Find the best individual
        Configuration bestConfig = findBestConfiguration();
        //Save the bset individual into a file
        saveConfiguration(bestConfig);

        return bestConfig;
    }

    /**
     * Clone certain number of individuals according to total score
     * @param newPopulation
     */
    private void cloneElites(Configuration[] newPopulation) {
        sortPopulation();
        for (int i = 0; i < numElites; i++) {
            newPopulation[i].deepCopy(population[i]);
        }
    }

    /**
     * Find the highest score and make remaining individuals equal it
     * @param startingIndex
     * @param newPopulation
     */
    private void selectRemainingIndividuals(int startingIndex, Configuration[] newPopulation) {
        for (int i = startingIndex; i < popSize; i++) {
            shufflePopulation(startingIndex);
            Configuration bestConfig = population[0];
            for (int j = 0; j < tournamentSize; j++) {
                if (bestConfig.getScore() < population[j].getScore()) {
                    bestConfig.deepCopy(population[j]);
                }
            }
            newPopulation[i].deepCopy(bestConfig);
        }
    }

    private void saveNewPopulation(Configuration[] newPopulation) {
        for (int i = 0; i < popSize; i++) {
            population[i].deepCopy(newPopulation[i]);
        }
    }

    /**
     * Sort Configurations in a population according to total score (descending)
     */
    public void sortPopulation() {

        Arrays.asList(population);
        List<Configuration> popList = new ArrayList<Configuration>(Arrays.asList(population));
        Collections.sort(popList);
        population = popList.toArray(population);

    }

    public void printPopScores() {
        for (int i = 0; i < popSize; i++) {
            System.out.println(population[i].getScore());
        }
    }

    public void printNewPopScores(Configuration[] newPop) {
        for (int i = 0; i < popSize; i++) {
            System.out.println(newPop[i].getScore());
        }
    }


    private Configuration findBestConfiguration() {
        Configuration bestConfig = population[0];
        for (Configuration config : population) {
            if (bestConfig.getScore() < config.getScore()) {
                bestConfig = config;
            }
        }
        return bestConfig;
    }

    /*
    https://stackoverflow.com/questions/5865453/java-writing-to-a-text-file
     */
    private void saveConfiguration(Configuration bestConfig) {
        try {
            String fileName = "";
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            BufferedWriter out = new BufferedWriter(new FileWriter("datafile/bestConfig" + timeLog + ".txt"));
            for (int row = 0; row < gridHeight; row++) {
                for (int col = 0; col < gridWidth; col++) {
                    if (bestConfig.getCell(row,col)) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                }
                out.newLine();
            }
            out.close();
        } catch (IOException e) {}
    }


    /**
     * Implement mutation and crossover of all individuals in the population
     * @param startingInd
     * @param mutationChance
     * @param crossoverChance
     */
    private void applyVariationOperators(int startingInd, int mutationChance, int crossoverChance) {
        for (int i = startingInd; i < popSize; i++) {
            population[i].mutation(mutationChance);
        }
        crossover(startingInd, crossoverChance);
    }

    private void crossover(int startingInd, int crossoverChance) {
        shufflePopulation(startingInd);

        for (int i = startingInd; i < (popSize - startingInd)/2; i++) {
            int randChance = randGen.nextInt(100);
            if (randChance < crossoverChance) {
                int rowInd1 = randGen.nextInt(gridHeight);
                int rowInd2 = randGen.nextInt(gridHeight);
                int colInd1 = randGen.nextInt(gridWidth);
                int colInd2 = randGen.nextInt(gridWidth);

                int height = Math.max(rowInd1, rowInd2) - Math.min(rowInd1, rowInd2);
                int width = Math.max(colInd1, colInd2) - Math.min(colInd1, colInd2);
                rowInd1 = Math.min(rowInd1, rowInd2);
                colInd1 = Math.min(colInd1, colInd2);

                boolean[][] region1 = population[i].getCellRegion(rowInd1, colInd1, height, width);
                boolean[][] region2 = population[i + (popSize - startingInd)/ 2].getCellRegion(rowInd1, colInd1, height, width);
                population[i].setCellRegion(rowInd1, colInd1, height, width, region2);
                population[i + (popSize - startingInd)/ 2].setCellRegion(rowInd1, colInd1, height, width, region1);
            }
        }
    }

    /**
     * Shuffle individuals randomly
     * @param startingInd
     */
    private void shufflePopulation(int startingInd) {
        for (int i = startingInd; i < popSize; i++) {
            int newIndex = randGen.nextInt(popSize-startingInd) + startingInd;
            Configuration temp = population[newIndex];
            population[newIndex] = population[i];
            population[i] = temp;
        }
    }

    public static void baseCaseTesting() {
        int[] saturationLevels = {10, 30, 50, 70, 90};
        EvolutionaryAgent testAgent;

        for (int i = 0; i < saturationLevels.length; i++) {
            testAgent = new EvolutionaryAgent(16, 16, 500);

            testAgent.generateStartingPopulation();
            System.out.println("==========================");
            System.out.println("Test Saturation Level: " + String.valueOf(saturationLevels[i]));
            System.out.println("Pop Size: " + String.valueOf(testAgent.popSize));
            System.out.println("Num Game Gens: " + String.valueOf(testAgent.numGameGens));

            for (Configuration config : testAgent.population) {
                config.setRandomConfiguration(saturationLevels[i]);
                testAgent.myGrid.setStartingConfiguration(config);
                config.setScore(testAgent.myGrid.runGame(testAgent.numGameGens, false));
            }
            double avgFitness = testAgent.calcAvgFitness();
            double stdDev = testAgent.calculateStandardDev(avgFitness);
            testAgent.saveTestResults(i + 1, avgFitness, stdDev, saturationLevels[i]);
            System.out.println("Avg. Fitness of final gen: " + String.valueOf(avgFitness));
            System.out.println("Std. Dev of final gen: " + String.valueOf(stdDev));
        }
    }

    public static void knownPatternTesting() {
        EvolutionaryAgent resultRecorder = new EvolutionaryAgent(16, 16, 500);
        CellGrid myGrid = new CellGrid(16, 16);
        String fileName;
        double score;

        for (int i = 0; i < 3; i++) {
            fileName = "FamousPatterns/p" + String.valueOf(i + 1) + ".txt";
            Configuration startingConfig = myGrid.loadStartingConfig(fileName);
            myGrid.setStartingConfiguration(startingConfig);
            score = myGrid.runGame(50, false);
            resultRecorder.saveTestResults(i + 1, score, 0.0, 0);
        }
    }

    private void saveTestResults(int testNum, double avgFitness, double stdDev, int saturationLevel) {
        try {
            String testName = "BaseTest" + String.valueOf(testNum);
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String fileName = testName + timeLog;

            BufferedWriter out = new BufferedWriter(new FileWriter("testresults/" + fileName + ".txt"));
            out.write("==========================");
            out.write("Test: " + testName + "\n");
            out.write("Test Saturation Level: " + saturationLevel + "\n");
            out.write("Test time: " + timeLog + "\n");
            out.write("Pop Size: " + String.valueOf(popSize) + "\n");
            out.write("Num Game Gens: " + String.valueOf(numGameGens) + "\n");
            out.write("Avg. Fitness of final gen: " + String.valueOf(avgFitness) + "\n");
            out.write("Std. Dev of final gen: " + String.valueOf(stdDev) + "\n");
            out.close();
        } catch (IOException e) {}
    }

}