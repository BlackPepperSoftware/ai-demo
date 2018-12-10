package uk.co.blackpepper.neuroevolution;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Innovator {

    private Map<Pair<Integer, Integer>, Integer> splits;
    private Map<Pair<Integer, Integer>, Integer> connections;
    private int innovationNumber;

    public Innovator() {
        splits = new HashMap<>();
        connections = new HashMap<>();
        innovationNumber = 0;
    }

    public Integer retrieveOrGenerateInnovationBetween(int input, int output) {
        return Optional.ofNullable(connections.get(new Pair<>(input, output)))
                .orElseGet(() -> nextInnovation(input, output));
    }

    public Integer retrieveOrGenerateNodeIdBetween(int input, int output) {
        return Optional.ofNullable(splits.get(new Pair<>(input, output)))
                .orElseGet(() -> nextNodeId(input, output));
    }

    public Integer nextNodeId(int input, int output) {
        int id = nextNodeId();
        splits.put(new Pair<>(input, output), id);
        return id;
    }

    public Integer nextNodeId() {
        return innovationNumber++;
    }

    public Integer nextInnovation(int input, int output) {
        int innovation = nextInnovation();
        connections.put(new Pair<>(input, output), innovation);
        return innovation;
    }

    public Integer nextInnovation() {
        return innovationNumber++;
    }

    public class Pair<I,O> {
        private final I left;
        private final O right;

        private Pair(final I input, final O output) {
            this.left = input;
            this.right = output;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Pair)) {
                return false;
            }

            Pair that = (Pair) object;

            return left.equals(that.left)
                    && right.equals(that.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, "-", right);
        }
    }


}
