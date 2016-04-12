package devices;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.Block;
import core.BlockChain;

public class table extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1780474438396658030L;
	JTable table;

	public table() {
		Thread t = new Thread() {
			public void run() {
				while(true){
					for(int i = BlockChain.MainChain.size()-1;i>=0;i--){
						Block b = BlockChain.MainChain.get(i);
						setLayout(new FlowLayout());

						String[] columnNames = {"type","value" };

						String[][] data = b.valuesArr();

						table = new JTable(data, columnNames);
						table.setPreferredScrollableViewportSize(new Dimension(500,70));
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
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();

	}

}


