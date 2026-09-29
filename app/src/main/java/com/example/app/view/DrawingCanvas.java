package com.example.app.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrawingCanvas extends View {
    private Paint paint;
    private Path currentPath;
    private List<DrawingPath> paths = new ArrayList<>();
    private String currentTool = "line";
    private float startX, startY;
    private RectF rectF;

    public DrawingCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);

        for (DrawingPath drawingPath : paths) {
            paint.setColor(drawingPath.color);
            paint.setStrokeWidth(drawingPath.strokeWidth);
            canvas.drawPath(drawingPath.path, paint);
        }

        if (currentPath != null) {
            canvas.drawPath(currentPath, paint);
        }
    }

    private void drawGrid(Canvas canvas) {
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1);

        float width = getWidth();
        float height = getHeight();
        float gridSize = 50;

        for (float x = 0; x < width; x += gridSize) {
            canvas.drawLine(x, 0, x, height, gridPaint);
        }

        for (float y = 0; y < height; y += gridSize) {
            canvas.drawLine(0, y, width, y, gridPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                currentPath = new Path();
                currentPath.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                if (currentTool.equals("line")) {
                    currentPath.lineTo(x, y);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (currentTool.equals("rectangle")) {
                    rectF = new RectF(startX, startY, x, y);
                    currentPath.addRect(rectF, Path.Direction.CW);
                } else if (currentTool.equals("circle")) {
                    float radius = (float) Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
                    currentPath.addCircle(startX, startY, radius, Path.Direction.CW);
                }
                paths.add(new DrawingPath(currentPath, paint.getColor(), paint.getStrokeWidth()));
                currentPath = null;
                invalidate();
                break;
        }
        return true;
    }

    public void setTool(String tool) {
        this.currentTool = tool;
    }

    public void undo() {
        if (!paths.isEmpty()) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }
    public void setBackgroundBitmap(Bitmap bitmap) {
        setBackground(new BitmapDrawable(getResources(), bitmap));
        invalidate();
    }
    public void savePlanWithName(String fileName) {
        try {
            // Get the directory for saving plans
            File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File planFile = new File(picturesDir, fileName);

            // Create output stream
            FileOutputStream outputStream = new FileOutputStream(planFile);

            // Save the current drawing as a PNG
            Bitmap bitmap = getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Close the stream
            outputStream.flush();
            outputStream.close();

            // Notify the gallery to update
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(planFile));
            getContext().sendBroadcast(mediaScanIntent);

            Toast.makeText(getContext(), "Plan saved as: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to save plan!", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the current bitmap from the canvas
    private Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }


    public void clearCanvas() {
        paths.clear();
        invalidate();
    }

    public boolean savePlan() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "saved_plan.png");
        boolean isSaved = saveDrawing(file);
        if (isSaved) {
            Toast.makeText(getContext(), "Plan saved successfully to Pictures folder!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to save plan.", Toast.LENGTH_SHORT).show();
        }
        return isSaved;
    }

    public boolean downloadPlan() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "downloaded_plan.png");
        boolean isDownloaded = saveDrawing(file);
        if (isDownloaded) {
            Toast.makeText(getContext(), "Plan downloaded successfully to Downloads folder!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to download plan.", Toast.LENGTH_SHORT).show();
        }
        return isDownloaded;
    }

    private boolean saveDrawing(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class DrawingPath {
        Path path;
        int color;
        float strokeWidth;

        DrawingPath(Path path, int color, float strokeWidth) {
            this.path = path;
            this.color = color;
            this.strokeWidth = strokeWidth;
        }
    }
}