package Lab4_5;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private JFileChooser fileChooser = null;

	private JCheckBoxMenuItem showAxisMenuItem;
	private JCheckBoxMenuItem showMarkersMenuItem;

	public GraphicsDisplay display = new GraphicsDisplay();

	private boolean fileLoaded = false;
	public MainFrame() {

		super("Построение графиков функций");

		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		
		setLocation((kit.getScreenSize().width - WIDTH)/2,
				(kit.getScreenSize().height - HEIGHT)/2);

		//setExtendedState(MAXIMIZED_BOTH);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Файл");
		menuBar.add(fileMenu);

		Action openGraphicsAction = new AbstractAction("Открыть файл") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				if (fileChooser==null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					
					openGraphics(fileChooser.getSelectedFile());
				}
			}
		};

		fileMenu.add(openGraphicsAction);

		JMenu graphicsMenu = new JMenu("График");
		menuBar.add(graphicsMenu);

		Action showAxisAction = new AbstractAction("Показывать оси координат") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				display.setShowAxis(showAxisMenuItem.isSelected());
			}
		};
		showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
		graphicsMenu.add(showAxisMenuItem);
		showAxisMenuItem.setSelected(true);

		Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				display.setShowMarkers(showMarkersMenuItem.isSelected());
			}
		};
		showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
		graphicsMenu.add(showMarkersMenuItem);
		showMarkersMenuItem.setSelected(true);

		graphicsMenu.addMenuListener(new GraphicsMenuListener());
		getContentPane().add(display, BorderLayout.CENTER);
	}

	protected void openGraphics(File selectedFile) {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
			ArrayList<Double[]> graphicsData = new ArrayList<Double[]>(50);

            while(in.available() > 0) {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData.add(new Double[]{x, y});
            }

			if (graphicsData.size() > 0) {
				fileLoaded = true;
				this.display.displayGraphics(graphicsData);
			}
			in.close();
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, 
					"Файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
			return;
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, 
					"Ошибка чтения координат точек из файла", "Ошибка загрузки данных",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	
	public static void main(String[] args) throws IOException{
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private class GraphicsMenuListener implements MenuListener {
		public void menuSelected(MenuEvent e) {
			showAxisMenuItem.setEnabled(fileLoaded);
			showMarkersMenuItem.setEnabled(fileLoaded);
		}

		public void menuDeselected(MenuEvent e) {
		}
		
		public void menuCanceled(MenuEvent e) {
		}
	}
}
