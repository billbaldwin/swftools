package tv.porst.swfretools.parser.structures;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseFlag;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUBits;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUBitsIf;
import tv.porst.splib.binaryparser.Flag;
import tv.porst.splib.binaryparser.UBits;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;

/**
 * Used to parse Matrix structures.
 * 
 * @author sp
 */
public final class MatrixParser {

	/**
	 * Parses a Matrix structure.
	 * 
	 * @param parser Provides the input data.
	 * @param fieldName Name of the matrix field that is parsed.
	 * 
	 * @return The parsed Matrix value.
	 * 
	 * @throws SWFParserException Thrown if parsing the Matrix value failed.
	 */
	public static Matrix parse(final SWFBinaryParser parser, final String fieldName) throws SWFParserException {

		if (parser == null) {
			throw new IllegalArgumentException("Parser argument must not be null");
		}

		if (fieldName == null) {
			throw new IllegalArgumentException("Field name argument must not be null");
		}

		final Flag hasScale = parseFlag(parser, 0x00006, fieldName + "::HasScale");
		final UBits nScaleBits = parseUBitsIf(parser, 5, 0x00006, hasScale.value(), fieldName + "::NScaleBits");
		final UBits scaleX = parseUBitsIf(parser, nScaleBits == null ? 0 : nScaleBits.value(), 0x00006, hasScale.value(), fieldName + "::ScaleX");
		final UBits scaleY = parseUBitsIf(parser, nScaleBits == null ? 0 : nScaleBits.value(), 0x00006, hasScale.value(), fieldName + "::ScaleY");
		final Flag hasRotate = parseFlag(parser, 0x00006, fieldName + "::HasRotate");
		final UBits nRotateBits = parseUBitsIf(parser, 5, 0x00006, hasRotate.value(), fieldName + "::NRotateBits");
		final UBits rotateSkew0 = parseUBitsIf(parser, nRotateBits == null ? 0 : nRotateBits.value(), 0x00006, hasRotate.value(), fieldName + "::rotateSkew0");
		final UBits rotateSkew1 = parseUBitsIf(parser, nRotateBits == null ? 0 : nRotateBits.value(), 0x00006, hasRotate.value(), fieldName + "::rotateSkew1");
		final UBits nTranslateBits = parseUBits(parser, 5, 0x00006, fieldName + "::NTranslateBits");
		final UBits translateX = parseUBits(parser, nTranslateBits == null ? 0 : nTranslateBits.value(), 0x00006, fieldName + "::TranslateX");
		final UBits translateY = parseUBits(parser, nTranslateBits == null ? 0 : nTranslateBits.value(), 0x00006, fieldName + "::TranslateY");

		parser.align();

		return new Matrix(hasScale, nScaleBits, scaleX, scaleY, hasRotate, nRotateBits, rotateSkew0, rotateSkew1, nTranslateBits, translateX, translateY);
	}

	/**
	 * Parses a Matrix structure.
	 * 
	 * @param parser Provides the input data.
	 * @param condition The condition to be met for the structure to be parsed.
	 * @param fieldName Name of the matrix field that is parsed.
	 * 
	 * @return The parsed Matrix value or null if the condition was true.
	 * 
	 * @throws SWFParserException Thrown if parsing the Matrix value failed.
	 */
	public static Matrix parseIf(final SWFBinaryParser parser, final boolean condition, final String fieldName) throws SWFParserException {
		return condition ? parse(parser, fieldName) : null;
	}

	/**
	 * Parses a Matrix structure.
	 * 
	 * @param parser Provides the input data.
	 * @param condition The condition to be met for the structure to be parsed.
	 * @param fieldName Name of the matrix field that is parsed.
	 * 
	 * @return The parsed Matrix value or null if the condition was true.
	 * 
	 * @throws SWFParserException Thrown if parsing the Matrix value failed.
	 */
	public static Matrix parseIf(final SWFBinaryParser parser, final Flag condition, final String fieldName) throws SWFParserException {
		return condition.value() ? parse(parser, fieldName) : null;
	}
}
