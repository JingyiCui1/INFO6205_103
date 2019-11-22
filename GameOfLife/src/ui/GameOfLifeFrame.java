package ui;

import model.CellGrid;
import model.Configuration;
import model.EvolutionaryAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class GameOfLifeFrame extends JFrame {

    private JButton startGameBtn = new JButton("Start Game");
    private JTextField generationText = new JTextField();

    /**
     * flag to note whether game is started
     */
    private boolean isStart = false;

    /**
     * stop flag
     */
    private boolean stop = false;

    private final int MAXIMUM_GENARATION = 1000;


    private JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
    private JPanel gridPanel = new JPanel();

    private JPanel[][] pnMatrix;

    private Configuration bestPattern;
    private EvolutionaryAgent myAgent;

    private HashMap<String,Integer> scoreMap;



    public GameOfLifeFrame() {
        setTitle("Game Of Life");
        startGameBtn.addActionListener(new StartGameActioner());
        buttonPanel.add(startGameBtn);
        buttonPanel.setBackground(Color.WHITE);
        generationText.setHorizontalAlignment(JTextField.CENTER);
        buttonPanel.add(generationText);
        getContentPane().add("North", buttonPanel);


        myAgent = new EvolutionaryAgent(5);
        bestPattern = myAgent.evolvePattern();


        initGridLayout();
        showMatrix();
        gridPanel.updateUI();
        generationText.setText("Total generation: 0");
        this.setSize(1000, 1200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void showMatrix() {

        boolean[][] matrix = bestPattern.getConfigMatrix();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x]) {
                    pnMatrix[y][x].setBackground(Color.PINK);
                } else {
                    pnMatrix[y][x].setBackground(Color.WHITE);
                    pnMatrix[y][x].setBorder(BorderFactory.createLineBorder(Color.PINK));
                }
            }
        }
    }

    /**
     * Show grid layout
     */
    private void initGridLayout() {

        int rows = bestPattern.getMatrixHeight();
        int cols = bestPattern.getMatrixWidth();
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows, cols));
        pnMatrix = new JPanel[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JPanel panel = new JPanel();
                pnMatrix[y][x] = panel;
                gridPanel.add(panel);
            }
        }
        add("Center", gridPanel);
    }


    private class StartGameActioner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isStart) {

                new Thread(new GameControlTask()).start();
                isStart = true;
                stop = false;
                startGameBtn.setText("Stop Game");
            } else {
                stop = true;
                isStart = false;
                startGameBtn.setText("Start Game");
            }
        }
    }

    private class GameControlTask implements Runnable {

        @Override
        public void run() {

            int totalScore = 0;
            myAgent.myGrid.setStartingConfiguration(bestPattern);

            while(!stop){
                scoreMap = new HashMap<>();
                boolean[][] oldMatrix = myAgent.myGrid.getCellMatrix();
                boolean[][] temp=new boolean[18][18];
                for(int i=0;i<oldMatrix.length;i++) {
                    for (int j = 0; j < oldMatrix.length; j++) {
                          temp[i][j]=oldMatrix[i][j];
                    }
                }


              /* System.out.println("Matrix1");
                for(int i=0;i<oldMatrix.length;i++){
                    for(int j=0;j<oldMatrix.length;j++){
                        if(oldMatrix[i][j]){
                            System.out.print("1 ");
                        }else{
                            System.out.print("0 ");
                        }

                    }
                    System.out.println();
                }*/
                totalScore += myAgent.myGrid.nextGen();
                boolean[][] newMatrix= myAgent.myGrid.getCellMatrix();
              /* System.out.println("Matrix2");
                for(int i=0;i<newMatrix.length;i++){
                    for(int j=0;j<newMatrix.length;j++){
                        if(newMatrix[i][j]){
                            System.out.print("1 ");
                        }else{
                            System.out.print("0 ");
                        }

                    }
                    System.out.println();
                }*/


               int stopStatus = myAgent.myGrid.stopCheck(temp,newMatrix);
               if(stopStatus==1){
                    stop=true;
                    System.out.println("There is no cell any more. Game Over!");
                    break;
                }else if(stopStatus==2){
                    stop=true;
                    System.out.println("The generation number is larger than the maximum number. Game Over!");
                    break;
                }else if(stopStatus==3){
                    stop=true;
                    System.out.println("The status is the same as the previous one. Game Over!");
                    break;
                }

                generationText.setText("Total generations: "+myAgent.myGrid.getGenCount());
                showMatrix2();


               try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }


           /* while (!stop) {
                if((cellMatrix.getTotalGeneration()+1)==MAXIMUM_GENARATION){
                    stop=true;
                }
                cellMatrix.transform();
                generationText.setText("Total generations: "+cellMatrix.getTotalGeneration());
                showMatrix();

               try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }*/

        }
    }

    public void showMatrix2(){
        boolean[][] matrix = myAgent.myGrid.getCellMatrix();
        for (int row = 1; row < matrix.length -1; row++) {
            for (int col = 1; col < matrix.length-1; col++) {
                if (matrix[row][col]) {
                    pnMatrix[row-1][col-1].setBackground(Color.PINK);
                } else {
                    pnMatrix[row-1][col-1].setBackground(Color.WHITE);
                    pnMatrix[row-1][col-1].setBorder(BorderFactory.createLineBorder(Color.PINK));
                }
            }
        }
    }

}
