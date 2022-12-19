package Lab2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import static java.lang.Math.*;


public class MainFrame extends JFrame  {
	public static final double pi = 3.1415;
	public static final double e = 2.7183;
	private static final int WIDTH = 500;
	private static final int HEIGHT = 420;	
	
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JTextField textFieldZ;
	
	private JTextField textFieldResult;
	
	private ButtonGroup radioButtons = new ButtonGroup();
	private ButtonGroup radioButtonsV = new ButtonGroup();
	
	private Box hboxFormulaType = Box.createHorizontalBox();
	Box variable = Box.createHorizontalBox();

	private int formulaId = 1;
	private int valueId = 1;
	
	Double result = 0.0;
	
	
	public Double calculate1(Double x, Double y, Double z) {
		return (sin(pi * y * y) + log10(y * y))/
				(sin(pi * z * z) + sin(x) + log10(z * z) + (x * x) + pow(e, cos(z * x)));
		}
	
	public Double calculate2(Double x, Double y, Double z){
		return (pow(cos(pow(e, y)) + pow(e, y * y)+ sqrt(1 / x), 1 / 4) / 
				pow(cos(pi * z * z * z) + pow(log10(1 + z), 2),cos(y)));		
	}
	

	
	private void addRadioButton(String buttonName, final int formulaId) {	
		JRadioButton button = new JRadioButton(buttonName);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				MainFrame.this.formulaId = formulaId;
			}
			
		});
		radioButtons.add(button);
		hboxFormulaType.add(button);
	}
	private void addRadioButtonVar(String buttonName, final int valueId) {	
		JRadioButton button = new JRadioButton(buttonName);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				MainFrame.this.valueId = valueId;
			}
			
		});
		radioButtonsV.add(button);
		variable.add(button);
	}
	
	public MainFrame() {
		super("Вычисление формулы");
		setSize(WIDTH,HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - WIDTH)/2,
				(kit.getScreenSize().height - HEIGHT)/2);
		
		hboxFormulaType.add(Box.createHorizontalGlue());
		addRadioButton("Формула 1", 1);
		addRadioButton("Формула 2", 2);
		
		radioButtons.setSelected(
		radioButtons.getElements().nextElement().getModel(), true);
		hboxFormulaType.add(Box.createHorizontalGlue());
		
		hboxFormulaType.add(Box.createHorizontalGlue());
    	
		hboxFormulaType.setBorder(
		BorderFactory.createLineBorder(Color.GRAY));
		
		JLabel labelForX = new JLabel("X:");
		textFieldX = new JTextField("0", 10);
		textFieldX.setMaximumSize(textFieldX.getPreferredSize());
		
		JLabel labelForY = new JLabel("Y:");
		textFieldY = new JTextField("0", 10);
		textFieldY.setMaximumSize(textFieldY.getPreferredSize());
		
		JLabel labelForZ = new JLabel("Z:");
		textFieldZ = new JTextField("0", 10);
		textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());
		
		Box hboxVariables = Box.createHorizontalBox();
		hboxVariables.setBorder(
		BorderFactory.createLineBorder(Color.GRAY));
		hboxVariables.add(Box.createHorizontalGlue());
		hboxVariables.add(labelForX);
		hboxVariables.add(Box.createHorizontalStrut(10));
		hboxVariables.add(textFieldX);
		
		hboxVariables.add(Box.createHorizontalStrut(10));
		hboxVariables.add(labelForY);
		hboxVariables.add(Box.createHorizontalStrut(10));
		hboxVariables.add(textFieldY);
		
		hboxVariables.add(Box.createHorizontalStrut(10));
		hboxVariables.add(labelForZ);
		hboxVariables.add(Box.createHorizontalStrut(10));
		hboxVariables.add(textFieldZ);
		hboxVariables.add(Box.createHorizontalGlue());
		
		variable.add(Box.createHorizontalGlue());
		addRadioButtonVar("Переменная 1", 1);
		addRadioButtonVar("Переменная 2", 2);
		addRadioButtonVar("Переменная 3", 3);
		
		radioButtonsV.setSelected(
		radioButtonsV.getElements().nextElement().getModel(), true);
		variable.add(Box.createHorizontalGlue());
		
		variable.add(Box.createHorizontalGlue());
    	
		variable.setBorder(
		BorderFactory.createLineBorder(Color.GRAY));
		
		JLabel labelForResult = new JLabel("Результат:");
		
		textFieldResult = new JTextField("", 20);
		textFieldResult.setMaximumSize(
		textFieldResult.getPreferredSize());
		
		Box hboxResult = Box.createHorizontalBox();
		hboxResult.add(Box.createHorizontalGlue());
		hboxResult.add(labelForResult);
		hboxResult.add(Box.createHorizontalStrut(10));
		hboxResult.add(textFieldResult);
		hboxResult.add(Box.createHorizontalGlue());
		hboxResult.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		JButton buttonCalc = new JButton("Вычислить");
		buttonCalc.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ev) {
			try {
				 Double x = Double.parseDouble(textFieldX.getText());
				 Double y = Double.parseDouble(textFieldY.getText());
				 Double z = Double.parseDouble(textFieldZ.getText());
				 if (formulaId == 1)
				result = calculate1(x, y, z);
				 else
				result = calculate2(x, y, z);
				 textFieldResult.setText(result.toString());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(MainFrame.this,
				"Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
				JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		JButton buttonReset = new JButton("MC");
		buttonReset.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ev) {
			if(valueId == 1)
				textFieldX.setText("0");
			else if(valueId == 2)
				textFieldY.setText("0");
			else
				textFieldZ.setText("0");
		textFieldResult.setText("");
		}
		});
		
		JButton buttonSum = new JButton("M+");
		buttonSum.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ev) {
			 Double x = Double.parseDouble(textFieldX.getText());
			 Double y = Double.parseDouble(textFieldY.getText());
			 Double z = Double.parseDouble(textFieldZ.getText());
			if(valueId == 1)
				result += x;
			else if(valueId == 2)
				result += y;
			else
				result += z;
			textFieldResult.setText(result.toString());
		}
		});
		
		Box hboxButtons = Box.createHorizontalBox();
		hboxButtons.add(Box.createHorizontalGlue());
		hboxButtons.add(buttonCalc);
		hboxButtons.add(Box.createHorizontalStrut(30));
		hboxButtons.add(buttonReset);
		hboxButtons.add(Box.createHorizontalStrut(30));
		hboxButtons.add(buttonSum);
		hboxButtons.add(Box.createHorizontalGlue());
		hboxButtons.setBorder(
		BorderFactory.createLineBorder(Color.GRAY));

		Box contentBox = Box.createVerticalBox();
		contentBox.add(Box.createVerticalGlue());
		contentBox.add(hboxFormulaType);
		contentBox.add(hboxVariables);
		contentBox.add(variable);
		contentBox.add(hboxResult);
		contentBox.add(hboxButtons);
		contentBox.add(Box.createVerticalGlue());
		getContentPane().add(contentBox, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		}

}