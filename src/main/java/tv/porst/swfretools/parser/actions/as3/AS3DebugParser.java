package tv.porst.swfretools.parser.actions.as3;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.structures.EncodedU30;
import tv.porst.swfretools.parser.structures.EncodedU30Parser;

/**
 * Parses ActionScript 3 'debug' instructions.
 */
public final class AS3DebugParser {

	public static AS3Debug parse(final SWFBinaryParser parser, final String fieldName) throws SWFParserException {
		final UINT8 opcode = parseUINT8(parser, 0x00006, fieldName + "::opcode");
		final UINT8 debugType = parseUINT8(parser, 0x00006, fieldName + "::debug_type");
		final EncodedU30 index = EncodedU30Parser.parse(parser, fieldName + "::index");
		final UINT8 reg = parseUINT8(parser, 0x00006, fieldName + "::reg");
		final EncodedU30 extra = EncodedU30Parser.parse(parser, fieldName + "::extra");

		return new AS3Debug(opcode, debugType, index, reg, extra);
	}

}
