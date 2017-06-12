package org.bstone.util.dataformat.obj;

import org.bstone.util.dataformat.DataFormatParser;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Andy on 6/11/17.
 */
public class OBJFormatParser extends DataFormatParser<WavefrontOBJ>
{
	public OBJFormatParser(int bufferSize)
	{
		super(bufferSize);
	}

	@Override
	protected WavefrontOBJ readData(Cursor cursor) throws IOException
	{
		VertexBuffer vb = new VertexBuffer();
		FaceBuffer fb = new FaceBuffer();
		while (!isEndOfFile(cursor.getChar()))
		{
			this.readLine(cursor, vb, fb);
			this.consumeWhiteSpaces(cursor);
		}
		return new WavefrontOBJ(vb, fb);
	}

	protected void readLine(Cursor cursor, VertexBuffer vb, FaceBuffer fb) throws IOException
	{
		switch (cursor.getChar())
		{
			case '#':
				this.consumeComment(cursor);
				break;
			case 'v':
				this.readVertex(cursor, vb);
				break;
			case 'f':
				this.readFace(cursor, fb);
				break;
			default:
				this.consumeLine(cursor);
		}
	}

	protected void readFace(Cursor cursor, FaceBuffer dst) throws IOException
	{
		cursor.read(this.reader, this.capture);//f
		this.consumeWhiteSpaces(cursor);

		this.readFaceVertex(cursor, dst.buffer);
		dst.pushBuffer();

		this.consumeWhiteSpaces(cursor);

		this.readFaceVertex(cursor, dst.buffer);
		dst.pushBuffer();

		this.consumeWhiteSpaces(cursor);

		this.readFaceVertex(cursor, dst.buffer);
		dst.pushBuffer();

		this.consumeHorizontalWhiteSpaces(cursor);

		while (!this.readEndOfLine(cursor))
		{
			this.readFaceVertex(cursor, dst.buffer);
			dst.pushBuffer();

			this.consumeHorizontalWhiteSpaces(cursor);
		}
	}

	protected IntBuffer readFaceVertex(Cursor cursor, IntBuffer dst) throws IOException
	{
		String geometry = this.readUnsignedInteger(cursor);
		dst.put(Integer.parseInt(geometry));
		if (!this.readChar(cursor, '/'))
		{
			dst.put(-1);
			dst.put(-1);
			return dst;
		}

		if (!this.readChar(cursor, '/'))
		{
			String texture = this.readUnsignedInteger(cursor);
			dst.put(Integer.parseInt(texture));
			if (!this.readChar(cursor, '/'))
			{
				dst.put(-1);
				return dst;
			}
		}
		else
		{
			dst.put(-1);
		}

		String normal = this.readUnsignedInteger(cursor);
		dst.put(Integer.parseInt(normal));

		return dst;
	}

	protected VertexBuffer readVertex(Cursor cursor, VertexBuffer dst) throws IOException
	{
		cursor.read(this.reader, this.capture);//v

		if (this.readChar(cursor, 't'))
		{
			this.consumeWhiteSpaces(cursor);
			this.readTextureVertex(cursor, dst.getBuffer());
			dst.pushBuffer(VertexBuffer.Vertex.TEXTURE);
		}
		else if (this.readChar(cursor, 'n'))
		{
			this.consumeWhiteSpaces(cursor);
			this.readNormalVertex(cursor, dst.getBuffer());
			dst.pushBuffer(VertexBuffer.Vertex.NORMAL);
		}
		else if (this.readChar(cursor, 'p'))
		{
			this.consumeWhiteSpaces(cursor);
			this.readParameterSpaceVertex(cursor, dst.getBuffer());
			dst.pushBuffer(VertexBuffer.Vertex.PARAMSPACE);
		}
		else if (this.readChar(cursor, ' '))
		{
			this.consumeWhiteSpaces(cursor);
			this.readGeometryVertex(cursor, dst.getBuffer());
			dst.pushBuffer(VertexBuffer.Vertex.GEOMETRY);
		}
		else
		{
			throw formatError(cursor, "unknown geometry type");
		}

		return dst;
	}

	protected FloatBuffer readGeometryVertex(Cursor cursor, FloatBuffer dst) throws IOException
	{
		int start = dst.position();
		String num;

		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		this.consumeWhiteSpaces(cursor);
		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		this.consumeWhiteSpaces(cursor);
		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		if (!this.readEndOfLine(cursor))
		{
			num = this.readNumber(cursor);
			dst.put(Float.parseFloat(num));
		}
		else
		{
			dst.put(1.0F);
		}
		int end = dst.position();
		if (end - start != 4) throw formatError(cursor, "Geometry geometry requires 4 components!");
		return dst;
	}

	protected FloatBuffer readTextureVertex(Cursor cursor, FloatBuffer dst) throws IOException
	{
		int start = dst.position();
		String num;

		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		if (!this.readEndOfLine(cursor))
		{
			this.consumeWhiteSpaces(cursor);
			num = this.readNumber(cursor);
			dst.put(Float.parseFloat(num));
		}
		else
		{
			dst.put(0);
		}

		if (!this.readEndOfLine(cursor))
		{
			this.consumeWhiteSpaces(cursor);
			num = this.readNumber(cursor);
			dst.put(Float.parseFloat(num));
		}
		else
		{
			dst.put(0);
		}

		int end = dst.position();
		if (end - start != 3) throw formatError(cursor, "Texture geometry requires 3 components!");
		return dst;
	}

	protected FloatBuffer readNormalVertex(Cursor cursor, FloatBuffer dst) throws IOException
	{
		int start = dst.position();
		String num;

		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		this.consumeWhiteSpaces(cursor);
		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		this.consumeWhiteSpaces(cursor);
		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		int end = dst.position();
		if (end - start != 3) throw formatError(cursor, "Normal geometry requires 3 components!");
		return dst;
	}

	protected FloatBuffer readParameterSpaceVertex(Cursor cursor, FloatBuffer dst) throws IOException
	{
		int start = dst.position();
		String num;

		num = this.readNumber(cursor);
		dst.put(Float.parseFloat(num));

		if (!this.readEndOfLine(cursor))
		{
			this.consumeWhiteSpaces(cursor);
			num = this.readNumber(cursor);
			dst.put(Float.parseFloat(num));
		}
		else
		{
			dst.put(0);
		}

		if (!this.readEndOfLine(cursor))
		{
			this.consumeWhiteSpaces(cursor);
			num = this.readNumber(cursor);
			dst.put(Float.parseFloat(num));
		}
		else
		{
			dst.put(0);
		}

		int end = dst.position();
		if (end - start != 3)
			throw formatError(cursor, "Parameter Space geometry requires 3 components!");
		return dst;
	}

	protected String readNumber(Cursor cursor) throws IOException
	{
		this.startBufferCapture(cursor);
		{
			this.readChar(cursor, '-');

			char firstDigit = cursor.getChar();

			requireAs(cursor, this::readDigit, "digit");

			if (firstDigit != '0')
			{
				this.consumeDigits(cursor);
			}

			this.readDecimal(cursor);
			this.readExponent(cursor);
		}
		return this.stopBufferCapture(cursor);
	}

	private boolean readDecimal(Cursor cursor) throws IOException
	{
		if (!this.readChar(cursor, '.')) return false;

		requireAs(cursor, this::readDigit, "digit");

		this.consumeDigits(cursor);
		return true;
	}

	private boolean readExponent(Cursor cursor) throws IOException
	{
		if (!this.readChar(cursor, 'e') && !this.readChar(cursor, 'E')) return false;
		if (!this.readChar(cursor, '+')) this.readChar(cursor, '-');

		requireAs(cursor, this::readDigit, "digit");

		this.consumeDigits(cursor);
		return true;
	}

	protected boolean readEndOfLine(Cursor cursor) throws IOException
	{
		char c;
		while (!isVerticalWhiteSpace(c = cursor.getChar()) && !isEndOfFile(c) && this.readWhiteSpace(cursor))
		{
		}

		boolean end = isEndOfFile(c);
		boolean flag = isVerticalWhiteSpace(c);
		if (flag)
		{
			cursor.read(this.reader, this.capture);
		}
		return flag || end;
	}

	protected void consumeComment(Cursor cursor) throws IOException
	{
		cursor.read(this.reader, this.capture);//#
		this.consumeLine(cursor);
	}
}
