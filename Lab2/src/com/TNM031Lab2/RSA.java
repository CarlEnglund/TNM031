package com.TNM031Lab2;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {

    //Function for generating a large prime number for a BigInteger
    private BigInteger largePrime() {
        SecureRandom random = new SecureRandom();
        return BigInteger.probablePrime(1024, random);
    };

    //p, q and e should be large random primes. n is the factor of p times q.
    private BigInteger p = largePrime();
    private BigInteger q = largePrime();
    private BigInteger e = largePrime();
    private BigInteger n = p.multiply(q);

    //Create message to send and turn it into a BigInteger
    private String message = "Hello 123";
    private BigInteger messageLength = new BigInteger(message.getBytes());

    //mod n c = m^e
    private BigInteger c = messageLength.modPow(e, n);

    //de = 1 (mod (p-1)(q-1))
    private BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    private BigInteger d = e.modInverse(phi);

    //m = c^d
    private BigInteger decryptedBigInteger = c.modPow(d, n);

    //Decrypt message back to original
    private String decryptedMessage = new String(decryptedBigInteger.toByteArray());

    public static void main(String[] args) {
        RSA Enctryption = new RSA();

	System.out.println(Enctryption.message);
        System.out.println(Enctryption.c);
        System.out.println(Enctryption.n);
        System.out.println(Enctryption.decryptedMessage);
    }
}
