package Modelos;

import java.util.Objects;

public class Edge {
    private Vertex origin;
    private Vertex end;
    private int value;

    public Edge(Vertex origin, Vertex end, int value) {
        this.origin = origin;
        this.end = end;
        this.value = value;
    }

    public Vertex getOrigin() {
        return origin;
    }

    public Vertex getEnd() {
        return end;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        return origin.equals(other.origin) && end.equals(other.end) && value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, end, value);
    }

    @Override
    public String toString() {
        return origin + " -> " + value + " -> " + end;
    }
}
