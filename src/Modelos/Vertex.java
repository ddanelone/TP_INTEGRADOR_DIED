package Modelos;

import java.util.Objects;

public class Vertex {
    private int value;
    private double page_rank;

    public double getPageRank() {
        return page_rank;
    }

    public void setPageRank(double page_rank) {
        this.page_rank = page_rank;
    }

    public Vertex(int value) {
        this.value = value;
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
        Vertex other = (Vertex) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
