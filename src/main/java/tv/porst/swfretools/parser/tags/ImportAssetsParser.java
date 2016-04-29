package tv.porst.swfretools.parser.tags;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseString;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT16;

import java.util.ArrayList;
import java.util.List;

import tv.porst.splib.binaryparser.AsciiString;
import tv.porst.splib.binaryparser.UINT16;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.structures.RecordHeader;
import tv.porst.swfretools.parser.structures.Symbol;
import tv.porst.swfretools.parser.structures.SymbolList;
import tv.porst.swfretools.parser.structures.SymbolParser;

/**
 * Class for parsing ImportAssets tags.
 */
public final class ImportAssetsParser {

	/**
	 * Parses an ImportAssets tag.
	 * 
	 * @param parser Provides the input data.
	 * @param header Previously parsed header of the tag.
	 * 
	 * @return Returns the parsed tag.
	 * 
	 * @throws SWFParserException Thrown if parsing the tag failed.
	 */
	public static ImportAssetsTag parse(final RecordHeader header, final SWFBinaryParser parser) throws SWFParserException {

		final AsciiString url = parseString(parser, 0x00006, "ImportAssets::URL");
		final UINT16 count= parseUINT16(parser, 0x00006, "ImportAssets::Count");

		final List<Symbol> symbols = new ArrayList<Symbol>();

		for (int i=0;i<count.value();i++) {
			symbols.add(SymbolParser.parse(parser, String.format("ImportAssets::Symbols[%d]", i)));
		}

		return new ImportAssetsTag(header, url, count, new SymbolList(symbols));
	}
}