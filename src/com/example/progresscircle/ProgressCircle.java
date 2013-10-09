/**
 *
 */
package com.example.progresscircle;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * Class of ProgressCircle
 *
 * @author i-kugyon
 */
public class ProgressCircle extends View {

	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_OUTER = 1;
	public static final int ALIGN_INNER = -1;

	private int barAlign = ALIGN_CENTER;

	// Sizes (with defaults)
	private int fullRadius = 100;
	private int barLength = 0;
	private int barWidth = 20;
	private int rimWidth = 20;
	private int textSize = 20;

	// Padding (with defaults)
	private int paddingTop = 5;
	private int paddingBottom = 5;
	private int paddingLeft = 5;
	private int paddingRight = 5;

	// Colors (with defaults)
	private int rimColor = 0xFF0A0A0A;
	private int barColor = 0xFF33B5E5;
	private int textColor = 0xFF33B5E5;

	// Paints
	private Paint barPaint = new Paint();
	private Paint rimPaint = new Paint();
	private Paint startPaint = new Paint();
	private Paint textPaint = new Paint();

	// Rectangles
	private RectF barBounds = new RectF();
	private RectF rimBounds = new RectF();

	// Other
	private float max = 0;
	private float progress = 0;

	private String text = "";
	private String[] splitText = {};

	@SuppressLint("Recycle")
	public ProgressCircle(Context context, AttributeSet attrs) {
		super(context, attrs);

		parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressCircle));
	}

	/**
	 * Now we know the dimensions of the view, setup the bounds and paints
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		if (this.getLayoutParams().width == LayoutParams.WRAP_CONTENT || this.getLayoutParams().height == LayoutParams.WRAP_CONTENT
				|| this.getLayoutParams().width == LayoutParams.MATCH_PARENT || this.getLayoutParams().height == LayoutParams.MATCH_PARENT) {
            throw new IllegalArgumentException("MATCH_PARENT, FILL_PARENT, WRAP_CONTENT not available.");
		}
		if (this.getLayoutParams().width != this.getLayoutParams().height) {
            throw new IllegalArgumentException("Width and height shall have the same value.");
		}

		this.getLayoutParams().width = fullRadius + paddingLeft + paddingRight + barWidth * 2;
		this.getLayoutParams().height = fullRadius + paddingTop + paddingBottom + barWidth * 2;

		setupBounds();
		setupPaints();
		invalidate();
	}


	/**
	 * Set the bounds of the component
	 */
	private void setupBounds() {
		paddingTop = this.getPaddingTop();
		paddingBottom = this.getPaddingBottom();
		paddingLeft = this.getPaddingLeft();
		paddingRight = this.getPaddingRight();

		int rimPadding = barWidth - rimWidth / 2;
		if (barAlign == ALIGN_INNER) {
			rimPadding = rimWidth / 2;
		} else if (barAlign == ALIGN_CENTER) {
			rimPadding = barWidth - rimWidth;
		}
		rimBounds = new RectF(paddingLeft + rimPadding, paddingTop + rimPadding, this.getLayoutParams().width - paddingRight - rimPadding, this.getLayoutParams().height - paddingBottom - rimPadding);
		barBounds = new RectF(paddingLeft + barWidth /2, paddingTop + barWidth /2, this.getLayoutParams().width - paddingRight - barWidth /2, this.getLayoutParams().height - paddingBottom - barWidth /2);

		fullRadius = (this.getLayoutParams().width - paddingRight - barWidth) / 2;
	}

	/**
	 * Set the properties of the paints we're using to draw the progress wheel
	 */
	private void setupPaints() {
		barPaint.setColor(barColor);
		barPaint.setAntiAlias(true);
		barPaint.setStyle(Style.STROKE);
		barPaint.setStrokeWidth(barWidth);

		rimPaint.setColor(rimColor);
		rimPaint.setAntiAlias(true);
		rimPaint.setStyle(Style.STROKE);
		rimPaint.setStrokeWidth(rimWidth);

		startPaint.setColor(rimColor);
		startPaint.setAntiAlias(true);
		startPaint.setStyle(Style.STROKE);
		startPaint.setStrokeWidth(5.0f);

		textPaint.setColor(textColor);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
	}

	/**
	 * Parse the attributes passed to the view from the XML
	 *
	 * @param a
	 *            the attributes to parse
	 */
	private void parseAttributes(TypedArray a) {
		barWidth = (int) a.getDimension(R.styleable.ProgressCircle_barWidth, barWidth);
		rimWidth = (int) a.getDimension(R.styleable.ProgressCircle_rimWidth, rimWidth);
		barColor = a.getColor(R.styleable.ProgressCircle_barColor, barColor);
		barAlign = (int) a.getInt(R.styleable.ProgressCircle_barAlign, barAlign);
		textSize = (int) a.getDimension(R.styleable.ProgressCircle_textSize, textSize);
		textColor = (int) a.getColor(R.styleable.ProgressCircle_textColor, textColor);

		// if the text is empty , so ignore it
		if (a.hasValue(R.styleable.ProgressCircle_text)) {
			setText(a.getString(R.styleable.ProgressCircle_text));
		}

		rimColor = (int) a.getColor(R.styleable.ProgressCircle_rimColor, rimColor);
	}

	// ----------------------------------
	// Animation stuff
	// ----------------------------------

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (barWidth != rimWidth) {
			float lineX = rimBounds.left + rimBounds.width() / 2;
			float lineY = paddingTop + barWidth;
			canvas.drawLine(lineX, paddingTop, lineX, lineY, startPaint);
		}

		// Draw the rim
		canvas.drawArc(rimBounds, 360, 360, false, rimPaint);
		// Draw the bar
		canvas.drawArc(barBounds, - 90, barLength, false, barPaint);
		// Draw the text (attempts to center it horizontally and vertically)
		int offsetNum = 0;
		for (String s : splitText) {
			float offset = textPaint.measureText(s) / 2;
			canvas.drawText(s, barBounds.left + barBounds.width() / 2 - offset, barBounds.top + barBounds.height() / 2 + (textSize * offsetNum + (textSize) / 2) - ((splitText.length - 1) * (textSize / 2)) - paddingTop, textPaint);
			offsetNum++;
		}
	}

	public void reset() {
		progress = 0.0f;
		invalidate();
	}

	// ----------------------------------
	// Getters + setters
	// ----------------------------------

	/**
	 * Set the text in the progress bar Doesn't invalidate the view
	 *
	 * @param text
	 *            the text to show ('\n' constitutes a new line)
	 */
	public void setText(String text) {
		this.text = text;
		splitText = this.text.split("\n");
	}

	public int getBarAlign() {
		return barAlign;
	}

	public void setBarAlign(int barAlign) {
		this.barAlign = barAlign;
	}

	public int getBarLength() {
		return barLength;
	}

	public void setBarLength(int barLength) {
		this.barLength = barLength;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getBarColor() {
		return barColor;
	}

	public void setBarColor(int barColor) {
		this.barColor = barColor;
	}

	public int getRimColor() {
		return rimColor;
	}

	public void setRimColor(int rimColor) {
		this.rimColor = rimColor;
	}

	public Shader getRimShader() {
		return rimPaint.getShader();
	}

	public void setRimShader(Shader shader) {
		this.rimPaint.setShader(shader);
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getRimWidth() {
		return rimWidth;
	}

	public void setRimWidth(int rimWidth) {
		this.rimWidth = rimWidth;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		BigDecimal barLengthBig = new BigDecimal(progress);
		barLengthBig = barLengthBig.divide(new BigDecimal(max), 5, BigDecimal.ROUND_HALF_UP);
		barLengthBig = barLengthBig.multiply(new BigDecimal(360));
		barLength = barLengthBig.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

		this.progress = progress;
	}

}