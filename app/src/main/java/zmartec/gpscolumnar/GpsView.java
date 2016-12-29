package zmartec.gpscolumnar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.view.View;

import java.util.Iterator;

/**
 * Created on 2016/12/29 17:49
 * company: Zmartec
 */
public class GpsView extends View{
    GpsStatus mGpsStatus;
    private int swidth = 8;
    private int nDist = 3;
    private int noriginx = 30;//横坐标数字距离
    private int noriginy = 180;//纵坐标高度

    public GpsView(Context context) {
        super(context);
    }

    public void setGpsStatus(GpsStatus gpsStatus) {
        mGpsStatus = gpsStatus;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int count = 0;
        int gpscount = 0;
        int bdcount = 0;
        Rect mDestRect;
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(16);
        mPaint.setShader(null);
        canvas.drawLine(noriginx, 0, noriginx, noriginy, mPaint);
        canvas.drawLine(noriginx, noriginy, 170, noriginy, mPaint);//修改原始坐标y轴的高度
        canvas.drawText("10", 10, noriginy - 10 * 3, mPaint);
        canvas.drawText("20", 10, noriginy - 20 * 3, mPaint);
        canvas.drawText("30", 10, noriginy - 30 * 3, mPaint);
        canvas.drawText("40", 10, noriginy - 40 * 3, mPaint);
        canvas.drawText("itemsatelite", 50, 10, mPaint);
        canvas.drawText("10", 200, noriginy - 10 * 3, mPaint);
        canvas.drawText("20", 200, noriginy - 20 * 3, mPaint);
        canvas.drawText("30", 200, noriginy - 30 * 3, mPaint);
        canvas.drawText("40", 200, noriginy - 40 * 3, mPaint);

        canvas.drawText("bd", 240, 10, mPaint);
        canvas.drawLine(220, 0, 220, noriginy, mPaint);
        canvas.drawLine(220, noriginy, 400, noriginy, mPaint);
        if (mGpsStatus != null) {
            int maxGpsStale = mGpsStatus.getMaxSatellites();
            Iterator<GpsSatellite> iterator = mGpsStatus.getSatellites().iterator();
            while (iterator.hasNext() && count <= maxGpsStale) {
                GpsSatellite gpsSatellite = iterator.next();
                int nNu = gpsSatellite.getPrn();
                int nSnr = (int) gpsSatellite.getSnr();
                boolean b = gpsSatellite.usedInFix();
                count++;
                if (nNu < 35) {
                    if (nSnr > 0) {
                        if (b) {
                            mPaint.setColor(Color.BLUE);
                        } else {
                            mPaint.setColor(Color.GREEN);
                        }
                        int nx = noriginx + gpscount * (swidth + nDist);
                        canvas.drawRect(nx, noriginy - 2 * nSnr, nx + swidth, noriginy, mPaint);
                        gpscount++;
                    }
                } else if (nNu >= 160) {
                    if (nSnr > 0) {
                        if (b) {
                            mPaint.setColor(Color.GREEN);
                        } else {
                            mPaint.setColor(Color.BLUE);
                        }
                        int nx = 220 + bdcount * (swidth + nDist);
                        canvas.drawRect(nx, noriginy - 4 * nSnr, nx + swidth, noriginy, mPaint);
                        bdcount++;
                    }

                }
            }

        }
    }
}
