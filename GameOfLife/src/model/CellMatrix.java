package model;

import java.util.Arrays;

public class CellMatrix {

    //Height of Matrix
    private int height;

    //Width of Matrix
    private int width;

    //Number of generations
    private int totalGeneration=0;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getTotalGeneration() {
        return totalGeneration;
    }

    //Status of cells, 1 for live, 0 for dead
    private int[][] matrix;

    public CellMatrix(int height, int width, int[][] matrix) {
        this.height = height;
        this.width = width;
        this.matrix = matrix;
    }

    /**
     * Move to a new status/generation
     * Rules:
     * 1. Each "dead" cell will come alive if it has exactly three neighbors
     * 2. Each "live" cell will die if it has fewer than two or more than three neighbors
     */
    public void transform(){
        int[][] nextMatrix=new int[height][width];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                nextMatrix[y][x]=0;
                int neighbors= findLifedNum(y,x);
                //There are 3 neighbors -> live
                if(neighbors==3){
                    nextMatrix[y][x]=1;
                }
                //There are 2 neighbors -> keep the status
                else if(neighbors==2){
                    nextMatrix[y][x]=matrix[y][x];
                }
            }
        }
        matrix=nextMatrix;
        totalGeneration++;
    }



    /**
     * Count live neighbors for each cell
     */

    public int findLifedNum(int y, int x){
        int num=0;
        //W
        if(x!=0){
            num+=matrix[y][x-1];
        }
        //NW
        if(x!=0&&y!=0){
            num+=matrix[y-1][x-1];
        }
        //N
        if(y!=0){
            num+=matrix[y-1][x];
        }
        //NE
        if(x!=width-1&&y!=0){
            num+=matrix[y-1][x+1];
        }
        //E
        if(x!=width-1){
            num+=matrix[y][x+1];
        }
        //SE
        if(x!=width-1&&y!=height-1){
            num+=matrix[y+1][x+1];
        }
        //S
        if(y!=height-1){
            num+=matrix[y+1][x];
        }
        //SW
        if(x!=0&&y!=height-1){
            num+=matrix[y+1][x-1];
        }
        return num;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            sb.append(Arrays.toString(matrix[i]) + "\n");
        }
        return sb.toString();
    }

}
