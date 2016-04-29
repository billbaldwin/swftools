package tv.porst.swfretools.parser.structures;

import tv.porst.splib.binaryparser.Bits;
import tv.porst.splib.binaryparser.IFileElement;
import tv.porst.splib.binaryparser.UBits;
import tv.porst.swfretools.parser.SWFParserHelpers;

/**
 * Represents a GlyphEntry structure.
 * 
 * @author sp
 *
 */
public final class GlyphEntry implements IFileElement {

	/**
	 * Glyph index into current font.
	 */
	private final UBits glyphIndex;

	/**
	 * X advance value for glyph.
	 */
	private final Bits advanceIndex;

	/**
	 * Creates a new GlyphEntry object.
	 * 
	 * @param glyphIndex Glyph index into current font.
	 * @param advanceIndex X advance value for glyph.
	 */
	public GlyphEntry(final UBits glyphIndex, final Bits advanceIndex) {

		this.glyphIndex = glyphIndex;
		this.advanceIndex = advanceIndex;
	}

	/**
	 * Returns the X advance value for glyph.
	 *
	 * @return The X advance value for glyph.
	 */
	public Bits getAdvanceIndex() {
		return advanceIndex;
	}

	@Override
	public int getBitLength() {
		return SWFParserHelpers.addBitLengths(glyphIndex, advanceIndex);
	}

	@Override
	public int getBitPosition() {
		return glyphIndex.getBitPosition();
	}

	/**
	 * Returns the glyph index into current font.
	 *
	 * @return The glyph index into current font.
	 */
	public UBits getGlyphIndex() {
		return glyphIndex;
	}
}