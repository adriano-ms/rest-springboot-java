package br.com.adrianoms.math;

public class SimpleMath {

	public SimpleMath() {
		super();
	}
	
	public Double sum(Double numberOne, Double numberTwo) {
		return numberOne + numberTwo;
	}
	
	public Double subtraction(Double numberOne,Double numberTwo) {
		return numberOne - numberTwo;
	}

	public Double multiplication(Double numberOne, Double numberTwo) {
		return numberOne * numberTwo;
	}
	
	public Double division(Double numberOne, Double numberTwo) {
		return numberOne / numberTwo;
	}

	public Double average(Double numberOne, Double numberTwo) {
		return (numberOne + numberTwo) / 2;
	}
	
	public Double power(Double numberOne, Double numberTwo) {
		return Math.pow(numberOne, numberTwo);
	}
	
	public Double root(Double numberOne, Double numberTwo) {
		return Math.pow(numberOne,(1/numberTwo));
	}
}
