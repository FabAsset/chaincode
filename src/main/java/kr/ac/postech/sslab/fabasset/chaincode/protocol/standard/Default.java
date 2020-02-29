package kr.ac.postech.sslab.fabasset.chaincode.protocol.standard;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.postech.sslab.fabasset.chaincode.manager.TokenManager;
import kr.ac.postech.sslab.fabasset.chaincode.client.Address;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Default {
    private static final String QUERY_OWNER = "{\"selector\":{\"owner\":\"%s\"}}";

    private Default() {}

    public static boolean mint(ChaincodeStub stub, String tokenId) throws JsonProcessingException {
        String caller = Address.getMyAddress(stub);

        TokenManager nft = new TokenManager();
        String type = "base";
        return nft.mint(stub, tokenId, type, caller, null, null);
    }

    public static boolean burn(ChaincodeStub stub, String tokenId) throws IOException {
        String caller = Address.getMyAddress(stub);
        String owner = ERC721.ownerOf(stub, tokenId);
        if (!caller.equals(owner)) {
            return false;
        }

        TokenManager nft = TokenManager.read(stub, tokenId);
        return nft.burn(stub, tokenId);
    }

    public static String getType(ChaincodeStub stub, String tokenId) throws IOException {
        TokenManager nft = TokenManager.read(stub, tokenId);
        return nft.getType();
    }

    public static List<String> tokenIdsOf(ChaincodeStub stub, String owner) {
        String query = String.format(QUERY_OWNER, owner);

        List<String> tokenIds = new ArrayList<>();
        QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
        while(resultsIterator.iterator().hasNext()) {
            String tokenId = resultsIterator.iterator().next().getKey();
            tokenIds.add(tokenId);
        }

        return tokenIds;
    }
}