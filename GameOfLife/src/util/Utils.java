package util;

import model.CellMatrix;

import java.util.Random;


public class Utils {

    /**
     * Generate Random Cells
     * @return
     */
    public static CellMatrix initMatrixFromFile() {
        Random random = new Random();
        int height = 1 + random.nextInt(100);
        int width = 1 + random.nextInt(100);
        int duration = 200;
        int totalNum = 300;
        int[][] matrix = new int[height][width];


        //开始逐行初始化
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (random.nextInt(3) % 3 == 0) {
                    matrix[y][x] = 1;
                } else {
                    matrix[y][x] = 0;
                }
            }
        }
        CellMatrix cellMatrix = new CellMatrix(height, width, matrix);
        return cellMatrix;
    }
}
