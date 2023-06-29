package model;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    private final List<Integer> sequenceList;
    private int sequencePos = 0;

    public Sequence(String inputString) {
        sequenceList = new ArrayList<>();
        String[] numbers = inputString.split(" ");
        for (String number : numbers) {
            sequenceList.add(Integer.parseInt(number));
        }
    }

    public int getNextNumber() {
        if (sequencePos == sequenceList.size()) {
            sequencePos = 0;
        }
        return sequenceList.get(sequencePos++);
    }

    public List<Integer> getSequenceList() {
        return sequenceList;
    }
}
