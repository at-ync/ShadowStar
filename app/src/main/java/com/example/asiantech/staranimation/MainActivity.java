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
                Bitmap shadow = addShadow(bitmap, bitmap.getHeight(), bitmap.getWidth(), Color.YELLOW, 200);
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
        animatorSet.setDuration(500L);
        animatorSet.start();
    }

    private void onAnimationScale(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.3f, 1.7f, 2f),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.3f, 1.7f, 2f),
                ObjectAnimator.ofFloat(view, "alpha", 0.6f, 0.4f, 0f));
        animatorSet.setDuration(200L);
        animatorSet.start();
    }

    //Shadow bitmap
    public Bitmap addShadow(final Bitmap bm, final int dstHeight, final int dstWidth, int color, int size) {
        Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);
        Matrix scaleToFit = new Matrix();
        Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dstWidth, dstHeight);
        Canvas maskCanvas = new Canvas(mask);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.NORMAL);
        paint.reset();
        paint.setAlpha(1);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);
        Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0, 0, paint);
        mask.recycle();
        return ret;
    }

}