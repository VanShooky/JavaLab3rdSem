package Lab3;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;

	private Double from;
	private Double to;
	private Double step;
	private Double[] coefficients;
	
	public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.coefficients = coefficients;
	}
	
	public Double getFrom() {
		return from;
	}
	
	public Double getTo() {
		return to;
	}
	
	public Double getStep() {
		return step;
	}
	
	public int getColumnCount() {
		return 3;
	}
	
	public int getRowCount() {
		return (int) Math.ceil((to - from) / step) + 1;
	}
	
    public boolean isTwoPairs(Double number) {
    	String numberAsString = Double.toString(number);
    	char first_character = numberAsString.charAt(0);
    	char last_character = numberAsString.charAt(numberAsString.length()-1);
        if(first_character != (last_character))
            return false;
        return true;
    }
	
	public Object getValueAt(int row, int col) {
		double x = from + step * row;
		double polynomial_value = 0.0;
		if(col == 0) {
			return x;
		} else{
			double prev = coefficients[coefficients.length - 1];
			polynomial_value += prev;
			for(int i = coefficients.length - 2; i >= 0; i--) {
				prev = prev * x + coefficients[i];
				polynomial_value += prev;
			}
			polynomial_value = Math.ceil(polynomial_value * 1000) / 1000;
			if(col == 1)
			return polynomial_value;
			else return isTwoPairs(polynomial_value);
		}
	}
	
	public String getColumnName(int col) {
		switch (col) {
		case 0:
		return "Значение X";
		case 1:
			return "Значение многочлена";
		default:
		return "Краевая симметрия";
		}
	}
	
    public Class<?> getColumnClass(int col) {
    	if(col == 2) return Boolean.class;
    	return Double.class;
    }
}
