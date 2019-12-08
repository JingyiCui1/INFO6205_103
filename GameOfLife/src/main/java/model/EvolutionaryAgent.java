package model;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * EvolutionaryAgent class applies an evolutionary algorithm to Conway's Game of
 * Life to evolve interesting patterns. This class manages the population,
 * selection, and mutation.
 */
public class EvolutionaryAgent{
    public  CellGrid myGrid;
    private Individual[] population;
    private Random randGen = new Random();

    private int gridHeight = 20;
    private int gridWidth = 20;

    private int popSize = 1000;
    private int numGens = 20; // it should be the length of population, but it will take a long time to run the project
    private int numElites = 20;
    private int tournamentSize = 40;

    private int liveChance = 40;
    private int mutationChance = 5;
    private final int MAX_GENERATION = 1000;//it should be 10000, but it will take a long time to run the project
    private static final Logger LOGGER = Logger.getLogger(EvolutionaryAgent.class);

    public EvolutionaryAgent() {
        myGrid = new CellGrid(gridHeight, gridWidth);
        generateStartingPopulation();
        //randGen.setSeed(10);
    }


    private void generateStartingPopulation() {

        population = new Individual[popSize];
        for (int i = 0; i < popSize; i++) {
            population[i] = new Individual(gridHeight, gridWidth);
        }
    }

    private void initializePopulation(int liveChance) {
        for (int i = 0; i < popSize; i++) {
            population[i].setRandomIndividual(liveChance);
        }
        LOGGER.info("Set random initial pattern for population successfully.");
    }

    private void initializeNewPopulation(Individual[] newPopulation) {
        for (int i = 0; i < popSize; i++) {
            newPopulation[i] = new Individual(gridHeight, gridWidth);
        }
    }


    public Individual evolvePattern() {
        //Initialize individuals in the population with gridHeight and gridWidth
        generateStartingPopulation();
        //Set initial status randomly of cells in each individual
        initializePopulation(liveChance);

        for (int gen = 0; gen < numGens; gen++) {
            for (Individual individual : population) {
                //Initialize cellMatrix in CellGrid using Individual
                myGrid.setStartingIndividual(individual);
                //Get max generation for each individual after numGameGens generations
                individual.setMaxGeneration(myGrid.runGame(MAX_GENERATION));
                LOGGER.info("The max generation for " + individual + " is " + individual.getMaxGeneration());
            }


            //Sort individuals by max generation descendently
            sortPopulation();

            // Selection
            Individual[] newPopulation = new Individual[popSize];
            initializeNewPopulation(newPopulation);
            cloneElites(newPopulation);
            selectRemainingIndividuals(numElites, newPopulation);
            saveNewPopulation(newPopulation);

            // Apply chance for mutation
            for (int i = 0; i < newPopulation.length; i++) {
                newPopulation[i].mutation(mutationChance);
            }

        }

        for (Individual individual: population) {
            myGrid.setStartingIndividual(individual);
            individual.setMaxGeneration(myGrid.runGame(MAX_GENERATION));
            LOGGER.info("The max generation(new population) for "+individual+" is "+individual.getMaxGeneration());
        }

        //Find the best individual
        Individual bestIndividual = findBestIndividual();
        return bestIndividual;
    }

    /**
     * Clone certain number of individuals according to max generation
     * @param newPopulation
     */
    private void cloneElites(Individual[] newPopulation) {
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
    private void selectRemainingIndividuals(int startingIndex, Individual[] newPopulation) {
        for (int i = startingIndex; i < popSize; i++) {
            shufflePopulation(startingIndex);
            Individual bestIndividual = population[0];
            for (int j = 0; j < tournamentSize; j++) {
                int randInt = randGen.nextInt(population.length-startingIndex)+startingIndex;
                bestIndividual.deepCopy(population[randInt]);

            }
            newPopulation[i].deepCopy(bestIndividual);
        }
    }

    private void saveNewPopulation(Individual[] newPopulation) {
        for (int i = 0; i < popSize; i++) {
            population[i].deepCopy(newPopulation[i]);
        }
    }

    /**
     * Sort Configurations in a population according to max generation (descending)
     */
    public void sortPopulation() {
        Arrays.asList(population);
        List<Individual> popList = new ArrayList<Individual>(Arrays.asList(population));
        Collections.sort(popList);
        population = popList.toArray(population);
        LOGGER.info("Sort population successfully");

    }


    private Individual findBestIndividual() {
        Individual bestIndividual = population[0];
        for (Individual individual : population) {
            if (bestIndividual.getMaxGeneration() < individual.getMaxGeneration()) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }


    /**
     * Shuffle individuals randomly
     * @param startingInd
     */
    private void shufflePopulation(int startingInd) {
        for (int i = startingInd; i < popSize; i++) {
            int newIndex = randGen.nextInt(popSize-startingInd) + startingInd;
            Individual temp = population[newIndex];
            population[newIndex] = population[i];
            population[i] = temp;
        }
    }

}