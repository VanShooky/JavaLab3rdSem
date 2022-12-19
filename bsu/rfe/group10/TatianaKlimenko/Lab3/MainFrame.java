package Lab3;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;

	private JTextField textFieldFrom;
	private JTextField textFieldTo;
	private JTextField textFieldStep;

	private Box hBoxResult;

	private GornerTableCellRenderer renderer = new GornerTableCellRenderer();

	private GornerTableModel data;

	public MainFrame(Double[] coefficients) {
		super("Табулирование многочлена");

		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu propertiesMenu = new JMenu("Свойства");
		menuBar.add(propertiesMenu);
		Action aboutProgram = new AbstractAction("О программе") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ev) {
				JOptionPane.showOptionDialog(MainFrame.this, "Выполнила:\nКлименко Татьяна\n10 группа\n", "О программе",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			}
		};
		propertiesMenu.add(aboutProgram);
		
		JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
		textFieldFrom = new JTextField("0.0", 10);
		textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());

		JLabel labelForTo = new JLabel("до:");
		textFieldTo = new JTextField("0.0", 10);
		textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());

		JLabel labelForStep = new JLabel("с шагом:");
		textFieldStep = new JTextField("0.1", 10);
		textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
		
		Box hboxRange = Box.createHorizontalBox();

		hboxRange.setBorder(BorderFactory.createBevelBorder(1));
		hboxRange.add(Box.createHorizontalGlue());
		hboxRange.add(labelForFrom);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldFrom);
		hboxRange.add(Box.createHorizontalStrut(20));

		hboxRange.add(labelForTo);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldTo);
		hboxRange.add(Box.createHorizontalStrut(20));

		hboxRange.add(labelForStep);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldStep);
		hboxRange.add(Box.createHorizontalGlue());

		hboxRange.setPreferredSize(new Dimension((int)hboxRange.getMaximumSize().getWidth(),
				(int)hboxRange.getMinimumSize().getHeight() * 2));
		getContentPane().add(hboxRange, BorderLayout.NORTH);

		JButton buttonCalc = new JButton("Вычислить");
		
		buttonCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					Double from = Double.parseDouble(textFieldFrom.getText());
					Double to = Double.parseDouble(textFieldTo.getText());
					Double step = Double.parseDouble(textFieldStep.getText());
					data = new GornerTableModel(from, to, step, coefficients);
					
					JTable table = new JTable(data);
					table.setDefaultRenderer(Double.class, renderer);
					table.setRowHeight(30);
					hBoxResult.removeAll();

					hBoxResult.add(new JScrollPane(table));
					getContentPane().validate();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой",
							"Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		JButton buttonReset = new JButton("Очистить поля");
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				textFieldFrom.setText("0.0");
				textFieldTo.setText("0.0");
				textFieldStep.setText("0.1");

				hBoxResult.removeAll();
				hBoxResult.add(new JPanel());
				getContentPane().validate();
			}
		});

		Box hboxButtons = Box.createHorizontalBox();
		hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
		hboxButtons.add(Box.createHorizontalGlue());
		hboxButtons.add(buttonCalc);
		hboxButtons.add(Box.createHorizontalStrut(30));
		hboxButtons.add(buttonReset);
		hboxButtons.add(Box.createHorizontalGlue());

		hboxButtons.setPreferredSize(new Dimension((int) hboxButtons.getMaximumSize().getWidth(),
				(int) hboxButtons.getMinimumSize().getHeight() * 2));
		getContentPane().add(hboxButtons, BorderLayout.SOUTH);

		hBoxResult = Box.createHorizontalBox();
		hBoxResult.add(new JPanel());
		getContentPane().add(hBoxResult, BorderLayout.CENTER);

	}

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Коэффициенты не заданы");
			System.exit(-1);
		}

		Double[] coefficients = new Double[args.length];
		int i = 0;
		try {
			for (String arg : args) {
				coefficients[i++] = Double.parseDouble(arg);
			}
		} catch (NumberFormatException ex) {
			System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
			System.exit(-2);
		}
		
		MainFrame frame = new MainFrame(coefficients);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}