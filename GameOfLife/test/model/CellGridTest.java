import javafx.scene.control.Cell;
import model.CellGrid;
import model.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/** 
* CellGrid Tester. 
* 
* @author <Authors name> 
* @since <pre>11月 25, 2019</pre> 
* @version 1.0 
*/ 
public class CellGridTest {

    Configuration config1;
    CellGrid cellGrid;

    String str  = "10111000001100000100" +
                  "00000001000000000000" +
                  "01000001000001000110" +
                  "00000000000000000000" +
                  "00010001000010000010" +
                  "11100010101010000000" +
                  "10101000000000000000" +
                  "00000000011100110000" +
                  "11111110000000101010" +
                  "10001010000000000000" +
                  "00000011001100010000" +
                  "10010010000010000000" +
                  "00000010111000000001" +
                  "11100000000000000000" +
                  "00000000010010010101" +
                  "11010101000101000000" +
                  "10000000010000010000" +
                  "00001000001000000100" +
                  "01101000000000000000" +
                  "10111100000000000000";


    boolean[][] m;
    @Before
    public void before() throws Exception {
        m = new boolean[20][20];
        int index = 0;
        for(int i=0;i<m.length;i++){
            for(int j=0;j<m[0].length;j++){
                if(str.charAt(index)=='0'){
                    m[i][j]=false;
                }else if(str.charAt(index)=='1'){
                    m[i][j]=true;
                }
                index++;
            }
        }

        config1 = new Configuration(m);
        cellGrid = new CellGrid(20,20);
        cellGrid.setStartingConfiguration(config1);
    }



    /**
    *
    * Method: setStartingConfiguration(Configuration initialConfig)
    *
    */
    @Test
    public void testSetStartingConfiguration() throws Exception {
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                Assert.assertTrue(config1.getConfigMatrix()[i][j]==cellGrid.getCellMatrix()[i+1][j+1]);
            }
        }
    }

    /**
    *
    * Method: runGame(int numGenerations)
    *
    */
    @Test
    public void testRunGame() throws Exception {
           int generation = cellGrid.runGame(10);
           Assert.assertEquals(10,generation);

    }

    /**
    *
    * Method: nextGen()
    *
    */
    @Test
    public void testNextGen() throws Exception {
            boolean[][] matrix= cellGrid.nextGen();
            String s =
                    "00010000000000000000" +
                    "01110000000000000110" +
                    "00000000000000000000" +
                    "00000000000000000110" +
                    "01100001000100000000" +
                    "10100001000100000000" +
                    "10110000000000000000" +
                    "10000000001000110000" +
                    "11111010001000100000" +
                    "10101000000000010000" +
                    "00000011000100000000" +
                    "00000110100000000000" +
                    "10100001010000000000" +
                    "01000000101000000010" +
                    "00000000000010000000" +
                    "11000000101010101000" +
                    "11001000001000000000" +
                    "01010000000000000000" +
                    "01100000000000000000" +
                    "00101100000000000000";



            int index = 0;
            boolean temp= false;
            for(int i=1;i<matrix.length-1;i++){
               for(int j=1;j<matrix[0].length-1;j++){
                if(s.charAt(index)=='0'){
                    temp=false;
                    Assert.assertEquals(temp,matrix[i][j]);
                }else if(s.charAt(index)=='1'){
                    temp=true;
                    Assert.assertEquals(temp,matrix[i][j]);
                }
                index++;
            }
        }

    }

    /**
    *
    * Method: evalNeighborhoodType(String neighborhood)
    *
    */
    @Test
    public void testEvalNeighborhoodType() throws Exception {
          boolean eval = cellGrid.evalNeighborhoodType("101110000");
          Assert.assertEquals(true,eval);
          boolean eval2 = cellGrid.evalNeighborhoodType("111110000");
          Assert.assertEquals(false,eval2);
          boolean eval3 = cellGrid.evalNeighborhoodType("100110000");
          Assert.assertEquals(true,eval3);

    }

    /**
    *
    * Method: stopCheck(boolean[][] oldMatrix, boolean[][] newMatrix)
    *
    */
    @Test
    public void testStopCheck() throws Exception {
        Configuration same = new Configuration(m);

        boolean[][] temp = new boolean[22][22];
        for(int i=0;i<temp.length;i++){
            for(int j=0;j<temp[0].length;j++){
                temp[i][j] = cellGrid.getCellMatrix()[i][j];
            }
        }

        boolean[][] temp2 = new boolean[22][22];
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                temp2[i+1][j+1] = same.getConfigMatrix()[i][j];
            }
        }

        int result3 = cellGrid.stopCheck(temp2,temp);
        Assert.assertEquals(3,result3);



        Configuration c = new Configuration(20,20);
        CellGrid test = new CellGrid(20,20);
        test.setStartingConfiguration(c);
        boolean[][] dead = test.getCellMatrix();
        int result1 = test.stopCheck(temp,dead);
        Assert.assertEquals(1,result1);


        Configuration c2 = new Configuration(m);
        CellGrid test2 = new CellGrid(20,20);
        test2.setStartingConfiguration(c);
        test2.setGenCount(10001);
        int result2 = test2.stopCheck(temp2,temp);
        Assert.assertEquals(2,result2);

    }


    /**
    *
    * Method: isAliveNextGen(String neighborhood)
    *
    */
    @Test
    public void testIsAliveNextGen() throws Exception {
        boolean alive = cellGrid.isAliveNextGen("000010010");
        Assert.assertEquals(false,alive);

    }


    /**
    *
    * Method: copyChunk(int rowIndex, int leftColBound, int rightColBound)
    *
    */
    @Test
    public void testCopyChunk() throws Exception {
          String chunk = cellGrid.copyChunk(1,3,7);
          Assert.assertEquals("1110",chunk);
    }



} 
