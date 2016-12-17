package goclient.util;

/**
 * @author Kacper
 *
 */
public class DoublePair {

	public final double x;
	public final double y;

	public DoublePair(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof DoublePair) {
			return equals((DoublePair) o);
		} else {
			return super.equals(o);
		}
	}

	public boolean equals(DoublePair pair) {
		return (x == pair.x && y == pair.y);
	}

	@Override
	public int hashCode() {
		return 2047 * new Double(x).hashCode() + new Double(y).hashCode();
	}
}
