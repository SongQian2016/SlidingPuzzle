package groupapp.cs.psu.slidingpuzzle;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;

import groupapp.cs.psu.slidingpuzzle.grid.objects.Board;
import groupapp.cs.psu.slidingpuzzle.grid.objects.Coordinates;

public class DisplayGrid extends View {

    /** The board. */
    private Board board;
    /** The width. */
    private float width;

    /** The height. */
    private float height;

    /**
     * Instantiates a new board view.
     *
     * @param context
     *            the context
     * @param board
     *            the board
     */
    public DisplayGrid(Context context, Board board) {
        super(context);
        this.board = board;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w / this.board.size();
        this.height = h / this.board.size();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Locate place.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the place
     */
    public Coordinates locateCoordinates(float x, float y) {
        int ix = (int) (x / width);
        int iy = (int) (y / height);

        return board.at(ix, iy);
    }

    /**
     * Locate place.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the place
     */
    public Coordinates locateCoordinates1(float x, float y) {
        int ix = (int) (x / width);
        int iy = (int) (y / height);

        return board.at(iy-1,ix);
    }

    /**
     * Locate place.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the place
     */
    public Coordinates locateCoordinates2(float x, float y) {
        int ix = (int) (x / width);
        int iy = (int) (y / height);

        return board.at(iy-1,ix );
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        Coordinates p = locateCoordinates(event.getX(), event.getY());
        if (p != null && p.slidable() && !board.solved()) {
            p.slide();
            invalidate();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.sky));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);

        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.grain));
        dark.setStrokeWidth(15);

        // Draw the major grid lines
        for (int i = 0; i < this.board.size(); i++) {
            canvas.drawLine(0, i * height, getWidth(), i * height, dark);
            canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
        }

        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.oxblood));
        foreground.setStyle(Style.FILL);
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);

        float x = width / 2;
        FontMetrics fm = foreground.getFontMetrics();
        float y = (height / 2) - (fm.ascent + fm.descent) / 2;

        Iterator<Coordinates> it = board.coordinates().iterator();
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                if (it.hasNext()) {
                    Coordinates p = it.next();
                    if (p.hasBlock()) {
                        if (p.getBlock().operation() == ' ') {
                            String number = Integer.toString(p.getBlock().number());
                            canvas.drawText(number, i * width + x, j * height + y,
                                    foreground);
                        }else{
                            String operation = Character.toString(p.getBlock().operation());
                            canvas.drawText(operation, i * width + x, j * height + y,
                                    foreground);
                        }
                    } else {
                        canvas.drawRect(i * width, j * height, i * width
                                + width, j * height + height, dark);
                    }
                }
            }
        }
    }
}