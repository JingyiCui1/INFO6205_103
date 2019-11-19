package ui;

import model.CellMatrix;
import util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private CellMatrix cellMatrix;
    private JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
    private JPanel gridPanel = new JPanel();

    private JPanel[][] pnMatrix;




    public GameOfLifeFrame() {
        setTitle("Game Of Life");
        startGameBtn.addActionListener(new StartGameActioner());
        buttonPanel.add(startGameBtn);
        buttonPanel.setBackground(Color.WHITE);
        generationText.setHorizontalAlignment(JTextField.CENTER);
        buttonPanel.add(generationText);
        getContentPane().add("North", buttonPanel);


        cellMatrix = Utils.initMatrixFromFile();
        initGridLayout();
        showMatrix();
        gridPanel.updateUI();
        generationText.setText("Total generation: 0");
        this.setSize(1000, 1200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void showMatrix() {

        int[][] matrix = cellMatrix.getMatrix();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x] == 1) {
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
        int rows = cellMatrix.getHeight();
        int cols = cellMatrix.getWidth();
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


            while (!stop) {
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
            }

        }
    }

}
