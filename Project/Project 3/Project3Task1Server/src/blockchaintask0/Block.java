/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 16th, 2019
 *
 * This program demonstrates a very simple stand-alone Blockchain.
 */

package blockchaintask0;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * This class represents a simple Block.
 */
public class Block {

    /**
     * Each Block object has an index - the position of the block on the chain.
     * The first block (the so called Genesis block) has an index of 0.
     */
    private int index;

    /**
     * Each block has a timestamp - a Java Timestamp object, it holds the time of the block's creation.
     */
    //2019-10-09 11:29:59
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp timestamp;
    /**
     * Each block has a field named Tx - a String holding the block's single transaction details.
     */
    private java.lang.String Tx;
    /**
     * Each block has a String field named PrevHash - the SHA256 hash of a block's parent.
     * This is also called a hash pointer.
     */
    private String PrevHash;
    /**
     * Each block holds a nonce - a BigInteger value determined by a proof of work routine.
     * This has to be found by the proof of work logic.
     * It has to be found so that this block has a hash of the proper difficulty.
     * The difficulty is specified by a small integer representing the number of leading hex zeroes the hash must have.
     */
    private BigInteger nonce;
    /**
     * Each block has a field named difficulty - it is an int that specifies the exact number of
     * left most hex digits needed by a proper hash.
     * The hash is represented in hexadecimal.
     * If, for example, the difficulty is 3, the hash must have three leading hex 0's (or,1 and 1/2 bytes).
     * Each hex digit represents 4 bits.
     */
    private int difficulty;

    /**
     * This the Block constructor.
     * @param index This is the position within the chain. Genesis is at 0.
     * @param timestamp This is the time this block was added.
     * @param Tx This is the transaction to be included on the blockchain.
     * @param difficulty This is the number of leftmost nibbles that need to be 0.
     */
    public Block(int index,
                 java.sql.Timestamp timestamp,
                 java.lang.String Tx,
                 int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.Tx = Tx;
        this.difficulty = difficulty;
        nonce = BigInteger.ZERO;
        PrevHash = "";
    }

    /**
     * Override Java's toString method
     *
     * @return A JSON representation of all of this block's data is returned.
     */
    public String toString() {
        //convert object Block to string
        return JSON.toJSONString(this);
    }

    /**
     * This method computes a hash of the concatenation of
     * the index, timestamp, data, previousHash, nonce, and difficulty.
     *
     * @return a String holding Hexadecimal characters
     */
    public String calculateHash() {
        return BabyHash.ComputeSHA_256_as_Hex_String(toString());
    }

    /**
     * The proof of work methods finds a good hash.
     * It increments the nonce until it produces a good hash.
     *
     * It continues this process, burning electricity and CPU cycles,
     * until it gets lucky and finds a good hash.
     *
     * @return a String with a hash that has the appropriate number of leading hex zeroes.
     * The difficulty value is already in the block.
     * This is the number of hex 0's a proper hash must have.
     */
    public String proofOfWork() {
        do {
            // This method calls calculateHash() to compute
            // a hash of the concatenation of the index, timestamp, data,
            // previousHash, nonce, and difficulty.
            String hash = calculateHash();

            int numberLeadingHexZeroes = getNumberLeadingHexZeroes(hash);

            // If the hash has the appropriate number of leading hex zeroes,
            if (numberLeadingHexZeroes >= difficulty) {
                // it is done and returns that proper hash.
                return hash;
            }

            // If the hash does not have the appropriate number of leading hex zeroes,
            // it increments the nonce by 1 and tries again.
            nonce = nonce.add(BigInteger.ONE);
        } while (true);
    }

    /**
     * Helper method that find out how many leading zeros in the hash string
     * @param hash the Hash string
     * @return the number of zero(s) in the prefix
     */
    public int getNumberLeadingHexZeroes(String hash) {
        int number = 0;
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') {
                number++;
            } else {
                return number;
            }
        }
        return number;
    }

    /**
     * Simple getter method
     *
     * @return index of block
     */
    public int getIndex() {
        return index;
    }

    /**
     * Simple setter method
     *
     * @param index the index of this block in the chain
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Simple getter method
     *
     * @return timestamp of this block
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Simple setter method
     *
     * @param timestamp of when this block was created
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Simple getter method
     *
     * @return this block's transaction
     */
    public String getTx() {
        return Tx;
    }

    /**
     * Simple setter method
     *
     * @param tx represents the transaction held by this block
     */
    public void setTx(String tx) {
        this.Tx = tx;
    }

    /**
     * Simple getter method
     *
     * @return previous hash
     */
    public String getPrevHash() {
        return PrevHash;
    }

    /**
     * Simple setter method
     *
     * @param prevHash a hash-pointer to this block's parent
     */
    public void setPrevHash(String prevHash) {
        this.PrevHash = prevHash;
    }

    /**
     * This method returns the nonce for this block.
     * The nonce is a number that has been found to cause
     * the hash of this block to have the correct number of
     * leading hexadecimal zeroes.
     *
     * @return a BigInteger representing the nonce for this block.
     */
    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    /**
     * Simple getter method
     *
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Simple setter method
     *
     * @param difficulty determines how much work is required to produce a proper hash
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
