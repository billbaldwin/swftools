package tv.porst.swfretools.parser.structures;

import tv.porst.splib.binaryparser.Flag;
import tv.porst.splib.binaryparser.Float32;
import tv.porst.splib.binaryparser.IFileElement;
import tv.porst.splib.binaryparser.UBits;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFParserHelpers;

/**
 * Represents a ConvolutionFilter structure.
 * 
 * @author sp
 *
 */
public final class ConvolutionFilter implements IFileElement {

	/**
	 * Horizontal matrix size.
	 */
	private final UINT8 matrixX;

	/**
	 * Vertical matrix size.
	 */
	private final UINT8 matrixY;

	/**
	 * Divisor applied to the matrix values.
	 */
	private final Float32 divisor;

	/**
	 * Bias applied to the matrix values.
	 */
	private final Float32 bias;

	/**
	 * Matrix values.
	 */
	private final Float32List matrix;

	/**
	 * Default color for pixels outside the image.
	 */
	private final RGBA defaultColor;

	/**
	 * Reserved bits.
	 */
	private final UBits reserved;

	/**
	 * Clamp mode flag.
	 */
	private final Flag clamp;

	/**
	 * Preserve alpha flag.
	 */
	private final Flag preserveAlpha;

	/**
	 * Creates a new ConvolutionFilter object.
	 * 
	 * @param matrixX Horizontal matrix size.
	 * @param matrixY Vertical matrix size.
	 * @param divisor Divisor applied to the matrix values.
	 * @param bias Bias applied to the matrix values.
	 * @param matrix Matrix values.
	 * @param defaultColor Default color for pixels outside the image.
	 * @param reserved Reserved bits.
	 * @param clamp Clamp mode flag.
	 * @param preserveAlpha Preserve the alpha flag.
	 */
	public ConvolutionFilter(final UINT8 matrixX, final UINT8 matrixY, final Float32 divisor,
			final Float32 bias, final Float32List matrix, final RGBA defaultColor, final UBits reserved,
			final Flag clamp, final Flag preserveAlpha) {

		this.matrixX = matrixX;
		this.matrixY = matrixY;
		this.divisor = divisor;
		this.bias = bias;
		this.matrix = matrix;
		this.defaultColor = defaultColor;
		this.reserved = reserved;
		this.clamp = clamp;
		this.preserveAlpha = preserveAlpha;
	}

	/**
	 * Returns the bias applied to the matrix values.
	 *
	 * @return The bias applied to the matrix values.
	 */
	public Float32 getBias() {
		return bias;
	}

	@Override
	public int getBitLength() {
		return SWFParserHelpers.addBitLengths(matrixX, matrixY, divisor, bias, matrix, defaultColor, reserved, clamp);
	}

	@Override
	public int getBitPosition() {
		return matrixX.getBitPosition();
	}

	/**
	 * Returns the clamp mode flag.
	 *
	 * @return The clamp mode flag.
	 */
	public Flag getClamp() {
		return clamp;
	}

	/**
	 * Returns the default color for pixels outside the image.
	 *
	 * @return The default color for pixels outside the image.
	 */
	public RGBA getDefaultColor() {
		return defaultColor;
	}

	/**
	 * Returns the divisor applied to the matrix values.
	 *
	 * @return The divisor applied to the matrix values.
	 */
	public Float32 getDivisor() {
		return divisor;
	}

	/**
	 * Returns the matrix values.
	 *
	 * @return The matrix values.
	 */
	public Float32List getMatrix() {
		return matrix;
	}

	/**
	 * Returns the horizontal matrix size.
	 *
	 * @return The horizontal matrix size.
	 */
	public UINT8 getMatrixX() {
		return matrixX;
	}

	/**
	 * Returns the vertical matrix size.
	 *
	 * @return The vertical matrix size.
	 */
	public UINT8 getMatrixY() {
		return matrixY;
	}

	/**
	 * Returns the preserve alpha flag.
	 *
	 * @return The preserve alpha flag.
	 */
	public Flag getPreserveAlpha() {
		return preserveAlpha;
	}

	/**
	 * Returns the reserved bits.
	 *
	 * @return The reserved bits.
	 */
	public UBits getReserved() {
		return reserved;
	}
}