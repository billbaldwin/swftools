package tv.porst.swfretools.parser.tags;

import tv.porst.splib.binaryparser.UINT16;
import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.structures.Matrix;
import tv.porst.swfretools.parser.structures.RecordHeader;
import tv.porst.swfretools.parser.structures.Rect;
import tv.porst.swfretools.parser.structures.TextRecordList;

/**
 * Represents a DefineText tag.
 */
public final class DefineTextTag extends Tag {

	/**
	 * ID for this character.
	 */
	private final UINT16 characterId;

	/**
	 * Bounds of the text.
	 */
	private final Rect textBounds;

	/**
	 * Transformation matrix for the text.
	 */
	private final Matrix textMatrix;

	/**
	 * Bits in each glyph index.
	 */
	private final UINT8 glyphBits;

	/**
	 * Bits in each advance value.
	 */
	private final UINT8 advanceBits;

	/**
	 * Text records.
	 */
	private final TextRecordList textRecords;

	/**
	 * End marker of the record.
	 */
	private final UINT8 end;

	/**
	 * Creates a new DefineText tag object.
	 * 
	 * @param header Tag header.
	 * @param characterId ID for this character.
	 * @param textBounds Bounds of the text.
	 * @param textMatrix Transformation matrix for the text.
	 * @param glyphBits Bits in each glyph index.
	 * @param advanceBits Bits in each advance value.
	 * @param textRecords Text records.
	 * @param end End marker of the record.
	 */
	public DefineTextTag(final RecordHeader header, final UINT16 characterId, final Rect textBounds,
			final Matrix textMatrix, final UINT8 glyphBits, final UINT8 advanceBits,
			final TextRecordList textRecords, final UINT8 end) {
		super(header);

		this.characterId = characterId;
		this.textBounds = textBounds;
		this.textMatrix = textMatrix;
		this.glyphBits = glyphBits;
		this.advanceBits = advanceBits;
		this.textRecords = textRecords;
		this.end = end;
	}

	/**
	 * Returns the bits in each advance value.
	 * 
	 * @return The bits in each advance value.
	 */
	public UINT8 getAdvanceBits() {
		return advanceBits;
	}

	/**
	 * Returns the ID for this character.
	 * 
	 * @return The ID for this character.
	 */
	public UINT16 getCharacterId() {
		return characterId;
	}

	/**
	 * Returns the end marker of the record.
	 *
	 * @return The end marker of the record.
	 */
	public UINT8 getEnd() {
		return end;
	}

	/**
	 * Returns the bits in each glyph index.
	 * 
	 * @return The bits in each glyph index.
	 */
	public UINT8 getGlyphBits() {
		return glyphBits;
	}

	/**
	 * Returns the bounds of the text.
	 * 
	 * @return The bounds of the text.
	 */
	public Rect getTextBounds() {
		return textBounds;
	}

	/**
	 * Returns the transformation matrix for the text.
	 * 
	 * @return The transformation matrix for the text.
	 */
	public Matrix getTextMatrix() {
		return textMatrix;
	}

	/**
	 * Returns the text records.
	 * 
	 * @return The text records.
	 */
	public TextRecordList getTextRecords() {
		return textRecords;
	}
}