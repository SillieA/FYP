package devices;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import utils.Block;
import utils.BlockChain;

public class table extends JFrame {

	JTable table;

	public table() {
		Thread t = new Thread() {
			public void run() {
				while(true){
					for(Block b : BlockChain.MainChain){
						setLayout(new FlowLayout());

						String[] columnNames = {"type","value" };

						String[][] data = b.valuesArr();

						table = new JTable(data, columnNames);
						table.setPreferredScrollableViewportSize(new Dimension(500,40));
						table.setFillsViewportHeight(true);

						JScrollPane scrollPane = new JScrollPane(table);
						add(scrollPane);
						repaint();
						getContentPane().repaint();
					}
					try {
						Thread.sleep(30000);
						getContentPane().removeAll();
						getContentPane().repaint();
						Thread.sleep(1000);
						System.out.println("table updated");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();

	}

}


