package tv.porst.swfretools.parser.actions.as3;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;

/**
 * Parses ActionScript 3 'bitxor' instructions.
 */
public final class AS3BitxorParser {

	public static AS3Bitxor parse(final SWFBinaryParser parser, final String fieldName) throws SWFParserException {

		final UINT8 opcode = parseUINT8(parser, 0x00006, fieldName + "::opcode");

		return new AS3Bitxor(opcode);
	}

}
