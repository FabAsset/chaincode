package com.github.fabasset.chaincode.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabasset.chaincode.constant.Key;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OperatorManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // operator relationship table
    private Map<String, Map<String, Boolean>> table;

    public void store(ChaincodeStub stub) throws JsonProcessingException {
        stub.putStringState(Key.OPERATORS_APPROVAL, toJSONString());
    }

    public static OperatorManager load(ChaincodeStub stub) throws IOException {
        OperatorManager manager = new OperatorManager();
        Map<String, Map<String, Boolean>> table;
        String json = stub.getStringState(Key.OPERATORS_APPROVAL);

        if (json.trim().length() == 0) {
            table = new HashMap<>();
        }
        else {
            table = objectMapper.readValue(json,
                    new TypeReference<HashMap<String, HashMap<String, Boolean>>>(){});
        }

        manager.setTable(table);

        return manager;
    }

    public Map<String, Map<String, Boolean>> getTable() {
        return table;
    }

    public void setTable(Map<String, Map<String, Boolean>> table) {
        this.table = table;
    }

    private String toJSONString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(table);
    }
}
