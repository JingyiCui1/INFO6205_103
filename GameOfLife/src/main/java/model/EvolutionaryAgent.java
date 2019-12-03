package model;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * EvolutionaryAgent class applies an evolutionary algorithm to Conway's Game of
 * Life to evolve interesting patterns. This class manages the population,
 * selection, and mutation.
 */
public class EvolutionaryAgent {
    public CellGrid myGrid;
    private Configuration[] population;
    private Random randGen = new Random();

    private int gridHeight = 20;
    private int gridWidth = 20;

    private int popSize = 1000;
    private int numGens = 100;
    private int numElites = 20;
    private int tournamentSize = 40;

    private int liveChance = 20;
    private int mutationChance = 5;
    private int crossoverChance = 5;
    private final int MAX_GENERATION = 1000;
    private static final Logger LOGGER = Logger.getLogger(EvolutionaryAgent.class);


    public EvolutionaryAgent() {
        myGrid = new CellGrid(gridHeight, gridWidth);
        generateStartingPopulation();
    }

    /**
     * Initialize myGrid(cellMatrix, liveDieTable, generationTable),
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

    private void generateStartingPopulation() {

        population = new Configuration[popSize];
        for (int i = 0; i < popSize; i++) {
            population[i] = new Configuration(gridHeight, gridWidth);
        }
    }

    private void initializePopulation(int liveChance) {
        for (int i = 0; i < popSize; i++) {
            population[i].setRandomConfiguration(liveChance);
        }
        LOGGER.info("Set random initial pattern for population successfully.");
    }

    private void initializeNewPopulation(Configuration[] newPopulation) {
        for (int i = 0; i < popSize; i++) {
            newPopulation[i] = new Configuration(gridHeight, gridWidth);
        }
    }


    public Configuration evolvePattern() {
        //Initialize individuals in the population with gridHeight and gridWidth
        //generateStartingPopulation();
        //Set initial status randomly of cells in each individual
        initializePopulation(liveChance);
        boolean hyperMutationTriggered = false;

       // for (int gen = 0; gen < numGens; gen++) {
            // Evaluation
            for (Configuration config: population) {
                //Initialize cellMatrix in CellGrid using Configuration
                myGrid.setStartingConfiguration(config);
                //Get max generation for each individual after numGameGens generations
                config.setMaxGeneration(myGrid.runGame(MAX_GENERATION));
            }
            //Sort individuals by max generation descendently
            sortPopulation();

            // Selection (w/ elitism)
            //Create a new population
            //Configuration[] newPopulation = new Configuration[popSize];
            //Initialize new population with gridHeight and gridWidth
            //initializeNewPopulation(newPopulation);
            //Clone first numElites individuals from population to new population
            //cloneElites(newPopulation);
            //Shuffle remaining individuals in the new population and find the max generation, then
            //make remaining equal to it
            //selectRemainingIndividuals(numElites, newPopulation);
            //Save the new population into population
            //saveNewPopulation(newPopulation);

            // Apply chance for mutation
            //Implement mutation and crossover based on mutationChance and crossoverChance
            //applyVariationOperators(numElites/2, mutationChance, crossoverChance);


        /*for (Configuration config: population) {
            //Initialize cellMatrix
            myGrid.setStartingConfiguration(config);
            //Get max generation for each individual after numGameGens generations
            config.setMaxGeneration(myGrid.runGame(MAX_GENERATION));
        }*/

        //Find the best individual
        Configuration bestConfig = population[0];//findBestConfiguration();
        return bestConfig;
    }

    /**
     * Clone certain number of individuals according to max generation
     * @param newPopulation
     */
    private void cloneElites(Configuration[] newPopulation) {
        sortPopulation();
        for (int i = 0; i < numElites; i++) {
            newPopulation[i].deepCopy(population[i]);
        }
    }

    /**
     * Find the highest generation num and make remaining individuals equal it
     * @param startingIndex
     * @param newPopulation
     */
    private void selectRemainingIndividuals(int startingIndex, Configuration[] newPopulation) {
        for (int i = startingIndex; i < popSize; i++) {
            shufflePopulation(startingIndex);
            Configuration bestConfig = population[0];
            for (int j = 0; j < tournamentSize; j++) {
                if (bestConfig.getMaxGeneration() < population[j].getMaxGeneration()) {
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
     * Sort Configurations in a population according to max generation (descending)
     */
    public void sortPopulation() {
        Arrays.asList(population);
        List<Configuration> popList = new ArrayList<Configuration>(Arrays.asList(population));
        Collections.sort(popList);
        population = popList.toArray(population);
        LOGGER.info("Sort population successfully");

    }


    private Configuration findBestConfiguration() {
        Configuration bestConfig = population[0];
        for (Configuration config : population) {
            if (bestConfig.getMaxGeneration() < config.getMaxGeneration()) {
                bestConfig = config;
            }
        }
        return bestConfig;
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

}