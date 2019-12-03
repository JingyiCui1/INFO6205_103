import model.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;
import java.util.Random;

/** 
* Configuration Tester. 
* 
* @author <Authors name> 
* @since <pre>11月 24, 2019</pre> 
* @version 1.0 
*/ 
public class ConfigurationTest {

    Configuration matrix1;

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


    @Before
    public void init(){
        boolean[][] m = new boolean[20][20];
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

        matrix1 = new Configuration(m);
    }


/** 
* 
* Method: mutation(int mutationChance) 
* 
*/ 
     @Test
     public void testMutation() throws Exception {
         boolean status;
         int randInt;
         Random randGen = new Random();

         for (int row = 0; row < matrix1.getMatrixHeight(); row++) {
             for (int col = 0; col < matrix1.getMatrixWidth(); col++) {
                 randInt = randGen.nextInt(100);
                 if (randInt < 10) {

                     Assert.assertTrue(randInt<10);
                     status = randGen.nextBoolean();
                     matrix1.getConfigMatrix()[row][col] = status;
                     Assert.assertEquals(matrix1.getConfigMatrix()[row][col],status);
                 }
             }
         }

     }

/** 
* 
* Method: deepCopy(Configuration copyFrom) 
* 
*/ 
     @Test
     public void testDeepCopy() throws Exception {
         String str2  = "10111000101100000100" +
                        "00000001000000000000" +
                        "01000001000001000110" +
                        "00000000000000000000" +
                        "00010001000010000010" +
                        "11100010101010010000" +
                        "10101000000000000000" +
                        "00000000011100110000" +
                        "11111110000000101010" +
                        "10001010000000010000" +
                        "00000011001100010000" +
                        "10010010000010000000" +
                        "00000010111000010001" +
                        "11100000000000000000" +
                        "00000000010010010101" +
                        "11000101000101000000" +
                        "10000000010000010000" +
                        "00001000001000000100" +
                        "01101000000000000000" +
                        "10111100000000000000";

         boolean[][] temp = new boolean[20][20];
         int index = 0;
         for(int i=0;i<temp.length;i++){
             for(int j=0;j<temp[0].length;j++){
                 if(str2.charAt(index)==0){
                     temp[i][j]=false;
                 }else if(str2.charAt(index)==1){
                     temp[i][j]=true;
                 }
                 index++;
             }
         }
         Configuration copyFrom = new Configuration(temp);
         matrix1.deepCopy(copyFrom);
         for(int i=0;i<20;i++){
             for(int j=0;j<20;j++){
                 Assert.assertTrue(matrix1.getConfigMatrix()[i][j]==copyFrom.getConfigMatrix()[i][j]);

             }
         }

     }



} 
