package model;
/**
 * cellGrid class Implementation
 *
 *
 * This class implements a cellGrid object, which simulates Conway's Game of
 * Life. It includes a very basic graphical interface which can print the
 * current state of the board to the terminal. It is included to aid with
 * debugging, and may not be useful for displaying full runs.
 *
 *
 * TO - DO:
 *
 * This project currently uses a lookup table for three by three neighborhoods of cells.
 * This is currently implemented to use a mutable object boolean[][] as the key. I have
 * since learned that the keys must not be mutable objects. Therefore, I should rewrite
 * code to make the keys immutable objects. I will change the row implementation to instead
 * code 0s and 1s instead of copying arrays directly. The neighborhood will be represented
 * as a string of 0s and 1s instead of a boolean[][] which should let me properly use
 * neighborhoods as keys.
 *
 * Test: if the lookup table works properly - try lots of values of neighborhoods.
 * Test: if next gen works properly.
 * Test: if makeNeighborhood properly adjusts.
 * Test: simple oscillating pattern.
 *
 *
 */


import java.util.HashMap;


public class CellGrid {

    private int gridHeight;
    private int gridWidth;
    private boolean[][] cellMatrix;
    private HashMap<String, Boolean> liveDieTable;
    private HashMap<String, Integer> scoreMap;
    private int genCount = 0;
    private final int MAX_GENERATION_COUNT = 10000;


    /*
   Returns a cellGrid object. Note that the user dimensions are increased by
   two - this is specific to the implementation and should not affect the
   user's experience.
   @param gridHeight    the int number of rows in the grid.
   @param gridWidth     the int number of columns in the grid.
   @return the cellGrid object
    */
    public CellGrid(int gridHeight, int gridWidth) {
        this.gridHeight = gridHeight + 2;
        this.gridWidth = gridWidth + 2;


        cellMatrix = new boolean[this.gridHeight][this.gridWidth];
        liveDieTable = new HashMap<String, Boolean>();
        scoreMap = new HashMap<String, Integer>();
        String tempNeighborhood = "";
        initializeLiveDieTable(0, tempNeighborhood);
    }

    /*
        Returns a cellGrid object.
        @return the cellGrid object
         */
    public CellGrid() {
        cellMatrix = new boolean[gridHeight][gridWidth];
        liveDieTable = new HashMap<String, Boolean>();
        scoreMap = new HashMap<String, Integer>();
        String tempNeighborhood = "";
        initializeLiveDieTable(0, tempNeighborhood);
    }


    public boolean[][] getCellMatrix() {
        return cellMatrix;
    }

    public int getGenCount() {
        return genCount;
    }

    public void setGenCount(int genCount) {
        this.genCount = genCount;
    }

    /*
    Sets the initial configuration of living and dead cells on the cellMatrix.
    Assumes that the input is of the proper dimensions.
    @param initialConfig    2-D array that holds boolean values, with true
                            denoting living cells, false denoting dead cells.
     */

    /**
     * Set the initial status of cellMatrix given configMatrix
     * @param initialConfig
     */
    public void setStartingConfiguration(Configuration initialConfig) {
        genCount=0;
        for (int row = 1; row < gridHeight - 1; row++) {
            for (int col = 1; col < gridWidth - 1; col++) {
                cellMatrix[row][col] = initialConfig.getCell(row-1, col-1);
            }
        }
    }


    public double runGame(int numGenerations) {
        int totalScore = 0;

        for (int gen = 0; gen < numGenerations; gen++) {
            scoreMap = new HashMap<String, Integer>();
            totalScore += nextGen();
        }

        return totalScore;
    }

    /*
    Updates which cells are alive and which cells are dead in the next
    generation. The cells on the outer edge do not count as live cells and
    may not come to life. Returns the score for each generation.
     */

    /**
     * Produce the next generation according to liveDieTable
     * and return scores
     * @return
     */
    public double nextGen() {
        boolean[][] cellMatrixNew = new boolean[this.gridHeight][this.gridWidth];
        String rowChunkA = "";
        String rowChunkB = "";
        String rowChunkC = "";
        String neighborhood;
        int rowARelInd;
        double score = 0;
        int numLiveCells = 0;

        for (int curCol = 1; curCol < gridWidth -1; curCol++) {
            rowChunkA = copyChunk(0, curCol-1, curCol +2);
            rowChunkB = copyChunk(1, curCol-1, curCol +2);
            rowARelInd = 0;

            for (int curRow = 1; curRow < gridHeight -1; curRow++) {
                switch (rowARelInd) {
                    case 0:
                        rowChunkC = copyChunk(curRow+1, curCol-1, curCol+2);
                        break;
                    case 1:
                        rowChunkB = copyChunk(curRow+1, curCol-1, curCol+2);
                        break;
                    case 2:
                        rowChunkA = copyChunk(curRow+1, curCol-1, curCol+2);
                        break;
                    default:
                        System.out.println("It's dead, Jim.");
                }
                neighborhood = makeNeighborhood(rowARelInd, rowChunkA, rowChunkB, rowChunkC);
                cellMatrixNew[curRow][curCol] = isAliveNextGen(neighborhood);
                if (isAliveNextGen(neighborhood)) {
                    score += 1;
                }
                score += scoreNeighborhood(neighborhood, curRow, curCol);
                if (rowARelInd == 0) {
                    rowARelInd = 2;
                } else { rowARelInd--; }

            }
        }
        updateCellMatrix(cellMatrixNew);
        genCount++;
        return score;
    }

    private String makeNeighborhood(int rowARelInd, String rowChunkA, String rowChunkB, String rowChunkC) {
        String neighborhood = "";
        switch (rowARelInd) {
            case 0:
                neighborhood = rowChunkA + rowChunkB + rowChunkC;
                break;
            case 1:
                neighborhood = rowChunkC + rowChunkA + rowChunkB;
                break;
            case 2:
                neighborhood = rowChunkB + rowChunkC + rowChunkA;
                break;
            default:
                System.out.println("It's dead in the neighborhood, Jim.");
        }
        return neighborhood;
    }

    private boolean isAliveNextGen(String neighborhood) {
        boolean status = liveDieTable.get(neighborhood);
        return status;
    }

    private double scoreNeighborhood(String neighborhood, int curRow, int curCol) {
        if (neighborhood.equals("000000000")) {
            return 0;
        }
        String neighborhoodAndLocation = neighborhood + "r" + String.valueOf(curRow) + "c" + String.valueOf(curCol);

        if (scoreMap.containsKey(neighborhood)) {
            if (scoreMap.containsKey(neighborhoodAndLocation)) {
                return 250;
            } else {
                scoreMap.put(neighborhoodAndLocation, 1);
                return 500;
            }
        } else {
            scoreMap.put(neighborhood, 1);
            scoreMap.put(neighborhoodAndLocation, 1);
            return 0;
        }
    }

    private String copyChunk(int rowIndex, int leftColBound, int rightColBound) {
        String chunk = "";
        for (int i = leftColBound; i < rightColBound; i++) {
            if (cellMatrix[rowIndex][i]) {
                chunk += "1";
            } else {
                chunk += "0";
            }
        }
        return chunk;
    }

    private void updateCellMatrix(boolean[][] cellMatrixNew) {
        for(int i=0; i < gridHeight; i++)
            for(int j=0; j < gridWidth; j++)
                cellMatrix[i][j]=cellMatrixNew[i][j];
    }

    //Add all cases of cells status and their neighbors into liveDieTable
    private void initializeLiveDieTable(int curCellIndex, String neighborhood) {
        if (curCellIndex > 8) {
            addNeighborhoodToTable(neighborhood, evalNeighborhoodType(neighborhood));
            //System.out.println("n="+neighborhood+", eval="+evalNeighborhoodType(neighborhood));
        }
        else {
            String neighborhood0 = neighborhood + "0";
            String neighborhood1 = neighborhood + "1";
            initializeLiveDieTable(curCellIndex + 1, neighborhood0);
            //System.out.println("n0="+neighborhood0);
            initializeLiveDieTable(curCellIndex + 1, neighborhood1);
            //System.out.println("n1="+neighborhood1);
        }
    }

    private void addNeighborhoodToTable(String neighborhood, boolean resultingStatus) {
        this.liveDieTable.put(neighborhood, evalNeighborhoodType(neighborhood));
    }

    /**
     * Check the status of the cell according to its neighbors
     * the 4th character is the status of the current cell
     * others are statuses of its neighbors
     * 1. if there are exactly 3 neighbors alive of this cell, then the cell is alive
     * 2. if there are exactly 2 neighbors alive of this cell and the cell is alive, then the cell is alive
     * 3. others are dead
     * @param neighborhood
     * @return
     */

    public boolean evalNeighborhoodType(String neighborhood) {
        int neighborCount = 0;
        boolean curAlive = false;
        char alive = '1';

        if (neighborhood.charAt(4) == alive) {
            curAlive = true;
        }

        for (int i = 0; i < 9; i++) {
            if (neighborhood.charAt(i) == alive) {
                if (i != 4) {
                    neighborCount++;
                }
            }
        }

        if (neighborCount == 3) {
            return true;
        } else {
            if (curAlive && neighborCount == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Stop conditions
     * 1. There is no live cell in the matrix
     * 2. The generation number is larger than the maximum generation number
     * 3. The current status of individual is the same as the last status
     */

    public int stopCheck(boolean[][] oldMatrix,boolean[][] newMatrix){

        if(getGenCount()>MAX_GENERATION_COUNT) return 2;

        if(countLiveCell()==0) return 1;

        if(equalCheck(oldMatrix,newMatrix)){
            return 3;
        }

        return -1;
    }

    private boolean equalCheck(boolean[][] matrix1,boolean[][] matrix2){

        for(int row=1;row<matrix1.length-1;row++){
            for(int col=1;col<matrix1[0].length-1;col++){
                if(matrix1[row][col]!=matrix2[row][col]){
                    return false;
                }
            }
        }

        return true;
    }

    private int countLiveCell(){
        int countLive = 0;
        for(int row=1;row<gridHeight-1;row++){
            for(int col=1;col<gridWidth-1;col++){
                if(cellMatrix[row][col]){
                    countLive++;
                }
            }
        }
        return countLive;
    }

}