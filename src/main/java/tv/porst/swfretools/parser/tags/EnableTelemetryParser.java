package tv.porst.swfretools.parser.tags;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseUBits;

import tv.porst.splib.binaryparser.UBits;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.structures.RecordHeader;

public class EnableTelemetryParser {

	public static EnableTelemetryTag parse(final RecordHeader header, final SWFBinaryParser parser) throws SWFParserException {

		@SuppressWarnings("unused")
		final UBits reserved = parseUBits(parser, 16, 0x00006, "EnableTelemetry::Reserved");

		return new EnableTelemetryTag(header);
	}

}
