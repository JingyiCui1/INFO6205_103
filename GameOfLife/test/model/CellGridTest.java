import javafx.scene.control.Cell;
import model.CellGrid;
import model.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

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
        for(int i=0;i<m.length;i++){
            for(int j=0;j<m[0].length;j++){
                if(str.charAt(i*2+j)=='0'){
                    m[i][j]=false;
                }else if(str.charAt(i*2+j)=='1'){
                    m[i][j]=true;
                }
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

    }

    /**
    *
    * Method: nextGen()
    *
    */
    @Test
    public void testNextGen() throws Exception {
    //TODO: Test goes here...
    }

    /**
    *
    * Method: evalNeighborhoodType(String neighborhood)
    *
    */
    @Test
    public void testEvalNeighborhoodType() throws Exception {
    //TODO: Test goes here...
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
* Method: makeNeighborhood(int rowARelInd, String rowChunkA, String rowChunkB, String rowChunkC)
*
*/
@Test
public void testMakeNeighborhood() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = CellGrid.getClass().getMethod("makeNeighborhood", int.class, String.class, String.class, String.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
}

/**
*
* Method: isAliveNextGen(String neighborhood)
*
*/
@Test
public void testIsAliveNextGen() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = CellGrid.getClass().getMethod("isAliveNextGen", String.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
}

/**
*
* Method: scoreNeighborhood(String neighborhood, int curRow, int curCol)
*
*/
@Test
public void testScoreNeighborhood() throws Exception {
//TODO: Test goes here...
/* 
try { 
   Method method = CellGrid.getClass().getMethod("scoreNeighborhood", String.class, int.class, int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: copyChunk(int rowIndex, int leftColBound, int rightColBound) 
* 
*/ 
@Test
public void testCopyChunk() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CellGrid.getClass().getMethod("copyChunk", int.class, int.class, int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: updateCellMatrix(boolean[][] cellMatrixNew) 
* 
*/ 
@Test
public void testUpdateCellMatrix() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CellGrid.getClass().getMethod("updateCellMatrix", boolean[][].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: initializeLiveDieTable(int curCellIndex, String neighborhood) 
* 
*/ 
@Test
public void testInitializeLiveDieTable() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CellGrid.getClass().getMethod("initializeLiveDieTable", int.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: addNeighborhoodToTable(String neighborhood, boolean resultingStatus) 
* 
*/ 
@Test
public void testAddNeighborhoodToTable() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CellGrid.getClass().getMethod("addNeighborhoodToTable", String.class, boolean.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: equalCheck(boolean[][] matrix1, boolean[][] matrix2) 
* 
*/ 
@Test
public void testEqualCheck() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CellGrid.getClass().getMethod("equalCheck", boolean[][].class, boolean[][].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: countLiveCell() 
* 
*/ 
@Test
public void testCountLiveCell() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = CellGrid.getClass().getMethod("countLiveCell"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
