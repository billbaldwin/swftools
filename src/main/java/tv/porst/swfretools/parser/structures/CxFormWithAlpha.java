package tv.porst.swfretools.parser.structures;

import tv.porst.splib.binaryparser.Bits;
import tv.porst.splib.binaryparser.Flag;
import tv.porst.splib.binaryparser.IFileElement;
import tv.porst.splib.binaryparser.UBits;
import tv.porst.swfretools.parser.SWFParserHelpers;

/**
 * Represents a CxFormWithAlpha structure.
 * 
 * @author sp
 *
 */
public final class CxFormWithAlpha implements IFileElement {

	/**
	 * Has color addition values flag.
	 */
	private final Flag hasAddTerms;

	/**
	 * Has color multiply values flag.
	 */
	private final Flag hasMultTerms;

	/**
	 * Bits in each field.
	 */
	private final UBits nBits;

	/**
	 * Red multiply value.
	 */
	private final Bits redMultTerm;

	/**
	 * Green multiply value.
	 */
	private final Bits greenMultTerm;

	/**
	 * Blue multiply value.
	 */
	private final Bits blueMultTerm;

	/**
	 * Alpha multiply value.
	 */
	private final Bits alphaMultTerm;

	/**
	 * Red addition value.
	 */
	private final Bits redAddTerm;

	/**
	 * Green addition value.
	 */
	private final Bits greenAddTerm;

	/**
	 * Blue addition value.
	 */
	private final Bits blueAddTerm;

	/**
	 * Alpha addition value.
	 */
	private final Bits alphaAddTerm;

	/**
	 * Creates a new FocalGradient object.
	 * 
	 * @param hasAddTerms Has color addition values flag.
	 * @param hasMultTerms Has color multiply values flag.
	 * @param nBits Bits in each field.
	 * @param redMultTerm Red multiply value.
	 * @param greenMultTerm Green multiply value.
	 * @param blueMultTerm Blue multiply value.
	 * @param alphaMultTerm Alpha multiply value.
	 * @param redAddTerm Red addition value.
	 * @param greenAddTerm Green addition value.
	 * @param blueAddTerm Blue addition value.
	 * @param alphaAddTerm Alpha addition value.
	 */
	public CxFormWithAlpha(final Flag hasAddTerms, final Flag hasMultTerms, final UBits nBits,
			final Bits redMultTerm, final Bits greenMultTerm, final Bits blueMultTerm,
			final Bits alphaMultTerm, final Bits redAddTerm, final Bits greenAddTerm,
			final Bits blueAddTerm, final Bits alphaAddTerm) {

		this.hasAddTerms = hasAddTerms;
		this.hasMultTerms = hasMultTerms;
		this.nBits = nBits;
		this.redMultTerm = redMultTerm;
		this.greenMultTerm = greenMultTerm;
		this.blueMultTerm = blueMultTerm;
		this.alphaMultTerm = alphaMultTerm;
		this.redAddTerm = redAddTerm;
		this.greenAddTerm = greenAddTerm;
		this.blueAddTerm = blueAddTerm;
		this.alphaAddTerm = alphaAddTerm;
	}

	/**
	 * Returns the alpha addition value.
	 *
	 * @return The alpha addition value.
	 */
	public Bits getAlphaAddTerm() {
		return alphaAddTerm;
	}

	/**
	 * Returns the alpha multiply value.
	 *
	 * @return The alpha multiply value.
	 */
	public Bits getAlphaMultTerm() {
		return alphaMultTerm;
	}

	@Override
	public int getBitLength() {
		return SWFParserHelpers.addBitLengths(hasAddTerms, hasMultTerms, nBits, redMultTerm, greenMultTerm,
				blueMultTerm, alphaMultTerm, redAddTerm, greenAddTerm, blueAddTerm, alphaAddTerm);
	}

	@Override
	public int getBitPosition() {
		return hasAddTerms.getBitPosition();
	}

	/**
	 * Returns the blue addition value.
	 *
	 * @return The blue addition value.
	 */
	public Bits getBlueAddTerm() {
		return blueAddTerm;
	}

	/**
	 * Returns the blue multiply value.
	 *
	 * @return The blue multiply value.
	 */
	public Bits getBlueMultTerm() {
		return blueMultTerm;
	}

	/**
	 * Returns the green addition value.
	 *
	 * @return The green addition value.
	 */
	public Bits getGreenAddTerm() {
		return greenAddTerm;
	}

	/**
	 * Returns the green multiply value.
	 *
	 * @return The green multiply value.
	 */
	public Bits getGreenMultTerm() {
		return greenMultTerm;
	}

	/**
	 * Returns the has color addition values flag.
	 *
	 * @return The has color addition values flag.
	 */
	public Flag getHasAddTerms() {
		return hasAddTerms;
	}

	/**
	 * Returns the has color multiply values flag.
	 *
	 * @return The has color multiply values flag.
	 */
	public Flag getHasMultTerms() {
		return hasMultTerms;
	}

	/**
	 * Returns the bits in each field.
	 *
	 * @return The bits in each field.
	 */
	public UBits getnBits() {
		return nBits;
	}

	/**
	 * Returns the red addition value.
	 *
	 * @return The red addition value.
	 */
	public Bits getRedAddTerm() {
		return redAddTerm;
	}

	/**
	 * Returns the red multiply value.
	 *
	 * @return The red multiply value.
	 */
	public Bits getRedMultTerm() {
		return redMultTerm;
	}
}