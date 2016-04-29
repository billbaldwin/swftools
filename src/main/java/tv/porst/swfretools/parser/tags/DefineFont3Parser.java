package tv.porst.swfretools.parser.tags;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseFlag;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseINT16;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseINT16If;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseString;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT16;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT16If;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT32;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8;

import java.util.ArrayList;
import java.util.List;

import tv.porst.splib.binaryparser.AsciiString;
import tv.porst.splib.binaryparser.Flag;
import tv.porst.splib.binaryparser.INT16;
import tv.porst.splib.binaryparser.IParsedINTElement;
import tv.porst.splib.binaryparser.UINT16;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.structures.INT16List;
import tv.porst.swfretools.parser.structures.KerningRecord;
import tv.porst.swfretools.parser.structures.KerningRecordList;
import tv.porst.swfretools.parser.structures.KerningRecordParser;
import tv.porst.swfretools.parser.structures.ParsedINTElementList;
import tv.porst.swfretools.parser.structures.RecordHeader;
import tv.porst.swfretools.parser.structures.Rect;
import tv.porst.swfretools.parser.structures.RectList;
import tv.porst.swfretools.parser.structures.RectParser;
import tv.porst.swfretools.parser.structures.Shape3;
import tv.porst.swfretools.parser.structures.Shape3List;
import tv.porst.swfretools.parser.structures.Shape3Parser;

/**
 * Class for parsing DefineFont3 tags.
 */
public final class DefineFont3Parser {

	/**
	 * Parses a DefineFont3 tag.
	 * 
	 * @param parser Provides the input data.
	 * @param header Previously parsed header of the tag.
	 * 
	 * @return Returns the parsed tag.
	 * 
	 * @throws SWFParserException Thrown if parsing the tag failed.
	 */
	public static DefineFont3Tag parse(final RecordHeader header, final SWFBinaryParser parser) throws SWFParserException {

		final int endPosition = parser.getBytePosition() + header.getNormalizedLength();

		final UINT16 fontId = parseUINT16(parser, 0x00006, "DefineFont3::FontId");
		final Flag fontFlagsHasLayout = parser.readFlag();
		final Flag fontFlagsShiftJIS = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsShiftJIS");
		final Flag fontFlagsSmallText = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsSmallText");
		final Flag fontFlagsANSI = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsANSI");
		final Flag fontFlagsWideOffsets = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsWideOffsets");
		final Flag fontFlagsWideCodes = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsWideCodes");
		final Flag fontFlagsItalic = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsItalic");
		final Flag fontFlagsBold = parseFlag(parser, 0x00006, "DefineFont3::FontFlagsBold");
		final UINT8 languageCode = parseUINT8(parser, 0x00006, "DefineFont3::LanguageCode");
		final UINT8 fontNameLen = parseUINT8(parser, 0x00006, "DefineFont3::FontNameLen");
		final AsciiString fontName = parseString(parser, fontNameLen.value(), 0x00006, "DefineFont3::FontName");
		final UINT16 numGlyphs = parseUINT16(parser, 0x00006, "DefineFont3::NumGlyphs");

		final List<IParsedINTElement> offsetTable = new ArrayList<IParsedINTElement>();

		for (int i=0;i<numGlyphs.value();i++) {
			if (fontFlagsWideOffsets.value()) {
				offsetTable.add(parseUINT32(parser, 0x00006, String.format("DefineFont3::OffsetTable[%d]", i)));
			}
			else {
				offsetTable.add(parseUINT16(parser, 0x00006, String.format("DefineFont3::OffsetTable[%d]", i)));
			}
		}

		// Reading the CodeTableOffset is tricky. I have seen valid tags with NumGlyphs=0 where
		// the CodeTableOffset was omitted and I have seen some that have it.
		final boolean parseCodeTableOffset = parser.getBytePosition() < endPosition;

		final IParsedINTElement codeTableOffset = parseCodeTableOffset ? (fontFlagsWideOffsets.value() ? parseUINT32(parser, 0x00006, "DefineFont3::CodeTableOffset") : parseUINT16(parser, 0x00006, "DefineFont3::CodeTableOffset")) : null;

		final List<Shape3> glyphShapeTable = new ArrayList<Shape3>();

		for (int i=0;i<numGlyphs.value();i++) {
			glyphShapeTable.add(Shape3Parser.parse(parser, String.format("GlyphShapeTable[%d]", i)));
		}

		final List<IParsedINTElement> codeTable = new ArrayList<IParsedINTElement>();

		for (int i=0;i<numGlyphs.value();i++) {
			if (fontFlagsWideCodes.value()) {
				offsetTable.add(parseUINT16(parser, 0x00006, String.format("DefineFont3::CodeTable[%d]", i)));
			}
			else {
				offsetTable.add(parseUINT8(parser, 0x00006, String.format("DefineFont3::CodeTable[%d]", i)));
			}
		}

		final boolean parseFont = parser.getBytePosition() < endPosition || (fontFlagsHasLayout != null && fontFlagsHasLayout.value());

		final INT16 fontAscent = parseINT16If(parser, 0x00006, parseFont, "DefineFont3::FontAscent");
		final INT16 fontDescent = parseINT16If(parser, 0x00006, parseFont, "DefineFont3::FontDescent");
		final INT16 fontLeading = parseINT16If(parser, 0x00006, parseFont, "DefineFont3::FontLeading");

		final List<INT16> fontAdvanceTable = new ArrayList<INT16>();
		final List<Rect> fontBoundsTable = new ArrayList<Rect>();

		if (parseFont) {
			for (int i=0;i<numGlyphs.value();i++) {
				fontAdvanceTable.add(parseINT16(parser, 0x00006, String.format("DefineFont3::FontAdvanceTable[%d]", i)));
			}

			for (int i=0;i<numGlyphs.value();i++) {
				fontBoundsTable.add(RectParser.parse(parser, String.format("DefineFont3::FontBoundsTable[%d]", i)));
			}
		}

		final UINT16 kerningCount = parseUINT16If(parser, 0x00006, parseFont, "DefineFont3::KerningCount");

		final List<KerningRecord> fontKerningTable = new ArrayList<KerningRecord>();

		if (parseFont) {
			for (int i=0;i<kerningCount.value();i++) {
				fontKerningTable.add(KerningRecordParser.parse(parser, fontFlagsWideCodes.value(), String.format("DefineFont3::FontKerningTable[%d]", i)));
			}
		}

		return new DefineFont3Tag(header, fontId, fontFlagsHasLayout, fontFlagsShiftJIS,
				fontFlagsSmallText, fontFlagsANSI, fontFlagsWideOffsets, fontFlagsWideCodes,
				fontFlagsItalic, fontFlagsBold, languageCode, fontNameLen, fontName,
				numGlyphs, new ParsedINTElementList(offsetTable), codeTableOffset, new Shape3List(glyphShapeTable),
				new ParsedINTElementList(codeTable), fontAscent,
				fontDescent, fontLeading, new INT16List(fontAdvanceTable), new RectList(fontBoundsTable), kerningCount,
				new KerningRecordList(fontKerningTable));
	}
}