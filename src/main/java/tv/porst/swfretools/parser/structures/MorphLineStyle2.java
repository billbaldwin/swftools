package tv.porst.swfretools.parser.structures;

import tv.porst.splib.binaryparser.Flag;
import tv.porst.splib.binaryparser.IFileElement;
import tv.porst.splib.binaryparser.UBits;
import tv.porst.splib.binaryparser.UINT16;
import tv.porst.swfretools.parser.SWFParserHelpers;

/**
 * Represents a MorphLineStyle2 structure.
 * 
 * @author sp
 *
 */
public final class MorphLineStyle2 implements IFileElement {

	/**
	 * Width of line in start shape in twips.
	 */
	private final UINT16 startWidth;

	/**
	 * Width of line in end shape in twips.
	 */
	private final UINT16 endWidth;

	/**
	 * Start-cap style.
	 */
	private final UBits startCapStyle;

	/**
	 * Join style.
	 */
	private final UBits joinStyle;

	/**
	 * Has fill flag.
	 */
	private final Flag hasFillFlag;

	/**
	 * Horizontal stroke thickness scaling flag.
	 */
	private final Flag noHScaleFlag;

	/**
	 * Vertical stroke thickness scaling flag.
	 */
	private final Flag noVScaleFlag;

	/**
	 * Anchor pixel alignment flag.
	 */
	private final Flag pixelHintingFlag;

	/**
	 * Reserved bits.
	 */
	private final UBits reserved;

	/**
	 * Close flag.
	 */
	private final Flag noClose;

	/**
	 * End-cap style.
	 */
	private final UBits endCapStyle;

	/**
	 * Miter limit factor.
	 */
	private final UINT16 miterLimitFactor;

	/**
	 * Color value of start shape.
	 */
	private final RGBA startColor;

	/**
	 * Color value of end shape.
	 */
	private final RGBA endColor;

	/**
	 * Fill style.
	 */
	private final MorphFillStyle fillType;

	/**
	 * Creates a new MorphLineStyle2 object.
	 * 
	 * @param startWidth Width of line in start shape in twips.
	 * @param endWidth Width of line in end shape in twips.
	 * @param startCapStyle Start-cap style.
	 * @param joinStyle Join style.
	 * @param hasFillFlag Has fill flag.
	 * @param noHScaleFlag Horizontal stroke thickness scaling flag.
	 * @param noVScaleFlag Vertical stroke thickness scaling flag.
	 * @param pixelHintingFlag Anchor pixel alignment flag.
	 * @param reserved Reserved bits.
	 * @param noClose Close flag.
	 * @param endCapStyle End-cap style.
	 * @param miterLimitFactor Miter limit factor.
	 * @param startColor Color value of start shape.
	 * @param endColor Color value of end shape.
	 * @param fillType Fill style.
	 */
	public MorphLineStyle2(final UINT16 startWidth, final UINT16 endWidth, final UBits startCapStyle,
			final UBits joinStyle, final Flag hasFillFlag, final Flag noHScaleFlag,
			final Flag noVScaleFlag, final Flag pixelHintingFlag, final UBits reserved,
			final Flag noClose, final UBits endCapStyle, final UINT16 miterLimitFactor,
			final RGBA startColor, final RGBA endColor, final MorphFillStyle fillType) {

		this.startWidth = startWidth;
		this.endWidth = endWidth;
		this.startCapStyle = startCapStyle;
		this.joinStyle = joinStyle;
		this.hasFillFlag = hasFillFlag;
		this.noHScaleFlag = noHScaleFlag;
		this.noVScaleFlag = noVScaleFlag;
		this.pixelHintingFlag = pixelHintingFlag;
		this.reserved = reserved;
		this.noClose = noClose;
		this.endCapStyle = endCapStyle;
		this.miterLimitFactor = miterLimitFactor;
		this.startColor = startColor;
		this.endColor = endColor;
		this.fillType = fillType;

	}

	@Override
	public int getBitLength() {
		return SWFParserHelpers.addBitLengths(startWidth, endWidth, startCapStyle, joinStyle, hasFillFlag,
				noHScaleFlag, noVScaleFlag, pixelHintingFlag, reserved, noClose, endCapStyle, miterLimitFactor,
				startColor, endColor, fillType);
	}

	@Override
	public int getBitPosition() {
		return startWidth.getBitPosition();
	}

	/**
	 * Returns the end-cap style.
	 *
	 * @return The end-cap style.
	 */
	public UBits getEndCapStyle() {
		return endCapStyle;
	}

	/**
	 * Returns the color value of end shape.
	 *
	 * @return The color value of end shape.
	 */
	public RGBA getEndColor() {
		return endColor;
	}

	/**
	 * Returns the width of line in end shape in twips.
	 *
	 * @return The width of line in end shape in twips.
	 */
	public UINT16 getEndWidth() {
		return endWidth;
	}

	/**
	 * Returns the fill style.
	 *
	 * @return The fill style.
	 */
	public MorphFillStyle getFillType() {
		return fillType;
	}

	/**
	 * Returns the has fill flag.
	 *
	 * @return The has fill flag.
	 */
	public Flag getHasFillFlag() {
		return hasFillFlag;
	}

	/**
	 * Returns the join style.
	 *
	 * @return The join style.
	 */
	public UBits getJoinStyle() {
		return joinStyle;
	}

	/**
	 * Returns the miter limit factor.
	 *
	 * @return The miter limit factor.
	 */
	public UINT16 getMiterLimitFactor() {
		return miterLimitFactor;
	}

	/**
	 * Returns the close flag.
	 *
	 * @return The close flag.
	 */
	public Flag getNoClose() {
		return noClose;
	}

	/**
	 * Returns the horizontal stroke thickness scaling flag.
	 *
	 * @return The horizontal stroke thickness scaling flag.
	 */
	public Flag getNoHScaleFlag() {
		return noHScaleFlag;
	}

	/**
	 * Returns the vertical stroke thickness scaling flag.
	 *
	 * @return The vertical stroke thickness scaling flag.
	 */
	public Flag getNoVScaleFlag() {
		return noVScaleFlag;
	}

	/**
	 * Returns the anchor pixel alignment flag.
	 *
	 * @return The anchor pixel alignment flag.
	 */
	public Flag getPixelHintingFlag() {
		return pixelHintingFlag;
	}

	/**
	 * Returns the reserved bits.
	 *
	 * @return The reserved bits.
	 */
	public UBits getReserved() {
		return reserved;
	}

	/**
	 * Returns the start-cap style.
	 *
	 * @return The start-cap style.
	 */
	public UBits getStartCapStyle() {
		return startCapStyle;
	}

	/**
	 * Returns the color value of start shape.
	 *
	 * @return The color value of start shape.
	 */
	public RGBA getStartColor() {
		return startColor;
	}

	/**
	 * Returns the width of line in start shape in twips.
	 *
	 * @return The width of line in start shape in twips.
	 */
	public UINT16 getStartWidth() {
		return startWidth;
	}
}