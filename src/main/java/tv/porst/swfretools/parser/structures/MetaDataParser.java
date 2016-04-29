package tv.porst.swfretools.parser.structures;

import java.util.ArrayList;
import java.util.List;

import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;

public class MetaDataParser {

	public static MetaData parse(final SWFBinaryParser parser, final String fieldName) throws SWFParserException {
		final EncodedU30 name = EncodedU30Parser.parse(parser, fieldName + "::name");
		final EncodedU30 itemCount = EncodedU30Parser.parse(parser, fieldName + "::item_count");

		final List<ItemInfo> items = new ArrayList<ItemInfo>();

		// Adobe's documentation is wrong on this. Instead of [key,value,key,value,...],
		// it's [key,key,...,value,value,...]
		List<EncodedU30> keys = new ArrayList<>();
		List<EncodedU30> values = new ArrayList<>();
		for (int i=0;i<itemCount.value();i++) {
			keys.add(EncodedU30Parser.parse(parser, ""));
		}
		for (int i=0;i<itemCount.value();i++) {
			values.add(EncodedU30Parser.parse(parser, ""));
		}
		for (int i = 0; i < itemCount.value(); i++ ) {
			items.add(new ItemInfo(keys.get(i), values.get(i)));
		}

		return new MetaData(name, itemCount, new ItemInfoList(items));
	}

}
