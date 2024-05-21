package br.com.adrianoms;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.adrianoms.exceptions.UnsupportedMathOperationException;

@RestController
public class MathController {

	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/sum/{numberOne}/{numberTwo}")
	public Double sum(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception 
	{
		if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please, set a numeric value!");
		}
		return convertToDouble(numberOne) + convertToDouble(numberTwo);
	}
	
	@GetMapping("/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception 
	{
		if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please, set a numeric value!");
		}
		return convertToDouble(numberOne) - convertToDouble(numberTwo);
	}
	
	@GetMapping("/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception 
	{
		if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please, set a numeric value!");
		}
		return convertToDouble(numberOne) * convertToDouble(numberTwo);
	}
	
	@GetMapping("/division/{numberOne}/{numberTwo}")
	public Double division(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception 
	{
		if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please, set a numeric value!");
		}
		return convertToDouble(numberOne) / convertToDouble(numberTwo);
	}

	@GetMapping("/average/{numberOne}/{numberTwo}")
	public Double average(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception 
	{
		if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please, set a numeric value!");
		}
		return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
	}
	
	@GetMapping("/squareroot/{number}")
	public Double squareroot(
			@PathVariable(value = "number") String number) throws Exception 
	{
		if(!isNumeric(number)) {
			throw new UnsupportedMathOperationException("Please, set a numeric value!");
		}
		return Math.sqrt(convertToDouble(number));
	}

	private Double convertToDouble(String strNumber) {
		if(strNumber == null) {
			return 0D;
		}
		String number = strNumber.replaceAll(",", ".");
		if(isNumeric(number)) {
			return Double.parseDouble(number);
		}
		return 0D;
	}

	private boolean isNumeric(String strNumber) {
		if(strNumber == null) {
			return false;
		}
		String number = strNumber.replaceAll(",", ".");
		return number.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
}
