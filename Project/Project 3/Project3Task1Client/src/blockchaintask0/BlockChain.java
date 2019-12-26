/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 16th, 2019
 *
 * This program demonstrates a very simple stand-alone Blockchain.
 */

package blockchaintask0;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a simple BlockChain.
 */
public class BlockChain {
    List<Block> ds_chain = new ArrayList();
    String chainHash = "";

    /**
     * This BlockChain has exactly two instance members - an ArrayList to hold Blocks and
     * a chain hash to hold a SHA256 hash of the most recently added Block.
     * This constructor creates an empty ArrayList for Block storage.
     * This constructor sets the chain hash to the empty string.
     */
    public BlockChain() {
    }

    /**
     * This routine acts as a test driver for your Blockchain.
     * It will begin by creating a BlockChain object and then adding the Genesis block to the chain.
     *
     * All hashes will have the proper number of zero hex digits representing the most significant nibbles in the hash.
     * A nibble is 4 bits.
     * If the difficulty is specified as three,
     * then all hashes will begin with 3 or more zero hex digits (or 3 nibbles, or 12 zero bits).
     *
     * It is menu driven and will continously provide the user with seven options:
     * Block Chain Menu
     *
     * 0. View basic blockchain status.
     *
     * 1. Add a transaction to the blockchain.
     *
     * 2. Verify the blockchain.
     *
     * 3. View the blockchain.
     *
     * 4. Corrupt the chain.
     *
     * 5. Hide the corruption by repairing the chain.
     *
     * 6. Exit.
     *
     * If the user selects option 0, the program will display:
     * The number of blocks on the chain Current hashes per second by this machine
     * Difficulty of most recent block Nonce for most recent block Chain hash
     *
     * If the user selects option 1, the program will prompt for and then read the difficulty level for this block.
     * It will then prompt for and then read a line of data from the user (representing a transaction).
     * The program will display the time it took to add this block.
     * Note: The first block added after Genesis has index 1. The second has 2 and so on.
     *
     * If the user selects option 2, then call the isChainValid method and display the results.
     * It is important to note that this method will execute fast.
     * Blockchains are easy to validate but time consuming to modify.
     * Your program needs to display the number of milliseconds it took for validate to run.
     *
     * If the user selects option 3, display the entire Blockchain contents as a correctly formed JSON document.
     *
     * If the user selects option 4, she wants to corrupt the chain.
     * Ask her for the block index (0..size-1) and ask her for the new data that will be placed in the block.
     * Her new data will be placed in the block.
     * At this point, option 2 (verify chain) should show false.
     * In other words, she will be making a data change to a particular block and the chain itself will become invalid.
     *
     * If the user selects 5, she wants to repair the chain.
     * That is, she wants to recompute the proof of work for each node that has become invalid - due perhaps,
     * to an earlier selection of option 4. The program begins at the Genesis block and checks each block in turn.
     * If any block is found to be invalid, it executes repair logic.
     *
     * Important:
     *
     * Within your comments in the main routine, you must describe how this system behaves
     * with blocks of difficulty 4 and blocks with difficulty 5.
     * How much time does it take for your blockchain to add blocks with the two difficulty levels of 4 and 5?
     * Add a few blocks and give your average times.
     *
     * In addition, be sure to specify how much time it takes for your verify method to run.
     * Again, consider blocks with difficulty 4 and 5.
     *
     * You should employ the system clock.
     * You should be able to make clear statements describing the approximate run times associated with
     * addBlock(), isChainValid(), and chainRepair().
     * @param args
     */
    public static void main(java.lang.String[] args) {
        System.out.println("Running...");
        BlockChain blockChain = new BlockChain();

        try {
            // The Genesis block will be created with an empty string as the pervious hash and a difficulty of 2.
            // The Genesis block is at position 0.
            Block lastBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2);
            lastBlock.setPrevHash(blockChain.getChainHash());
            String chainHash = lastBlock.proofOfWork();
            blockChain.setChainHash(chainHash);
            blockChain.addBlock(lastBlock);
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            while(true) {
                System.out.println("Main menu:");
                System.out.println("0. View basic blockchain status.");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Corrupt the chain.");
                System.out.println("5. Hide the Corruption by repairing the chain.");
                System.out.println("6. Exit");
                String nextLine = typed.readLine();
                if (nextLine.charAt(0) == '6') {
                    System.out.println("Exit...");
                    break;
                }

                if (nextLine.charAt(0) == '0') {
                    System.out.println(String.format("Current size of chain: %d", blockChain.getChainSize()));
                    System.out.println(String.format("Current hashes per second by this machine: %d",
                            blockChain.hashesPerSecond()));
                    System.out.println(String.format("Difficulty of most recent block: %d", lastBlock.getDifficulty()));
                    System.out.println(String.format("Nonce for most recent block: %s",
                            lastBlock.getNonce().toString()));
                    System.out.println("Chain hash:");
                    System.out.println(blockChain.getChainHash());
                } else {
                    int difficultyInput;

                    if (nextLine.charAt(0) == '1') {
                        // All blocks added to the Blockchain will have a difficulty
                        // passed in to the program by the user at run time.
                        System.out.println("Enter difficulty > 0");
                        difficultyInput = Integer.valueOf(typed.readLine());
                        if (difficultyInput > 0) {
                            System.out.println("Enter transaction");
                            String transaction = typed.readLine();
                            long currentTimeMillis = System.currentTimeMillis();

                            Block newBlock = new Block(
                                    // increase the index by one
                                    lastBlock.getIndex() + 1,
                                    new Timestamp(System.currentTimeMillis()),
                                    transaction,
                                    difficultyInput);

                            // the blockchain class has a field that hold the hash of the last block
                            newBlock.setPrevHash(blockChain.getChainHash());

                            // update the class field using the hash value of new block
                            chainHash = newBlock.proofOfWork();
                            blockChain.setChainHash(chainHash);

                            // Add a block containing that transaction to the block chain.
                            blockChain.addBlock(newBlock);
                            lastBlock = newBlock;
                            System.out.println(String.format(
                                    "Total execution time to add this block was %d milliseconds",
                                    System.currentTimeMillis() - currentTimeMillis));
                        }
                    } else {
                        long currentTimeMillis;

                        // If the user selects option 2
                        if (nextLine.charAt(0) == '2') {
                            currentTimeMillis = System.currentTimeMillis();
                            System.out.println("Verifying entire chain");

                            // call the isChainValid method and display the results.
                            System.out.println(String.format("Chain verification: %b", blockChain.isChainValid()));

                            // display the number of milliseconds it took for validate to run.
                            System.out.println(String.format(
                                    "Total execution time required to verify the chain was %d milliseconds",
                                    System.currentTimeMillis() - currentTimeMillis));

                        } else if (nextLine.charAt(0) == '3') {
                            System.out.println("View the Blockchain");
                            System.out.println(JSON.toJSONString(blockChain));

                        } else if (nextLine.charAt(0) == '4') {
                            System.out.println("Corrupt the Blockchain");
                            System.out.println("Enter block ID of block to Corrupt");

                            difficultyInput = Integer.valueOf(typed.readLine());
                            if (difficultyInput >= 0 && difficultyInput < blockChain.getChainSize()) {
                                System.out.println(String.format("Enter new data for block %d", difficultyInput));

                                // get the block that user want to change
                                List<Block> blocks = blockChain.getDs_chain();
                                Block block = (Block)blocks.get(difficultyInput);

                                // add new data for block
                                String transaction = typed.readLine();
                                block.setTx(transaction);
                                System.out.println(String.format("Block %d now holds %s",
                                        difficultyInput, block.getTx()));
                            }
                        } else if (nextLine.charAt(0) == '5') {
                            System.out.println("Repairing the entire chain");
                            currentTimeMillis = System.currentTimeMillis();
                            blockChain.repairChain();
                            System.out.println(String.format(
                                    "Total execution time required to repair the chain was %d milliseconds",
                                    System.currentTimeMillis() - currentTimeMillis));
                        }
                    }
                }
            }
        } catch (Exception var13) {
            System.out.println("Exception: " + var13.getMessage());
        } finally {
            ;
        }

        System.out.println("bye!");
    }

    /**
     * Simple get method
     *
     * @return the current system time
     */
    @JSONField(
            serialize = false
    )
    public Timestamp getTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }

    /**
     * simple set method
     *
     * @return a reference to the most recently added Block.
     */
    @JSONField(
            serialize = false
    )
    public Block getLatestBlock() {
        return (Block)this.ds_chain.get(this.getChainSize() - 1);
    }

    /**
     * simple get method
     *
     * @return the size of the chain in blocks.
     */
    @JSONField(
            serialize = false
    )
    public int getChainSize() {
        return this.ds_chain.size();
    }

    public List<Block> getDs_chain() {
        return this.ds_chain;
    }

    public void setDs_chain(List<Block> ds_chain) {
        this.ds_chain = ds_chain;
    }

    /**
     * It uses a simple string - "00000000" to hash.
     * @return hashes per second of the computer holding this chain.
     */
    public int hashesPerSecond() {
        long count = 1000000L;
        long currentTimeMillis = System.currentTimeMillis();

        for(int i = 0; (long)i < count; ++i) {
            String var6 = BabyHash.ComputeSHA_256_as_Hex_String("00000000");
        }

        long time2 = this.getTime().getTime();
        return (int)(count * 1000L / (System.currentTimeMillis() - currentTimeMillis));
    }

    /**
     * A new Block is being added to the BlockChain.
     * This new block's previous hash must hold the hash of the most recently added block.
     * After this call on addBlock, the new block becomes the most recently added block on the BlockChain.
     * The SHA256 hash of every block must exhibit proof of work, i.e.,
     * have the requisite number of leftmost 0's defined by its difficulty.
     * Suppose our new block is x.
     * And suppose the old block-chain was a <-- b <-- c <-- d
     * then the chain after addBlock completes is a <-- b <-- c <-- d <-- x.
     * Within the block x, there is a previous hash field.
     * This previous hash field holds the hash of the block d.
     * The block d is called the parent of x.
     * The block x is the child of the block d.
     * It is important to also maintain a hash of the most recently added block in a chain hash.
     * Let's look at our two chains again. a <-- b <-- c <-- d.
     * The chain hash will hold the hash of d.
     * After adding x, we have a <-- b <-- c <-- d <-- x.
     * The chain hash now holds the hash of x.
     * The chain hash is not defined within a block but is defined within the block chain.
     * The arrows are used to describe these hash pointers.
     * If b contains the hash of a then we write a <-- b.
     * @param newBlock is added to the BlockChain as the most recent block
     */
    public void addBlock(Block newBlock) {
        this.ds_chain.add(newBlock);
    }

    /**
     * If either check fails, return false. Otherwise, return true.
     *
     * Continue checking until you have validated the entire chain.
     * The first check will involve a computation of a hash in Block 0
     * and a comparison with the hash pointer in Block 1.
     * If they match and if the proof of work is correct,
     * go and visit the next block in the chain.
     *
     * @return true if and only if the chain is valid
     */
    @JSONField(
            serialize = false
    )
    public boolean isChainValid() {
        if (this.ds_chain.size() < 1) {
            return false;
        } else {
            // If the chain only contains one block, the genesis block at position 0,
            Block block = (Block)this.ds_chain.get(0);
            if (block.getIndex() != 0) {
                return false;
            } else {
                // computes the hash of the first block
                String previousHash = block.calculateHash();

                int numberLeadingHexZeroes = block.getNumberLeadingHexZeroes(previousHash);

                // checks the hash has the requisite number of leftmost 0's (proof of work)
                // as specified in the difficulty field.
                if (numberLeadingHexZeroes < block.getDifficulty()) {
                    return false;
                } else {

                    // If the chain has more blocks than one, begin checking from block one.
                    for(int i = 1; i < this.ds_chain.size(); ++i) {
                        block = (Block)this.ds_chain.get(i);
                        String hash = block.calculateHash();
                        numberLeadingHexZeroes = block.getNumberLeadingHexZeroes(hash);
                        if (numberLeadingHexZeroes < block.getDifficulty()) {
                            return false;
                        }

                        if (!block.getPrevHash().equals(previousHash)) {
                            return false;
                        }

                        previousHash = hash;
                    }

                    // At the end, check that the chain hash is also correct.
                    if (!previousHash.equals(this.getChainHash())) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    /**
     * This routine repairs the chain.
     * It checks the hashes of each block and ensures that any illegal hashes are recomputed.
     * After this routine is run, the chain will be valid. The routine does not modify any difficulty values.
     * It computes new proof of work based on the difficulty specified in the Block.
     *
     * Foe example, here is our chain:
     *
     * a <- b <- c <- d
     *
     * The chain hash is the hash of block d - the most recent block.
     * If you corrupt block b, two thing happen:
     * 1) The pointer in c is no longer correct.
     * 2) The hash of b no longer yields the correct number of 0's.
     *
     * To repair this chain, an attacker will need to do the following:
     * 1) Computer proof of work on b. This is needed because we need to generate the correct number of 0's.
     * 2) Change the hash pointer in c so it has the new hash that we computed in 1.
     * 3) Now, c is broken. We need to do this again on c.
     * 4) This continues until we finally compute a new chain hash.
     */
    public void repairChain() {
        if (!this.isChainValid() && this.ds_chain.size() >= 1) {
            Block block = (Block)this.ds_chain.get(0);
            if (block.getIndex() != 0) {
                block.setIndex(0);
            }

            String previousHash = block.proofOfWork();

            for(int i = 1; i < this.ds_chain.size(); ++i) {
                block = (Block)this.ds_chain.get(i);
                block.setPrevHash(previousHash);
                previousHash = block.proofOfWork();
            }

            this.setChainHash(previousHash);
        }
    }

    public String getChainHash() {
        return this.chainHash;
    }

    public void setChainHash(String chainHash) {
        this.chainHash = chainHash;
    }
}
