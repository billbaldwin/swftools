package tv.porst.swfretools.parser.structures;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseString;
import tv.porst.splib.binaryparser.AsciiString;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;

/**
 * Parses FrameLabels structures.
 * 
 * @author sp
 *
 */
public final class FrameLabelsParser {

	/**
	 * Parses a GlyphEntry structure.
	 * 
	 * @param parser The parser that parses the structure.
	 * @param fieldName The name of the structure in the parent structure.
	 * 
	 * @return The parsed structure.
	 * 
	 * @throws SWFParserException Thrown if the structure could not be parsed.
	 */
	public static FrameLabel parse(final SWFBinaryParser parser, final String fieldName) throws SWFParserException {

		final EncodedU32 offset = EncodedU32Parser.parse(parser, fieldName + "::Offset");
		final AsciiString name = parseString(parser, 0x00006, fieldName + "::Name");

		return new FrameLabel(offset, name);
	}
}