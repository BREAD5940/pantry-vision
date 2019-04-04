package org.team5940.pantry.vision;

public interface State<S> extends Interpolable<S>, CSVWritable {
	double distance(final S other);

	boolean equals(final Object other);

	String toString();

	String toCSV();
}
