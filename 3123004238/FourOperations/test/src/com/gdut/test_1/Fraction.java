package com.gdut.test_1;

import java.math.BigInteger;

public class Fraction {
    private BigInteger numerator;
    private BigInteger denominator;

    public Fraction(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
        simplify();
    }

    public Fraction(String numeratorStr, String denominatorStr) {
        this.numerator = new BigInteger(numeratorStr.trim());
        this.denominator = new BigInteger(denominatorStr.trim());
        simplify();
    }

    private void simplify() {
        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            denominator = denominator.negate();
            numerator = numerator.negate();
        }
    }

    public Fraction add(Fraction other) {
        BigInteger newNumerator = numerator.multiply(other.denominator).add(other.numerator.multiply(denominator));
        BigInteger newDenominator = denominator.multiply(other.denominator);
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction subtract(Fraction other) {
        BigInteger newNumerator = numerator.multiply(other.denominator).subtract(other.numerator.multiply(denominator));
        BigInteger newDenominator = denominator.multiply(other.denominator);
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction multiply(Fraction other) {
        BigInteger newNumerator = numerator.multiply(other.numerator);
        BigInteger newDenominator = denominator.multiply(other.denominator);
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        BigInteger newNumerator = this.numerator.multiply(other.denominator);
        BigInteger newDenominator = this.denominator.multiply(other.numerator);
        return new Fraction(newNumerator, newDenominator);
    }

    public String toProperFractionString() {
        if (numerator.equals(BigInteger.ZERO)) {
            return "0";
        }
        String sign = numerator.compareTo(BigInteger.ZERO) < 0 ? "-" : "";
        BigInteger absNumerator = numerator.abs();
        BigInteger absDenominator = denominator.abs();
        BigInteger whole = absNumerator.divide(absDenominator);
        BigInteger remainder = absNumerator.remainder(absDenominator);

        if (remainder.equals(BigInteger.ZERO)) {
            return sign + whole; // 返回整数部分
        } else if (whole.equals(BigInteger.ZERO)) {
            return sign + remainder + "/" + absDenominator; // 返回真分数
        } else {
            return sign + whole + "’" + remainder + "/" + absDenominator; // 返回带分数
        }
    }
}