package model;

import org.apache.log4j.Logger;

import java.util.HashMap;


public class CellGrid{

    private int gridHeight;
    private int gridWidth;
    private boolean[][] cellMatrix;
    private HashMap<String, Boolean> liveDieTable;
    private int genCount = 0;
    private final int MAX_GENERATION_COUNT = 1000;
    private static final Logger LOGGER = Logger.getLogger(CellGrid.class);


    public CellGrid(int gridHeight, int gridWidth) {
        this.gridHeight = gridHeight + 2;
        this.gridWidth = gridWidth + 2;


        cellMatrix = new boolean[this.gridHeight][this.gridWidth];
        liveDieTable = new HashMap<String, Boolean>();
        String tempNeighborhood = "";
        initializeLiveDieTable(0, tempNeighborhood);
    }


    public CellGrid() {
        cellMatrix = new boolean[22][22];
        liveDieTable = new HashMap<String, Boolean>();
        String tempNeighborhood = "";
        initializeLiveDieTable(0, tempNeighborhood);
    }

    public CellGrid(boolean[][] matrix) {
        this.gridHeight = matrix.length;
        this.gridWidth = matrix.length;
        cellMatrix = matrix;
        liveDieTable = new HashMap<String, Boolean>();
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


    /**
     * Set the initial status of cellMatrix given individualMatrix
     * @param initialIndividual
     */
    public void setStartingIndividual(Individual initialIndividual) {
        genCount=0;
        for (int row = 1; row < gridHeight - 1; row++) {
            for (int col = 1; col < gridWidth - 1; col++) {
                cellMatrix[row][col] = initialIndividual.getCell(row-1, col-1);
            }
        }
    }


    public int runGame(int numGenerations) {

        boolean[][] old = this.cellMatrix;


        for (int gen = 0; gen < numGenerations; gen++) {
            boolean[][] newMatrix = nextGen();
            int stopStatus = this.stopCheck(old,newMatrix);
            if(stopStatus == 1 || stopStatus == 2 || stopStatus == 3 || stopStatus == 4) {
                break;
            }
            updateCellMatrix(newMatrix);
            genCount++;
        }
        return genCount;
    }


    /**
     * Produce the next generation according to liveDieTable
     * and return new cell matrix
     * @return
     */
    public boolean[][] nextGen() {
        boolean[][] cellMatrixNew = new boolean[this.gridHeight][this.gridWidth];
        String rowChunkA = "";
        String rowChunkB = "";
        String rowChunkC = "";
        String neighborhood;
        int rowARelInd;

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
                if (rowARelInd == 0) {
                    rowARelInd = 2;
                } else { rowARelInd--; }

            }
        }
        return cellMatrixNew;
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

    public boolean isAliveNextGen(String neighborhood) {
        boolean status = liveDieTable.get(neighborhood);
        return status;
    }

    public String copyChunk(int rowIndex, int leftColBound, int rightColBound) {
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

    public void updateCellMatrix(boolean[][] cellMatrixNew) {
        for(int i=0; i < gridHeight; i++)
            for(int j=0; j < gridWidth; j++)
                cellMatrix[i][j]=cellMatrixNew[i][j];


    }

    //Add all cases of cells status and their neighbors into liveDieTable
    private void initializeLiveDieTable(int curCellIndex, String neighborhood) {
        if (curCellIndex > 8) {
            addNeighborhoodToTable(neighborhood, evalNeighborhoodType(neighborhood));
        }
        else {
            String neighborhood0 = neighborhood + "0";
            String neighborhood1 = neighborhood + "1";
            initializeLiveDieTable(curCellIndex + 1, neighborhood0);
            initializeLiveDieTable(curCellIndex + 1, neighborhood1);
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

        if(this.genCount>MAX_GENERATION_COUNT) return 2;

        if(countLiveCell()==0) return 1;

        if(equalCheck(oldMatrix,newMatrix)){
            return 3;
        }

        if(cycleCheck()){
            return 4;
        }

        return -1;
    }

    public boolean equalCheck(boolean[][] matrix1,boolean[][] matrix2){

        for(int row=1;row<matrix1.length-1;row++){
            for(int col=1;col<matrix1[0].length-1;col++){
                if(matrix1[row][col]!=matrix2[row][col]){
                    return false;
                }
            }
        }

        return true;
    }

    public boolean cycleCheck(){

        boolean[][] newMatrix = this.nextGen();
        CellGrid c = new CellGrid(newMatrix);
        boolean[][] cMatrix = c.nextGen();
        boolean b1 = equalCheck(cellMatrix,cMatrix);
        if(b1) return true;

        return false;
    }




    private int countLiveCell(){
        int countLive = 0;
        for(int row=0;row<gridHeight-1;row++){
            for(int col=0;col<gridWidth-1;col++){
                if(cellMatrix[row][col]){
                    countLive++;
                }
            }
        }
        return countLive;
    }

    public void printMatrix(boolean[][] matrix){

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        if(matrix.length==20){
            for(int i=0;i<matrix.length;i++){
                for(int j=0;j<matrix[0].length;j++){
                    if(matrix[i][j]){
                        sb.append(1);
                    }else{
                        sb.append(0);
                    }

                }
                sb.append("\n");
            }
        }else{

            for(int i=1;i<matrix.length-1;i++){
                for(int j=1;j<matrix[0].length-1;j++){
                    if(matrix[i][j]){
                        sb.append(1);
                    }else{
                        sb.append(0);
                    }

                }
                sb.append("\n");
            }
        }

        LOGGER.info(sb.toString());
    }

}