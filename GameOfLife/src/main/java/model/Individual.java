package model;
import org.apache.log4j.Logger;

import java.util.Random;

public class Individual implements Comparable<Individual>{
    private boolean[][] individualMatrix;
    private int matrixHeight = 20;
    private int matrixWidth = 20;
    private int maxGeneration = 0;
    private Random randGen = new Random();
    private static final Logger LOGGER = Logger.getLogger(Individual.class);

    public Individual(int matrixHeight, int matrixWidth) {
        this.matrixHeight = matrixHeight;
        this.matrixWidth = matrixWidth;
        individualMatrix = new boolean[matrixHeight][matrixWidth];
        //randGen.setSeed(10);
    }

    public Individual(boolean[][] matrix){
        this.matrixHeight = matrix.length;
        this.matrixWidth = matrix[0].length;
        individualMatrix = matrix;
        //randGen.setSeed(10);
    }

    public int getMaxGeneration() {
        return maxGeneration;
    }

    public void setMaxGeneration(int maxGeneration) {
        this.maxGeneration = maxGeneration;
    }

    public int getMatrixHeight() {
        return matrixHeight;
    }

    public int getMatrixWidth() {
        return matrixWidth;
    }

    public boolean[][] getIndividualMatrix() {
        return individualMatrix;
    }

    public int compareTo(Individual otherIndividual) {
        if (this.maxGeneration > otherIndividual.getMaxGeneration()) {
            return -1;
        } else if(this.maxGeneration < otherIndividual.getMaxGeneration()) {
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * Mutation of a cell
     * Generate a random number, if this number is less than mutationChance
     * then its status will be generated randomly (true or false)
     * @param mutationChance
     */

    public void mutation(int mutationChance) {
        boolean status;
        int randInt;

        for (int row = 0; row < matrixHeight; row++) {
            for (int col = 0; col < matrixWidth; col++) {
                randInt = randGen.nextInt(100);
                if (randInt < mutationChance) {
                    status = randGen.nextBoolean();
                    individualMatrix[row][col] = status;
                }
            }
        }
    }

    public void deepCopy(Individual copyFrom) {
        for (int row = 0; row < matrixHeight; row++) {
            for (int col = 0; col < matrixWidth; col++) {
                this.setCell(row, col, copyFrom.getCell(row, col));
            }
        }
        this.setMaxGeneration(copyFrom.getMaxGeneration());
    }

    /**
     * Set random initial status of the cell
     * It will randomly produce a number in [0,100)
     * If this number is less than cellChanceToLive, then it is live
     * Otherwise, this cell is dead
     * @param cellChanceToLive
     */
    public void setRandomIndividual(int cellChanceToLive)  {
        if (cellChanceToLive > 100) {
            cellChanceToLive = 100;

        } else {
            if (cellChanceToLive < 0) {
                cellChanceToLive = 0;
            }
        }

        for (int row = 0; row < matrixHeight; row++) {
            for (int col = 0; col < matrixWidth; col++) {
                int randInt = randGen.nextInt(100);
                if (randInt < cellChanceToLive) {
                    individualMatrix[row][col] = true;
                }
            }
        }

        LOGGER.info("The random initial matrix for "+this+":");
        new CellGrid().printMatrix(individualMatrix);

    }

    public void setCell(int row, int col, boolean status) {
        individualMatrix[row][col] = status;
    }

    public boolean getCell(int row, int col) {
        return individualMatrix[row][col];
    }

    /**
     * Replace certain region of cells of an individual given a region
     * @param topRowInd
     * @param leftColInd
     * @param height
     * @param width
     * @param region
     */
    public void setCellRegion(int topRowInd, int leftColInd, int height, int width, boolean[][] region) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                individualMatrix[topRowInd + row][leftColInd + col] = region[row][col];
            }

        }
    }

    /**
     * Get certain region of cells of an individual
     * @param topRowInd
     * @param leftColInd
     * @param height
     * @param width
     * @return
     */
    public boolean[][] getCellRegion(int topRowInd, int leftColInd, int height, int width) {
        boolean[][] region = new boolean[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                region[row][col] = individualMatrix[topRowInd + row][leftColInd + col];
            }
        }
        return region;
    }

}