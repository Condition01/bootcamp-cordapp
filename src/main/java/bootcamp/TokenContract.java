package bootcamp;

import jdk.nashorn.internal.parser.Token;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        //SHAPE
        Command command = tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();

        if(tx.getInputs().size() != 0){
            throw new IllegalArgumentException("Inputs must to be 0");
        }

        if(tx.getOutputs().size() != 1){
            throw new IllegalArgumentException("Outputs must to be 1");
        }

        if(tx.getCommands().size() != 1){
            throw new IllegalArgumentException("Must have just 1 command");
        }

        //CONTENT

       ContractState output = tx.getOutput(0);

       if(!(output instanceof TokenState)){
           throw new IllegalArgumentException("You cant inssue a State diferent of a TokenState");
       }

       TokenState tokenState = (TokenState) output;

       if(!(tokenState.getAmount() > 0)){
           throw new IllegalArgumentException("You only can transact a positive value");
       }

       if(!(command.getValue() instanceof Commands.Issue)){
           throw new IllegalArgumentException("You cant have an command who's not an Issuer");
       }

        //SIGN

        if(!(requiredSigners.contains(command.getSigners()))){
            throw new IllegalArgumentException("The issuer has to be a signer!");
        }
    }


    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}