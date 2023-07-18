package Modelos;

public class Edge<T extends Comparable<T>> {

	private Vertex<T> origin;
	private Vertex<T> end;
	private Number value;

	public Edge(){
		value=1.0;
	} 
	
	public Edge(Vertex<T> ini,Vertex<T> fin){
		this();
		this.origin = ini;
		this.end = fin;
	}

	public Edge(Vertex<T> ini,Vertex<T> fin,Number val){
		this(ini,fin);
		this.value= val;
	}

	public Vertex<T> getOrigin() {
		return origin;
	}

	public void setOrigin(Vertex<T> origin) {
		this.origin = origin;
	}

	public Vertex<T> getEnd() {
		return end;
	}

	public void setEnd(Vertex<T> end) {
		this.end = end;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((value == null) ? 0 : value.intValue());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	

}
