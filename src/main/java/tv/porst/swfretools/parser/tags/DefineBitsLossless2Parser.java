package tv.porst.swfretools.parser.tags;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseByteArray;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT16;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8If;
import tv.porst.splib.binaryparser.UINT16;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.structures.ByteArray;
import tv.porst.swfretools.parser.structures.RecordHeader;

/**
 * Class for parsing DefineBitsLossless2 tags.
 */
public final class DefineBitsLossless2Parser {

	/**
	 * Parses a DefineBitsLossless2 tag.
	 * 
	 * @param parser Provides the input data.
	 * @param header Previously parsed header of the tag.
	 * 
	 * @return Returns the parsed tag.
	 * 
	 * @throws SWFParserException Thrown if parsing the tag failed.
	 */
	public static DefineBitsLossless2Tag parse(final RecordHeader header, final SWFBinaryParser parser) throws SWFParserException {

		final UINT16 characterId = parseUINT16(parser, 0x00006, "DefineBitsLossless2::CharacterId");
		final UINT8 bitmapFormat = parseUINT8(parser, 0x00006, "DefineBitsLossless2::BitmapFormat");
		final UINT16 bitmapWidth = parseUINT16(parser, 0x00006, "DefineBitsLossless2::BitmapWidth");
		final UINT16 bitmapHeight = parseUINT16(parser, 0x00006, "DefineBitsLossless2::BitmapHeight");

		final int bitmapFormatValue = bitmapFormat.value();

		final UINT8 bitmapColorTableSize = parseUINT8If(parser, 0x00006, bitmapFormatValue == 3, "DefineBitsLossless2::BitmapColorTableSize");

		final int numberOfBytes = header.getNormalizedLength() - 2 - 1 - 2 - 2 - (bitmapColorTableSize == null ? 0 : 1);
		final ByteArray zlibBitmapData = parseByteArray(parser, numberOfBytes, 0x00006, "DefineBitsLossless::ZlibBitmapData");

		return new DefineBitsLossless2Tag(header, characterId, bitmapFormat, bitmapWidth, bitmapHeight, bitmapColorTableSize, zlibBitmapData);
	}
}