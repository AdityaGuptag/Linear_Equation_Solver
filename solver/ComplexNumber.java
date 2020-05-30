package solver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexNumber {
    private double realPart = 0.0;
    private double imaginaryPart = 0.0;
    private char sign = ' ';
    private final String regexPattern = "^([-|+]?[0-9.]*)([+|-][0-9.]*)?([i])?$";

    public ComplexNumber(String complexNumber) {
        Pattern pattern = Pattern.compile(this.regexPattern);
        Matcher matcher = pattern.matcher(complexNumber);
        if (matcher.find()) {
            if (matcher.group(2) == null) {
                if (matcher.group(3) == null) {
                    this.realPart = Double.parseDouble(matcher.group(1));
                } else if ("".equals(matcher.group(1))) {
                    this.imaginaryPart = 1.0;
                } else if ("-".equals(matcher.group(1))) {
                    this.imaginaryPart = -1.0;
                } else {
                    this.imaginaryPart = Double.parseDouble(matcher.group(1));
                }
            } else {
                this.realPart = Double.parseDouble(matcher.group(1));
                if ("+".equals(matcher.group(2))) {
                    this.imaginaryPart = 1.0;
                } else if ("-".equals(matcher.group(2))) {
                    this.imaginaryPart = -1.0;
                } else {
                    this.imaginaryPart = Double.parseDouble(matcher.group(2));
                }
            }
            if (this.imaginaryPart > 0) {
                this.sign = '+';
            } else if (this.imaginaryPart < 0) {
                this.sign = '-';
            } else {
                this.sign = ' ';
            }
        }
    }

    public ComplexNumber(double realPart, double imaginaryPart, char sign) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
        this.sign = sign;
    }

    public boolean checkIfZero() {
        return this.realPart == 0.0 && this.imaginaryPart == 0.0;
    }

    public double getRealPart() {
        return realPart;
    }

    public void setRealPart(double realPart) {
        this.realPart = realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }

    public ComplexNumber productWithConst(double constant) {
        ComplexNumber tempNum = new ComplexNumber(this.realPart, this.imaginaryPart, this.sign);
        tempNum.setRealPart(constant * this.realPart);
        tempNum.setImaginaryPart(constant * this.imaginaryPart);
        if (tempNum.getImaginaryPart() > 0) {
            tempNum.setSign('+');
        } else if (tempNum.getImaginaryPart() < 0) {
            tempNum.setSign('-');
        } else {
            tempNum.setSign(' ');
        }
        return tempNum;
    }

    public ComplexNumber divideByComplexNum(ComplexNumber other) {
        ComplexNumber tempNum = this.prodWithComplexNum(new ComplexNumber(other.getRealPart(), -1 * other.getImaginaryPart(), other.getSign()));
        double denominator = Math.pow(other.getRealPart(), 2) + Math.pow(other.getImaginaryPart(), 2);
        tempNum.setRealPart(tempNum.getRealPart() / denominator);
        tempNum.setImaginaryPart(tempNum.getImaginaryPart() / denominator);
        if (tempNum.getImaginaryPart() > 0) {
            tempNum.setSign('+');
        } else if (tempNum.getImaginaryPart() < 0) {
            tempNum.setSign('-');
        } else {
            tempNum.setSign(' ');
        }
        return tempNum;
    }

    public void addToComplexNum(ComplexNumber other) {
        this.realPart = this.realPart + other.getRealPart();
        this.imaginaryPart = this.imaginaryPart + other.getImaginaryPart();
        if (this.imaginaryPart > 0) {
            this.sign = '+';
        } else if (this.imaginaryPart < 0) {
            this.sign = '-';
        } else {
            this.sign = ' ';
        }
    }

    public ComplexNumber prodWithComplexNum(ComplexNumber other) {
        ComplexNumber tempNum = new ComplexNumber(this.realPart, this.imaginaryPart, this.sign);
        tempNum.setRealPart(this.realPart * other.getRealPart() +
                -1 * this.imaginaryPart * other.getImaginaryPart());
        tempNum.setImaginaryPart(this.imaginaryPart * other.getRealPart() +
                this.realPart * other.getImaginaryPart());
        if (tempNum.getImaginaryPart() > 0) {
            tempNum.setSign('+');
        } else if (tempNum.getImaginaryPart() < 0) {
            tempNum.setSign('-');
        } else {
            tempNum.setSign(' ');
        }
        return tempNum;
    }

    @Override
    public String toString() {
        if (this.realPart == 0.0) {
            return Double.toString(this.imaginaryPart) + 'i';
        } else if (this.imaginaryPart == 0.0 || this.sign == ' ' ) {
            return Double.toString(this.realPart);
        } else if (this.getSign() == '-') {
            return Double.toString(this.realPart) + this.imaginaryPart + 'i';
        }  else {
            return Double.toString(this.realPart) + this.sign + this.imaginaryPart + 'i';
        }
    }

    public boolean equals(ComplexNumber other) {
        return this.realPart == other.getRealPart() && this.imaginaryPart == other.getImaginaryPart();
    }
}
