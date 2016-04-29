package tv.porst.swfretools.parser.tags;

import tv.porst.splib.binaryparser.UINT16;
import tv.porst.swfretools.parser.structures.RecordHeader;
import tv.porst.swfretools.parser.structures.TagList;

/**
 * Represents a DefineSprite tag.
 */
public final class DefineSpriteTag extends Tag {

	/**
	 * Character ID of the sprite.
	 */
	private final UINT16 spriteId;

	/**
	 * Number of frames in sprite.
	 */
	private final UINT16 frameCount;

	/**
	 * A series of tags.
	 */
	private final TagList controlTags;

	/**
	 * Creates a new DefineSprite tag object.
	 * 
	 * @param header Tag header.
	 * @param spriteId Character ID of the sprite.
	 * @param frameCount Number of frames in sprite.
	 * @param controlTags A series of tags.
	 */
	public DefineSpriteTag(final RecordHeader header, final UINT16 spriteId, final UINT16 frameCount, final TagList controlTags) {
		super(header);

		this.spriteId = spriteId;
		this.frameCount = frameCount;
		this.controlTags = controlTags;
	}

	/**
	 * Returns the control tags of the sprite.
	 * 
	 * @return The control tags of the sprite.
	 */
	public TagList getControlTags() {
		return controlTags;
	}

	/**
	 * Returns the number of frames in sprite.
	 * 
	 * @return The number of frames in sprite.
	 */
	public UINT16 getFrameCount() {
		return frameCount;
	}

	/**
	 * Returns the character ID of sprite.
	 * 
	 * @return The character ID of sprite.
	 */
	public UINT16 getSpriteId() {
		return spriteId;
	}
}