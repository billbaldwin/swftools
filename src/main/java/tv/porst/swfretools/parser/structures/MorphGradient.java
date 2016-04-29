package tv.porst.swfretools.parser.structures;

import tv.porst.splib.binaryparser.IFileElement;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFParserHelpers;

/**
 * Represents a MorphGradient structure.
 * 
 * @author sp
 *
 */
public final class MorphGradient implements IFileElement {

	/**
	 * Number of gradients.
	 */
	private final UINT8 numGradients;

	/**
	 * Gradient records.
	 */
	private final MorphGradientRecordList gradientRecords;

	/**
	 * Creates a new MorphGradient object.
	 * 
	 * @param numGradients Number of gradients.
	 * @param gradientRecords Gradient records.
	 */
	public MorphGradient(final UINT8 numGradients, final MorphGradientRecordList gradientRecords) {

		this.numGradients = numGradients;
		this.gradientRecords = gradientRecords;

	}

	@Override
	public int getBitLength() {
		return SWFParserHelpers.addBitLengths(numGradients, gradientRecords);
	}

	@Override
	public int getBitPosition() {
		return numGradients.getBitPosition();
	}

	/**
	 * Returns the gradient records.
	 *
	 * @return The gradient records.
	 */
	public MorphGradientRecordList getGradientRecords() {
		return gradientRecords;
	}

	/**
	 * Returns the number of gradients.
	 *
	 * @return The number of gradients.
	 */
	public UINT8 getNumGradients() {
		return numGradients;
	}
}