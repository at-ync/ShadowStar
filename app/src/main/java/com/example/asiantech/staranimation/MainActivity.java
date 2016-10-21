package com.example.asiantech.staranimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mImgStar;
    private ImageView mImageShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View mContainer = findViewById(R.id.activity_main);
        mImgStar = (ImageView) findViewById(R.id.imageView);
        mImageShadow = (ImageView) findViewById(R.id.imageViewShadow);
        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartAnimation(mImgStar);
            }
        });
    }

    private void onStartAnimation(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotation", 0, 60, 15, 0));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Toast.makeText(MainActivity.this, "animation start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Toast.makeText(MainActivity.this, "animation End", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_start);
                Bitmap shadow = addShadow(bitmap, bitmap.getHeight(), bitmap.getWidth(), Color.YELLOW, 300);
                mImageShadow.setImageBitmap(shadow);
                onAnimationScale(mImageShadow);

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Toast.makeText(MainActivity.this, "animation Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                Toast.makeText(MainActivity.this, "animation Repeat", Toast.LENGTH_SHORT).show();
            }
        });
        animatorSet.setDuration(1000L);
        animatorSet.start();
    }

    private void onAnimationScale(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1, 2f, 0),
                ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1, 2f, 0),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0.8f, 0.5f, 0));
        animatorSet.setDuration(1000L);
        animatorSet.start();
    }

    //Shadow bitmap
    public Bitmap addShadow(final Bitmap bm, final int dstHeight, final int dstWidth, int color, int size) {
        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);
        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth, dstHeight);
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        final Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dstWidth, dstHeight);
        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);
        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.NORMAL);
        paint.reset();
        paint.setAlpha(1);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);
        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0, 0, paint);
        mask.recycle();
        return ret;
    }

}