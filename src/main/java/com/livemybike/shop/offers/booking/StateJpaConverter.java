package com.livemybike.shop.offers.booking;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Converter
public class StateJpaConverter implements AttributeConverter<State, String> {

    private static final Map<String, State> stringToStateMap;

    static {
        Map<String, State> map = new HashMap<>();
        map.put(State.REQUEST_STATE_STRING, State.REQUEST_STATE);
        map.put(State.APPROVED_STATE_STRING, State.APPROVED_STATE);
        map.put(State.CANCELED_STATE_STRING, State.CANCELED_STATE);
        stringToStateMap = Collections.unmodifiableMap(map);
    }


    @Override
    public String convertToDatabaseColumn(State state) {
        return state.getValue();
    }

    @Override
    public State convertToEntityAttribute(String state) {
        State result = stringToStateMap.get(state);
        if (result == null) {
            throw new IllegalArgumentException("Invalid booking state: " + state);
        }
        return result;
    }

}
