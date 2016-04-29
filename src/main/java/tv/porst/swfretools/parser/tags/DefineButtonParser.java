package tv.porst.swfretools.parser.tags;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT16;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8;

import java.util.ArrayList;
import java.util.List;

import tv.porst.splib.binaryparser.UINT16;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.actions.as2.Action;
import tv.porst.swfretools.parser.actions.as2.ActionRecordParser;
import tv.porst.swfretools.parser.structures.ActionList;
import tv.porst.swfretools.parser.structures.ButtonRecord;
import tv.porst.swfretools.parser.structures.ButtonRecordList;
import tv.porst.swfretools.parser.structures.ButtonRecordParser;
import tv.porst.swfretools.parser.structures.RecordHeader;

/**
 * Class for parsing DefineButton tags.
 */
public final class DefineButtonParser {

	/**
	 * Parses a DefineButton tag.
	 * 
	 * @param parser Provides the input data.
	 * @param header Previously parsed header of the tag.
	 * 
	 * @return Returns the parsed tag.
	 * 
	 * @throws SWFParserException Thrown if parsing the tag failed.
	 */
	public static DefineButtonTag parse(final RecordHeader header, final SWFBinaryParser parser) throws SWFParserException {

		final UINT16 buttonId = parseUINT16(parser, 0x00006, "DefineButton::ButtonId");

		final List<ButtonRecord> characters = new ArrayList<ButtonRecord>();

		do {
			if (parser.peekUInt8().value() == 0) {
				break;
			}

			characters.add(ButtonRecordParser.parse(parser, String.format("DefineButton::Characters[%d]", characters.size())));

		} while (true);

		final UINT8 characterEndFlag = parseUINT8(parser, 0x00006, "DefineButton::CharacterEndFlag");

		final int actionRecordSize = parser.getBytePosition() - header.getBitPosition() / 8 + header.getNormalizedLength() - 1;

		final List<Action> actions = ActionRecordParser.parse(parser, actionRecordSize, "DefineButton::Actions");

		final UINT8 actionEndFlag = parseUINT8(parser, 0x00006, "DefineButton::ActionEndFlag");

		return new DefineButtonTag(header, buttonId, new ButtonRecordList(characters), characterEndFlag, new ActionList(actions), actionEndFlag);
	}
}