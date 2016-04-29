package tv.porst.swfretools.parser.actions.as3;

import tv.porst.splib.binaryparser.UINT8;
import tv.porst.swfretools.parser.SWFParserHelpers;

public class AS3Hasnext2 extends AS3Instruction {

	private final UINT8 objectReg;

	private final UINT8 indexReg;

	public AS3Hasnext2(final UINT8 opcode, final UINT8 objectReg, final UINT8 indexReg) {
		super(opcode);

		this.objectReg = objectReg;
		this.indexReg = indexReg;
	}
	@Override
	public int getBitLength() {
		return SWFParserHelpers.addBitLengths(getOpcode(), objectReg, indexReg);
	}

	/**
	 * Returns the
	 *
	 * @return The
	 */
	public UINT8 getIndexReg() {
		return indexReg;
	}

	/**
	 * Returns the
	 *
	 * @return The
	 */
	public UINT8 getObjectReg() {
		return objectReg;
	}

}
