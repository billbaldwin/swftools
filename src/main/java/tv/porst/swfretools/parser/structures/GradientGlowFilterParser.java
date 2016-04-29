package tv.porst.swfretools.parser.structures;

import static tv.porst.swfretools.parser.SWFParserHelpers.parseFlag;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUBits;
import static tv.porst.swfretools.parser.SWFParserHelpers.parseUINT8;

import java.util.ArrayList;
import java.util.List;

import tv.porst.splib.binaryparser.Flag;
import tv.porst.splib.binaryparser.UBits;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFBinaryParser;
import tv.porst.swfretools.parser.SWFParserException;

/**
 * Parses GradientGlowFilter structures.
 * 
 * @author sp
 */
public final class GradientGlowFilterParser {

	/**
	 * Parses a GradientGlowFilter structure.
	 * 
	 * @param parser The parser that parses the structure.
	 * @param fieldName The name of the structure in the parent structure.
	 * 
	 * @return The parsed structure.
	 * 
	 * @throws SWFParserException Thrown if the structure could not be parsed.
	 */
	public static GradientGlowFilter parse(final SWFBinaryParser parser, final String fieldName) throws SWFParserException {

		final UINT8 numColors = parseUINT8(parser, 0x00006, fieldName + "::NumColors");

		final List<RGBA> gradientColors = new ArrayList<RGBA>();

		for (int i=0;i<numColors.value();i++) {
			gradientColors.add(RGBAParser.parse(parser, String.format(fieldName + "::GradientColors[%d]", i)));
		}

		final List<UINT8> gradientRatio = new ArrayList<UINT8>();

		for (int i=0;i<numColors.value();i++) {
			gradientRatio.add(parseUINT8(parser, 0x00006, String.format(fieldName + "::GradientRatio[%d]", i)));
		}

		final Fixed blurX = FixedParser.parse(parser, fieldName + "::BlurX");
		final Fixed blurY = FixedParser.parse(parser, fieldName + "::BlurY");
		final Fixed angle = FixedParser.parse(parser, fieldName + "::Angle");
		final Fixed distance = FixedParser.parse(parser, fieldName + "::Distance");
		final Fixed8 strength = Fixed8Parser.parse(parser, fieldName + "::Strength");
		final Flag innerShadow = parseFlag(parser, 0x00006, fieldName + "::InnerShadow");
		final Flag knockout = parseFlag(parser, 0x00006, fieldName + "::KnockOut");
		final Flag compositeSource = parseFlag(parser, 0x00006, fieldName + "::CompositeSource");
		final Flag onTop = parseFlag(parser, 0x00006, fieldName + "::OnTop");
		final UBits passes = parseUBits(parser, 4, 0x00006, fieldName + "::Passes");

		return new GradientGlowFilter(numColors, new RGBAList(gradientColors), new UINT8List(gradientRatio), blurX, blurY, angle, distance, strength, innerShadow, knockout, compositeSource, onTop, passes);
	}
}